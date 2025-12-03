# meeting-room-booking-system

## 기술 스택 및 버전
- Java: 17
- Spring Boot: 3.x
- Database: H2 (개발/테스트), MySQL (운영)
- Build Tool: Gradle 8.x
- API Documentation: Swagger/OpenAPI 3.0
- Containerization: Docker, Docker Compose

## 모듈 구조
- core: 회의실 예약 시스템 핵심 로직
- payment-mock-server: 결제 시스템 Mock 서버

## 실행 방법
```bash
# 빌드
./gradlew :core:clean :core:build
./gradlew :payment-mock-server:clean :payment-mock-server:build

# 실행
docker-compose up

# 종료
docker-compose down
```

## API 문서
- http://localhost:8080/docs

## 테스트 실행
```bash
./gradlew :core:test
```
