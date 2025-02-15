
JAR := "build/libs/kotlin-minimal-example-1.0-SNAPSHOT-standalone.jar"

infra:
    podman compose up -d

build:
    ./gradlew clean build

run: build
    java -jar {{ JAR }}