buildscript {
    dependencies {
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.50")
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id ("com.android.application") version "8.3.0" apply false
    id ("com.android.library") version "8.3.0" apply false
    id ("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id ("org.jetbrains.kotlin.plugin.serialization") version "1.8.10" apply false
    alias(libs.plugins.google.gms.google.services) apply false
}