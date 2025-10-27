# Makefile for managing the Money Management application with Docker Compose

include .env

help:
	@echo "Makefile commands:"
	@echo "  up     - Start the application using Docker Compose"
	@echo "  logs   - Tail the application logs"
	@echo "  down   - Stop the application and remove containers"
	@echo "  ps     - List running containers"
	@echo "  db     - Access the database container shell"

up:
	@echo "Starting application..."
	@docker compose up -d
	@echo "Application started. Access it at http://localhost:8080"

logs:
	@echo "Tailing application logs..."
	@docker compose logs -f

down:
	@echo "Stopping application..."
	@docker compose down -v
	@echo "Application stopped."

ps: 
	@echo "Listing running containers..."
	@docker compose ps

db:
	@echo "Accessing database container shell..."
	@docker compose exec db psql -U ${DB_USER} -d ${DB_NAME}

.PHONY: help up down logs ps db