# EC Cash App

Fiktive EC-Bankautomaten-Anwendung fuer eine Softwaretesting-Pruefung an der Hochschule Bremen.

## Start

Voraussetzungen:

- JDK 21
- Maven 3.9+
- Docker Desktop fuer PostgreSQL
- Node.js 20+ fuer Playwright

```powershell
docker compose up -d
mvn package -DskipTests
java -jar target\ec-cash-app-1.0.0.jar
```

Die Anwendung laeuft danach standardmaessig auf Port `8080`:

- ATM: http://localhost:8080/atm
- Admin: http://localhost:8080/admin
- Swagger UI: http://localhost:8080/swagger-ui/index.html

Falls Port `8080` bereits belegt ist, kann die Anwendung auf Port `8081` gestartet werden:

```powershell
java -jar target\ec-cash-app-1.0.0.jar --server.port=8081
```

Dann gelten diese URLs:

- ATM: http://localhost:8081/atm
- Admin: http://localhost:8081/admin
- Swagger UI: http://localhost:8081/swagger-ui/index.html

Admin-Login:

- Benutzer: `admin`
- Passwort: `admin123`

Hinweis fuer Windows: Wenn das Projekt in einem Pfad mit Leerzeichen, OneDrive-Anteil oder Umlauten liegt, kann `mvn spring-boot:run` je nach Java-/Maven-Umgebung mit `ClassNotFoundException: de.hsbremen.atm.AtmApplication` fehlschlagen. Ursache ist dann haeufig ein falsch kodierter Klassenpfad des Spring-Boot-Maven-Plugins. Der empfohlene Start erfolgt deshalb ueber das gebaute Jar:

```powershell
mvn package -DskipTests
java -jar target\ec-cash-app-1.0.0.jar
```

Falls beim Jar-Start die Meldung `Port 8080 was already in use` erscheint, laeuft die Anwendung wahrscheinlich bereits. Pruefen:

```powershell
Get-NetTCPConnection -LocalPort 8080 -State Listen
```

Den zugehoerigen Prozess anzeigen:

```powershell
Get-NetTCPConnection -LocalPort 8080 -State Listen | ForEach-Object { Get-Process -Id $_.OwningProcess }
```

Alternativ kann die Anwendung auf einem anderen Port gestartet werden, z.B. auf `8081`:

```powershell
java -jar target\ec-cash-app-1.0.0.jar --server.port=8081
```

## Docker-Troubleshooting

Falls `docker compose up -d` mit einer Meldung wie dieser fehlschlaegt:

```text
unable to get image 'postgres:16': error during connect ... dockerDesktopLinuxEngine ... The system cannot find the file specified.
```

dann ist Docker installiert, aber Docker Desktop bzw. die Linux Engine laeuft nicht.

Abhilfe:

1. Docker Desktop ueber das Windows-Startmenue starten.
2. Falls Docker Desktop dazu auffordert, anmelden.
3. Warten, bis Docker Desktop vollstaendig geladen ist und die Engine laeuft.
4. Danach im Projektordner erneut ausfuehren:

```powershell
docker compose up -d
```

Wichtig: Der Windows-Dienst "Docker Desktop Service" kann laufen, obwohl Docker Desktop noch nicht geoeffnet, nicht angemeldet oder die Linux Engine noch nicht gestartet ist. In diesem Zustand erscheint haeufig trotzdem die Pipe-Fehlermeldung `dockerDesktopLinuxEngine`.

Wenn Docker Desktop nicht startet, Docker Desktop einmal als Administrator starten oder den Windows-Dienst "Docker Desktop Service" ueber die Diensteverwaltung starten. Danach Docker Desktop erneut oeffnen und auf die laufende Engine warten.

## Testdaten

| Person | Kartennummer | PIN | Typ |
|---|---|---|---|
| Max Mustermann | `100000000001` | `1234` | Hausbank |
| Erika Musterfrau | `200000000001` | `5678` | Fremdbank |
| Ali Beispiel | `100000000002` | `1111` | abgelaufen |
| Dana Deaktiviert | `100000000003` | `9999` | Konto deaktiviert |
| Ungueltige Karte | `999999999999` | `0000` | ungueltig |

## Tests

Backend-Tests:

```powershell
mvn test
```

Playwright vorbereiten:

```powershell
cd e2e
npm install
npx playwright install
npm test
```

## Bearbeitungsorte

- Unit-Tests: `src/test/java/de/hsbremen/atm/unit`
- API-Tests: `src/test/java/de/hsbremen/atm/api`
- Playwright-E2E-Tests: `e2e/tests`
- Aufgabenstellung: `docs/STUDENT_TASK.md`
- Bewertungsbogen: `docs/GRADING_RUBRIC.md`
- API-Hinweise: `docs/API.md`
- Testhinweise: `docs/TESTING.md`
- Offene Punkte fuer Lehrende: `docs/OPEN_POINTS_FOR_LECTURER.md`
