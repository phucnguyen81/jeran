# Description

 Help do exercises in the book `Introduction to SQL`.

## Run locally

- Local setup: see `Dockerfile`
- Launch: `mvn jetty:run`
- Launch at: `http://localhost:8080/app`

## Run with docker

- Build: `docker build -f Dockerfile -t jeran .`
- Run: `docker run -d --name jeran -p 8080:8080 jeran`
- Run on current data: `docker run -d --name jeran -p 8080:8080 -v .:/app jeran`
