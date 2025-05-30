#Usar una imagen base con java 17
FROM openjdk:17-slim

#directorio donde se colocara la aplicacion
WORKDIR /app

#Copiar el archivo jar del proyecto al directorio /app en el contenedor
COPY target/desarrolloAPP-0.0.1-SNAPSHOT.jar /app/ms-spring-security-jwt.jar

#Exponer el puerto que usa la aplicacion
EXPOSE 4002

#Comando para ejecutar la aplicacion
CMD ["java", "-jar", "/app/ms-spring-security-jwt.jar"]