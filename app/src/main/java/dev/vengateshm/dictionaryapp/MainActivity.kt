package dev.vengateshm.dictionaryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import dev.vengateshm.dictionaryapp.feature_dictionary.presentation.WordInfoItem
import dev.vengateshm.dictionaryapp.feature_dictionary.presentation.WordInfoViewModel
import dev.vengateshm.dictionaryapp.ui.theme.DictionaryAppTheme
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DictionaryAppTheme {
                val viewModel = hiltViewModel<WordInfoViewModel>()
                val wordInfoState = viewModel.wordInfoState.value
                val scaffoldState = rememberScaffoldState()

                LaunchedEffect(key1 = true) {
                    viewModel.uiEvent.collectLatest { uiEvent ->
                        when (uiEvent) {
                            is WordInfoViewModel.UiEvent.ShowSnackBar -> {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = uiEvent.message
                                )
                            }
                        }
                    }
                }

                Scaffold(scaffoldState = scaffoldState) {
                    Box(modifier = Modifier.background(color = MaterialTheme.colors.background)) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            TextField(
                                value = viewModel.searchQuery.value,
                                onValueChange = viewModel::onSearch,
                                modifier = Modifier.fillMaxWidth(),
                                label = {
                                    Text(text = "Search word...")
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(wordInfoState.wordInfoItems.size) { index ->
                                    val wordInfo = wordInfoState.wordInfoItems[index]
                                    if (index > 0) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }

                                    WordInfoItem(wordInfo = wordInfo)

                                    if (index < wordInfoState.wordInfoItems.size - 1) {
                                        Divider()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DictionaryAppTheme {
        Greeting("Android")
    }
}