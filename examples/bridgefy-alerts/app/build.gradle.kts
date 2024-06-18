@file:Suppress("UnstableApiUsage")

import java.util.Date

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.gms)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.appdistribution)
//    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.download)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kover)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.detekt)
    alias(libs.plugins.playPublisher)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.licenseManager)
    id("com.spotify.ruler")
}

val Bridgefy_API_Key = "REPLACE_WITH_YOUR_API_KEY"

android {
    namespace = "me.bridgefy.example.android.alerts"

    compileSdk = AppInfo.AndroidSdk.COMPILE

    defaultConfig {
        minSdk = AppInfo.AndroidSdk.MIN
        targetSdk = AppInfo.AndroidSdk.TARGET

        applicationId = AppInfo.APPLICATION_ID
        versionCode = AppInfo.Version.CODE
        versionName = AppInfo.Version.NAME

        manifestPlaceholders["BfSDKKey"] = Bridgefy_API_Key

        buildConfigField("String", "BUILD_NUMBER", "\"${System.getProperty("BUILD_NUMBER")}\"")
        buildConfigField("String", "API_KEY", "\"$Bridgefy_API_Key\"")

        room {
            schemaDirectory("$projectDir/schema")
        }

        // used by Room, to test migrations
        ksp {
            // options that are not yet in the Room Gradle plugin
            // https://developer.android.com/jetpack/androidx/releases/room#gradle-plugin
            arg("room.incremental", "true")
            arg("room.generateKotlin", "true")
        }

        // for use with Room gradle plugin
//        room {
//
//        }

        // Integration tests
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xopt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-Xopt-in=androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi",
            "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi",
            "-Xopt-in=androidx.compose.ui.ExperimentalComposeUiApi",
            "-Xopt-in=kotlin.experimental.ExperimentalTypeInference",
            "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi",
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",

            // use the following to ignore enforcement version of Kotlin with Compose
            // "-P", "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        )
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
    }

    lint {
        ignoreTestSources = true
        abortOnError = true
        disable.addAll(listOf("InvalidPackage", "DialogFragmentCallbacksDetector"))
    }

    buildTypes {
        val debug by getting {
            versionNameSuffix = " DEV"
            buildConfigField("long", "BUILD_TIME", "0l")
        }
        val release by getting {
            versionNameSuffix = ""
            buildConfigField("long", "BUILD_TIME", "${Date().time}l")
            // R8
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    sourceSets {
        getByName("main") {
            java.srcDir("src/main/kotlin")
        }
        getByName("test") {
            java.srcDir("src/test/kotlin")
        }
        getByName("androidTest") {
            assets.srcDir("$projectDir/schemas")
        }
    }
}

dependencies {

    implementation(
        group = "me.bridgefy",
        name = "android-sdk",
        version = "1.1.4",
        ext = "aar",
    ) {
        isTransitive = true
    }

    implementation(libs.androidx.preference)

    // Android
    coreLibraryDesugaring(libs.android.desugar)
    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.datastorePrefs)

    // Compose
    implementation(libs.compose.ui)
    debugImplementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material3.windowsize)

    // Code
    implementation(libs.kotlin.serialization.json)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.okio)
    implementation(libs.timber)

    // Inject
    implementation(libs.google.hilt.library)
    ksp(libs.google.hilt.compiler)

    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.work)

    // Android Architecture Components
    implementation(libs.androidx.lifecycle.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.work.runtime)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.dbtools.room)

    // Network
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.okhttp.loggingInterceptor)
    implementation(libs.retrofit)

    implementation(libs.androidx.compose.constraintlayout)
    implementation(libs.androidx.compose.material)
    implementation(libs.google.pager)
    implementation(libs.google.permissions)

    // Test (Integration)
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.test.manifest)

    // Test (Unit)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.mockK)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.okhttp.mockwebserver)
    testImplementation(libs.dbtools.roomJdbc)
    testImplementation(libs.xerial.sqlite)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// ===== Kover (JUnit Coverage Reports) =====
// ./gradlew koverHtmlReportDebug
// ./gradlew koverXmlReportDebug
// ./gradlew koverVerifyDebug
koverReport {
    filters {
        excludes {
            packages(
                "*hilt_aggregated_deps*",
                "*codegen*",
                // App Specific
                "me.bridgefy.example.android.alerts.ui",
            )

            classes(
                "*Fragment",
                "*Fragment\$*",
                "*Activity",
                "*Activity\$*",
                "*.databinding.*",
                "*.BuildConfig",
                "*Factory",
                "*_HiltModules*",
                "*_Impl*",
                "*ComposableSingletons*",
                "*Hilt*",
                "*Initializer*",
                // App Specific
                "*MainAppScaffoldWithNavBarKt*",
            )

            annotatedBy(
                "*Composable*",
                "*HiltAndroidApp*",
                "*HiltViewModel*",
                "*HiltWorker*",
                "*AndroidEntryPoint*",
                "*Module*",
                "*SuppressCoverage*",
            )
        }
    }

    verify {
        rule {
            minBound(25) // minimum percent coverage without failing build (Line percent)
        }
    }
}

ruler {
    abi.set("arm64-v8a")
    locale.set("en")
    screenDensity.set(375)
    sdkVersion.set(31)
}

tasks.register<de.undercouch.gradle.tasks.download.Download>("downloadDetektConfig") {
    download {
        onlyIf { !file("$projectDir/build/config/detektConfig.yml").exists() }
        src("https://raw.githubusercontent.com/ICSEng/AndroidPublic/main/detekt/detektConfig-20230629.yml")
        dest("$projectDir/build/config/detektConfig.yml")
    }
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    this.jvmTarget = "17"
    dependsOn("downloadDetektConfig")
}

// ./gradlew detekt
detekt {
    allRules = true // fail build on any finding
    buildUponDefaultConfig = true // preconfigure defaults
    // point to your custom config defining rules to run, overwriting default behavior
    config.from(files("$projectDir/build/config/detektConfig.yml"))
//    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        html.required.set(true) // observe findings in your browser with structure and code snippets
        xml.required.set(true) // checkstyle like format mainly for integrations like Jenkins
        txt.required.set(true) // similar to the console output, contains issue signature to manually edit baseline files
    }
}

// ./gradlew createLicenseReports
// ./gradlew --stacktrace -i createLicenseReports
licenseManager {
    variantName = "release"
    outputDirs = listOf("./src/main/assets", "./build/licenses")
    excludeGroups = listOf("org.dbtools")
    invalidLicensesUrl =
        "https://raw.githubusercontent.com/ICSEng/AndroidPublic/main/license/invalid-licenses.json"
}
