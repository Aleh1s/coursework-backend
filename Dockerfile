FROM openjdk:17

EXPOSE 8080

COPY coursework-backend/target/coursework-backend-refugees.jar /apps/app.jar
COPY coursework-backend/entrypoint.sh /apps/entrypoint.sh

RUN chmod +x /apps/entrypoint.sh

CMD ["/apps/entrypoint.sh"]