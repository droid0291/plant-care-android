package com.plantcare.app.ui.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.plantcare.app.data.model.UrgencyLevel
import com.plantcare.app.ui.theme.*

@Composable
fun HealthBadge(
    status: String,
    urgency: UrgencyLevel,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, emoji) = when (urgency) {
        UrgencyLevel.LOW -> Triple(UrgencyLow.copy(alpha = 0.15f), UrgencyLow, "🌿")
        UrgencyLevel.MEDIUM -> Triple(UrgencyMedium.copy(alpha = 0.15f), UrgencyMedium, "⚠️")
        UrgencyLevel.HIGH -> Triple(UrgencyHigh.copy(alpha = 0.15f), UrgencyHigh, "🔶")
        UrgencyLevel.CRITICAL -> Triple(UrgencyCritical.copy(alpha = 0.15f), UrgencyCritical, "🚨")
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Health Status",
                    style = MaterialTheme.typography.labelMedium,
                    color = textColor.copy(alpha = 0.8f)
                )
                Text(
                    text = status,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = emoji, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = textColor
                ) {
                    Text(
                        text = urgency.displayLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}
