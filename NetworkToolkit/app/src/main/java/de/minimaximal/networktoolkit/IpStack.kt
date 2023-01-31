package de.minimaximal.networktoolkit

import android.content.Context
import android.content.ContextWrapper
import android.net.ConnectivityManager
import android.net.DhcpInfo
import android.net.LinkAddress
import android.net.wifi.WifiManager
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.apache.commons.net.util.SubnetUtils
import java.net.InetAddress


@Composable
fun IpStackView(contextWrapper: ContextWrapper) {

    val cm = contextWrapper.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    contextWrapper.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val ips = remember { mutableStateListOf<LinkAddress>() }
    val masks = remember { mutableStateListOf<Mask>() }
    val gate = remember { mutableStateOf("") }
    val dns = remember { mutableStateOf(Dns(null, null)) }
    val dhcp = remember { mutableStateOf(Dhcp(null, null)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        LazyRow(horizontalArrangement = Arrangement.spacedBy(30.dp)) {
            item {
                Button(onClick = {
                    clearIpStack(ips, masks, gate, dns, dhcp, true)
                    getIPs(cm, ips)
                }) {
                    Text("get local ip")
                }
            }

            if (!ips.isEmpty()) {
                item {
                    Button(onClick = {
                        // clearIpStack(ips, masks, gate, dns, dhcp, false)
                        getMask(masks, ips)
                        getGate(cm, gate)
                        getDns(cm, dns)
                        getDhcp(cm, dhcp)
                    }) {
                        Text("get additional information")
                    }
                }
            }
        }

        Button(onClick = {
            clearIpStack(ips, masks, gate, dns, dhcp, true)
        }) {
            Text("clear")
        }


        LazyColumn {
            items(ips.size) { index ->
                Text(text = "ip(" + index + "): " + ips[index].toString())
            }
            items(masks.size) { index ->
                Text(text = "netmask(" + index + "): " + masks[index].netmask)
                Text(text = "network size(" + index + "): " + masks[index].addressCount.toString() + " addresses")
                Text(text = "network address(" + index + "): " + masks[index].networkAddress)
                Text(text = "broadcast(" + index + "): " + masks[index].broadcastAddress)
            }

            if (gate.value != "") {
                item {
                    Text(text = "default gateway: " + gate.value)
                }
            }

            dns.value.dnsServer?.let {
                items(it.size) { index ->
                    Text(text = "dns server(" + index + "): " + dns.value.dnsServer!![index])
                }
            }


            if (dns.value.domainName != null) {
                item {
                    Text(text = "domain name: " + dns.value.domainName)
                }
            }

            if (dhcp.value.dhcpServer != null) {
                item {
                    Text(text = "dhcp server: " + dhcp.value.dhcpServer)
                }
            }

            if (dhcp.value.dhcpLease != null) {
                item {
                    Text(text = "dhcp lease: " + dhcp.value.dhcpLease)
                }
            }
        }
    }
}

fun getIPs(cm: ConnectivityManager, ips: MutableList<LinkAddress>) {
    ips.addAll(checkIPv4L(cm.getLinkProperties(cm.activeNetwork)?.linkAddresses))
}


fun getMask(
    masks: SnapshotStateList<Mask>, ips: SnapshotStateList<LinkAddress>
) {
    for (it in ips) {
        val utils = SubnetUtils(it.toString())
        utils.isInclusiveHostCount = true
        val netmask = utils.info.netmask
        val addressCount = utils.info.addressCountLong
        val networkAddress = utils.info.networkAddress
        val broadcastAddress = utils.info.broadcastAddress
        val mask = Mask(netmask, addressCount, networkAddress, broadcastAddress)
        masks.add(mask)
    }
}

fun getGate(cm: ConnectivityManager, gate: MutableState<String>) {
    gate.value =
        cm.getLinkProperties(cm.activeNetwork)?.routes?.firstOrNull { it.isDefaultRoute }?.gateway.toString()
            .removePrefix("/")
}

fun getDns(cm: ConnectivityManager, dns: MutableState<Dns>) {
    val network = cm.getLinkProperties(cm.activeNetwork)
    val dnsServers: MutableList<String> = mutableListOf()
    for (it in checkIPv4I(network?.dnsServers)) {
        dnsServers.add(it.toString().removePrefix("/"))
    }
    if (dnsServers.isEmpty()) {
        dnsServers.add("no dns server found")
    }
    var domainName = network?.domains
    if (domainName == null) {
        domainName = "n/a"
    }
    dns.value = Dns(dnsServers, domainName)
}

fun getDhcp(cm: ConnectivityManager, dhcp: MutableState<Dhcp>) {
    var dhcpServer = "readout of dhcp server is only available for android 11 or newer"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        dhcpServer = cm.getLinkProperties(cm.activeNetwork)?.dhcpServerAddress?.toString()
            ?.removePrefix("/").toString()
    }
    val dhcpLease = DhcpInfo().leaseDuration.toString()

    dhcp.value = Dhcp(dhcpServer, dhcpLease)
}

fun checkIPv4I(addresses: MutableList<InetAddress>?): MutableList<InetAddress> {
    val list: MutableList<InetAddress> = mutableListOf()
    if (addresses != null) {
        for (it in addresses) {
            if (it.javaClass.toString() == "class java.net.Inet4Address") {
                list.add(it)
            }
        }
    }
    return list
}

fun checkIPv4L(addresses: MutableList<LinkAddress>?): MutableList<LinkAddress> {
    val list: MutableList<LinkAddress> = mutableListOf()
    if (addresses != null) {
        for (it in addresses) {
            if (it.address.javaClass.toString() == "class java.net.Inet4Address") {
                list.add(it)
            }
        }
    }
    return list
}

fun clearIpStack(
    ips: SnapshotStateList<LinkAddress>,
    masks: SnapshotStateList<Mask>,
    gate: MutableState<String>,
    dns: MutableState<Dns>,
    dhcp: MutableState<Dhcp>,
    withIp: Boolean
) {
    if (withIp) {
        ips.clear()
    }
    masks.clear()
    gate.value = ""
    dns.value = Dns(null, null)
    dhcp.value = Dhcp(null, null)
}


data class Mask(
    val netmask: String,
    val addressCount: Long,
    val networkAddress: String,
    val broadcastAddress: String
)

data class Dns(
    val dnsServer: List<String>?, val domainName: String?
)

data class Dhcp(
    val dhcpServer: String?, val dhcpLease: String?
)