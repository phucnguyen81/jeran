# Description

 Help do exercises in the book `Introduction to SQL`.

## Local Development

- Install `jdk8` and `maven`
- Launch: `mvn jetty:run`
- Launch at: `http://localhost:8080/app`

## Development with Docker

- Build: `docker build -t jeran .`
- Run web server: `docker run -d --name jeran -p 8080:8080 jeran`
- Run to connect later: `docker run -d --name jeran -p 8080:8080 jeran sleep infinity`
- Stop the server: `docker stop jeran`
