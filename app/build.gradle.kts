plugins {
    // Plugins do Android e Kotlin são essenciais
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Plugin do Google Services para Firebase
    alias(libs.plugins.googleServices)
    id("kotlin-parcelize") // << --- ADICIONE ESTA LINHA AQUI
    id("androidx.navigation.safeargs.kotlin") // << ADICIONE ESTA LINHA
}

android {
    namespace = "com.example.avaliadf"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.avaliadf"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true // Adicione esta linha
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        // Nada aqui, pois não estamos usando Jetpack Compose para a UI.
    }
}

dependencies {
    // Dependências essenciais para AndroidX e Views tradicionais
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0") // Mantenha ou atualize para a versão estável mais recente

    // Dependência para AppCompatActivity (necessário para MainActivity que estende AppCompatActivity)
    implementation(libs.androidx.appcompat)
    // Dependência para extensões Kotlin em Activity (útil para enableEdgeToEdge)
    implementation(libs.androidx.activity.ktx)

    // ADICIONADO: Dependência para o Material Components (necessário para o tema MaterialComponents.Light.NoActionBar)
    implementation(libs.material.components)

    // Dependências do Firebase
    // A BOM (Bill of Materials) do Firebase garante que todas as suas bibliotecas Firebase usem versões compatíveis.
    implementation(platform(libs.firebase.bom))
    // Adicione as bibliotecas específicas do Firebase que você usará (Firestore e Auth, por exemplo)
    implementation(libs.firebase.firestore.ktx) // Para o banco de dados Firestore (Kotlin)
    implementation(libs.firebase.auth.ktx)     // Para autenticação (Kotlin)

    // Dependências de teste
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Navigation Component (para navegação entre Fragments)
    val navVersion = "2.7.7" // Use a versão estável mais recente
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    implementation("com.github.bumptech.glide:glide:4.16.0")
}