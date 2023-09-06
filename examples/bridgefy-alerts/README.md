# Bridgefy SDK Example Project (Alerts)

This example project demonstrates how to integrate and use the Bridgefy SDK in a Kotlin-based Android application. The Bridgefy SDK allows developers to enable offline communication and data transfer using Bluetooth Low Energy (BLE) mesh networks.

## Description

The example application in this repository showcases how to:

- Set up the Bridgefy SDK in your project.
- Initialize and start using the SDK.
- Send and receive data through the Bridgefy mesh network.

## Installation

To use the Bridgefy SDK in your project, follow these steps:

1. Clone this repository to your local machine.

2. Open the project in your favourite IDE.

3. Follow the installation instructions provided in the official Bridgefy SDK documentation to integrate the library into your project.

   https://github.com/bridgefy/sdk-android

4. Ensure you've configured the necessary permissions in your project's `Manifest.xml` file, as described in the Bridgefy documentation.

5. Run the application on a Bluetooth Low Energy (BLE)-compatible device.

## Configuration

To set up and use the Bridgefy SDK in your application, follow these steps:

1. Change the Bridgefy SDK API Key with your own API key in the `build.gradle.kts` file, line 19 and run it.

   ```kotlin
   val Bridgefy_API_Key = "REPLACE_WITH_YOUR_API_KEY"