FROM public.ecr.aws/amazoncorretto/amazoncorretto:8
EXPOSE 2222
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]