# iPin2ToBitwarden

Ein einfaches Java Tool um eine iPin2 CSV-Datei in eine JSON-Datei für Bitwarden zu migrieren.

## Was macht das Tool?

Dieses Programm liest eine CSV-Datei aus iPin2 ein und wandelt die enthaltenen Einträge in ein Bitwarden-kompatibles JSON-Format um.

Dabei werden unter anderem übernommen:

- Eintragsname
- Notizen
- Benutzername
- Passwort
- URLs
- Zusätzliche Felder
- Kennzeichnung sicherer Felder

Am Ende wird eine JSON-Datei erzeugt, die anschließend in Bitwarden importiert werden kann.

---

## Voraussetzungen

Damit das Programm funktioniert, brauchst du:

- ein Windows-, Linux- oder macOS-System mit Java
- eine exportierte CSV-Datei aus iPin2
- Bitwarden, um die erzeugte JSON-Datei zu importieren

---

## iPin2 CSV Export erstellen

#### Schritt 1: iPin2 öffen und in den Tab `Mehr` gehen
<img width="414" height="855" alt="image" src="https://github.com/user-attachments/assets/a8f4624b-94ff-48ff-a244-24c656b59f3a" />

#### Schritt 2: `Export` öffnen und `CSV Export` auswählen
<img width="414" height="858" alt="image" src="https://github.com/user-attachments/assets/9ec56616-b1e5-4845-9cb9-4b4e3f867462" />

#### Schritt 3: Oben rechts `Plus` auswählen um einen neuen Export zu erstellen (optional Kopfzeile mit erzeugen lassen)
<img width="414" height="858" alt="image" src="https://github.com/user-attachments/assets/789f0fcd-106a-40b7-ac9d-5e783498dcd0" />

---

## iPin2ToBitwarden verwenden

1. Starte das Programm (unter Windows mithilfe der `start.bat` Datei).
2. Wähle die CSV-Datei aus, die aus iPin2 exportiert wurde.
3. Das Tool liest die Datei automatisch ein.
4. Anschließend wird eine Datei mit dem Namen  
   `BitwardenJsonConverted.json`  
   im gleichen Ordner wie die CSV-Datei erstellt.

---

## Import in Bitwarden

Nachdem die JSON-Datei erstellt wurde:

#### Schritt 1: Öffne Bitwarden Web-Vault.

#### Schritt 2: Unter `Werkzeuge` den `Import` auswählen
<img width="275" height="339" alt="image" src="https://github.com/user-attachments/assets/54c53b78-99a7-4640-b60e-edb31f3ebeac" />

#### Schritt 3: Anschließen das Ziel für den Import auswählen sowie die Datei hochladen
<img width="923" height="799" alt="image" src="https://github.com/user-attachments/assets/80e70843-2d1e-4e33-96b4-7306d95498f1" />

---

## Welche Daten werden übernommen?

Das Tool verarbeitet pro Eintrag:

- **Gruppen**
- **Eintragsname**
- **Benutzername**
- **Passwort**
- **URLs**
- **Notizen**
- **Benutzerdefinierte Felder**

### Spezielle Felder

- Felder mit dem Namen **„Benutzername“** werden als Login-Benutzername behandelt.
- Felder mit dem Namen **„Passwort“** werden als Login-Passwort behandelt.
- Felder mit dem Namen **„URL“** werden als Webseite/URI übernommen.
- Andere Felder werden als zusätzliche Bitwarden-Felder exportiert.

---

## Hinweise

- Die erzeugte Datei überschreibt eine bereits vorhandene Datei mit demselben Namen.
- Das Tool erwartet eine gültige iPin2-CSV-Datei.
- Falls Daten in der CSV ungewöhnlich formatiert sind, kann es zu Importfehlern kommen.
- Die Log-Ausgabe wird zusätzlich in einer Datei namens `iPinToBitwarden.log` gespeichert.

---

## Log-Datei

Während der Konvertierung schreibt das Programm Protokolle in:

`iPinToBitwarden.log`

Darin findest du Informationen über:

- geladene CSV-Datei
- Anzahl gelesener Einträge
- Beginn und Ende der Konvertierung
- Fehler während des Exports

## Support

Falls du Probleme bei der Verwendung hast, prüfe zuerst:

- ob die CSV-Datei korrekt exportiert wurde
- ob Java installiert ist
- ob die erzeugte JSON-Datei im richtigen Ordner liegt
