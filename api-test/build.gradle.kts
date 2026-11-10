plugins {
    id("java")
}

group = "ru.v1ad54"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Source: https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core
    implementation("com.fasterxml.jackson.core:jackson-core:2.22.3")

    // Source: https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    implementation("com.fasterxml.jackson.core:jackson-databind:2.22.4")

    // Source: https://mvnrepository.com/artifact/io.rest-assured/rest-assured
    implementation("io.rest-assured:rest-assured:6.2.0")

    // Source: https://mvnrepository.com/artifact/org.assertj/assertj-core
    implementation("org.assertj:assertj-core:3.27.7")
}

tasks.test {
    useJUnitPlatform()

    // Стандартные выводы внутри тестов направляем в консоль
    testLogging {
        showStandardStreams = true
    }
}

tasks.register<Test>("TestApi") {
    group = "my_test_tasks"
    description = "Задача запускает тесты API."

    // Стандартные выводы внутри тестов направляем в консоль
    testLogging {
        showStandardStreams = true
    }

    useJUnitPlatform {
        includeTags("TestApi")
    }

    // Чтобы тесты всегда прогонялись заново, иначе не запускаются уже пройденные тесты
    outputs.upToDateWhen { false }

    // Зададим пути к тестовым и связанным классам
    val testSourceSet = sourceSets.test.get()
    testClassesDirs = testSourceSet.output.classesDirs
    classpath = testSourceSet.runtimeClasspath

}
