package me.bridgefy.example.android.alerts.ui.theme

import androidx.compose.ui.graphics.Color

// Colors based on the following colors -> https://m3.material.io/theme-builder#/custom
// Primary ba1a20
// Secondary 680008
// Tertiary a88d5b
// Neutral 998e8d

object AppPalette {
    val md_theme_light_primary = Color(0xFFBA1A20)
    val md_theme_light_onPrimary = Color(0xFFFFFFFF)
    val md_theme_light_primaryContainer = Color(0xFFFFDAD6)
    val md_theme_light_onPrimaryContainer = Color(0xFF410003)
    val md_theme_light_secondary = Color(0xFFAA3430)
    val md_theme_light_onSecondary = Color(0xFFFFFFFF)
    val md_theme_light_secondaryContainer = Color(0xFFFFDAD6)
    val md_theme_light_onSecondaryContainer = Color(0xFF410003)
    val md_theme_light_tertiary = Color(0xFF725B2E)
    val md_theme_light_onTertiary = Color(0xFFFFFFFF)
    val md_theme_light_tertiaryContainer = Color(0xFFFEDEA6)
    val md_theme_light_onTertiaryContainer = Color(0xFF261900)
    val md_theme_light_error = Color(0xFFBA1A1A)
    val md_theme_light_errorContainer = Color(0xFFFFDAD6)
    val md_theme_light_onError = Color(0xFFFFFFFF)
    val md_theme_light_onErrorContainer = Color(0xFF410002)
    val md_theme_light_background = Color(0xFFFFFBFF)
    val md_theme_light_onBackground = Color(0xFF201A19)
    val md_theme_light_surface = Color(0xFFFFFBFF)
    val md_theme_light_onSurface = Color(0xFF201A19)
    val md_theme_light_surfaceVariant = Color(0xFFF5DDDB)
    val md_theme_light_onSurfaceVariant = Color(0xFF534342)
    val md_theme_light_outline = Color(0xFF857371)
    val md_theme_light_inverseOnSurface = Color(0xFFFBEEEC)
    val md_theme_light_inverseSurface = Color(0xFF362F2E)
    val md_theme_light_inversePrimary = Color(0xFFFFB3AC)
    val md_theme_light_shadow = Color(0xFF000000)
    val md_theme_light_surfaceTint = Color(0xFFBA1A20)
    val md_theme_light_outlineVariant = Color(0xFFD8C2BF)
    val md_theme_light_scrim = Color(0xFF000000)

    val md_theme_dark_primary = Color(0xFFFFB3AC)
    val md_theme_dark_onPrimary = Color(0xFF680008)
    val md_theme_dark_primaryContainer = Color(0xFF930010)
    val md_theme_dark_onPrimaryContainer = Color(0xFFFFDAD6)
    val md_theme_dark_secondary = Color(0xFFFFB3AC)
    val md_theme_dark_onSecondary = Color(0xFF680008)
    val md_theme_dark_secondaryContainer = Color(0xFF891C1C)
    val md_theme_dark_onSecondaryContainer = Color(0xFFFFDAD6)
    val md_theme_dark_tertiary = Color(0xFFE1C38C)
    val md_theme_dark_onTertiary = Color(0xFF402D04)
    val md_theme_dark_tertiaryContainer = Color(0xFF584419)
    val md_theme_dark_onTertiaryContainer = Color(0xFFFEDEA6)
    val md_theme_dark_error = Color(0xFFFFB4AB)
    val md_theme_dark_errorContainer = Color(0xFF93000A)
    val md_theme_dark_onError = Color(0xFF690005)
    val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
    val md_theme_dark_background = Color(0xFF201A19)
    val md_theme_dark_onBackground = Color(0xFFEDE0DE)
    val md_theme_dark_surface = Color(0xFF201A19)
    val md_theme_dark_onSurface = Color(0xFFEDE0DE)
    val md_theme_dark_surfaceVariant = Color(0xFF534342)
    val md_theme_dark_onSurfaceVariant = Color(0xFFD8C2BF)
    val md_theme_dark_outline = Color(0xFFA08C8A)
    val md_theme_dark_inverseOnSurface = Color(0xFF201A19)
    val md_theme_dark_inverseSurface = Color(0xFFEDE0DE)
    val md_theme_dark_inversePrimary = Color(0xFFBA1A20)
    val md_theme_dark_shadow = Color(0xFF000000)
    val md_theme_dark_surfaceTint = Color(0xFFFFB3AC)
    val md_theme_dark_outlineVariant = Color(0xFF534342)
    val md_theme_dark_scrim = Color(0xFF000000)

    val seed = Color(0xFFBA1A20)

    fun lightColors(): AppColors {
        return AppColors(
            primary = md_theme_light_primary,
            onPrimary = md_theme_light_onPrimary,
            primaryContainer = md_theme_light_primaryContainer,
            onPrimaryContainer = md_theme_light_onPrimaryContainer,
            inversePrimary = md_theme_light_inversePrimary,
            secondary = md_theme_light_secondary,
            onSecondary = md_theme_light_onSecondary,
            secondaryContainer = md_theme_light_secondaryContainer,
            onSecondaryContainer = md_theme_light_onSecondaryContainer,
            tertiary = md_theme_light_tertiary,
            onTertiary = md_theme_light_onTertiary,
            tertiaryContainer = md_theme_light_tertiaryContainer,
            onTertiaryContainer = md_theme_light_onTertiaryContainer,
            background = md_theme_light_background,
            onBackground = md_theme_light_onBackground,
            surface = md_theme_light_surface,
            onSurface = md_theme_light_onSurface,
            surfaceVariant = md_theme_light_surfaceVariant,
            onSurfaceVariant = md_theme_light_onSurfaceVariant,
            surfaceTint = md_theme_light_surfaceTint,
            inverseSurface = md_theme_light_inverseSurface,
            inverseOnSurface = md_theme_light_inverseOnSurface,
            error = md_theme_light_error,
            onError = md_theme_light_onError,
            errorContainer = md_theme_light_errorContainer,
            onErrorContainer = md_theme_light_onErrorContainer,
            outline = md_theme_light_outline,
            outlineVariant = md_theme_light_outlineVariant,
            scrim = md_theme_light_scrim,
        )
    }

    fun darkColors(): AppColors {
        return AppColors(
            primary = md_theme_dark_primary,
            onPrimary = md_theme_dark_onPrimary,
            primaryContainer = md_theme_dark_primaryContainer,
            onPrimaryContainer = md_theme_dark_onPrimaryContainer,
            inversePrimary = md_theme_dark_inversePrimary,
            secondary = md_theme_dark_secondary,
            onSecondary = md_theme_dark_onSecondary,
            secondaryContainer = md_theme_dark_secondaryContainer,
            onSecondaryContainer = md_theme_dark_onSecondaryContainer,
            tertiary = md_theme_dark_tertiary,
            onTertiary = md_theme_dark_onTertiary,
            tertiaryContainer = md_theme_dark_tertiaryContainer,
            onTertiaryContainer = md_theme_dark_onTertiaryContainer,
            background = md_theme_dark_background,
            onBackground = md_theme_dark_onBackground,
            surface = md_theme_dark_surface,
            onSurface = md_theme_dark_onSurface,
            surfaceVariant = md_theme_dark_surfaceVariant,
            onSurfaceVariant = md_theme_dark_onSurfaceVariant,
            surfaceTint = md_theme_dark_surfaceTint,
            inverseSurface = md_theme_dark_inverseSurface,
            inverseOnSurface = md_theme_dark_inverseOnSurface,
            error = md_theme_dark_error,
            onError = md_theme_dark_onError,
            errorContainer = md_theme_dark_errorContainer,
            onErrorContainer = md_theme_dark_onErrorContainer,
            outline = md_theme_dark_outline,
            outlineVariant = md_theme_dark_outlineVariant,
            scrim = md_theme_dark_scrim,
        )
    }
}
