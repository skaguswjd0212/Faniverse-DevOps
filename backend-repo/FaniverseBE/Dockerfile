# 베이스 이미지 설정
FROM openjdk:21-jdk-slim

# 작업 디렉토리 생성
WORKDIR /app

# 프로젝트의 JAR 파일 복사
COPY build/libs/*.jar app.jar

# JAR 파일 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
