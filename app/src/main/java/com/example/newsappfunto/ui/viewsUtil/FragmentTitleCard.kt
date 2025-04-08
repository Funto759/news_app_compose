package com.example.newsappfunto.ui.viewsUtil

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Composable function that displays a title card for a fragment.
 *
 * @param display The title text to be displayed on the card.  This should be a string
 *                that represents the current fragment or section of the application.
 *                It will be prefixed with a book emoji for visual emphasis.
 */
@Composable
fun FragmentTitleCard(display: String) {
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
        Text(
            text = "📚 $display",
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color(0xFF1E88E5), // Soft blue for the text
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        )
    }
}