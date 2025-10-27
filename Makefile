# Makefile for managing the Money Management application with Docker Compose

include .env

help:
	@echo "Makefile commands:"
	@echo "  up     - Start the application using Docker Compose"
	@echo "  logs   - Tail the application logs"
	@echo "  down   - Stop the application and remove containers"
	@echo "  ps     - List running containers"
	@echo "  db     - Access the database container shell"

build:
	@echo "Building application..."
	@docker compose build
	@echo "Build complete."

up:
	@echo "Starting application..."
	@docker compose up -d
	@echo "Application started. Access it at http://localhost:8080"

logs:
	@echo "Tailing application logs..."
	@docker compose logs -f --tail=100 mm

down:
	@echo "Stopping application..."
	@docker compose down -v
	@echo "Application stopped."

ps: 
	@echo "Listing running containers..."
	@docker compose ps

db:
	@echo "Accessing database container shell..."
	@docker compose exec db mysql -u${DB_USER} -p${DB_PASSWORD} ${DB_NAME}

.PHONY: help build up down logs ps db