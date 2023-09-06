package me.bridgefy.example.android.alerts.ux.onboarding

import me.bridgefy.example.android.alerts.R

class OnBoardingItems(
    val imageResource: Int?,
    val titleResource: Int?,
    val descriptionResource: Int?,
    val warningResource: Int?,
) {
    companion object {
        fun getData(): List<OnBoardingItems> {
            return listOf(
                OnBoardingItems(
                    R.drawable.ic_launcher_foreground,
                    R.string.on_boarding_title_1,
                    R.string.on_boarding_desc_1,
                    null,
                ),
                OnBoardingItems(
                    R.drawable.bt_onboarding,
                    R.string.on_boarding_title_2,
                    R.string.on_boarding_desc_2,
                    R.string.on_boarding_warning_2,
                ),
                OnBoardingItems(
                    R.drawable.ic_launcher_foreground,
                    R.string.on_boarding_title_3,
                    R.string.on_boarding_desc_3,
                    null,
                ),
            )
        }
    }
}
