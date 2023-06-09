# Description

 Help do exercises in the book `Introduction to SQL`.

## Local Development

- Local setup: see `Dockerfile`
- Launch: `mvn jetty:run`
- Launch at: `http://localhost:8080/app`

## Development with Docker

- Build: `docker build -f Dockerfile -t jeran .`
- Run: `docker run -d --name jeran -p 8080:8080 jeran`
- Run to connect later: `docker run -d --name jeran -p 8080:8080 jeran sleep infinity`
- Run on current data: `docker run -d --name jeran -p 8080:8080 -v .:/app jeran`
