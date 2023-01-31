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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url


@Composable
fun WhoIsView() {
    val queryResult = remember {
        mutableStateListOf<String>()
    }

    Column(modifier = Modifier.padding(16.dp)){
        var text by remember {
            mutableStateOf(TextFieldValue(""))
        }
        OutlinedTextField(//todo also execute process when pressing enter on keyboard
            value = text,
            label = { Text(text = "Enter address")},
            onValueChange = {address -> text = address}
        )

        Button(onClick = {
            queryResult.add("")
            queryResult.removeRange(0, queryResult.lastIndex)
            try{
                queryResult.addAll(executeWhoIsQuery("google.com"))//todo remove hardcoded address
            } catch (e: Exception){
                queryResult.add("Error while trying to get Whois information")
                queryResult.add("Please check your internet connection and your input")
            }
        }) {
            Text("Go!")
        }
        ResultsView(results = queryResult.toList())
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

fun executeWhoIsQuery(domainName: String): List<String> {


    var result = emptyList<String>()
    val retrofit: Retrofit? = Retrofit.Builder()
        .baseUrl("https://whoisapi-whois-v2-v1.p.rapidapi.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    val networkinfo = retrofit?.create(WhoIsApiInterface::class.java)

    CoroutineScope(Dispatchers.IO).launch{

        val response =//todo maybe catch exception before delivery?
            networkinfo?.getWhoIsInfo("whoisserver/WhoisService?domainName=$domainName&apiKey=at_G0oAXgpjZaZlr7wFw90QeCqyv2dDE&outputFormat=JSON")
                //?.execute()

        //val responseMessage = response?.body()!!
        if (response != null) result = formatQueryResult(response)

    }
    result = result.ifEmpty { listOf("data") }
    return result
}

interface WhoIsApiInterface{
    @Headers("X-RapidAPI-Key: 50067bc5edmshc4f30e2b88e5c4fp15f033jsn21160563506f", "X-RapidAPI-Host: whoisapi-whois-v2-v1.p.rapidapi.com")
    @GET
    suspend fun getWhoIsInfo(@Url url: String): WhoisRecord
}


fun formatQueryResult(queryResult: WhoisRecord): List<String> {
    var formattedQuery = emptyList<String>()
    formattedQuery = formattedQuery.toMutableList()
    formattedQuery.add("creation date: ${queryResult.createdDate}")
    formattedQuery.add("update date: ${queryResult.updatedDate}")
    formattedQuery.add("expiration date: ${queryResult.expiresDate}")
    formattedQuery.add("registrar name: ${queryResult.registrarName}")
    formattedQuery.add("registrar IANA ID: ${queryResult.registrarIANAID}")
    formattedQuery.add("name servers: \n${queryResult.nameServers.rawText}")

    return formattedQuery
}