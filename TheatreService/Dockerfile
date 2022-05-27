FROM public.ecr.aws/amazoncorretto/amazoncorretto:8
EXPOSE 6667
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]