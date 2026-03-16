package com.metrolist.music

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.metrolist.innertube.YouTube
import com.metrolist.innertube.models.SongItem
import kotlinx.coroutines.launch

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Metrolist Desktop") {
        val scope = rememberCoroutineScope()
        var query by remember { mutableStateOf("") }
        var results by remember { mutableStateOf<List<SongItem>>(emptyList()) }
        var isLoading by remember { mutableStateOf(false) }

        MaterialTheme(colorScheme = darkColorScheme()) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextField(
                            value = query,
                            onValueChange = { query = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Buscar en YouTube Music...") }
                        )
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = {
                                scope.launch {
                                    isLoading = true
                                    YouTube.searchSummary(query).onSuccess { page ->
                                        results = page.summaries
                                            .flatMap { it.items }
                                            .filterIsInstance<SongItem>()
                                    }
                                    isLoading = false
                                }
                            },
                            enabled = query.isNotBlank() && !isLoading
                        ) {
                            if (isLoading) CircularProgressIndicator(size = 20.dp, color = MaterialTheme.colorScheme.onPrimary)
                            else Text("Buscar")
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    LazyColumn(modifier = Modifier.fillWeight(1f)) {
                        items(results) { song ->
                            ListItem(
                                headlineContent = { Text(song.title) },
                                supportingContent = { Text(song.artists.joinToString { it.name }) }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun Modifier.fillWeight(weight: Float): Modifier = this.then(Modifier.fillMaxHeight().fillMaxWidth())
