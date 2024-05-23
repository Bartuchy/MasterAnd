package com.example.masterand.app.profile.helpers

import java.util.regex.Pattern

class OutlinedTextFieldsValidator {

    fun isNameFieldValid(text: String): Boolean {
        return text.isNotEmpty()
    }

    fun isEmailFieldValid(text: String): Boolean {
        val emailRegex = Pattern.compile(
            "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        )
        return emailRegex.matcher(text).matches() && text.isNotEmpty()
    }

    fun isNumOfColorsFieldValid(text: String): Boolean {
        return try {
            text.toInt() in 5..10 && text.isNotEmpty()
        } catch (e: NumberFormatException) {
            println("Value is not a number")
            return false
        }
    }
}