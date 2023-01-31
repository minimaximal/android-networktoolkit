@file:OptIn(ExperimentalTextApi::class)

package de.minimaximal.networktoolkit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalTextApi::class)
@Composable
fun MainScreen() {
    val colorSwitch = listOf(
        MaterialTheme.colors.primary,
        MaterialTheme.colors.primaryVariant,
        MaterialTheme.colors.secondary
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            buildAnnotatedString {
                append("Willkommen im ")
                withStyle(
                    style = SpanStyle(
                        Brush.linearGradient(
                            colors = colorSwitch
                        ), fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Networktoolkit,\n\n")
                }
                append("mit einem Klick auf \"")
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary
                    )
                ) {
                    append("Network Toolkit")
                }
                append("\" in der Kopfzeile oder durch ")
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary
                    )
                ) {
                    append("Swipen von links nach rechts")
                }
                append(", gelangen Sie ins Menü. Dort kann das gewünschte Tool ausgewählt werden.\n\n")
                append("Wir wünschen viel Spaß beim Verwenden unserer App, bei Fragen stehen wir Ihnen gerne zur Verfügung.\n\nIhr ")
                withStyle(
                    style = SpanStyle(
                        Brush.linearGradient(
                            colors = colorSwitch
                        ), fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Networktoolkit Team")
                    append(" (Maximilian Koch, Paul Antoni, Viktoria Gönnheimer)")

                }
            }
        )
    }

}