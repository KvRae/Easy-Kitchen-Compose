package com.kvrae.easykitchen.di

import android.util.Log
import com.kvrae.easykitchen.data.local.dao.MealDao
import com.kvrae.easykitchen.data.local.database.EasyKitchenDb
import com.kvrae.easykitchen.data.remote.datasource.CategoryRemoteDataSource
import com.kvrae.easykitchen.data.remote.datasource.CategoryRemoteDataSourceImpl
import com.kvrae.easykitchen.data.remote.datasource.GeminiRemoteDataSource
import com.kvrae.easykitchen.data.remote.datasource.GeminiRemoteDataSourceImpl
import com.kvrae.easykitchen.data.remote.datasource.IngredientRemoteDataSource
import com.kvrae.easykitchen.data.remote.datasource.IngredientRemoteDataSourceImpl
import com.kvrae.easykitchen.data.remote.datasource.LoginRemoteDataSource
import com.kvrae.easykitchen.data.remote.datasource.LoginRemoteDataSourceImpl
import com.kvrae.easykitchen.data.remote.datasource.MealRemoteDataSource
import com.kvrae.easykitchen.data.remote.datasource.MealsRemoteDataSourceImpl
import com.kvrae.easykitchen.data.remote.datasource.RegisterRemoteDataSource
import com.kvrae.easykitchen.data.remote.datasource.RegisterRemoteDataSourceImpl
import com.kvrae.easykitchen.data.repository.AuthRepository
import com.kvrae.easykitchen.data.repository.AuthRepositoryImpl
import com.kvrae.easykitchen.data.repository.CategoryRepository
import com.kvrae.easykitchen.data.repository.CategoryRepositoryImpl
import com.kvrae.easykitchen.data.repository.GeminiRepository
import com.kvrae.easykitchen.data.repository.GeminiRepositoryImpl
import com.kvrae.easykitchen.data.repository.IngredientRepository
import com.kvrae.easykitchen.data.repository.IngredientRepositoryImpl
import com.kvrae.easykitchen.data.repository.LocationRepository
import com.kvrae.easykitchen.data.repository.LocationRepositoryImpl
import com.kvrae.easykitchen.data.repository.LoginRepository
import com.kvrae.easykitchen.data.repository.LoginRepositoryImpl
import com.kvrae.easykitchen.data.repository.MealRepository
import com.kvrae.easykitchen.data.repository.MealRepositoryImpl
import com.kvrae.easykitchen.data.repository.RegisterRepository
import com.kvrae.easykitchen.data.repository.RegisterRepositoryImpl
import com.kvrae.easykitchen.domain.usecases.FilterMealsByAreaUseCase
import com.kvrae.easykitchen.domain.usecases.FilterMealsByIngredientsUseCase
import com.kvrae.easykitchen.domain.usecases.GeminiChatUseCase
import com.kvrae.easykitchen.domain.usecases.GetCategoryUseCase
import com.kvrae.easykitchen.domain.usecases.GetGoogleSignInClientUseCase
import com.kvrae.easykitchen.domain.usecases.GetIngredientsUseCase
import com.kvrae.easykitchen.domain.usecases.GetMealsUseCase
import com.kvrae.easykitchen.domain.usecases.GetSignInIntentUseCase
import com.kvrae.easykitchen.domain.usecases.GetUserLocationUseCase
import com.kvrae.easykitchen.domain.usecases.HandleSignInResultUseCase
import com.kvrae.easykitchen.domain.usecases.LoginUseCase
import com.kvrae.easykitchen.domain.usecases.RegisterUseCase
import com.kvrae.easykitchen.domain.usecases.SendIdTokenToBackendUseCase
import com.kvrae.easykitchen.presentation.chat.ChatViewModel
import com.kvrae.easykitchen.presentation.filtered_meals.FilteredMealsViewModel
import com.kvrae.easykitchen.presentation.home.HomeViewModel
import com.kvrae.easykitchen.presentation.ingrendient.IngredientViewModel
import com.kvrae.easykitchen.presentation.login.GoogleAuthViewModel
import com.kvrae.easykitchen.presentation.login.LoginViewModel
import com.kvrae.easykitchen.presentation.meals.MealsViewModel
import com.kvrae.easykitchen.presentation.register.RegisterViewModel
import com.kvrae.easykitchen.utils.UserPreferencesManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
            install(ResponseObserver) {
                onResponse { response ->
                    Log.i("HTTP status:", "${response.status.value}")
                }
            }
            install(DefaultRequest) {
                header("Content-Type", "application/json")
                header("Accept", "application/json")
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 10000
                connectTimeoutMillis = 10000
                socketTimeoutMillis = 10000
            }
            engine {
                connectTimeout = 100_000
            }
        }
    }
}

val dataModule = module {

    single<LoginRemoteDataSource> { LoginRemoteDataSourceImpl(get()) }
    single<LoginRepository> { LoginRepositoryImpl(get()) }

    single<RegisterRemoteDataSource> { RegisterRemoteDataSourceImpl(get()) }
    single<RegisterRepository> { RegisterRepositoryImpl(get()) }

    single<IngredientRemoteDataSource> { IngredientRemoteDataSourceImpl(get()) }
    single<IngredientRepository> { IngredientRepositoryImpl(get()) }

    single<CategoryRemoteDataSource> {CategoryRemoteDataSourceImpl(get())}
    single<CategoryRepository> { CategoryRepositoryImpl(get())  }

    single<MealRemoteDataSource> {MealsRemoteDataSourceImpl(get())}

    single<MealRepository> {
        MealRepositoryImpl(
            get<MealRemoteDataSource>(),
            get<MealDao>()
        )
    }


    single<AuthRepository> { AuthRepositoryImpl(get()) }

    single<GeminiRemoteDataSource> { GeminiRemoteDataSourceImpl() }
    single<GeminiRepository> { GeminiRepositoryImpl(get()) }

    single<LocationRepository> { LocationRepositoryImpl(get()) }

    single { UserPreferencesManager(get()) }
}

val domainModule = module {
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { GetIngredientsUseCase(get()) }
    factory { GetCategoryUseCase(get()) }
    factory { GetMealsUseCase(get()) }
    factory { GetUserLocationUseCase(get()) }
    factory { FilterMealsByAreaUseCase() }
    factory { FilterMealsByIngredientsUseCase() }
    factory { GetGoogleSignInClientUseCase(get()) }
    factory { GetSignInIntentUseCase(get()) }
    factory { HandleSignInResultUseCase(get()) }
    factory { SendIdTokenToBackendUseCase(get()) }
    factory { GeminiChatUseCase(get()) }
}

val presentationModule = module {
    viewModel { MealsViewModel(get()) }
    single { IngredientViewModel(get()) }
    viewModel { FilteredMealsViewModel(get(), get()) }
    viewModel { LoginViewModel(
        get<LoginUseCase>(),
        get<UserPreferencesManager>()
    ) }
    viewModel { RegisterViewModel(get()) }
    viewModel {
        HomeViewModel(
            get<GetMealsUseCase>(),
            get<GetCategoryUseCase>(),
            get<GetUserLocationUseCase>(),
            get<FilterMealsByAreaUseCase>()
        )
    }
    viewModel {
        GoogleAuthViewModel(
            get<GetGoogleSignInClientUseCase>(),
            get<GetSignInIntentUseCase>(),
            get<HandleSignInResultUseCase>(),
            get<SendIdTokenToBackendUseCase>()
        )
    }
    viewModel { ChatViewModel(get<GeminiChatUseCase>()) }
}

val databaseModule = module {
    single { EasyKitchenDb.getInstance(get()) }
    single { get<EasyKitchenDb>().mealDao }
    single { get<EasyKitchenDb>().savedMealDao }
}