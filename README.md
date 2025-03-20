<p align="center">  
  <img src="https://www.gitbook.com/cdn-cgi/image/width=256,dpr=2,height=40,fit=contain,format=auto/https%3A%2F%2F3290834949-files.gitbook.io%2F~%2Ffiles%2Fv0%2Fb%2Fgitbook-x-prod.appspot.com%2Fo%2Fspaces%252F5XKIMMP6VF2l9XuPV80l%252Flogo%252Fd78nQFIysoU2bbM5fYNP%252FGroup%25203367.png%3Falt%3Dmedia%26token%3Df83a642d-8a9a-411f-9ef4-d7189a4c5f0a" />  
</p>  

<p align="center">  
  <img src="https://3290834949-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2F5XKIMMP6VF2l9XuPV80l%2Fuploads%2FD0HSf0lWC4pWB4U7inIw%2Fharegit.jpg?alt=media&token=a400cf7d-3254-4afc-bed0-48f7d98205b0"/>  
</p>  

The Bridgefy Software Development Kit (SDK) is a state-of-the-art, plug-and-play package that will let people use your mobile app when they don’t have access to the Internet, by using Bluetooth mesh networks.

Integrate the Bridgefy SDK into your Android and iOS app to reach the 3.5 billion people that don’t always have access to an Internet connection, and watch engagement and revenue grow!

**Website**. https://developer.bridgefy.me <br>  
**Email**. contact@bridgefy.me <br>  
**witter**. https://twitter.com/bridgefy <br>  
**Facebook**. https://www.facebook.com/bridgefy <br>

## Overview

![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=http%3A%2F%2F34.82.5.94%3A8081%2Fartifactory%2Flibs-release-local%2Fme%2Fbridgefy%2Fandroid-sdk%2Fmaven-metadata.xml)  ![GitHub last commit](https://img.shields.io/github/last-commit/bridgefy/sdk-android)  ![GitHub issues](https://img.shields.io/github/issues-raw/bridgefy/sdk-android?style=plastic)

Bridgefy creates mesh networks in which devices connect directly to each other in a decentralized manner. This allows users to communicate with nearby devices within a certain range, forming a network without the need for a centralized server or Internet access.

![networking](https://images.saymedia-content.com/.image/t_share/MTkzOTUzODU0MDkyNjE3MjIx/particlesjs-examples.gif)

The Bridgefy SDK provides a set of tools and APIs that developers can use to incorporate offline messaging, data transfer, and real-time communication features into their applications. It allows users to send data directly to other nearby devices using Bluetooth Low Energy.

### Table of contents

1. [Setup](#setup)
   - [Compatibility](#compatibility)
2. [Android Permissions](#android-permissions)
   - [Location permission for Bluetooth Low Energy Scanning](#location-permission-for-bluetooth-low-energy-scanning)
   - [Location from BLE scanning in API >= 31](#scan-in-the-background-and-support-apis-29--32)
   - [Connect peripherals](#connect-peripherals)
3. [Summary of available permissions](#summary-of-available-permissions)
4. [Usage](#usage)
   - [Start the SDK](#start-the-sdk)
   - [Bridgefy initialization](#bridgefy-initialization)
   - [Bridgefy session](#bridgefy-session)
   - [Nearby peer detection](#nearby-peer-detection)
   - [Send data](#send-data)
   - [Transmission Modes](#transmission-modes)
   - [Direct and Mesh transmission](#direct-and-mesh-transmission)
   - [Receive Data](#receive-data)
   - [Propagation Profiles](#propagation-profiles)
5. [Secure connections](#secure-connections)
   - [Established secure connection](#established-secure-connection)
   - [Recommendations for using a secure connection](#recommendations-for-using-a-secure-connection)
6. [Using ProGuard](#using-proguard)
7. [Supported Devices](#supported-devices)
8. [Contact & Support](#contact--support)

## Setup
Bridgefy SDK is available in our public repository. To install it you must follow the instructions:

```kotlin  
val bridgefy_release_maven_url = "http://34.82.5.94:8081/artifactory/libs-release-local"  
  
allprojects {  
    repositories {  
        maven {  
            url = java.net.URI(bridgefy_release_maven_url)  
            isAllowInsecureProtocol = true  
        }  
  }  
}  
```  
In the app module **build.gradle** :

```kotlin  
  
/**  
 * Declare dependencies  
 * @see http://www.gradle.org/docs/current/userguide/userguide_single.html#sec:how_to_declare_your_dependencies  
 */  
dependencies {  
    implementation (group = "me.bridgefy", name = "android-sdk", version = "1.2.3", ext = "aar") {  
        isTransitive = true  
    }  
}  
  
```  

### Compatibility

Supported on Android 6 (minSdk = 23) and above with compileSdk >= 31


## Android Permissions
Android requires additional permissions declared in the manifest for an app to run a BLE scan since API 23 (6.0 / Marshmallow) and perform a Bluetooth Low Energy connection since API 31 (Android 12). These permissions currently assume scanning is only used when the App is in the foreground, and that the App wants to derive the user's location from Bluetooth Low Energy signal (on API >= 23). Below are a number of additions you can make to your `AndroidManifext.xml` for your specific use case.

#### Location permission for Bluetooth Low Energy Scanning
Bridgefy uses the `uses-permission-sdk-23` tag to require location only on APIs >= 23, you can request the required permissions by adding the following to your `AndroidManifest.xml`:
```xml  
<uses-permission-sdk-23 android:name="android.permission.ACCESS_COARSE_LOCATION"  
    android:maxSdkVersion="30"  
    tools:node="replace" />  
  <uses-permission-sdk-23 android:name="android.permission.ACCESS_FINE_LOCATION"  
    android:maxSdkVersion="32"  
    tools:node="replace" />  
```  

#### Scan in the background and support APIs 29 & 32
You should add the following to your `AndroidManifest.xml`:
```xml  
<uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION"  
    android:maxSdkVersion="32" />  
```  
If you want to access the user's location in the background on APIs > 30, remove the `android:maxSdkVersion` attribute.

#### Location from BLE scanning in API >= 31

API 31 (Android 12) introduced new Bluetooth permissions. Bridgefy uses the `android:usesPermissionFlags="neverForLocation"` attribute on the `BLUETOOTH_SCAN` permission, which indicates scanning will not be used to derive the user's location, so location permissions are not required. If you need to locate the user with BLE scanning, use this instead, but keep in mind that you will still need `ACCESS_FINE_LOCATION`:
```xml  
<uses-permission android:name="android.permission.BLUETOOTH_SCAN" tools:node="replace" />  
```  

#### Connect peripherals
You add the `BLUETOOTH_CONNECT` permission that Bridgefy requests in APIs >= 31:
```xml  
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />  
```  

## Summary of available permissions
#### Required permissions
A summary of available runtime permissions used for BLE:

| from API | to API (inclusive) | Acceptable runtime permissions                                                                                                                                                                                               |  
|:--------:|:------------------:|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|  
|    18    |         22         | (No runtime permissions needed) |  
|    23    |         28         | One of below: <br>- `android.permission.ACCESS_COARSE_LOCATION`<br>- `android.permission.ACCESS_FINE_LOCATION` |  
|    29    |         30         | - `android.permission.ACCESS_FINE_LOCATION`<br>- `android.permission.ACCESS_BACKGROUND_LOCATION`\*                                                                                                                           |  
|    31    |      current       | - `android.permission.ACCESS_FINE_LOCATION`\*\*<br>- `android.permission.BLUETOOTH_SCAN`<br>- `android.permission.BLUETOOTH_ADVERTISE`<br>- `android.permission.BLUETOOTH_CONNECT` |  

* Needed if [scan is performed in background](https://developer.android.com/about/versions/10/privacy/changes#app-access-device-location)


# Usage
### Start the SDK

The following code shows how to start the SDK (using your API key) and how to assign the delegate.
#### Android Manifest
Locate the AndroidManifest.xml file within your project. It is usually located in the "app" or "src/main" directory. Add the <meta-data> element as application element inside the component tag.

````xml  
<manifest>  
  
  <application>  
  <meta-data  
  android:name="com.bridgefy.sdk.API_KEY"  
            android:value="..." />  
  </application>  
  </manifest>  
  
````  

#### Bridgefy initialization
By default the SDK starts using the **Standard propagation profile** mode

```kotlin  
    /**  
    * Initializes Bridgefy operations.  
    * Parameters:  
    * - bridgefyApiKey: UUID - The license key registered on the Bridgefy developer site.  
    * - delegate: BridgefyDelegate? - A delegate/listener to handle SDK activity and events.  
    * - logging: LogType (optional, default: LogType.None) - The logging priority level for SDK operations.  
    *   
    * Throws: BridgefyException if there is an error during initialization.  
    */  
    @Throws(BridgefyException::class)
    fun init(
      bridgefyApiKey: UUID,
      delegate: BridgefyDelegate?,
      logging: LogType = LogType.None,
    )
```  

The string **“bridgefyApiKey”** represents a valid API key. An Internet connection is needed at least for the first time in order to validate the license.

#### Logging Options

The SDK provides flexible logging options through the `LogType` class.

**None:** No logging.  
**ConsoleLogger:** Logs messages to the console with a specified priority.  
**ConsoleAndFileLogger:** Logs messages to both the console and a file with a specified priority.

**Log Priority**  
**priority:** The priority level for logging.


**Log File Rotation**
* Maximum of 5 log files.
* Each log file is limited to 1MB in size.

**Log files directory:** /storage/sdcard0/Android/data/`_app_package_`/files

To stop it, use the following code:
```kotlin  
   /**  
    * Stops Bridgefy operations and releases associated resources.  
    *  
    */  
   bridgefy.stop()  
```  

To start bridgefy operations:
````kotlin  
   /**  
    * Starts Bridgefy operations, allowing the SDK to participate in the Bridgefy network.  
    * Parameters:  
    * - userId: UUID? (optional) - The ID used to identify the user in the Bridgefy network.  
    *           If null is passed, the SDK will assign an autogenerated ID.  
    * - propagationProfile: PropagationProfile (optional, default: PropagationProfile.Standard)  
    *   - A profile that defines a series of properties and rules for the propagation of messages.  
    */  
   bridgefy.start(  
       userId: UUID? = null,  
       propagationProfile: PropagationProfile = PropagationProfile.Standard,  
   )  
  
````  
#### Bridgefy Methods
```kotlin  
    bridgefy.licenseExpirationDate() : Result<Date?>  
```  

**Description:** Retrieves the expiration date of the Bridgefy license.<br>  
**Returns:** A Result containing the expiration date as a Date object or null if the license information is not available.

````kotlin  
    bridgefy.updateLicense()  
````  

**Description:** Updates the Bridgefy license, if necessary.


### Bridgefy Session
Bridgefy SDK, providing various methods and properties for managing connections and secure communication.

#### Properties:

`bridgefy.isStarted: Boolean` Indicates whether the Bridgefy SDK is currently started.

#### Bridgefy Methods

````kotlin  
   bridgefy.destroySession()  
````  
**Description:** Destroys the current session, terminating any active connections and cleaning up resources.
```kotlin  
    bridgefy.currentUserId() : Result<UUID>  
```  
**Description:** Retrieves the UUID of the current Bridgefy user.<br>  
**Returns:** A Result containing the UUID of the current user or an error message.


### Nearby peer detection

The following method is invoked when a peer has established connection:

```kotlin  
    val delegate: BridgefyDelegate = object : BridgefyDelegate {  
  
    override fun onConnected(peerID: UUID) {  
        ...  
    }  
  
}  
```  

**peerID**: Identifier of the user that has established a connection.


When a peer is disconnected(out of range), the following method will be invoked:

```kotlin  
    val delegate: BridgefyDelegate = object : BridgefyDelegate {  
  
    override fun onDisconnected(peerID: UUID) {  
        ...  
    }  
  
}  
```  

**peerID**: Identifier of the disconnected user.



When a device is detected, notifies the list of connected users:

```kotlin  
    val delegate: BridgefyDelegate = object : BridgefyDelegate {  
  
    /**  
     * On connected peers  
     *  
     * @param connectedPeers  
     */  
    fun onConnectedPeers(connectedPeers: List<UUID>) {  
        ...  
    }  
  
}  
```  

**connectedPeers**: List of identifiers of the connected user.

#### Bridgefy method

````kotlin  
    val bridgefy: Bridgefy  
  bridgefy.connectedPeers() : Result<List<UUID>?>  
````  

**Description:** Retrieves a list of UUIDs representing the connected peers in the current session.<br>  
**Returns:** A Result containing a list of UUIDs of connected peers or null if the service is not started.

### Send data

The following method is used to send data using a transmission mode. This method returns the message ID (**messageID**) to the client.

```kotlin  
import me.bridgefy.commons.TransmissionMode  
// Bridgefy instance  
val bridgefy: Bridgefy  
  
// After start sdk, send ByteArray  
val messageId = bridgefy.send(  
    "Sample text!".toByteArray(Charsets.UTF_8),  
    TransmissionMode.P2P(peerID)  
)  
```  

**messageID**: Unique identifier related to the message.

### Transmission Modes:

```kotlin  
open class TransmissionMode {  
    /**  
     * Broadcast type propagate message on mesh network  
     */  
    data class Broadcast(val sender: UUID) : TransmissionMode()  
  
    /**  
     * Mesh type propagate message and find receiver on mesh network  
     *  
     */  
    data class Mesh(val receiver: UUID) : TransmissionMode()  
  
    /**  
     * Direct type allow direct message and if receiver isn't connected,  
     * the SDK change and propagate message with Mesh type  
     *  
     */  
    data class P2P(val receiver: UUID) : TransmissionMode()  
}  
```  

There are several modes for sending packets:

**P2P(val receiver: String)**: Sends the packet only when the receiver is in range. <br>  
**Mesh(val receiver: String)**: Sends the packet using mesh to only once receiver. It doesn't need the receiver to be in range. Receiver can be in range of a third receiver located within range of both sender and receiver at the same time, or receiver can be out of range of all other nodes, but eventually come within range of a node that at some point received the packet. Mesh messages can be received my multiple nodes, but can only be read by the intended receiver. <br>  
**Broadcast**: Sends a packet using mesh without a defined receiver. The packet is broadcast to all nearby users that are in range, who then broadcast it to all receivers that are in their range, and so on. If a user isn't in range, the packet will be delivered the next time said user comes within range of another user who did receive the packet. Broadcast messages can be read by all nodes that receive it. <br>

If there is no error when sending the message, will be received with the message id (**messageID**)

```kotlin  
    val delegate: BridgefyDelegate = object : BridgefyDelegate {  
    /**  
     * On send  
     *  
     * @param messageID  
     */  
    override fun onSend(messageID: UUID) {  
        ...  
    }  
  
}  
```  

otherwise, the following method will be received

```kotlin  
    val delegate: BridgefyDelegate = object : BridgefyDelegate {  
    /**  
     * On fail to send  
     *  
     * @param messageID  
     */  
    override fun onFailToSend(messageID: UUID) {  
        ...  
    }  
}  
```  

### Direct and Mesh transmission

Direct transmission is a mechanism used to deliver packets to a user that is nearby or visible (a connection has been detected).  
Mesh transmission is a mechanism used to deliver offline packets even when the receiving user isn’t nearby or visible. It can be achieved taking advantage of other nearby peers; these receive the package, hold it, and forward to other peers trying to find the receiver.

### Receive Data

When a packet has been received, the following method will be invoked:

```kotlin  
  
val delegate: BridgefyDelegate = object : BridgefyDelegate {  
    /**  
     * On receive  
     *  
     * @param data  
     * @param messageID  
     * @param transmissionMode  
     */  
    override fun onReceiveData(  
  data: ByteArray,  
        messageID: UUID,  
        transmissionMode: TransmissionMode,  
    ) {  
        ...  
    }  
}  
  
```  

**data**: Received Data object <br>  
**messageID**: Unique identifier related to the message. <br>  
**transmissionMode**: The transmission mode used when sending the message <br>

### Propagation Profiles

```kotlin  
/**  
 * Propagation profile  
 */  
enum class PropagationProfile {  
  Standard,  
    HighDensityEnvironment,  
    SparseEnvironment,  
    LongReach,  
    ShortReach  
}  
  
```  

| **Profile** | **Hops limit** |   **TTL(s)** | **Sharing Time** | **Maximum Propagation** | **Track list limit** |  
|--------------------------|:--------------:|:--------------:|:----------------:|:-----------------------:|:--------------------:|  
| Standard                 |      100       |  86400 (1 d) |      15000       |           200           |          50          |  
| High Density Environment |       50       |   3600 (1 h) |      10000       |           50            |          50          |  
| Sparse Environment       |      100       | 302400 (3.5 d) |      10000       |           250           |          50          |  
| Long Reach               |      250       |  604800 (7 d) |      15000       |          1000           |          50          |  
| Short Reach              |       50       |      1800      |      10000       |           50            |          50          |  

- **Hops limit:** The maximum number of hops a message can get. Each time a message is forwarded, is considered a hop.
- **TTL:** Time to live, is the maximum amount of time a message can be propagated since its creation.
- **Sharing time:** The maximum amount of time a message will be kept for forwarding.
- **Maximum propagation:** The maximum number of times a message will be forwarded from a device.
- **Track list limit:** The maximum number of UUID’s stored in an array to prevent sending the message to a peer which already forwarded the message.

## Secure connections

Part of Bridgefy's functionality is its ability to provide a secure connection for sending data within a mesh network. To ensure the privacy and security of sensitive data, Bridgefy SDK employs encryption techniques. Encryption involves transforming data into an unreadable format, which can only be deciphered by authorized recipients who possess the correct decryption key.

Bridgefy utilizes the Signal Protocol, a widely recognized and trusted encryption protocol, to encrypt sensitive data exchanged between devices in a mesh network. The Signal Protocol provides end-to-end encryption, meaning the data remains encrypted throughout its entire journey from the sender to the intended recipient. This ensures that even if someone intercepts the data, they won't be able to access its contents without the proper decryption key.

> However, companies and developers who use the Bridgefy SDK in their mobile apps also have the option to implement their own custom encryption if they prefer,  
> which doesn't require the establishment of a secure connection but needs robust encryption-key management practices


### Established secure connection

Bridgefy SDK offers the option to establish secure connections within the mesh network, encrypting the data traveling on the  
mesh using the Signal protocol. This ensures a secure connection and protects the data from unauthorized access.

When node is connected you can try to establishing a secure connection with follow method:

````kotlin  
  
// Bridgefy instance  
val bridgefy: Bridgefy  
  
bridgefy.establishSecureConnection(connectedNodeID: UUID)  
  
````  

**Throws:** A BridgefyException if there is an error during the connection establishment.


These methods are used to handle events related to the establishment of on-demand secure connections.  
The `onEstablishSecureConnection` function is invoked when a secure connection is successfully established,  
while the `onFailToEstablishSecureConnection` function is called when the establishment of a secure connection fails,  
providing details about the user involved and the reason for the failure.

````kotlin  
 val delegate: BridgefyDelegate = object : BridgefyDelegate {  
  
      /**  
       * This function is called to notify when an on-demand secure connection was successfully established.  
       *  
       * - userId: The ID of the user with whom the secure connection was established.  
       */  
      fun onEstablishSecureConnection(userId: UUID)  
       /**  
       * This function is called to notify when an on-demand secure connection could not be established.  
       *  
       * - userId: The ID of the user with whom the secure connection failed.  
       * - error: The error reason indicating why the secure connection failed.  
       */  
      fun onFailToEstablishSecureConnection(  
  userId: UUID,  
         error: BridgefyException,  
      )  
  }  
````  


> Bridgefy also allows users to implement their own custom encryption if they prefer not to use the Signal Protocol.  
> In such cases, where custom encryption is utilized, it's important to note that Bridgefy does not require the establishment of a secure connection between devices.

#### Bridgefy Method
```kotlin  
   bridgefy.fingerprint(userId: UUID): Result<BridgefyFingerprint>  
```  
**Description:** Generates a fingerprint for the secure connection established with a specified user.

**Parameters:**<br>
* **userId:** UUID - The UUID of the user for whom the fingerprint should be generated.

**Returns:** A Result containing the generated BridgefyFingerprint object or an error message. Returns null if a secure connection hasn't been established with the user.

````kotlin  
   bridgefy.isFingerprintValid(  
         fingerprintData: ByteArray,  
         userId: UUID,  
    ) : Result<Boolean>  
````  
**Description:** Verifies the validity of a fingerprint for a particular user.<br>  
**Parameters:**<br>
* **fingerprintData:** ByteArray - The fingerprint data to be verified as a ByteArray.<br>
* **userId:** UUID - The UUID of the user whose fingerprint is being verified.<br>

**Returns:** A Result containing true if the provided fingerprint data is valid, or false otherwise.<br>

### Recommendations for using a secure connection

While it's common to think that a secure connection needs to be established simultaneously with other connections, it's not always the case. In fact, many secure connection protocols allow for the establishment of connections at different times, depending on the specific requirements and circumstances.

By recommending a secure connection that is not simultaneous, it means that the focus should be on establishing a secure connection whenever it is feasible and appropriate, without being bound to the constraints of synchronizing the connection with other devices.

This approach allows for flexibility and adaptability in securing the connection. For example, a device may establish a secure connection with one device at a time, ensuring that each connection is properly encrypted and authenticated. This way, the security of the connection is not compromised, even if it takes place at different times.

Additionally, requesting a secure connection that is not simultaneous can also be beneficial in terms of resource management. Establishing secure connections simultaneously with multiple devices may impose a heavier burden on the device's resources, such as processing power and network bandwidth. By prioritizing security over simultaneous connections, the device can allocate resources effectively and maintain a high level of security for each connection.

## Using ProGuard

If you are using ProGuard in your project, include the following lines to your configuration file:

```gradle  
 -keep class me.bridgefy.** { public *; }  
 -dontwarn me.bridgefy.**  
 -keepclassmembernames class me.bridgefy.crypto.** { public *; }  
 -keep, allowshrinking class org.signal.libsignal.** { public *; }  
 -dontwarn me.bridgefy.crypto.**  
```  

## Supported Devices

Bridgefy's support for devices extends to smartphones and tablets, as long as they run iOS or Android, and have BLE. This makes Bridgefy a versatile platform that caters to various communication needs. By enabling connectivity across a diverse range of devices, Bridgefy ensures that users can easily establish connections and exchange information regardless of whether they have an internet connection or not.

## Contact & Support
+ contact@bridgefy.me

© 2024 Bridgefy Inc. All rights reserved
