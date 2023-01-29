@file:OptIn(ExperimentalTextApi::class)

package de.minimaximal.networktoolkit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.ExperimentalTextApi

@Composable
fun MainScreen(){
        Column {
            Text("Willkommen im Networktoolkit,")
            Text("in der Kopfzeile der App sehen Sie die verfügbaren Tools.")
            Text("Es gibt die \"Ping\" und \"WhoIs\" Funktion.\n")
            Text("Unter  \"Ping\" kann ein belibiger Host angepingt werden.")
            Text("Unter \"WhoIs\" können informationen zu einem gewünschten Host ausgelesen werden.")
            Text("Wir wünschen viel Spaß beim Verwenden unserer App.\n")
            Text("Bei Fragen stehen wir Ihnen gerne zur Verfügung,")
            Text("ihr Networktoolkit Team (Maximilian Koch, Paul Antoni, Viktoria Gönnheimer)")
        }

}