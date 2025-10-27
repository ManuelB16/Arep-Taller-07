# 🐦 Mini Twitter - AWS Lambda & API Gateway

Proyecto desarrollado en **Java 17** utilizando **AWS Lambda** como entorno serverless, con integración a **API Gateway** para exponer endpoints REST.  
El objetivo fue desplegar un backend funcional en la nube que permita crear y obtener publicaciones simulando una red social simple tipo “Twitter”.

## 🚀 Tecnologías utilizadas

- **Java 17**
- **Maven**
- **AWS Lambda**
- **AWS API Gateway (REST Proxy)**
- **AWS CloudWatch Logs**
- **AWS S3**

## Arquitectura del Proyecto 🔒

**Componentes**
- **Backend (AWS Lambda + API Gateway)**: expone endpoints REST:
  - `POST /twitter/createPost` → crea posts
  - `GET  /twitter/getPosts`   → lista posts
- **Frontend (S3 estático)**: HTML/JS que consume el API del Gateway.
- **Logs y Observabilidad**: **CloudWatch Logs** desde Lambda.

> **Handler Lambda:** `com.example.twitter.TwitterLambdaHandler::handleRequest`  
> **Jar:** `target/twitter-0.0.1-SNAPSHOT.jar`

## 📄 Descripción breve de clases

### `com.example.twitter.controller`
- **TwitterController.java** — Lógica de negocio: crea y lista posts (fuente única de verdad para los handlers).

### `com.example.twitter.lambda`
- **TwitterLambdaHandler.java** — Handler principal (API Gateway proxy). Rutea:
  - `GET  /twitter/getPosts`
  - `POST /twitter/createPost`
- **PostLambdaHandler.java** — Handler dedicado para creación de posts (si se invoca como función separada).
- **UserLambdaHandler.java** — Handler para operaciones de usuario (crear/consultar), si se separa por función.
- **StreamLambdaHandler.java** — Handler para operaciones del “stream”/timeline (listar/seed/limpiar), si se usa.

### `com.example.twitter.model`
- **TwitterApplication.java** — Punto de arranque cuando se ejecuta como app (útil para pruebas locales).

### `src/main/resources`
- **static/index.html** — Frontend estático mínimo para consumir la API.
- **application.properties** — Configuración de la app.

## 📊 Pruebas del Proyecto 

1. Construir:
   ```java
   mvn clean package -DskipTests
   ```

2. Ejecutar el JAR (local):java -jar target/twitter-0.0.1-SNAPSHOT.jar
3. Propar S3: https://arep-taller07.s3.us-east-1.amazonaws.com/index.html

  <img width="1350" height="664" alt="image" src="https://github.com/user-attachments/assets/61413365-d29b-4c67-b077-f2981c23d40f" />

5. Probar con cURL o Postman:
   **Crear POST**
   ```java
   curl -i -X POST -H "Content-Type: application/json" \-d '{"username":"mari","text":"hola"}' \"http://localhost:8080/twitter/createPost"
     ```
   **Ver los POSTS (GET)**
    ```java
    curl -i "http://localhost:8080/twitter/getPosts"
     ```
    <img width="1005" height="223" alt="image" src="https://github.com/user-attachments/assets/d02d3e41-9d6f-476a-a9ec-7ac4bda9a8ac" />
**Pruebas desde la consola de AWS Lambda (eventos proxy v1)**
1. POST /twitter/createPost:
 ```java
   {
  "resource": "/{proxy+}",
  "path": "/twitter/createPost",
  "httpMethod": "POST",
  "headers": { "Content-Type": "application/json" },
  "requestContext": { "stage": "default" },
  "body": "{\"username\":\"mari\",\"text\":\"hola\"}",
  "isBase64Encoded": false
}
```
https://github.com/user-attachments/assets/7573c43a-cd51-4223-bde9-4d097d96538e

2. GET /twitter/getPosts:
 ```java

{
  "resource": "/{proxy+}",
  "path": "/twitter/getPosts",
  "httpMethod": "GET",
  "headers": {},
  "requestContext": { "stage": "default" },
  "body": null,
  "isBase64Encoded": false
}
 ```

https://github.com/user-attachments/assets/4977c90e-5f61-44d2-9e0b-5088e37329ae

6. Separación en Microservicios
  - UserService: Gestionar Usuarios.
  - PostService: Crear y obtener posts
  - StreamService: Gestionar el flujo de publicaciones. Los microservicios creados se generaron dentro de lamba, para organizacion y rendimiento.
Pruebas:

UserService:

https://github.com/user-attachments/assets/dda3f3a9-9b2f-4d0a-aa61-7a2e74e3b996

PostService:

https://github.com/user-attachments/assets/c0e8d628-9b34-46de-9779-0f2daf4ad609

StreamService:

https://github.com/user-attachments/assets/b2617f7b-bbe8-4bde-a9f6-5ea7c69a0bb7

7. Pruebas
  <img width="358" height="107" alt="image" src="https://github.com/user-attachments/assets/ffe79b43-a4bc-4cca-9161-378b26c25132" />

8. Conclusion
   En el desarrollo del proyecto se consiguen fortalecer las practicas de cognito, la seguridad y a su vez, un clon de twitter apropiado y rapido de implementar.


Autores:

Sara Katherin Castillo Garcia y Manuel Felipe Barrera Barrera

Agradecimientos:

Profesor Daniel Benavides.

  
 




   













