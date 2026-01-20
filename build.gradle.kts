// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.serilization) apply false
    alias(libs.plugins.google.ksp) apply false
    alias { libs.plugins.compose.compiler } apply false
    id("androidx.room") version "2.8.4" apply false
}