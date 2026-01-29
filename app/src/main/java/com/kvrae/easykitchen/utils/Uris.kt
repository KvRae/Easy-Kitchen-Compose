package com.kvrae.easykitchen.utils
import com.kvrae.easykitchen.BuildConfig


//const val BASE_URL_TEST = BuildConfig.BASE_URL_TEST
const val BASE_URL =  BuildConfig.BASE_URL
const val CHAT_API_KEY = BuildConfig.API_KEY

const val INGREDIENT_IMAGE_URL = "https://www.themealdb.com/images/ingredients/"
const val CATEGORIES_URL = "${BASE_URL}categories"
const val INGREDIENTS_URL = "${BASE_URL}ingredients"
const val MEALS_URL = "${BASE_URL}food"

const val LOGIN_URL = "${BASE_URL}login"
const val GOOGLE_LOGIN_URL = "${BASE_URL}auth/google"
const val REGISTER_URL = "${BASE_URL}register"

const val FORGET_PASSWORD_URL = "${BASE_URL}forgot"
const val VERIFY_OTP_URL = "${BASE_URL}verify"
const val RESET_PASSWORD_URL = "${BASE_URL}reset"

// Server Health Check
const val HEALTH_CHECK_URL = BASE_URL  // Ping base URL to wake up server

