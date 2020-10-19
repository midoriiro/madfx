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
	this.jvmTarget = JavaVersion.VERSION_1_8.toString()
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
	this.jvmTarget = JavaVersion.VERSION_1_8.toString()
}