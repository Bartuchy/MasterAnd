package com.example.masterand

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

class OutlinedTextFieldsFactory {

    @Composable
    fun ProduceOutlinedTextField(
        label: String,
        text: MutableState<String>,
        supportingText: String,
        keyboardType: KeyboardType = KeyboardType.Text,
        validate: (String) -> Boolean
    ) {
        //var text = rememberSaveable { mutableStateOf("") }
        var hasInteracted by remember { mutableStateOf(false) }

        val isError = hasInteracted && !validate(text.value)

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState: FocusState ->
                    if (focusState.isFocused && text.value.isEmpty()) {
                        hasInteracted = true
                    }
                },
            value = text.value,
            onValueChange = {
                text.value = it
            },
            label = { Text(label) },
            singleLine = true,
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            supportingText = {
                if (isError)
                    Text(supportingText)
            },
            trailingIcon = {
                if (isError) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}