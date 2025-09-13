# Use official Playwright Java image with all browsers pre-installed
FROM mcr.microsoft.com/playwright/java:v1.55.0-jammy

# Set working directory
WORKDIR /app

# Set environment variables
ENV MAVEN_OPTS="-Xmx2048m"
ENV BROWSER=chrome
ENV HEADLESS=true

# Copy Maven files first for better layer caching
COPY pom.xml .
COPY src ./src

# Install dependencies and compile
RUN mvn clean compile test-compile -DskipTests

# Create directories for reports
RUN mkdir -p target/chaintest target/allure-results target/site/allure-maven-plugin

# Set default command
CMD ["mvn", "test", "-Dheadless=true"]

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

# Expose port for potential web dashboard
EXPOSE 8080

# Labels for metadata
LABEL maintainer="your-email@example.com"
LABEL description="Playwright Java Test Automation Framework"
LABEL version="2.0.0" 