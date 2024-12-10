# Bước 1: Build ứng dụng bằng Maven
FROM maven:3.8.5-openjdk-17 AS build

# Đặt thư mục làm việc bên trong Docker
WORKDIR /app

# Sao chép file pom.xml và cài đặt các phụ thuộc (dependencies)
COPY pom.xml .
# Sao chép toàn bộ mã nguồn vào Docker
COPY src ./src

# Build ứng dụng Spring Boot (tạo file JAR)
RUN mvn clean package -DskipTests

# Bước 2: Tạo image để chạy ứng dụng từ file JAR đã build
FROM openjdk:17-jdk-slim

# Sao chép file JAR từ build stage sang runtime stage
COPY --from=build /app/target/*.jar app.jar

# Expose cổng mặc định của ứng dụng Spring Boot
EXPOSE 8080

# Chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "/app.jar"]