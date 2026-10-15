# Sharenergy | Powerbank Rental Prototype

**A web concept for discovering, renting, and returning shared powerbanks.** This prototype combines a Java backend, a browser-based rental flow, and a local SQLite database.

[English](#english) | [Nederlands](#nederlands) | [Español](#español)

---

## English

### Overview

Sharenergy is a demonstration of a shared powerbank rental experience. Customers can simulate scanning a station QR code, register a phone number, check battery availability, start a rental, and review the calculated charge when returning a device. The inventory and rental records are stored locally in SQLite.

This is a concept application, not a production-ready rental or payment service.

### Features

- Station page with a simulated QR scan and live battery availability.
- Phone-number registration and powerbank selection.
- Rental timer and return flow.
- Usage summary with a calculated rental charge.
- SQLite persistence and an inventory status endpoint.
- Three sample powerbanks inserted when the database is first created.

### Technology

- Java 17
- Maven
- Javalin 6.1.3
- SQLite with SQLite JDBC
- JDBI 3.44.1
- HTML, CSS, and JavaScript

### Requirements

- JDK 17 or later
- Maven 3.6 or later
- Internet access on the first build to download Maven dependencies

### Run locally

Run these commands from the project root, where `pom.xml` is located:

```bash
mvn org.codehaus.mojo:exec-maven-plugin:3.3.0:java -Dexec.mainClass=com.sharenergy.Main
```

Open [http://localhost:7070/index.html](http://localhost:7070/index.html). Stop the server with `Ctrl+C`.

The application creates `sharenergy.db` in the working directory on first startup and seeds it with sample inventory. Keep the process running from the project root so the `web` directory and database are found correctly.

### Rental flow

1. Open the station page and click the QR image to simulate scanning.
2. Enter a phone number and continue.
3. Select an available powerbank.
4. End the rental to view the duration and calculated charge.
5. Confirm the simulated payment to reach the completion page.

### Pages

| Page | Purpose |
| --- | --- |
| `web/index.html` | Station entry point, simulated QR scan, and live inventory |
| `web/login.html` | Phone-number registration |
| `web/select.html` | Powerbank selection |
| `web/using.html` | Active rental timer and return action |
| `web/payment.html` | Rental summary and simulated payment confirmation |
| `web/summary.html` | Completion screen |
| `web/unavailable.html` | No-device-available screen |

### API endpoints

The backend listens on port `7070` and exposes the following routes:

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/lista-baterias` | Returns the powerbank inventory and availability |
| `GET` | `/api/registro?u={sessionId}&tel={phone}` | Registers a user by session and phone number |
| `GET` | `/api/alquilar?u={sessionId}` | Starts a rental using the first available powerbank |
| `GET` | `/api/alquilar?u={sessionId}&b={batteryId}` | Starts a rental for a selected powerbank |
| `GET` | `/api/devolver?u={sessionId}` | Ends the active rental and returns its duration and charge |
| `GET` | `/api/estado` | Displays an HTML inventory status view for development |

### Pricing and prototype limitations

The current backend calculates the charge as a €0.50 base fee plus €0.02 for each started rental minute, with a minimum of one billable minute. Returning a powerbank marks it as charging; a local simulation makes it available again after 10 seconds.

Payment is **not processed**. The confirmation button only advances to the completion page; no payment provider, authentication, production QR scanning, or deployment configuration is included. The current API also uses `GET` for actions that would normally require more secure HTTP methods in a production service.

---

## Nederlands

### Overzicht

Sharenergy is een demonstratie van het huren van gedeelde powerbanks. Gebruikers kunnen het scannen van een QR-code bij een station simuleren, een telefoonnummer registreren, de beschikbaarheid controleren, een huur starten en na het inleveren de berekende kosten bekijken. De voorraad en huurregistraties worden lokaal opgeslagen in SQLite.

Dit is een conceptapplicatie, geen productieklare verhuur- of betaaldienst.

### Functies

- Stationpagina met een gesimuleerde QR-scan en actuele beschikbaarheid.
- Registratie met een telefoonnummer en selectie van een powerbank.
- Huurklok en proces voor het inleveren.
- Gebruiksoverzicht met berekende huurkosten.
- SQLite-opslag en een endpoint voor de voorraadstatus.
- Drie voorbeeldpowerbanks worden toegevoegd wanneer de database voor het eerst wordt aangemaakt.

### Technologie

- Java 17
- Maven
- Javalin 6.1.3
- SQLite met SQLite JDBC
- JDBI 3.44.1
- HTML, CSS en JavaScript

### Vereisten

- JDK 17 of hoger
- Maven 3.6 of hoger
- Internettoegang bij de eerste build om Maven-afhankelijkheden te downloaden

### Lokaal starten

Voer de volgende opdracht uit vanuit de projectmap met `pom.xml`:

```bash
mvn org.codehaus.mojo:exec-maven-plugin:3.3.0:java -Dexec.mainClass=com.sharenergy.Main
```

Open [http://localhost:7070/index.html](http://localhost:7070/index.html). Stop de server met `Ctrl+C`.

Bij de eerste start maakt de applicatie `sharenergy.db` aan in de huidige map en voegt voorbeeldvoorraad toe. Start het proces vanuit de projectmap, zodat de map `web` en de database correct worden gevonden.

### Huurproces

1. Open de stationpagina en klik op de QR-afbeelding om het scannen te simuleren.
2. Voer een telefoonnummer in en ga verder.
3. Selecteer een beschikbare powerbank.
4. Beëindig de huur om de gebruiksduur en berekende kosten te bekijken.
5. Bevestig de gesimuleerde betaling om naar de afrondingspagina te gaan.

### Pagina's

| Pagina | Doel |
| --- | --- |
| `web/index.html` | Startpunt van het station, gesimuleerde QR-scan en actuele voorraad |
| `web/login.html` | Registratie met telefoonnummer |
| `web/select.html` | Selectie van een powerbank |
| `web/using.html` | Huurklok en actie om in te leveren |
| `web/payment.html` | Huurgegevens en gesimuleerde betalingsbevestiging |
| `web/summary.html` | Afrondingspagina |
| `web/unavailable.html` | Pagina wanneer er geen apparaten beschikbaar zijn |

### API-endpoints

De backend luistert op poort `7070` en biedt de volgende routes:

| Methode | Endpoint | Beschrijving |
| --- | --- | --- |
| `GET` | `/api/lista-baterias` | Geeft de powerbankvoorraad en beschikbaarheid terug |
| `GET` | `/api/registro?u={sessionId}&tel={phone}` | Registreert een gebruiker met sessie en telefoonnummer |
| `GET` | `/api/alquilar?u={sessionId}` | Start een huur met de eerste beschikbare powerbank |
| `GET` | `/api/alquilar?u={sessionId}&b={batteryId}` | Start een huur voor een geselecteerde powerbank |
| `GET` | `/api/devolver?u={sessionId}` | Beëindigt de actieve huur en geeft duur en kosten terug |
| `GET` | `/api/estado` | Toont een HTML-overzicht van de voorraad voor ontwikkeling |

### Tarieven en beperkingen van het prototype

De backend berekent momenteel €0,50 basiskosten plus €0,02 per begonnen huurminuut, met minimaal één factureerbare minuut. Na het inleveren krijgt een powerbank de status 'wordt opgeladen'; een lokale simulatie maakt deze na 10 seconden weer beschikbaar.

Er wordt **geen betaling verwerkt**. De bevestigingsknop opent alleen de afrondingspagina. Er is geen betaalprovider, authenticatie, echte QR-scan of productieconfiguratie opgenomen. De API gebruikt bovendien `GET` voor acties waarvoor in een productiedienst doorgaans veiligere HTTP-methoden nodig zijn.

---

## Español

### Descripción

Sharenergy es una demostración de alquiler de baterías externas compartidas. Permite simular el escaneo de un código QR de una estación, registrar un teléfono, consultar la disponibilidad, iniciar un alquiler y revisar el importe calculado al devolver el dispositivo. El inventario y los alquileres se guardan localmente en SQLite.

Es una aplicación conceptual, no un servicio de alquiler ni de pagos listo para producción.

### Funcionalidades

- Página de estación con escaneo QR simulado y disponibilidad actualizada.
- Registro mediante número de teléfono y selección de batería.
- Temporizador de alquiler y proceso de devolución.
- Resumen de uso con el importe calculado.
- Persistencia en SQLite y endpoint de estado del inventario.
- Inserción de tres baterías de ejemplo al crear la base de datos por primera vez.

### Tecnologías

- Java 17
- Maven
- Javalin 6.1.3
- SQLite con SQLite JDBC
- JDBI 3.44.1
- HTML, CSS y JavaScript

### Requisitos

- JDK 17 o posterior
- Maven 3.6 o posterior
- Acceso a internet en la primera compilación para descargar las dependencias de Maven

### Ejecución local

Ejecuta este comando desde la raíz del proyecto, donde se encuentra `pom.xml`:

```bash
mvn org.codehaus.mojo:exec-maven-plugin:3.3.0:java -Dexec.mainClass=com.sharenergy.Main
```

Abre [http://localhost:7070/index.html](http://localhost:7070/index.html). Detén el servidor con `Ctrl+C`.

En el primer inicio, la aplicación crea `sharenergy.db` en el directorio de trabajo y añade un inventario de ejemplo. Ejecuta el proceso desde la raíz del proyecto para que encuentre correctamente la carpeta `web` y la base de datos.

### Flujo de alquiler

1. Abre la página de la estación y pulsa la imagen QR para simular el escaneo.
2. Introduce un número de teléfono y continúa.
3. Selecciona una batería disponible.
4. Finaliza el alquiler para consultar la duración y el importe calculado.
5. Confirma el pago simulado para acceder a la página de finalización.

### Páginas

| Página | Uso |
| --- | --- |
| `web/index.html` | Inicio de la estación, escaneo QR simulado e inventario actualizado |
| `web/login.html` | Registro del número de teléfono |
| `web/select.html` | Selección de batería |
| `web/using.html` | Temporizador del alquiler y acción de devolución |
| `web/payment.html` | Resumen del alquiler y confirmación de pago simulada |
| `web/summary.html` | Página de finalización |
| `web/unavailable.html` | Página mostrada cuando no hay dispositivos disponibles |

### Endpoints de la API

El backend escucha en el puerto `7070` y ofrece estas rutas:

| Método | Endpoint | Descripción |
| --- | --- | --- |
| `GET` | `/api/lista-baterias` | Devuelve el inventario y la disponibilidad de las baterías |
| `GET` | `/api/registro?u={sessionId}&tel={phone}` | Registra un usuario por sesión y número de teléfono |
| `GET` | `/api/alquilar?u={sessionId}` | Inicia un alquiler con la primera batería disponible |
| `GET` | `/api/alquilar?u={sessionId}&b={batteryId}` | Inicia un alquiler con una batería seleccionada |
| `GET` | `/api/devolver?u={sessionId}` | Finaliza el alquiler activo y devuelve la duración y el importe |
| `GET` | `/api/estado` | Muestra una vista HTML del inventario para desarrollo |

### Tarifas y limitaciones del prototipo

El backend calcula actualmente una tarifa base de 0,50 € más 0,02 € por cada minuto iniciado de alquiler, con un mínimo de un minuto facturable. Al devolver una batería, queda marcada como recargándose; una simulación local vuelve a ponerla disponible después de 10 segundos.

**No se procesa ningún pago.** El botón de confirmación solo abre la página de finalización. No se incluye una pasarela de pago, autenticación, escaneo QR real ni configuración de despliegue para producción. Además, la API utiliza `GET` para acciones que normalmente requerirían métodos HTTP más seguros en un servicio de producción.