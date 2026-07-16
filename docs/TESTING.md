# Testhinweise

## Backend-Tests starten

```powershell
mvn test
```

Die Testkonfiguration verwendet `application-test.yml`.

## Unit-Tests

Unit-Tests liegen unter:

```text
src/test/java/de/hsbremen/atm/unit
```

Empfohlen:

- klare Arrange-Act-Assert-Struktur
- Grenzwerte testen
- fachliche Erwartung als Assertion ausdruecken
- nicht nur auf "kein Fehler" testen

## API-Tests

API-Tests liegen unter:

```text
src/test/java/de/hsbremen/atm/api
```

Empfohlen:

- komplette Workflows testen
- HTTP-Status pruefen
- Response Body pruefen
- nach schreibenden Operationen den Folgezustand pruefen
- fuer Admin-Endpunkte Basic Auth verwenden

## Playwright

Playwright-Tests liegen unter:

```text
e2e/tests
```

Start:

```powershell
cd e2e
npm install
npx playwright install
npm test
```

Die Spring-Boot-Anwendung muss dafuer lokal auf Port 8080 laufen.

