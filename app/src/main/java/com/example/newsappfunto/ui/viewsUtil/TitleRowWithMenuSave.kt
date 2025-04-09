package com.example.newsappfunto.ui.viewsUtil

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.newsappfunto.model.NewsViewModel
import com.example.newsappfunto.ui.screens.NewsListScreen
import com.example.newsappfunto.ui.theme.NewsAppFuntoTheme
import androidx.core.content.edit

@Composable
fun TitleRowWithMenuSave(
    search : (String) -> Unit,
    display: String,
    onClick: (String) -> Unit
) {
    var name by remember { mutableStateOf(display) }
    // A Card that holds a Row with the title and a dropdown menu.
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD) // Light background for contrast
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Title Text displaying the currently selected option.
            Text(
                text = "📚 ${name.uppercase()}",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color(0xFF1E88E5), // Soft blue
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
            // Dropdown menu icon.
            MinimalDropdownMenuSave(
                search = {search(it)},
                selectedOption = name,
                onClick = {
                    name = it
                    onClick(it)
                }
            )
        }
    }
}

@Composable
fun MinimalDropdownMenuSave(
    search : (String) -> Unit,
    selectedOption: String,
    onClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val options = listOf("all","business", "entertainment", "health", "general", "science", "sports", "technology")

    Box(modifier = Modifier.padding(start = 8.dp)) {
        IconButton(
            colors = IconButtonDefaults.iconButtonColors(),
            onClick = {
                search("")
                expanded = !expanded }
        ) {
            Icon(
                imageVector = Icons.Filled.FilterList,
                contentDescription = "More options",
                tint = Color(0xFF1E88E5)
            )
        }
        DropdownMenu(
            containerColor = Color(0xFF1E88E5),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        // Using a Row to place the option text and checkmark.
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = option.uppercase(),
                                color = Color.White
                            )
                            // If this option is the selected one, display a checkmark.
                            if (option.equals(selectedOption, ignoreCase = true)) {
                                Icon(
                                    imageVector = Icons.Outlined.Check, // Replace with a checkmark icon if desired.
                                    contentDescription = "Selected",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    },
                    onClick = {
                        saveSelectedCategory(context, option)
                        onClick(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun saveSelectedCategory(context: Context, category: String) {
    val sharedPref = context.getSharedPreferences("MyPref2", Context.MODE_PRIVATE)
    sharedPref.edit() { putString("selected_category2", category) }
}

