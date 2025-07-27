import java.io.ByteArrayOutputStream

// Docker 실행 파일 찾기
fun findDockerExecutable(): String? {
    val possiblePaths = listOf(
        "/usr/local/bin/docker",
        "/usr/bin/docker",
        "/opt/homebrew/bin/docker",
        "/Applications/Docker.app/Contents/Resources/bin/docker"
    )

    for (path in possiblePaths) {
        if (file(path).exists()) {
            return path
        }
    }

    // which 명령어로 docker 찾기
    try {
        val output = ByteArrayOutputStream()
        exec {
            commandLine("which", "docker")
            standardOutput = output
            isIgnoreExitValue = true
        }
        val dockerPath = output.toString().trim()
        if (dockerPath.isNotEmpty() && file(dockerPath).exists()) {
            return dockerPath
        }
    } catch (e: Exception) {
        // ignore
    }

    return null
}

// Docker 명령어 실행 헬퍼 함수
fun executeDockerCommand(vararg args: String): Boolean {
    val dockerPath = findDockerExecutable()
    if (dockerPath == null) {
        println("❌ Docker not found. Please make sure Docker Desktop is installed and running.")
        println("   You can also run Docker commands directly from terminal.")
        return false
    }

    return try {
        val output = ByteArrayOutputStream()
        val result = exec {
            // 전체 명령어 배열 생성 (docker 실행 파일 경로 포함)
            val fullCommand = if (args[0] == "docker") {
                listOf(dockerPath) + args.drop(1)
            } else {
                listOf(dockerPath) + args.toList()
            }

            commandLine(*fullCommand.toTypedArray())
            workingDir = projectDir
            standardOutput = output
            errorOutput = output
            isIgnoreExitValue = true
        }

        if (result.exitValue != 0) {
            println("Command failed: ${args.joinToString(" ")}")
            println(output.toString())
            false
        } else {
            true
        }
    } catch (e: Exception) {
        println("Error executing Docker command: ${e.message}")
        false
    }
}

// Docker Compose 명령어 실행
fun dockerCompose(vararg args: String): Boolean {
    return executeDockerCommand("compose", *args)
}

// Docker Compose 명령어 실행 (특정 파일 지정)
fun dockerComposeWithFile(file: String, vararg args: String): Boolean {
    return executeDockerCommand("compose", "-f", file, *args)
}

// 간편한 Docker Tasks
tasks.register("dockerUp") {
    group = "docker-simple"
    description = "Start study service with docker compose up -d"
    doLast {
        if (dockerCompose("up", "-d")) {
            println("✅ Study service started successfully")
            println("View logs: ./gradlew dockerLogs")
            println("Check status: docker ps")
        } else {
            println("❌ Failed to start study service")
            println("Make sure Docker Desktop is running")
        }
    }
}

tasks.register("dockerDown") {
    group = "docker-simple"
    description = "Stop study service with docker compose down"
    doLast {
        if (dockerCompose("down")) {
            println("✅ Study service stopped successfully")
        } else {
            println("❌ Failed to stop study service")
        }
    }
}

tasks.register("dockerRestart") {
    group = "docker-simple"
    description = "Restart study service"
    dependsOn("dockerDown", "dockerUp")
}

tasks.register("dockerStatus") {
    group = "docker-simple"
    description = "Show Docker container status"
    doLast {
        executeDockerCommand("docker", "ps", "--format", "table {{.Names}}\t{{.Status}}\t{{.Ports}}")
    }
}

tasks.register("dockerBuildAndRun") {
    group = "docker-simple"
    description = "Build JAR file and run study service"
    dependsOn("bootJar")
    finalizedBy("dockerUp")
    doLast {
        println("Building study service...")
    }
}

tasks.register("dockerLogs") {
    group = "docker-simple"
    description = "Show study service Docker logs"
    doLast {
        executeDockerCommand("docker", "logs", "-f", "asyncsite-study-service")
    }
}

// Study Service 전용 Docker Tasks
tasks.register("dockerBuild") {
    group = "docker-simple"
    description = "Build Study Service Docker image"
    dependsOn("bootJar")
    doLast {
        if (executeDockerCommand("docker", "build", "-t", "asyncsite/study-service:latest", ".")) {
            println("✅ Study service Docker image built successfully")
        } else {
            println("❌ Failed to build Docker image")
        }
    }
}

// Study Service Only (인프라가 이미 실행 중일 때)
tasks.register("dockerUpStudyOnly") {
    group = "docker-simple"
    description = "Start only study service when infrastructure is already running"
    doLast {
        if (dockerComposeWithFile("docker-compose.study-only.yml", "up", "-d")) {
            println("✅ Study service started successfully (using existing infrastructure)")
            println("View logs: ./gradlew dockerLogsStudyOnly")
            println("Check status: docker ps")
        } else {
            println("❌ Failed to start study service")
            println("Make sure infrastructure services are already running")
        }
    }
}

tasks.register("dockerDownStudyOnly") {
    group = "docker-simple"
    description = "Stop only study service (keeps infrastructure running)"
    doLast {
        if (dockerComposeWithFile("docker-compose.study-only.yml", "down")) {
            println("✅ Study service stopped successfully")
        } else {
            println("❌ Failed to stop study service")
        }
    }
}

tasks.register("dockerRestartStudyOnly") {
    group = "docker-simple"
    description = "Restart only study service"
    dependsOn("dockerDownStudyOnly", "dockerUpStudyOnly")
}

// Ensure clean happens before bootJar
tasks.named("bootJar") {
    mustRunAfter("clean")
}

tasks.register("dockerRebuildAndRunStudyOnly") {
    group = "docker-simple"
    description = "Completely rebuild and run study service (removes old container and image)"

    dependsOn("clean", "bootJar")

    doLast {
        // Stop and remove container
        if (dockerComposeWithFile("docker-compose.study-only.yml", "down")) {
            println("✅ Study service stopped and removed")
        }

        // Remove old image
        executeDockerCommand("docker", "rmi", "asyncsite/study-service:latest", "-f")
        println("✅ Old study service image removed")

        println("🔨 Building Docker image...")
        if (executeDockerCommand("docker", "build", "-t", "asyncsite/study-service:latest", ".")) {
            println("✅ Study service Docker image built successfully")

            println("🚀 Starting study service...")
            if (dockerComposeWithFile("docker-compose.study-only.yml", "up", "-d")) {
                println("✅ Study service started successfully")
                println("View logs: ./gradlew dockerLogsStudyOnly")
            } else {
                println("❌ Failed to start study service")
            }
        } else {
            println("❌ Failed to build Docker image")
        }
    }
}

tasks.register("dockerBuildAndRunStudyOnly") {
    group = "docker-simple"
    description = "Build JAR and run only study service"
    dependsOn("bootJar")
    finalizedBy("dockerUpStudyOnly")
    doLast {
        println("Building study service...")
    }
}

tasks.register("dockerLogsStudyOnly") {
    group = "docker-simple"
    description = "Show study service Docker logs"
    doLast {
        executeDockerCommand("docker", "logs", "-f", "asyncsite-study-service")
    }
}

tasks.register("dockerRunStandalone") {
    group = "docker-simple"
    description = "Run Study Service as standalone container (ensure infrastructure is running)"
    dependsOn("dockerBuild")
    doLast {
        // Stop and remove existing container if it exists
        executeDockerCommand("docker", "stop", "asyncsite-study-service")
        executeDockerCommand("docker", "rm", "asyncsite-study-service")

        if (executeDockerCommand(
                "docker", "run", "-d",
                "--name", "asyncsite-study-service",
                "--network", "asyncsite-network",
                "-p", "8083:8083",
                "-e", "SPRING_PROFILES_ACTIVE=docker",
                "-e", "EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://asyncsite-eureka:8761/eureka/",
                "-e", "SPRING_DATASOURCE_URL=jdbc:mysql://asyncsite-mysql:3306/studydb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                "-e", "SPRING_DATASOURCE_USERNAME=root",
                "-e", "SPRING_DATASOURCE_PASSWORD=asyncsite_root_2024!",
                "asyncsite/study-service:latest"
            )) {
            println("✅ Study service container started successfully")
            println("Access at: http://localhost:8083")
        } else {
            println("❌ Failed to start study service container")
            println("Make sure core-platform infrastructure is running")
        }
    }
}

// Help task to show all Docker commands
tasks.register("dockerHelp") {
    group = "docker-simple"
    description = "Show all available Docker commands"
    doLast {
        println("""
            |=== Study Service Docker Commands ===
            |
            |Full Stack Commands (includes infrastructure):
            |  ./gradlew dockerUp                  - Start all services with docker-compose
            |  ./gradlew dockerDown                - Stop all services
            |  ./gradlew dockerRestart             - Restart all services
            |  ./gradlew dockerBuildAndRun         - Build JAR and run all services
            |
            |Study Service Only Commands (when infrastructure is already running):
            |  ./gradlew dockerUpStudyOnly         - Start only study service
            |  ./gradlew dockerDownStudyOnly       - Stop only study service
            |  ./gradlew dockerRestartStudyOnly    - Restart only study service
            |  ./gradlew dockerBuildAndRunStudyOnly - Build JAR and run only study service
            |  ./gradlew dockerRebuildAndRunStudyOnly - Completely rebuild and run (removes old container/image)
            |  ./gradlew dockerLogsStudyOnly       - Show study service logs
            |
            |Other Commands:
            |  ./gradlew dockerBuild               - Build Docker image
            |  ./gradlew dockerStatus              - Show container status
            |  ./gradlew dockerLogs                - Show logs (follows output)
            |  ./gradlew runInfraOnly              - Run only infrastructure from core-platform
            |
            |Note: Use *StudyOnly commands when MySQL, Redis, and Eureka are already running.
        """.trimMargin())
    }
}

// Infrastructure only (for local development)
tasks.register("runInfraOnly") {
    group = "docker-simple"
    description = "Run only infrastructure (MySQL, Redis, Eureka) from core-platform for local development"
    doLast {
        val corePlatformDir = file("../core-platform")
        val dockerPath = findDockerExecutable()

        if (!corePlatformDir.exists()) {
            println("❌ Core platform directory not found at ../core-platform")
            return@doLast
        }

        if (dockerPath == null) {
            println("❌ Docker not found. Please make sure Docker Desktop is installed and running.")
            return@doLast
        }

        exec {
            commandLine(dockerPath, "compose", "up", "-d", "mysql", "redis", "eureka-server")
            workingDir = corePlatformDir
        }
        println("✅ Infrastructure services started from core-platform")
        println("MySQL: localhost:3306")
        println("Redis: localhost:6379")
        println("Eureka: http://localhost:8761")
    }
}