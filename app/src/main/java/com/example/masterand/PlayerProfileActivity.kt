package com.example.masterand

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.masterand.ui.theme.MasterAndTheme
import androidx.compose.ui.text.TextStyle

class PlayerProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MasterAndTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProfileScreen()
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleText()

        var profileImageUri by remember { mutableStateOf<Uri?>(null) }

        val imagePicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { selectedUri ->
                if (selectedUri != null) {
                    profileImageUri = selectedUri
                }
            })

        ProfileImageWithPicker(profileImageUri, selectImageOnClick = {
            imagePicker.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        })

        val errors = remember { mutableStateOf(mapOf<String, Boolean>()) }
        OutlinedTextFieldWithError(errors)

        NextButtonWithValidation(errors = errors)
    }
}

@Composable
fun TitleText(
    text: String = "MasterAnd",
    style: TextStyle = MaterialTheme.typography.displayLarge,
) {
    Text(
        text = text,
        style = style,
        modifier = Modifier
            .padding(bottom = 48.dp)
    )
}

@Composable
fun ProfileImageWithPicker(
    profileImageUri: Uri?,
    selectImageOnClick: () -> Unit
) {
    Box(Modifier.size(100.dp)) {
        IconButton(
            onClick = selectImageOnClick,
            modifier = Modifier
                .size(15.dp)
                .clip(CircleShape)
                .align(Alignment.TopEnd)
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_image_search_24),
                contentDescription = "Default profile photo",
            )
        }

        if (profileImageUri != null) {
            AsyncImage(
                model = profileImageUri,
                contentDescription = "Profile image",
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .align(Alignment.BottomCenter),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.baseline_question_mark_24),
                contentDescription = "Default profile photo",
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun OutlinedTextFieldWithError(errors: MutableState<Map<String, Boolean>>) {
    fun updateErrorState(key: String, value: Boolean) {
        errors.value = errors.value.toMutableMap().apply {
            this[key] = value
        }
    }

    val outlinedTextFieldsFactory = OutlinedTextFieldsFactory()
    val outlinedTextFieldsValidator = OutlinedTextFieldsValidator()

    outlinedTextFieldsFactory.ProduceOutlinedNameTextField(
        label = "Enter name",
        supportingText = "Name can't be empty",
        validate = { text ->
            val isValid = outlinedTextFieldsValidator.isNameFieldValid(text)
            updateErrorState("name", isValid)
            isValid
        }
    )
    outlinedTextFieldsFactory.ProduceOutlinedNameTextField(
        label = "Enter email",
        supportingText = "Email should be in proper format and can't be empty",
        validate = { text ->
            val isValid = outlinedTextFieldsValidator.isEmailFieldValid(text)
            updateErrorState("email", isValid)
            isValid
        }
    )
    outlinedTextFieldsFactory.ProduceOutlinedNameTextField(
        label = "Enter number of colors",
        supportingText = "Number of colors should be between 5 and 10 adn can't be empty",
        validate = { text ->
            val isValid = outlinedTextFieldsValidator.isNumOfColorsFieldValid(text)
            updateErrorState("numOfColors", isValid)
            isValid
        }
    )
}

@Composable
fun NextButtonWithValidation(errors: MutableState<Map<String, Boolean>>) {
    val hasErrors = errors.value.values.contains(false) || errors.value.isEmpty()

    Button(
        modifier = Modifier.fillMaxWidth(),
        enabled = !hasErrors,
        onClick = { /*TODO*/ }
    ) {
        Text("Next")
    }
}

//@Preview
//@Composable
//fun ProfileScreenInitialPreview() {
//    MasterAndTheme {
//        ProfileScreenInitial()
//    }
//}
