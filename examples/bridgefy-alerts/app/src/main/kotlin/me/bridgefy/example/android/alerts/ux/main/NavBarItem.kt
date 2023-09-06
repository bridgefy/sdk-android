package me.bridgefy.example.android.alerts.ux.main

import androidx.annotation.StringRes
import me.bridgefy.example.android.alerts.R
import me.bridgefy.example.android.alerts.ux.color.SendColorRoute
import me.bridgefy.example.android.alerts.ux.flash.SendFlashRoute
import me.bridgefy.example.android.alerts.ux.image.SendImageRoute
import me.bridgefy.example.android.alerts.ux.sound.SendSoundRoute
import me.bridgefy.example.android.alerts.ux.text.SendTextRoute

enum class NavBarItem(
    val unselectedImage: Int,
    val selectedImage: Int,
    val route: String,
    @StringRes val textResId: Int? = null,
) {
    IMAGE(R.drawable.image_outlined, R.drawable.image_filled, SendImageRoute.createRoute(), R.string.image_nav),
    COLOR(R.drawable.palette_outlined, R.drawable.palette_filled, SendColorRoute.createRoute(), R.string.color_nav),
    TEXT(R.drawable.text_outlined, R.drawable.text_filled, SendTextRoute.createRoute(), R.string.text_nav),
    FLASH(R.drawable.flash_outlined, R.drawable.flash_filled, SendFlashRoute.createRoute(), R.string.flash_nav),
    SOUND(R.drawable.sound_outlined, R.drawable.sound_filled, SendSoundRoute.createRoute(), R.string.sound_nav),
    ;

    companion object {
        fun getNavBarItemRouteMap(): Map<NavBarItem, String> {
            return values().associateWith { item -> item.route }
        }
    }
}
