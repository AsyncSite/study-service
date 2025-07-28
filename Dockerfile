FROM eclipse-temurin:21-jre

WORKDIR /app

# 보안을 위한 non-root 사용자 생성
RUN useradd -m -u 1001 appuser && \
    mkdir -p /app/logs && \
    chown -R appuser:appuser /app && \
    chmod 755 /app/logs

# 사전 빌드된 JAR 파일 복사
COPY --chown=appuser:appgroup build/libs/*.jar app.jar

# 헬스체크 설정
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8083/actuator/health || exit 1

USER appuser
EXPOSE 8083

ENTRYPOINT ["java", \
    "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-docker}", \
    "-jar", \
    "app.jar"]
