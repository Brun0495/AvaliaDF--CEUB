// app/src/main/java/com/example/avaliadf/data/util/ResultWrapper.kt
package com.example.avaliadf.data.util

sealed class ResultWrapper<out T> {
    data class Success<out T>(val data: T) : ResultWrapper<T>()
    data class Error(val exception: Exception? = null, val message: String? = null) : ResultWrapper<Nothing>()
    // Poderíamos adicionar um Loading aqui também, se o repositório for complexo
}