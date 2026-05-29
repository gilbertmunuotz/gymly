# Gymly

University assignment project - a gym management mobile app with an Android client and Spring Boot REST API.


## Project Structure

```
gymly/
├── backend/          # Spring Boot REST API (Java 21, Maven, PostgreSQL)
├── frontend/         # Android app (Java, XML, Retrofit)
├── .env.example      # Environment variable template (copy to .env locally)
└── README.md
```

### Backend packages (`com.gymly`)

| Package       | Purpose                    |
|---------------|----------------------------|
| `config`      | Security, CORS, Web config |
| `controller`  | REST endpoints             |
| `service`     | Business logic             |
| `repository`  | JPA repositories           |
| `model`       | JPA entities               |
| `dto`         | Request/response objects   |
| `exception`   | Error handling             |
| `security`    | JWT / auth                 |

### Android packages (`com.gymly`)

| Package        | Purpose                         |
|----------------|---------------------------------|
| `ui/*`         | Screens (auth, home, classes…)  |
| `data/api`     | Retrofit interfaces             |
| `data/model`   | API models                      |
| `data/local`   | SharedPreferences               |
| `network`      | Retrofit client                 |
| `utils`        | Constants                       |

## Prerequisites

- **JDK 21** - backend
- **Maven** - included via `./mvnw` wrapper
- **Android Studio** - frontend
- **PostgreSQL** - database 

## Getting Started

### 1. Environment variables

Create `backend/.env` from the template:

```bash
cp backend/.env.example backend/.env
```

Edit `backend/.env` with your PostgreSQL username. Leave `DB_PASSWORD` empty if your local Postgres has no password.

Spring Boot loads this file automatically via `springboot3-dotenv` — no manual `export` needed.

Never commit `.env` — it is gitignored.

### 2. Backend

```bash
cd backend
./mvnw test
./mvnw spring-boot:run
```

API base URL: `http://localhost:8080/api`

Health check: `GET http://localhost:8080/api/health`

### Database setup (Phase 2)

Create the database and run the SQL scripts:

```bash
createdb gymly
psql -d gymly -f backend/src/main/resources/db/schema.sql
psql -d gymly -f backend/src/main/resources/db/seed.sql
```

### 3. Android

1. Open the `frontend/` folder in Android Studio.
2. Sync Gradle.
3. Run on an emulator or device.

Debug builds use `http://10.0.2.2:8080/api/` to reach the backend from the Android emulator.

## Git Branching (Suggested)

```
main              → stable, demo-ready code
feature/<name>    → one feature or phase at a time
fix/<name>        → bug fixes
```