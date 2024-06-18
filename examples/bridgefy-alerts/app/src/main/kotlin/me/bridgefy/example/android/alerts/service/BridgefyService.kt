package me.bridgefy.example.android.alerts.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.bridgefy.commons.TransmissionMode
import me.bridgefy.commons.exception.BridgefyException
import me.bridgefy.commons.listener.BridgefyDelegate
import me.bridgefy.commons.propagation.PropagationProfile
import me.bridgefy.example.android.alerts.App
import me.bridgefy.example.android.alerts.BuildConfig
import me.bridgefy.example.android.alerts.R
import me.bridgefy.example.android.alerts.model.data.Commands
import me.bridgefy.example.android.alerts.model.data.MessageContentPayload
import me.bridgefy.example.android.alerts.model.data.MessagePayload
import me.bridgefy.example.android.alerts.model.repository.ConversationRepository
import me.bridgefy.example.android.alerts.model.repository.UserRepository
import me.bridgefy.example.android.alerts.ui.notification.NotificationChannels
import me.bridgefy.example.android.alerts.ux.main.MainActivity
import me.bridgefy.logger.enum.LogType
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class BridgefyService : Service(), BridgefyDelegate {

    @Inject
    lateinit var conversationRepository: ConversationRepository

    @Inject
    lateinit var userRepository: UserRepository

    private val last3MessagesReceived: ArrayList<UUID> = arrayListOf()

    private val serviceScope = CoroutineScope(Dispatchers.Main)
    private var bridgefyUUID: UUID? = null
    private val bridgefy by lazy { (applicationContext as App).bridgefy }

    companion object {
        const val START_SERVICE = "start_service"
        const val STOP_SERVICE = "stop"
        const val INIT_SDK = "init_sdk"
        const val SDK_SEND_MESSAGE = "sdk_message"
        const val SDK_SEND_IMAGE = "sdk_image"
        const val SDK_SEND_COLOR = "sdk_color"
        const val SDK_SEND_TEXT = "sdk_text"
        const val SDK_SEND_FLASH = "sdk_flash"
        const val SHOW_MAIN_SCREEN = "main_screen"
        const val SDK_SEND_SOUND = "sdk_sound"
        const val FOREGROUND_SERVICE = "foreground"
    }

    private fun insertNewMessage(messageId: UUID) {
        if (last3MessagesReceived.size == 3) last3MessagesReceived.removeAt(0)
        if (!last3MessagesReceived.contains(messageId)) last3MessagesReceived.add(messageId)
    }

    var isForegroundService = false
    var isBridgefyStarted = false

    inner class LocalBinder : Binder() {
        fun getService(): BridgefyService = this@BridgefyService
    }

    private val binder = LocalBinder()

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        isForegroundService = false
    }

    @SuppressLint("MissingPermission")
    fun doForegroundThings() {
        showToast("Going foreground")
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
        isForegroundService = true
        val builder = NotificationCompat.Builder(this, NotificationChannels.GENERAL.channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Bridgefy Alerts")
            .setContentText("Bridgefy SDK")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notification = builder.build()
        with(NotificationManagerCompat.from(this)) { notify(4, notification) }

        startForeground(4, notification)
    }

    private fun stopService() {
        showToast("Service stopping")
        try {
            stopForeground(true)
            isForegroundService = false
            stopSelf()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initBridgefySDK() {
        bridgefy.init(
            bridgefyApiKey = UUID.fromString(BuildConfig.API_KEY),
            delegate = this,
            logging = LogType.ConsoleLogger(Log.DEBUG),
        )

        bridgefy.start(
            userId = null,
            propagationProfile = PropagationProfile.LongReach,
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (val intentAction = intent?.action) {
            START_SERVICE -> {}
            STOP_SERVICE -> stopService()
            SDK_SEND_MESSAGE -> intent.getStringExtra("message")?.let { sendMessage(it) }
            SDK_SEND_IMAGE -> intent.getStringExtra("image")?.let { sendImageCommand(it.toInt()) }
            SDK_SEND_COLOR -> intent.getStringExtra("color")?.let { sendColorCommand(it) }
            SDK_SEND_TEXT -> intent.getStringExtra("text")?.let { sendTextCommand(it) }
            SDK_SEND_FLASH -> sendFlashCommand()
            SDK_SEND_SOUND -> sendSoundCommand()
            SHOW_MAIN_SCREEN -> showMainScreenCommand()
            INIT_SDK -> if (!isBridgefyStarted) initBridgefySDK()
            FOREGROUND_SERVICE -> doForegroundThings()
            else -> showToast(intentAction ?: "Empty action intent")
        }
        return START_STICKY
    }

    private fun sendImageCommand(image: Int) {
        serviceScope.launch {
            bridgefyUUID?.let { id ->
                val payload = MessagePayload(
                    id = System.currentTimeMillis(),
                    command = Commands.IMAGE.ordinal + 1,
                    image = image,
                )
                bridgefy.send(
                    Json.encodeToString(payload).toByteArray(Charsets.UTF_8),
                    TransmissionMode.Broadcast(id),
                )
            }
        }
    }

    private fun sendColorCommand(color: String) {
        serviceScope.launch {
            bridgefyUUID?.let { id ->
                val payload = MessagePayload(
                    id = System.currentTimeMillis(),
                    command = Commands.COLOR.ordinal + 1,
                    color = color,
                )
                bridgefy.send(
                    Json.encodeToString(payload).toByteArray(Charsets.UTF_8),
                    TransmissionMode.Broadcast(id),
                )
            }
        }
    }

    private fun sendTextCommand(text: String) {
        serviceScope.launch {
            bridgefyUUID?.let { id ->
                val payload = MessagePayload(
                    id = System.currentTimeMillis(),
                    text = text,
                    command = Commands.TEXT.ordinal + 1,
                )
                bridgefy.send(
                    Json.encodeToString(payload).toByteArray(Charsets.UTF_8),
                    TransmissionMode.Broadcast(id),
                )
            }
        }
    }

    private fun sendFlashCommand() {
        serviceScope.launch {
            bridgefyUUID?.let { id ->
                val payload = MessagePayload(
                    command = Commands.FLASHLIGHT.ordinal + 1,
                    id = System.currentTimeMillis(),
                )
                bridgefy.send(
                    Json.encodeToString(payload).toByteArray(Charsets.UTF_8),
                    TransmissionMode.Broadcast(id),
                )
            }
        }
    }

    private fun showMainScreenCommand() {
        val intent = Intent("command_intent")
        intent.putExtra("show_main", "")
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    private fun sendSoundCommand() {
        serviceScope.launch {
            bridgefyUUID?.let { id ->
                val payload = MessagePayload(
                    command = Commands.SOUND.ordinal + 1,
                    id = System.currentTimeMillis(),
                )
                bridgefy.send(
                    Json.encodeToString(payload).toByteArray(Charsets.UTF_8),
                    TransmissionMode.Broadcast(id),
                )
            }
        }
    }

    private fun sendMessage(message: String) {
        serviceScope.launch {
            userRepository.getUserData()?.let { userData ->
                bridgefyUUID?.let { id ->
                    val payload = MessagePayload(
                        ct = MessageContentPayload(
                            text = message,
                            username = userData.userName ?: "Bridgefy User",
                            platform = 1,
                            date = System.currentTimeMillis() / 1000,
                        ),
                        command = Commands.CHAT.ordinal + 1,
                    )

                    val messageID = bridgefy.send(
                        Json.encodeToString(payload).toByteArray(Charsets.UTF_8),
                        TransmissionMode.Broadcast(id),
                    )
                    conversationRepository.handleSentMessage(
                        message,
                        id.toString(),
                        messageID.toString(),
                    )
                }
            }
        }
    }

    private fun showToast(message: String) {
        println(message)
    }

    override fun onConnected(peerID: UUID) {
        println("onConnected $peerID")
    }

    override fun onConnectedPeers(connectedPeers: List<UUID>) {}
    override fun onDestroySession() {
        TODO("Not yet implemented")
    }

    override fun onDisconnected(peerID: UUID) {
        println("onDisconnected $peerID")
    }

    override fun onEstablishSecureConnection(userId: UUID) {
        println("onEstablishSecureConnection $userId")
    }

    override fun onFailToDestroySession(error: BridgefyException) {
        println("onFailToDestroySession ${error.message}")
    }

    override fun onFailToEstablishSecureConnection(userId: UUID, error: BridgefyException) {
        println("onFailToEstablishSecureConnection with $userId - ${error.message}")
    }

    override fun onFailToSend(messageID: UUID, error: BridgefyException) {
        println("onFailToSend with ID $messageID - ${error.message}")
    }

    override fun onFailToStart(error: BridgefyException) {
        println("onFailToStart ${error.message}")
    }

    override fun onFailToStop(error: BridgefyException) {
        println("onFailToStop ${error.localizedMessage}")
    }

    override fun onProgressOfSend(messageID: UUID, position: Int, of: Int) {}
    override fun onReceiveData(
        data: ByteArray,
        messageID: UUID,
        transmissionMode: TransmissionMode,
    ) {
        if (!last3MessagesReceived.contains(messageID)) {
            when (transmissionMode) {
                is TransmissionMode.Mesh,
                is TransmissionMode.Broadcast,
                -> processReceivedCommand(Json.decodeFromString(String(data)))

                else -> {}
            }
            insertNewMessage(messageID)
        }
    }

    private fun processReceivedCommand(
        messagePayload: MessagePayload,
    ) {
        when (messagePayload.command) {
            Commands.CHAT.ordinal + 1 -> {
                messagePayload.ct?.let { messageContent ->
                    handleReceivedMessageCommand(
                        messageContent,
                    )
                }
            }

            Commands.COLOR.ordinal + 1 -> {
                messagePayload.color?.let { colorRaw ->
                    handleReceivedActionCommand(
                        "color",
                        colorRaw,
                    )
                }
            }

            Commands.IMAGE.ordinal + 1 -> {
                messagePayload.image?.let { image ->
                    handleReceivedActionCommand(
                        "image",
                        image.toString(),
                    )
                }
            }

            Commands.SOUND.ordinal + 1 -> handleReceivedActionCommand("sound", "")
            Commands.TEXT.ordinal + 1 -> {
                messagePayload.text?.let { text -> handleReceivedActionCommand("text", text) }
            }

            Commands.FLASHLIGHT.ordinal + 1 -> handleReceivedActionCommand("flash", "")
        }
    }

    private fun handleReceivedMessageCommand(messageContentPayload: MessageContentPayload) {
        conversationRepository.handleReceivedMessage(
            message = messageContentPayload.text,
            senderName = messageContentPayload.username,
            senderId = UUID.randomUUID().toString(),
            messageId = UUID.randomUUID().toString(),
        )
    }

    private fun handleReceivedActionCommand(bName: String, content: String) {
        val intent = Intent("command_intent")
        intent.putExtra(bName, content)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    override fun onSend(messageID: UUID) {
        println("onSend $messageID")
    }

    override fun onStarted(userID: UUID) {
        isBridgefyStarted = true
        bridgefyUUID = userID
        println("onStarted $userID")
    }

    override fun onStopped() {
        isBridgefyStarted = false
        println("onStopped")
    }
}
