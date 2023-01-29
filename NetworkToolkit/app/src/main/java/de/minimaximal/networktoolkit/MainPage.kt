@file:OptIn(ExperimentalTextApi::class)

package de.minimaximal.networktoolkit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun MainScreen(){
        Column {
            Text(
                buildAnnotatedString {
                    append("Willkommen im ")
                    withStyle(style = SpanStyle(Brush.linearGradient(colors = listOf(Color.Red,Color.Blue,Color.Green)), fontWeight = FontWeight.Bold)) {
                        append("Networktoolkit")
                    }
                    append(",\n\nin der Kopfzeile der App sehen Sie die verfügbaren Tools. Es gibt die \"")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Blue)) {
                        append("Ping")
                    }
                    append("\" und \"")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Blue)) {
                        append("WhoIs")
                    }
                    append("\" Funktion. Unter  \"")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Blue)) {
                        append("Ping")
                    }
                    append("\" kann ein beliebiger Host angepingt werden. Unter  \"")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Blue)) {
                        append("WhoIs")
                    }
                    append("\" können Informationen zu einem gewünschten Host ausgelesen werden.")
                }
            )
            Text("Wir wünschen viel Spaß beim Verwenden unserer App, bei Fragen stehen wir Ihnen gerne zur Verfügung.\n")
            Text(
                buildAnnotatedString {
                    append("Ihr ")
                    withStyle(style = SpanStyle(Brush.linearGradient(colors = listOf(Color.Red,Color.Blue,Color.Green)), fontWeight = FontWeight.Bold)) {
                        append("Networktoolkit Team")
                    }
                    append(" (Maximilian Koch, Paul Antoni, Viktoria Gönnheimer)")
                }
            )
        }

}