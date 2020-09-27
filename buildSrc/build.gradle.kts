import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	`kotlin-dsl`
	kotlin("jvm") version "1.4.10"
}

repositories {
	jcenter()
}

dependencies {
	implementation("com.squareup:kotlinpoet:1.6.0")
	implementation("org.jsoup:jsoup:1.13.1")
	implementation(kotlin("stdlib-jdk8"))
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
	jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
	jvmTarget = "1.8"
}