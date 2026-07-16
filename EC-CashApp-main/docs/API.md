# API-Uebersicht

Swagger UI ist unter `http://localhost:8080/swagger-ui/index.html` verfuegbar.

## ATM

### Karte einstecken

`POST /api/atm/cards/insert`

```json
{
  "cardNumber": "100000000001"
}
```

### Sprache setzen

`POST /api/atm/sessions/{sessionId}/language`

```json
{
  "language": "de"
}
```

### PIN pruefen

`POST /api/atm/sessions/{sessionId}/pin`

```json
{
  "pin": "1234"
}
```

### Kontostand

`GET /api/atm/sessions/{sessionId}/balance`

### Auszahlung

`POST /api/atm/sessions/{sessionId}/withdrawals`

```json
{
  "amountCents": 5000
}
```

## Admin

Admin-Endpunkte sind mit HTTP Basic Auth geschuetzt.

- Benutzer: `admin`
- Passwort: `admin123`

### Konten

- `GET /api/admin/accounts`
- `POST /api/admin/accounts`
- `PUT /api/admin/accounts/{accountId}`
- `POST /api/admin/accounts/{accountId}/deactivate`
- `POST /api/admin/accounts/{accountId}/activate`

### Karten

- `GET /api/admin/cards`
- `PUT /api/admin/cards/{cardId}`
- `POST /api/admin/cards/{cardId}/pin`
- `POST /api/admin/cards/{cardId}/reset-attempts`

### Geldkassette

- `GET /api/admin/cassette`
- `PUT /api/admin/cassette`

## Testzeit

Fuer automatisierte Tests kann pro Request ein Zeitpunkt gesetzt werden:

```text
X-Test-Now: 2026-07-09T22:10:00Z
```

Der Header ist fuer API-Tests gedacht, in denen zeitabhaengige Fachlogik reproduzierbar geprueft werden soll.

