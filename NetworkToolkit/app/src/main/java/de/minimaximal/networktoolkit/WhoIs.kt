package de.minimaximal.networktoolkit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun WhoIsView() {
    //vals
    val queryResult = remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.padding(16.dp)){
        var text by remember {
            mutableStateOf(TextFieldValue(""))
        }
        OutlinedTextField(
            value = text,
            label = { Text(text = "Enter address")},
            onValueChange = {address -> text = address}
        )

        Button(onClick = {
            queryResult.value = ""
            try{
                queryResult.value = ExecuteWhoIsQuery()
            } catch (e: Exception){
                //todo: fix this
            }
        }) {
            Text("Go!")
        }
        ResultsView(results = FormatQueryResult(queryResult.value))
    }

}

@Composable
fun ResultsView(results: List<String>){
    LazyColumn(modifier = Modifier.padding(16.dp)){
        items(results.size){
                index -> Text(results[index])
        }
    }
}

fun ExecuteWhoIsQuery(): String {
    return "text\nmehrtext"
}

fun FormatQueryResult(queryResult: String): List<String> {
    val formattedQuery = (queryResult.split('\n'))
    return formattedQuery
}