# Android Networktoolkit
## Informationen für Herr Brodhaecker
### Implementierte Features
- Auslesen lokaler IP Stack
  - IP Adresse
  - Netzmaske (CIDR/IP)
    - Anzahl Adressen im Netzwerk
    - Netzadresse
    - Broadcastadresse
  - default Gateway
  - DNS
    - DNS Server
    - lokale DNS Domain
  - DHCP Server **(min. Android 11, SDK 30)**
- Ping Funktionalität mit einstellbaren Parametern
- API Abfrage für öffentliche IP
- Weiterverarbeitug der öffentlichen IP mit REST API für AS Informationen

### Hinweise zum Testen der App
<p>API Keys oder ähnliches wird nicht benötigt.<br>
Zum Testen der Funktionalität der App wird am besten ein **physisches Gerät** mit **mindestens Android 8** verwendet.<br>
Die Ping Funktionalität ist im Emulator von Android Studio nicht gegeben. (Ping kommt nicht durch)<br>
Das Auslesen des IP Stacks liefert im Emulator keine sinnvollen Werte.<br>
IPv6 wird nicht unterstützt.</p>

### Aufgetretene Probleme
- Verlust eines Teammitglieds durch Exmatrikulation
- Aufgrund von Problemen beim Parsen der Empfangenen Daten wurde sich nach 10h Debugging dazu entschieden das Feature nicht zu implementieren. (siehe whois Branch)

### Anmerkungen
<p>In der Contributor Liste von GitHub ist aus unbekannten technischen Gründen Paul Antoni nicht aufgeführt. -> siehe Commit History</p>
<p>Als externe API wird die REST API von RIPE STAT verwendet. Die Drittanbieterbibliothek commons-net von Apache wird für die Umrechnung der Netzmasken verwendet.<br>
Als Implementierung einer alternativen Gerätefunktionalität wird das Auslesen der lokalen Netzwerkinformationen verwendet.</p>
