# Money Management Application
This is a Spring Boot application for managing personal finances. It provides features to track income, expenses, and generate financial reports.

## Equipment Requirements

1. docker and docker-compose
    - Follow the official installation instructions for your operating system.
      - [Docker Installation Guide](https://docs.docker.com/get-docker/)
      - [Docker Compose Installation Guide](https://docs.docker.com/compose/install/)
2. make
    - Make is typically pre-installed on Unix-based systems. For Windows, you can use tools like Chocolatey or install Git Bash which includes Make.
      - [Make Installation Guide](https://www.gnu.org/software/make/)

3. git
    - Follow the official installation instructions for your operating system.
      - [Git Installation Guide](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)

4. github account
    - Create a GitHub account if you don't have one already.
      - [GitHub Sign Up](https://github.com/join)

5. vscode (recommended)
    - Download and install Visual Studio Code for code editing.
      - [VSCode Download](https://code.visualstudio.com/Download)

## Development Setup

1. Clone the repository
   ```bash
   git clone https://github.com/kanghouchao/money-management.git
   cd money-management
   ```

2. Set up environment variables
   copy the `.env.example` file to `.env` and modify the values as needed.
   ```bash
   cp .env.example .env
   ```

3. Start the development environment
    ```bash
    make build up
    ```

4. Access the application
    - Open your web browser and navigate to `http://localhost:8080` to access the Money Management application.

5. Stop the development environment
    ```bash
    make down
    ```

## Other Commands
- Enter the database container
    ```bash
    make db
    ```

- View logs
    ```bash
    make logs
    ```

