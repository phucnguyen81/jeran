FROM eclipse/ubuntu_jdk8

USER user

WORKDIR /app

COPY --chown=user:user pom.xml pom.xml

RUN mvn clean dependency:resolve-plugins install 

COPY --chown=user:user . .

EXPOSE 8080

ENTRYPOINT ["mvn"]

CMD ["jetty:run"]