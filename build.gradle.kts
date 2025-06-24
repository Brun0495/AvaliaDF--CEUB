// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // REMOVIDO: alias(libs.plugins.kotlin.compose) apply false // Este plugin não é mais necessário para o WebView
    alias(libs.plugins.googleServices) apply false // ADICIONADO/MANTIDO: Essencial para a integração com Firebase
    id("androidx.navigation.safeargs.kotlin") version "2.7.7" apply false // << ADICIONE ESTA LINHA
}