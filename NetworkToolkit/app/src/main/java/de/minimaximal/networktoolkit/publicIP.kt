package de.minimaximal.networktoolkit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

@Composable
    fun PublicIpView() {

        var ip by remember { mutableStateOf("") }
        var assAdd by remember { mutableStateOf("") }
        var assName by remember { mutableStateOf("") }
        var allAdd by remember { mutableStateOf("") }
        var allName by remember { mutableStateOf("") }
        var asn by remember { mutableStateOf("") }



        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                ip = getWhatsMyIp()
                assAdd = getAddressSpaceUsageAssAdd(ip)
                assName = getAddressSpaceUsageAssName(ip)
                allAdd = getAddressSpaceUsageAllAdd(ip)
                allName = getAddressSpaceUsageAllName(ip)
                asn = getNetworkInfo(ip)
            }) {
                Text("Get IP Address Information")
            }

            Text(
                modifier = Modifier.clickable {
                    // Call the API to get the IP address
                    ip = getWhatsMyIp()
                },
                text = "IP: $ip",
                fontSize = 30.sp
            )
            Text(
                modifier = Modifier.clickable {
                    asn = getNetworkInfo(ip)
                },
                text = "ASN: $asn",
                fontSize = 30.sp
            )
            Text(
                modifier = Modifier.clickable {
                    assAdd = getAddressSpaceUsageAssAdd(ip)
                },
                text = "Network: $assAdd",
                fontSize = 30.sp
            )
            Text(
                modifier = Modifier.clickable {
                    assName = getAddressSpaceUsageAssName(ip)
                },
                text = "Network Name: $assName",
                fontSize = 30.sp
            )
            Text(
                modifier = Modifier.clickable {
                    allAdd = getAddressSpaceUsageAllAdd(ip)
                },
                text = "AS Network: $allAdd",
                fontSize = 30.sp
            )
            Text(
                modifier = Modifier.clickable {
                    allName = getAddressSpaceUsageAllName(ip)
                },
                text = "AS Name: $allName",
                fontSize = 30.sp
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
        fun getAddressSpaceUsageApi(@Url url: String): Call<de.minimaximal.networktoolkit.api.ripe.addressspaceusage.Model>
    }

    interface NetworkInfoApi {
        @GET
        fun getNetworkInfoApi(@Url url: String): Call<de.minimaximal.networktoolkit.api.ripe.networkinfo.Model>
    }


    private val whatsmyip = retrofit?.create(WhatsMyIpApi::class.java)
    private val addressspaceusage = retrofit?.create(AddressSpaceUsageApi::class.java)
    private val networkinfo = retrofit?.create(NetworkInfoApi::class.java)


    private fun getWhatsMyIp(): String {
        val response = whatsmyip?.getWhatsMyIpApi()?.execute()
        if (response != null) {
            return response.body()?.data?.ip ?: "data"
        }
        return ""
    }

    private fun getAddressSpaceUsageAssAdd(ip: String): String {
        val response =
            addressspaceusage?.getAddressSpaceUsageApi("https://stat.ripe.net/data/address-space-usage/data.json?resource=$ip")
                ?.execute()
        if (response != null) {
            if (response.body()?.data?.assignments?.isEmpty() == false) {
                return response.body()?.data?.assignments?.get(0)?.address_range ?: "data"
            }
        }
        return ""
    }

    private fun getAddressSpaceUsageAssName(ip: String): String {
        val response =
            addressspaceusage?.getAddressSpaceUsageApi("https://stat.ripe.net/data/address-space-usage/data.json?resource=$ip")
                ?.execute()
        if (response != null) {
            if (response.body()?.data?.assignments?.isEmpty() == false) {
                return response.body()?.data?.assignments?.get(0)?.asn_name ?: "data"
            }
        }
        return ""
    }

    private fun getAddressSpaceUsageAllAdd(ip: String): String {
        val response =
            addressspaceusage?.getAddressSpaceUsageApi("https://stat.ripe.net/data/address-space-usage/data.json?resource=$ip")
                ?.execute()
        if (response != null) {
            if (response.body()?.data?.allocations?.isEmpty() == false) {
                return response.body()?.data?.allocations?.get(0)?.allocation ?: "data"
            }
        }
        return ""
    }

    private fun getAddressSpaceUsageAllName(ip: String): String {
        val response =
            addressspaceusage?.getAddressSpaceUsageApi("https://stat.ripe.net/data/address-space-usage/data.json?resource=$ip")
                ?.execute()
        if (response != null) {
            if (response.body()?.data?.allocations?.isEmpty() == false) {
                return response.body()?.data?.allocations?.get(0)?.asn_name ?: "data"
            }
        }
        return ""
    }

    private fun getNetworkInfo(ip: String): String {
        val response =
            networkinfo?.getNetworkInfoApi("https://stat.ripe.net/data/network-info/data.json?resource=$ip")
                ?.execute()
        if (response != null) {
            return response.body()?.data?.asns?.get(0) ?: "data"
        }
        return ""
    }