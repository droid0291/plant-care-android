package com.plantcare.app.ui.screens.result

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.plantcare.app.data.model.PlantAnalysis
import com.plantcare.app.data.model.UrgencyLevel
import com.plantcare.app.ui.screens.components.HealthBadge
import com.plantcare.app.ui.screens.components.InfoCard
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    analysisJson: String,
    onBack: () -> Unit
) {
    val analysis: PlantAnalysis = Json { ignoreUnknownKeys = true }.decodeFromString(analysisJson)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = analysis.identification.commonName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = analysis.identification.scientificName,
                            style = MaterialTheme.typography.bodySmall,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Plant family + confidence
            item {
                PlantMetaCard(analysis)
            }

            // Health badge
            item {
                HealthBadge(
                    status = analysis.health.status,
                    urgency = analysis.health.urgency,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Sunlight + Water side by side
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.WbSunny,
                        title = "Sunlight",
                        primary = analysis.sunlight.level,
                        secondary = analysis.sunlight.hoursPerDay,
                        tip = analysis.sunlight.tips
                    )
                    InfoCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.WaterDrop,
                        title = "Water",
                        primary = analysis.water.frequency,
                        secondary = analysis.water.amount,
                        tip = analysis.water.tips
                    )
                }
            }

            // Health issues (only if any)
            if (analysis.health.issuesDetected.isNotEmpty()) {
                item {
                    SectionHeader(icon = Icons.Default.Warning, title = "Issues Detected")
                }
                items(analysis.health.issuesDetected) { issue ->
                    BulletItem(text = issue, isWarning = true)
                }
            }

            // Improvement tips
            if (analysis.health.improvementTips.isNotEmpty()) {
                item {
                    SectionHeader(icon = Icons.Default.Build, title = "Improvement Tips")
                }
                items(analysis.health.improvementTips) { tip ->
                    BulletItem(text = tip)
                }
            }

            // Care tips
            item {
                SectionHeader(icon = Icons.Default.Spa, title = "Care Tips")
            }
            items(analysis.careTips) { tip ->
                BulletItem(text = tip)
            }

            // Fun facts
            item {
                SectionHeader(icon = Icons.Default.Star, title = "Fun Facts")
            }
            items(analysis.funFacts) { fact ->
                BulletItem(text = fact)
            }

            // RAG sources (subtle, for demo transparency)
            if (analysis.ragSourcesUsed.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Knowledge sources: ${analysis.ragSourcesUsed.joinToString(", ")}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun PlantMetaCard(analysis: PlantAnalysis) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
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
                    text = "Family: ${analysis.identification.family}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Confidence",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
                Text(
                    text = "${(analysis.identification.confidenceScore * 100).toInt()}%",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun BulletItem(text: String, isWarning: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = if (isWarning) "⚠ " else "• ",
            style = MaterialTheme.typography.bodyMedium,
            color = if (isWarning) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
    }
}
