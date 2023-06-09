FROM eclipse/ubuntu_jdk8

WORKDIR /app

COPY pom.xml pom.xml

RUN mvn clean dependency:resolve-plugins install 

COPY . .

EXPOSE 8080

CMD ["mvn", "jetty:run"]