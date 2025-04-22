package com.example.newsappfunto.ui.viewsUtil

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * A composable function that creates a search field for users to input search queries.
 *
 * @param searchQuery The current search query string.
 * @param onQueryChanged A lambda function that is called when the search query changes.
 *                      It receives the new query string as a parameter.
 * @param modifier An optional [Modifier] to apply to the search field.  Defaults to an empty modifier.
 */
@Composable
fun SearchField(
    initialQuery: String = "",
    debounceDuration: Long = 1000,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf(initialQuery) }
    // Trigger search only after the user stops typing for debounceDuration.
    LaunchedEffect(query) {
        delay(debounceDuration)
       onQueryChanged(query)
    }

    androidx.compose.material3.TextField(
        value = query,
        onValueChange = { query = it },
        placeholder = { Text(text = "Search") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search icon"
            )
        },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

