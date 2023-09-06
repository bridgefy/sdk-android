@file:Suppress("UnstableApiUsage")

import java.util.Date

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("org.dbtools.license-manager")
    id("de.undercouch.download") version "5.4.0"
    id("com.spotify.ruler")
    id("org.gradle.jacoco")
    alias(libs.plugins.ksp)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlin.serialization)
}

val sdkProdVersion = "REPLACE_WITH_YOUR_API_KEY"

android {
    namespace = "me.bridgefy.example.android.alerts"

    compileSdk = AppInfo.AndroidSdk.COMPILE

    defaultConfig {
        minSdk = AppInfo.AndroidSdk.MIN
        targetSdk = AppInfo.AndroidSdk.TARGET

        applicationId = AppInfo.APPLICATION_ID
        versionCode = AppInfo.Version.CODE
        versionName = AppInfo.Version.NAME

        manifestPlaceholders["BfSDKKey"] = sdkProdVersion

        buildConfigField("String", "BUILD_NUMBER", "\"${System.getProperty("BUILD_NUMBER")}\"")
        buildConfigField("String", "API_KEY", "\"$sdkProdVersion\"")

        // used by Room, to test migrations
        ksp {
            arg("room.schemaLocation", "$projectDir/schema")
            arg("room.incremental", "true")
            arg("room.generateKotlin", "true") // generate kotlin code (requires Room 2.6.x)
//            arg("room.useNullAwareTypeAnalysis", "false") // optional param to turn OFF TypeConverter analyzer (introduced in Room 2.4.0-beta02)
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
        version = "1.1.0",
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
    kapt(libs.google.hilt.compiler)

    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.work)

    // Android Architecture Components
    implementation(libs.androidx.lifecycle.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)

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

    implementation(libs.androidx.constraintlayout)
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
    testImplementation(libs.truth)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.okhttp.mockwebserver)
    testImplementation(libs.dbtools.roomJdbc)
    testImplementation(libs.xerial.sqlite)

    kaptTest(libs.dagger.compiler)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jacoco {
    toolVersion = libs.versions.jacoco.get().toString()
}

val jacocoTestReport = tasks.create("jacocoTestReport")

androidComponents.onVariants { variant ->
    val testTaskName = "test${variant.name.capitalize()}UnitTest"
    val reportTask =
        tasks.register("jacoco${testTaskName.capitalize()}Report", JacocoReport::class) {
            dependsOn(testTaskName)

            reports {
                xml.required.set(true)
                html.required.set(true)
            }

            classDirectories.setFrom(
                fileTree("$buildDir/tmp/kotlin-classes/${variant.name}") {
                    exclude(
                        listOf(
                            // Android
                            "**/R.class",
                            "**/R\$*.class",
                            "**/BuildConfig.*",
                            "**/Manifest*.*",
                            // App Specific
                        ),
                    )
                },
            )

            sourceDirectories.setFrom(
                files(
                    "$projectDir/src/main/java",
                    "$projectDir/src/main/kotlin",
                ),
            )
            executionData.setFrom(file("$buildDir/jacoco/$testTaskName.exec"))
        }

    jacocoTestReport.dependsOn(reportTask)
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

detekt {
    allRules = true
    buildUponDefaultConfig = true
    config.setFrom(files("$projectDir/build/config/detektConfig.yml"))
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    exclude("**/ui/compose/icons/**")

    exclude("**/about/samples/**")

    reports {
        html.required.set(true)
        xml.required.set(true)
        txt.required.set(true)
    }
}

licenseManager {
    variantName = "release"
    outputDirs = listOf("./src/main/assets", "./build/licenses")
    excludeGroups = listOf("org.dbtools")
    invalidLicensesUrl =
        "https://raw.githubusercontent.com/ICSEng/AndroidPublic/main/license/invalid-licenses.json"
}
