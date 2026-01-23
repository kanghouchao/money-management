# Money Management Application - Gemini Context

## Project Overview
This is a personal financial management application built with **Spring Boot** and **Thymeleaf**. It allows users to record income/expenses and view financial reports. The application is containerized using **Docker** and managed via **go-task**.

### Key Technologies
- **Language:** Java 21
- **Framework:** Spring Boot 3.5.9
- **Template Engine:** Thymeleaf
- **Database:** MySQL 8.0 (via Docker)
- **Build Tool:** Gradle
- **Task Runner:** Task (go-task)
- **Containerization:** Docker & Docker Compose

## Getting Started

### Prerequisites
- **Docker** & **Docker Compose**
- **Task** (go-task) - *Recommended for running project commands*
- **Java 21 JDK** (For local development outside Docker)

### Environment Setup
1. Copy the example environment file:
   ```bash
   cp .env.example .env
   ```
2. Configure `.env` with your desired database credentials (if changing from defaults).

### Building and Running
The project uses `Taskfile.yml` to simplify common operations.

| Action | Command | Description |
| :--- | :--- | :--- |
| **Build** | `task build` | Builds the Docker image for the application. |
| **Start** | `task up` | Starts the application and database in detached mode. |
| **Logs** | `task logs` | Tails the application logs. |
| **Stop** | `task down` | Stops and removes containers/volumes. |
| **DB Shell**| `task db` | Access the MySQL database shell inside the container. |

**Access the application at:** `http://localhost:8080`

## Project Structure

```
/
├── Taskfile.yml           # Definition of operational commands
├── build.gradle           # Gradle build configuration & dependencies
├── docker-compose.yml     # Docker services (app + db)
├── src/
│   ├── main/
│   │   ├── java/          # Java source code (com.technos.moneyManagement)
│   │   └── resources/
│   │       ├── templates/ # Thymeleaf HTML templates
│   │       ├── static/    # CSS, JS, images
│   │       └── application.properties # Spring Boot configuration
│   └── test/              # Unit and integration tests
```

## Development Conventions & Notes

- **Database Schema:** The current configuration uses `spring.jpa.hibernate.ddl-auto=create-drop`. **Warning:** This means the database schema and data are **destroyed and recreated** every time the application restarts.
- **Lombok:** The project uses Project Lombok. Ensure your IDE has the Lombok plugin installed and annotation processing enabled.
- **Locale:** The application is configured for the Japanese locale (`ja_JP`).
- **Styles:** Custom CSS is located in `src/main/resources/static/css`.
- **Validation:** Standard Jakarta Validation is used for form inputs.

## Troubleshooting

- **Database Connection:** If the app fails to start due to DB connection, ensure the `db` service is healthy. `task up` waits for the DB to be healthy, but check `docker compose ps` if issues persist.
- **Port Conflicts:** Ensure port `8080` is free on your host machine.
