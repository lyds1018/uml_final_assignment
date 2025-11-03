FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /build
# 先拷贝 pom 以利用缓存下载依赖
COPY pom.xml ./
RUN mvn -q -B -e -DskipTests dependency:go-offline
# 再拷贝源码并打包
COPY src ./src
RUN mvn -q -B -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /build/target/shop-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]


