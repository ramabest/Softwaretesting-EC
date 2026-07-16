# Anforderungsspezifikation v1.0: Fiktiver EC-Bankautomat

## 1. Ziel der Anwendung

Die Anwendung dient als Testobjekt fuer eine Abschlusspruefung im Bereich Softwareentwicklung und Softwaretesting an der Hochschule Bremen. Die Studierenden sollen anhand einer realistischen, aber ueberschaubaren Bankautomaten-Anwendung automatisierte Tests entwerfen, implementieren und auswerten.

Die Anwendung besteht aus zwei Hauptbereichen:

- einer Kundenoberflaeche fuer einen EC-Bankautomaten
- einer Admin-Oberflaeche zur Verwaltung fiktiver Konten, Karten und der virtuellen Geldkassette

Die Anwendung enthaelt absichtlich fachliche und technische Fehler. Diese Fehler sind den Studierenden nicht bekannt.

## 2. Empfohlener Technologie-Stack

- Programmiersprache: Java 21
- Backend: Spring Boot 3.x
- Datenbank: PostgreSQL
- Entwicklungsdatenbank: Docker Compose mit PostgreSQL
- UI: serverseitige Web-App mit Thymeleaf, HTMX und Bootstrap 5
- API-Dokumentation: OpenAPI/Swagger
- Unit-Tests: JUnit 5, AssertJ, Mockito
- API-Tests: REST Assured
- E2E-Tests: Playwright
- Build-Tool: Maven

Begruendung: Eine serverseitige Web-App bleibt kompatibel und leicht startbar, wirkt mit Bootstrap 5 modern genug und vermeidet unnoetige Frontend-Komplexitaet. Playwright kann die Anwendung trotzdem vollstaendig ueber den Browser testen.

## 3. Rollen

### 3.1 Kunde

Ein Kunde kann am Automaten:

- eine Karte einstecken bzw. auswaehlen
- eine Sprache waehlen
- eine PIN eingeben
- den Kontostand abfragen
- Bargeld abheben

### 3.2 Administrator

Ein Administrator kann:

- Konten erstellen
- Konten bearbeiten
- Konten deaktivieren
- Karten zu Konten verwalten
- PINs von Karten manipulieren
- Karten sperren, entsperren oder als abgelaufen markieren
- die virtuelle Geldkassette konfigurieren

## 4. Fachliche Anforderungen Kundenoberflaeche

### 4.1 Kartentypen

Die Anwendung kennt drei Kartenarten:

- Hausbankkarte
- Fremdbankkarte
- ungueltige bzw. falsche Karte

Eine Karte hat mindestens folgende Eigenschaften:

- Kartennummer
- Kartenart
- zugeordnetes Konto
- PIN
- Ablaufdatum
- Status: aktiv, gesperrt, abgelaufen, ungueltig
- Anzahl falscher PIN-Eingaben

### 4.2 Karteneingabe

Der Kunde kann eine Karte virtuell einstecken, indem er eine Kartennummer eingibt oder eine Karte aus Testdaten auswaehlt.

Die Anwendung muss folgende Kartenfaelle unterscheiden:

- gueltige Hausbankkarte
- gueltige Fremdbankkarte
- abgelaufene Karte
- gesperrte Karte
- ungueltige oder unbekannte Karte
- Karte zu deaktiviertem Konto

### 4.3 Sprache

Nach Erkennung einer grundsaetzlich gueltigen Karte kann der Kunde eine Sprache waehlen:

- Deutsch
- Englisch

Die Sprache betrifft mindestens:

- PIN-Dialog
- Hauptmenue
- Kontostandsanzeige
- Auszahlungsdialog
- Fehlermeldungen
- Abschlussmeldung

### 4.4 PIN-Pruefung

Der Kunde muss eine vierstellige numerische PIN eingeben.

Regeln:

- Bei korrekter PIN wird der Zugang freigegeben.
- Bei falscher PIN wird der Fehlversuchszaehler der Karte erhoeht.
- Nach drei falschen PIN-Eingaben wird die Karte gesperrt.
- Eine gesperrte Karte darf nicht mehr fuer Kontostand oder Auszahlung verwendet werden.
- Der Fehlversuchszaehler wird nach erfolgreicher PIN-Eingabe auf 0 zurueckgesetzt.

### 4.5 Kontostand

Der Kunde kann nach erfolgreicher PIN-Pruefung den Kontostand abfragen.

Der Kontostand wird in Euro mit zwei Nachkommastellen angezeigt.

### 4.6 Auszahlung

Der Kunde kann nach erfolgreicher PIN-Pruefung Bargeld abheben.

Erlaubte Standardbetraege:

- 20 EUR
- 50 EUR
- 100 EUR
- 200 EUR
- 500 EUR
- 1.000 EUR
- 2.000 EUR
- 5.000 EUR

Regeln:

- Das Tageslimit betraegt 5.000 EUR pro Karte.
- Die Zuruecksetzung des Tageslimits erfolgt um 00:00 Uhr Ortszeit Europe/Berlin.
- Das Konto darf nach Auszahlung und Gebuehr maximal bis 50.000 EUR ins Soll geraten.
- Ein Konto darf maximal 100.000 EUR Guthaben haben.
- Deaktivierte Konten duerfen keine Auszahlung erhalten.
- Abgelaufene, gesperrte oder ungueltige Karten duerfen keine Auszahlung erhalten.
- Die virtuelle Geldkassette muss den Betrag mit verfuegbaren Scheinen ausgeben koennen.
- Der Kontostand wird erst nach erfolgreicher Pruefung aller Bedingungen veraendert.

### 4.7 Fremdbankgebuehr

Bei Fremdbankkarten faellt eine Gebuehr von 2,5 Prozent des Auszahlungsbetrags an.

Beispiel:

- Auszahlung: 200,00 EUR
- Gebuehr: 5,00 EUR
- Belastung des Kontos: 205,00 EUR

Die Gebuehr wird nicht in bar ausgezahlt, sondern nur dem Konto belastet.

### 4.8 Geldkassette

Die virtuelle Geldkassette enthaelt Scheine folgender Stueckelung:

- 5 EUR
- 10 EUR
- 20 EUR
- 50 EUR
- 100 EUR
- 200 EUR
- 500 EUR

Vorgeschlagene Standardbefuellung fuer Testdaten:

| Schein | Anzahl | Summe |
|---:|---:|---:|
| 500 EUR | 20 | 10.000 EUR |
| 200 EUR | 50 | 10.000 EUR |
| 100 EUR | 100 | 10.000 EUR |
| 50 EUR | 200 | 10.000 EUR |
| 20 EUR | 500 | 10.000 EUR |
| 10 EUR | 300 | 3.000 EUR |
| 5 EUR | 200 | 1.000 EUR |

Gesamtsumme: 54.000 EUR

Hinweis: Die 500-EUR-Note wird hier fuer die fiktive Pruefungsanwendung bewusst zugelassen, auch wenn reale Automaten sie in der Praxis nicht typischerweise ausgeben.

## 5. Fachliche Anforderungen Admin-Oberflaeche

### 5.1 Konto erstellen

Ein Admin kann ein Girokonto erstellen mit:

- Vorname
- Nachname
- IBAN
- Kontostand in Cent
- Ueberziehungslimit in Cent
- Status: aktiv oder deaktiviert

Validierungen:

- IBAN muss eindeutig sein.
- Startguthaben darf maximal 100.000 EUR betragen.
- Ueberziehungslimit darf maximal 50.000 EUR betragen.
- Kontostand und Limits werden intern als Long-Werte in Cent gespeichert.

### 5.2 Konto bearbeiten

Ein Admin kann aendern:

- Name des Kontoinhabers
- Kontostand
- Ueberziehungslimit
- Aktivstatus

### 5.3 Konto deaktivieren

Ein deaktiviertes Konto:

- darf keinen Kontostand am Automaten anzeigen
- darf keine Auszahlung erhalten
- bleibt im Adminbereich sichtbar
- kann spaeter wieder aktiviert werden

### 5.4 Karten verwalten

Ein Admin kann fuer eine Karte:

- PIN setzen
- Ablaufdatum setzen
- Status setzen
- Fehlversuchszaehler zuruecksetzen
- Karte einem Konto zuordnen

### 5.5 Geldkassette verwalten

Ein Admin kann je Scheinart die Anzahl der vorhandenen Scheine setzen.

Validierungen:

- Anzahl darf nicht negativ sein.
- Anzahl muss ganzzahlig sein.
- Die Gesamtsumme soll im Adminbereich angezeigt werden.

## 6. Datenmodell

### 6.1 Account

- `id: Long`
- `iban: String`
- `ownerFirstName: String`
- `ownerLastName: String`
- `balanceCents: Long`
- `overdraftLimitCents: Long`
- `active: boolean`
- `createdAt: Instant`
- `updatedAt: Instant`

### 6.2 Card

- `id: Long`
- `cardNumber: String`
- `cardType: enum(HOUSE_BANK, FOREIGN_BANK, INVALID)`
- `pinHash: String`
- `expiresOn: LocalDate`
- `status: enum(ACTIVE, BLOCKED, EXPIRED, INVALID)`
- `failedPinAttempts: int`
- `accountId: Long`
- `createdAt: Instant`
- `updatedAt: Instant`

### 6.3 CashCassetteSlot

- `id: Long`
- `denominationCents: int`
- `noteCount: int`

### 6.4 Withdrawal

- `id: Long`
- `cardId: Long`
- `accountId: Long`
- `amountCents: Long`
- `feeCents: Long`
- `totalDebitCents: Long`
- `createdAt: Instant`
- `businessDate: LocalDate`
- `status: enum(APPROVED, DECLINED)`
- `declineReason: String`

## 7. API-Skizze

### 7.1 ATM API

`POST /api/atm/cards/insert`

Prueft eine Karte und startet eine ATM-Session.

`POST /api/atm/sessions/{sessionId}/language`

Setzt die Sprache der Session.

`POST /api/atm/sessions/{sessionId}/pin`

Prueft die PIN.

`GET /api/atm/sessions/{sessionId}/balance`

Liefert den Kontostand.

`POST /api/atm/sessions/{sessionId}/withdrawals`

Fuehrt eine Auszahlung durch.

`POST /api/atm/sessions/{sessionId}/finish`

Beendet die Session.

### 7.2 Admin API

`GET /api/admin/accounts`

Listet Konten.

`POST /api/admin/accounts`

Erstellt ein Konto.

`PUT /api/admin/accounts/{accountId}`

Bearbeitet ein Konto.

`POST /api/admin/accounts/{accountId}/deactivate`

Deaktiviert ein Konto.

`POST /api/admin/accounts/{accountId}/activate`

Aktiviert ein Konto.

`GET /api/admin/cards`

Listet Karten.

`PUT /api/admin/cards/{cardId}`

Bearbeitet eine Karte.

`POST /api/admin/cards/{cardId}/pin`

Setzt eine neue PIN.

`GET /api/admin/cassette`

Liefert die aktuelle Kassettenbefuellung.

`PUT /api/admin/cassette`

Setzt die Kassettenbefuellung.

## 8. Vorgeschlagene Projektstruktur

```text
ec-atm-exam/
  README.md
  pom.xml
  docker-compose.yml
  src/
    main/
      java/
        de/hsbremen/atm/
          AtmApplication.java
          account/
            Account.java
            AccountRepository.java
            AccountService.java
            AccountController.java
            AccountAdminController.java
          card/
            Card.java
            CardType.java
            CardStatus.java
            CardRepository.java
            CardService.java
            PinService.java
            CardAdminController.java
          cash/
            CashCassetteSlot.java
            CashCassetteRepository.java
            CashDispenserService.java
            CashCassetteAdminController.java
          withdrawal/
            Withdrawal.java
            WithdrawalRepository.java
            WithdrawalService.java
            FeeCalculator.java
            DailyLimitService.java
            WithdrawalController.java
          session/
            AtmSession.java
            AtmSessionRepository.java
            AtmSessionService.java
          web/
            AtmWebController.java
            AdminWebController.java
          common/
            MoneyFormatter.java
            BusinessClock.java
            ApiError.java
            GlobalExceptionHandler.java
          config/
            SecurityConfig.java
            OpenApiConfig.java
            DataInitializer.java
      resources/
        application.yml
        application-dev.yml
        application-test.yml
        db/
          migration/
            V1__create_schema.sql
            V2__insert_test_data.sql
        templates/
          atm/
            insert-card.html
            language.html
            pin.html
            menu.html
            balance.html
            withdraw.html
            result.html
          admin/
            dashboard.html
            accounts.html
            account-form.html
            cards.html
            card-form.html
            cassette.html
        static/
          css/
            app.css
          js/
            app.js
    test/
      java/
        de/hsbremen/atm/
          unit/
            FeeCalculatorTest.java
            DailyLimitServiceTest.java
          api/
            AtmWithdrawalApiTest.java
            AdminAccountApiTest.java
          support/
            TestDataFactory.java
            AbstractPostgresIntegrationTest.java
      resources/
        application-test.yml
  e2e/
    package.json
    playwright.config.ts
    tests/
      atm-card-lock.spec.ts
      atm-withdrawal.spec.ts
  docs/
    API.md
    TESTING.md
    STUDENT_TASK.md
```

## 9. Hinweise fuer Studierende

Die Studierenden bearbeiten hauptsaechlich folgende Bereiche:

- Unit-Tests unter `src/test/java/de/hsbremen/atm/unit`
- API-Tests unter `src/test/java/de/hsbremen/atm/api`
- E2E-Tests unter `e2e/tests`

Fuer den Unit-Test sollen sie gezielt eine fachliche Komponente isoliert testen, z. B.:

- `FeeCalculator`
- `DailyLimitService`
- `CashDispenserService`

Fuer API-Tests sollen sie die REST-Schnittstellen gegen eine laufende Testdatenbank pruefen, z. B.:

- Auszahlung mit Fremdbankgebuehr
- Tageslimit ueber Mitternacht Europe/Berlin
- Auszahlung bei deaktiviertem Konto
- Verhalten bei abgelaufener oder gesperrter Karte

Fuer E2E-Tests sollen sie Playwright verwenden und die Anwendung aus Kundensicht bedienen.

## 10. Testdaten

### 10.1 Konten

| Name | IBAN | Kontostand | Ueberziehung | Status |
|---|---|---:|---:|---|
| Max Mustermann | DE00100100100000000001 | 1.250,00 EUR | 2.000,00 EUR | aktiv |
| Erika Musterfrau | DE00100100100000000002 | 800,00 EUR | 1.000,00 EUR | aktiv |
| Ali Beispiel | DE00100100100000000003 | 2.000,00 EUR | 500,00 EUR | aktiv |
| Dana Deaktiviert | DE00100100100000000004 | 500,00 EUR | 500,00 EUR | deaktiviert |
| Greta Guthaben | DE00100100100000000005 | 99.900,00 EUR | 5.000,00 EUR | aktiv |

### 10.2 Karten

| Karteninhaber | Kartennummer | Kartenart | PIN | Status |
|---|---|---|---|---|
| Max Mustermann | 100000000001 | Hausbank | 1234 | aktiv |
| Erika Musterfrau | 200000000001 | Fremdbank | 5678 | aktiv |
| Ali Beispiel | 100000000002 | Hausbank | 1111 | abgelaufen |
| Dana Deaktiviert | 100000000003 | Hausbank | 9999 | aktiv |
| Ungueltige Karte | 999999999999 | ungueltig | 0000 | ungueltig |

## 11. Pruefungsanforderungen an automatisierte Tests

Die Studierenden sollen mindestens folgende automatisierte Tests erstellen:

1. Einen Unit-Test, der einen mittelmaessig schwierig auffindbaren Fehler aufdeckt.
2. Zwei API-Tests, die schwierige Fehler aufdecken.
3. Einen End-to-End-Test mit Playwright, der einen einfachen Logikfehler aufdeckt.

Bonuspunkte koennen fuer Codeanalyse, aussagekraeftige Testdaten, saubere Assertions und nachvollziehbare Fehlerberichte vergeben werden.

## 12. Nichtfunktionale Anforderungen

- Die Anwendung muss lokal startbar sein.
- Die Anwendung muss reproduzierbare Testdaten bereitstellen.
- Die Anwendung muss ueber Swagger/OpenAPI dokumentiert sein.
- Die UI soll modern, klar und nicht ueberladen wirken.
- Geldwerte werden intern ausschliesslich als Long-Werte in Cent gespeichert.
- Fachliche Zeitlogik fuer Tageslimits verwendet Europe/Berlin.
- Tests muessen automatisiert ausfuehrbar sein.

