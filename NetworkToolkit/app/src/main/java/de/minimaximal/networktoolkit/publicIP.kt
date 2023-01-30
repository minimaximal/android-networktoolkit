// TODO: https://stat.ripe.net/docs/02.data-api/address-space-usage.html
// TODO: network message

package de.minimaximal.networktoolkit

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.minimaximal.networktoolkit.api.ripe.addressspaceusage.Model
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

@Composable
fun PublicIpView() {
    val message = remember { mutableStateOf("") }
    val ip = remember { mutableStateOf("") }
    val asn = remember { mutableStateOf("") }
    val assAdd = remember { mutableStateOf("") }
    val assName = remember { mutableStateOf("") }
    val allAdd = remember { mutableStateOf("") }
    val allName = remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row() {

            Button(onClick = {
                clearPubIP(message, ip, asn, assAdd, assName, allAdd, allName, true)
                getWhatsMyIp(ip, message)
            }) {
                Text("get pulic IP")
            }


            Button(onClick = {
                clearPubIP(message, ip, asn, assAdd, assName, allAdd, allName, true)
            }) {
                Text("clear")
            }
        }


        if (ip.value != "" && ip.value != errorIo && ip.value != errorNoInfo) {
            Button(onClick = {
                clearPubIP(message, ip, asn, assAdd, assName, allAdd, allName, false)
                getNetworkInfo(ip.value, asn, message)
                getAddressSpaceUsage(ip.value, assAdd, assName, allAdd, allName, message)
            }) {
                Text("get additional information")
            }
        }



        Text(
            text = "IP: ${ip.value}",
        )
        Text(
            text = "ASN: ${asn.value}",
        )
        Text(
            text = "Network: ${assAdd.value}",
        )
        Text(
            text = "Network Name: ${assName.value}",
        )
        Text(
            text = "AS Network: ${allAdd.value}",
        )
        Text(
            text = "AS Name: ${allName.value}",
        )

    }
}


private val retrofit: Retrofit? = Retrofit.Builder()
    .baseUrl("https://stat.ripe.net/")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()


interface WhatsMyIpApi {
    @GET("data/whats-my-ip/data.json")
    fun getWhatsMyIpApi(): Call<de.minimaximal.networktoolkit.api.ripe.whatsmyip.Model>
}

interface AddressSpaceUsageApi {
    @GET
    fun getAddressSpaceUsageApi(@Url url: String): Call<Model>
}

interface NetworkInfoApi {
    @GET
    fun getNetworkInfoApi(@Url url: String): Call<de.minimaximal.networktoolkit.api.ripe.networkinfo.Model>
}


private val whatsmyip = retrofit?.create(WhatsMyIpApi::class.java)
private val addressspaceusage = retrofit?.create(AddressSpaceUsageApi::class.java)
private val networkinfo = retrofit?.create(NetworkInfoApi::class.java)

private const val errorIo = "I/O ERROR: check network connection or input parameters"
private const val errorNoInfo = "no information available"

private fun getWhatsMyIp(ip: MutableState<String>, message: MutableState<String>) {
    CoroutineScope(Dispatchers.Main).launch {
        try {
            val response = withContext(Dispatchers.IO) { whatsmyip?.getWhatsMyIpApi()?.execute() }
            if (response != null) {
                ip.value = (response.body()?.data?.ip ?: "data")
            } else {
                ip.value = errorNoInfo
            }
        } catch (e: Exception) {
            message.value = errorIo
        }
    }
}

private fun getNetworkInfo(ip: String, Asn: MutableState<String>, message: MutableState<String>) {
    CoroutineScope(Dispatchers.Main).launch {
        try {
            val response = withContext(Dispatchers.IO) {
                networkinfo?.getNetworkInfoApi("https://stat.ripe.net/data/network-info/data.json?resource=$ip")
                    ?.execute()
            }
            if (response != null) {
                Asn.value = (response.body()?.data?.asns?.get(0) ?: "data")
            } else {
                Asn.value = errorNoInfo
            }
        } catch (e: Exception) {
            message.value = errorIo
        }
    }

}


private fun getAddressSpaceUsage(
    ip: String,
    AssAdd: MutableState<String>,
    AssName: MutableState<String>,
    AllAdd: MutableState<String>,
    AllName: MutableState<String>,
    message: MutableState<String>
) {
    CoroutineScope(Dispatchers.Main).launch {
        try {
            val response = withContext(Dispatchers.IO) {
                addressspaceusage?.getAddressSpaceUsageApi("https://stat.ripe.net/data/address-space-usage/data.json?resource=$ip")
                    ?.execute()
            }

            getAddressSpaceUsageAssAdd(AssAdd, message, response)
            getAddressSpaceUsageAssName(AssName, message, response)
            getAddressSpaceUsageAllAdd(AllAdd, message, response)
            getAddressSpaceUsageAllName(AllName, message, response)

        } catch (e: Exception) {
            message.value = errorIo
        }
    }
}

private fun getAddressSpaceUsageAssAdd(
    AssAdd: MutableState<String>,
    message: MutableState<String>,
    response: Response<Model>?
) {
    CoroutineScope(Dispatchers.Main).launch {
        try {
            AssAdd.value = errorNoInfo
            if (response != null) {
                if (response.body()?.data?.assignments?.isEmpty() == false) {
                    AssAdd.value =
                        response.body()?.data?.assignments?.get(0)?.address_range ?: "data"
                }
            }
        } catch (e: Exception) {
            message.value = errorIo
        }
    }
}

private fun getAddressSpaceUsageAssName(
    AssName: MutableState<String>,
    message: MutableState<String>,
    response: Response<Model>?
) {
    CoroutineScope(Dispatchers.Main).launch {
        try {
            AssName.value = errorNoInfo
            if (response != null) {
                if (response.body()?.data?.assignments?.isEmpty() == false) {
                    AssName.value = response.body()?.data?.assignments?.get(0)?.asn_name ?: "data"
                }
            }
        } catch (e: Exception) {
            message.value = errorIo
        }
    }
}

private fun getAddressSpaceUsageAllAdd(
    AllAdd: MutableState<String>,
    message: MutableState<String>,
    response: Response<Model>?
) {
    CoroutineScope(Dispatchers.Main).launch {
        try {
            AllAdd.value = errorNoInfo
            if (response != null) {
                if (response.body()?.data?.allocations?.isEmpty() == false) {
                    AllAdd.value = response.body()?.data?.allocations?.get(0)?.allocation ?: "data"
                }
            }


        } catch (e: Exception) {
            message.value = errorIo
        }
    }
}

private fun getAddressSpaceUsageAllName(
    AllName: MutableState<String>,
    message: MutableState<String>,
    response: Response<Model>?
) {
    CoroutineScope(Dispatchers.Main).launch {
        try {
            AllName.value = errorNoInfo
            if (response != null) {
                if (response.body()?.data?.allocations?.isEmpty() == false) {
                    AllName.value = response.body()?.data?.allocations?.get(0)?.asn_name ?: "data"
                }
            }
        } catch (e: Exception) {
            message.value = errorIo
        }
    }
}


private fun clearPubIP(
    message: MutableState<String>,
    ip: MutableState<String>,
    asn: MutableState<String>,
    assAdd: MutableState<String>,
    assName: MutableState<String>,
    allAdd: MutableState<String>,
    allName: MutableState<String>,
    withIp: Boolean
) {
    message.value = ""
    if (withIp) {
        ip.value = ""
    }
    asn.value = ""
    assAdd.value = ""
    assName.value = ""
    allAdd.value = ""
    allName.value = ""
}