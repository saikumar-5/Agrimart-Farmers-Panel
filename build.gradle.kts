// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}

buildscript {
    repositories {
        google()  // Make sure the Google repository is included
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.8.1") // Replace with your version
        classpath("com.google.gms:google-services:4.4.2")  // Add this line
    }
}


allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
