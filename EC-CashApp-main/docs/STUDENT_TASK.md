# Abschlussprojekt: Testen der EC Cash App

## 1. Ausgangssituation

Du testest eine fiktive Anwendung fuer einen EC-Bankautomaten. Die Anwendung besteht aus:

- einer Kundenoberflaeche fuer den Automaten
- einer Admin-Oberflaeche zur Verwaltung von Konten, Karten und Geldkassette
- einer REST-API fuer ATM- und Admin-Funktionen
- einer PostgreSQL-Datenbank

Die Anwendung kann fachliche oder technische Fehler enthalten. Deine Aufgabe ist es, diese systematisch zu analysieren, Testfaelle herzuleiten, automatisierte Tests zu implementieren und gefundene Fehler nachvollziehbar zu dokumentieren.

Bearbeitungszeit: 4 Wochen.

Gruppengroesse: 3 Studierende pro Gruppe.

## 2. Bearbeitungsmodus

Das Projekt ist als Grey-Box-Testaufgabe angelegt:

- Du erhaeltst die lauffaehige Anwendung.
- Du erhaeltst Quellcode, API-Hinweise und Testdaten.
- Du darfst Black-Box-, Grey-Box- und White-Box-Techniken kombinieren.
- Codeanalyse ist erlaubt und kann Bonuspunkte bringen.

Zustandstabellen und Zustandsdiagramme sind in dieser Aufgabe nicht gefordert.

## 2.1 Erlaubte Hilfsmittel

Erlaubt sind insbesondere:

- Vorlesungsunterlagen und eigene Mitschriften
- Dokumentationen der verwendeten Werkzeuge und Bibliotheken
- ISTQB-Website und ISTQB-Glossar
- Fachliteratur und serioese Online-Quellen
- KI-Werkzeuge, sofern deren Nutzung transparent dokumentiert wird

Bei Nutzung von KI-Werkzeugen muessen mindestens angegeben werden:

- verwendetes Werkzeug
- Zweck der Nutzung
- relevante Prompts oder eine nachvollziehbare Prompt-Zusammenfassung
- Bewertung, welche Inhalte uebernommen, angepasst oder verworfen wurden

Die Verantwortung fuer fachliche Richtigkeit, Eigenleistung und Quellenarbeit liegt bei der Gruppe.

## 3. Pflichtaufgaben

### 3.1 Testkonzept

Erstelle ein Testkonzept fuer die EC Cash App. Das Testkonzept muss mindestens enthalten:

- Testziele
- Testobjekt und Testumfang
- nicht getestete Bereiche mit Begruendung
- Testarten und Teststufen
- Testumgebung
- Testdatenstrategie
- Risiken und Priorisierung
- Ein- und Austrittskriterien
- Rollen bzw. Verantwortlichkeiten, soweit relevant

### 3.2 Flussdiagramme

Erstelle Flussdiagramme fuer die wichtigsten Ablaeufe:

- Kundenablauf: Karte verwenden, Sprache waehlen, PIN pruefen, Kontostand abfragen oder Geld abheben
- Adminablauf: Konto/Karte verwalten und Geldkassette verwalten

Die Diagramme muessen fachlich lesbar sein und als Grundlage fuer die Use Cases und Testfaelle dienen.

### 3.3 Use Cases

Definiere mindestens 5 Use Cases, die direkt aus den Flussdiagrammen abgeleitet sind:

- 3 Use Cases fuer Kundenablaeufe
- 2 Use Cases fuer Adminablaeufe

Jeder Use Case muss mindestens enthalten:

- Name
- Ziel
- beteiligter Akteur
- Vorbedingungen
- Standardablauf
- Alternativ- oder Fehlerablaeufe
- Nachbedingungen
- Bezug zum Flussdiagramm

### 3.4 Testdaten

Testdaten sind zentral zu verwalten. Du darfst diese nicht unkontrolliert in einzelnen Tests verstreuen.

Erwartet wird mindestens:

- eine zentrale Testdatenuebersicht im Report
- konsistente Verwendung der vorgegebenen Testkonten und Karten
- nachvollziehbare Erzeugung oder Ruecksetzung zusaetzlicher Testdaten
- klare Kennzeichnung, welche Testdaten fuer welchen Testfall verwendet werden

### 3.5 Testfaelle

Definiere mindestens 10 Testfaelle.

Jeder Testfall muss mindestens enthalten:

- Testfall-ID
- Titel
- Bezug zu Use Case, Risiko oder Anforderung
- Vorbedingungen
- Testdaten
- Testschritte
- erwartetes Ergebnis
- tatsaechliches Ergebnis
- Status
- Prioritaet bzw. Risikogewichtung

Die Testfaelle muessen nach bekannten risikobasierten Methoden gewichtet werden. Geeignete Methoden sind z. B.:

- Eintrittswahrscheinlichkeit x Schadenshoehe
- Risk Priority Number bzw. FMEA-nahe Bewertung
- MoSCoW mit begruendeter Risikozuordnung
- geschaeftskritische Gewichtung nach Auswirkung auf Geld, Sicherheit und Verfuegbarkeit

Die gewaehlte Methode muss im Testkonzept erklaert und konsistent angewendet werden.

### 3.6 Automatisierte Tests

Implementiere mindestens:

1. Einen Unit-Test, der einen Fehler in einer fachlichen Backend-Komponente aufdeckt.
2. Zwei API-Tests, die Fehler in REST-basierten Workflows aufdecken.
3. Einen End-to-End-Test mit Playwright, der einen Logikfehler aus Kundensicht aufdeckt.

Die Tests muessen automatisiert ausfuehrbar sein.

Bearbeitungsorte:

- Unit-Tests: `src/test/java/de/hsbremen/atm/unit`
- API-Tests: `src/test/java/de/hsbremen/atm/api`
- Playwright-Tests: `e2e/tests`

### 3.7 Bug-Tickets

Fuer gefundene Fehler sind Bug-Tickets nach ISTQB-orientierter Defect-Report-Logik zu erstellen.

Jedes Bug-Ticket muss mindestens enthalten:

- eindeutige Ticket-ID
- Titel
- betroffene Funktion oder Komponente
- Schweregrad
- Prioritaet
- Umgebung
- Vorbedingungen
- Reproduktionsschritte
- erwartetes Verhalten
- tatsaechliches Verhalten
- Belege, z. B. Screenshot, Logauszug oder Testausgabe
- Verweis auf automatisierten Test, sofern vorhanden

### 3.8 Report

Erstelle einen Abschlussreport primaer als PDF. Der Report soll sich an den Vorgaben fuer wissenschaftliche Arbeiten der Hochschule Bremen orientieren.

Als formale Referenzen gelten insbesondere:

- HSB-Leitfaden zur Erstellung wissenschaftlicher Arbeiten, sofern fuer Deinen Studiengang einschlaegig
- Ordnung der Hochschule Bremen zur Sicherung guter wissenschaftlicher Praxis
- Vorgaben der Lehrveranstaltung

Der fachliche Testbericht soll ISTQB-orientiert aufgebaut sein. Nutze insbesondere Begriffe wie Testbasis, Testziel, Testfall, erwartetes Ergebnis, tatsaechliches Ergebnis, Fehlerwirkung/Failure, Defect/Bug, Schweregrad und Prioritaet konsistent.

Der Report muss mindestens enthalten:

- Deckblatt
- Inhaltsverzeichnis
- Einleitung und Zielsetzung
- Beschreibung des Testobjekts
- Testkonzept
- Flussdiagramme
- Use Cases
- Testdatenkonzept
- Risikoanalyse und Gewichtung
- Testfalluebersicht
- Beschreibung der automatisierten Tests
- Bug-Tickets bzw. Fehleruebersicht
- Bewertung der Testergebnisse
- Reflexion und Grenzen der Untersuchung
- Fazit
- Literatur- und Quellenverzeichnis, sofern Quellen genutzt wurden
- Anhang, z. B. Testprotokolle, Screenshots, relevante Auszuege

## 4. Erwartete Testarten

### Unit-Test

Teste eine einzelne fachliche Komponente isoliert. Geeignete Kandidaten sind:

- `FeeCalculator`
- `DailyLimitService`
- `CashDispenserService`

### API-Test

Teste die REST-API gegen eine Testdatenbank. Geeignete Workflows sind:

- Karte einstecken
- PIN pruefen
- Kontostand abfragen
- Geld abheben
- Konto im Adminbereich deaktivieren
- Tageslimit pruefen

### End-to-End-Test

Teste die Kundenoberflaeche mit Playwright aus Sicht eines Automatenkunden.

## 5. Abgabe

Gib ab:

- Abschlussreport primaer als PDF
- Quellcode mit automatisierten Tests, entweder per GitHub-Repository oder als ZIP-Datei mit den relevanten Dateien
- Testkonzept
- Flussdiagramme
- 5 Use Cases
- zentrale Testdatenuebersicht
- mindestens 10 definierte und gewichtete Testfaelle
- Bug-Tickets fuer gefundene Fehler
- kurze Start- und Ausfuehrungsanleitung, falls vom Projektstandard abgewichen wird

Bei GitHub-Abgabe muss der finale Stand klar erkennbar sein, z. B. durch Commit-Hash, Tag oder Release. Bei ZIP-Abgabe duerfen Build-Artefakte wie `target/` oder `node_modules/` weggelassen werden.

## 6. Bonus

Bonuspunkte sind moeglich fuer:

- nachvollziehbare Codeanalyse
- begruendete Fehlerursachenanalyse
- korrigierende Fixes im Code
- Nachweis, dass der Fix durch Tests abgesichert ist

Ein Fix ohne nachvollziehbaren Testnachweis wird nicht voll bewertet.
