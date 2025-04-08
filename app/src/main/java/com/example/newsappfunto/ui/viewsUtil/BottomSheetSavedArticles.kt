//package com.example.newsappfunto.ui.viewsUtil
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.ModalBottomSheetState
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.ModalBottomSheet
//import androidx.compose.material3.SheetState
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.MutableState
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import com.example.newsappfunto.data.Articles
//import com.example.newsappfunto.model.NewsViewModel
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun BottomSheetSavedArticles(
//    showBottomSheet: Boolean,
//    sheetState: SheetState,
//    viewModel: NewsViewModel,
//    scope: CoroutineScope,
//    url: String,
//    saveState: MutableState<Boolean>,
//    articleState: MutableState<Articles?>
//
//
//    ){
//    ModalBottomSheet(
//        onDismissRequest = { showBottomSheet = false },
//        sheetState = sheetState
//    ) {
//        // Use a Column to stack the prompt and the buttons vertically.
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(16.dp) // Vertical spacing between items.
//        ) {
//            // Prompt message in the center.
//            Text(
//                text = "Are you sure you want to unsave this article?",
//                style = MaterialTheme.typography.bodyLarge,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.fillMaxWidth()
//            )
//            // First button: Delete Article.
//            Button(
//                onClick = {
//                    scope.launch { sheetState.hide() }.invokeOnCompletion {
//                        if (!sheetState.isVisible) {
//                            showBottomSheet = false
//                        }
//                    }
//                    scope.launch {
//                        viewModel.SingleGetArticles(url)
//                        delay(2000)
//                        viewModel.deleteArticles(articleState.value!!)
//                        saveState.value = false
//                    }
//                },
//                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(text = "Delete Article", color = Color.White)
//            }
//            // Second button: Cancel.
//            Button(
//                onClick = {
//                    scope.launch { sheetState.hide() }.invokeOnCompletion {
//                        if (!sheetState.isVisible) {
//                            showBottomSheet = false
//                        }
//                    }
//                },
//                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(text = "Cancel", color = Color.White)
//            }
//        }
//    }
//
//}