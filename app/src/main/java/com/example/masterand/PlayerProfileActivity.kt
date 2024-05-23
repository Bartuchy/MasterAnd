package com.example.masterand

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.masterand.ui.theme.MasterAndTheme
import com.example.masterand.viewmodel.AppViewModelProvider
import com.example.masterand.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

class PlayerProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MasterAndTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavigationGraph(navController)
                }
            }
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "ProfileScreen") {
        composable(route = "ProfileScreen",
            enterTransition = {
                fadeIn(animationSpec = tween(500)) +
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = EaseIn
                            )
                        )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(500)) +
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = EaseOut
                            )
                        )
            }) {
            ProfileScreen(onNavigateToGameScreen = { playerId, numberOfColors ->
                navController.navigate(
                    "GameScreen/$playerId/$numberOfColors"
                )
            })
        }
        composable(
            "GameScreen/{playerId}/{numberOfColors}",
            arguments = listOf(navArgument("numberOfColors") { type = NavType.StringType })
        ) { backStackEntry ->
            val playerId = backStackEntry.arguments?.getString("playerId")!!.toLong()
            val numberOfColors = backStackEntry.arguments?.getString("numberOfColors")!!.toInt()

            GameScreen(
                onNavigateToProfileScreen = { navController.navigate("ProfileScreen") },
                onNavigateToResultsScreen = { score -> navController.navigate("ResultsScreen/$playerId/$score/$numberOfColors") },
                numberOfColors = numberOfColors,
                playerId = playerId
            )

        }
        composable("ResultsScreen/{playerId}/{score}/{numberOfColors}") { backStackEntry ->
            val score = backStackEntry.arguments?.getString("score")!!.toInt()
            val numberOfColors = backStackEntry.arguments?.getString("numberOfColors")!!.toInt()
            val playerId = backStackEntry.arguments?.getString("playerId")!!.toLong()

            ResultsScreen(
                onNavigateToGameScreen = { navController.navigate("GameScreen/$playerId/$numberOfColors") },
                onNavigateToProfileScreen = { navController.navigate("ProfileScreen") },
                score = score
            )
        }
    }

}


@Composable
fun ProfileScreen(
    onNavigateToGameScreen: (playerId: Long, numberOfColors: String) -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        TitleText()

//        var profileImageUri by remember { mutableStateOf<Uri?>(null) }

        val imagePicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { selectedUri ->
                if (selectedUri != null) {
                    viewModel.profileImageUri.value = selectedUri
                }
            })

        ProfileImageWithPicker(viewModel.profileImageUri.value, selectImageOnClick = {
            imagePicker.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        })

//        val errors = remember {
//            mutableStateMapOf(
//                Pair("name", false), Pair("email", false), Pair("numberOfColors", false)
//            )
//        }

//        val name = rememberSaveable { mutableStateOf("") }
//        val email = rememberSaveable { mutableStateOf("") }
//        val numberOfColors = rememberSaveable { mutableStateOf("") }

        OutlinedTextFieldWithError(
            viewModel.name,
            viewModel.email,
            viewModel.numberOfColors,
            viewModel.errors
        )
        println("xd" + viewModel.errors.values.toList())
        StartGameButtonWithValidation(
            viewModel,
            onNavigateToGameScreen,
            errors = viewModel.errors
        )
    }
}

@Composable
fun TitleText(
    text: String = "MasterAnd",
    style: TextStyle = MaterialTheme.typography.displayLarge,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Text(
        text = text,
        style = style,
        modifier = Modifier
            .padding(bottom = 48.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin.Center
            }
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
fun OutlinedTextFieldWithError(
    name: MutableState<String>,
    email: MutableState<String>,
    numberOfColors: MutableState<String>,
    errors: SnapshotStateMap<String, Boolean>
) {
    fun updateErrorState(key: String, value: Boolean) {
        errors[key] = value
    }

    val outlinedTextFieldsFactory = OutlinedTextFieldsFactory()
    val outlinedTextFieldsValidator = OutlinedTextFieldsValidator()

    outlinedTextFieldsFactory.ProduceOutlinedTextField(
        label = "Enter name",
        name,
        supportingText = "Name can't be empty",
        validate = { text ->
            val isValid = outlinedTextFieldsValidator.isNameFieldValid(text)
            updateErrorState("name", isValid)
            isValid
        }
    )
    outlinedTextFieldsFactory.ProduceOutlinedTextField(
        label = "Enter email",
        email,
        supportingText = "Email should be in proper format and can't be empty",
        validate = { text ->
            val isValid = outlinedTextFieldsValidator.isEmailFieldValid(text)
            updateErrorState("email", isValid)
            isValid
        }
    )
    outlinedTextFieldsFactory.ProduceOutlinedTextField(
        label = "Enter number of colors",
        numberOfColors,
        supportingText = "Number of colors should be between 5 and 10 adn can't be empty",
        validate = { text ->
            val isValid = outlinedTextFieldsValidator.isNumOfColorsFieldValid(text)
            updateErrorState("numberOfColors", isValid)
            isValid
        }
    )
}

@Composable
fun StartGameButtonWithValidation(
    viewModel: ProfileViewModel,
    onNavigateToGameScreen: (playerId: Long, numberOfColors: String) -> Unit,
    errors: SnapshotStateMap<String, Boolean>
) {
    val coroutineScope = rememberCoroutineScope()
    val hasErrors = errors.values.contains(false)

    Button(
        modifier = Modifier.fillMaxWidth(),
        enabled = !hasErrors,
        onClick = {
            coroutineScope.launch {
                var loggedInPlayerId = viewModel.savePlayer()
                println("PlayerID: $loggedInPlayerId")
                if (loggedInPlayerId == -1L) {
                    val player = viewModel.getPlayerByEmail()
                    loggedInPlayerId = player?.playerId ?: -1L
                }
                onNavigateToGameScreen(loggedInPlayerId, viewModel.numberOfColors.value)
            }

        }
    ) {
        Text("Start game")
    }
}

//@Preview
//@Composable
//fun ProfileScreenInitialPreview() {
//    MasterAndTheme {
//        ProfileScreenInitial()
//    }
//}
