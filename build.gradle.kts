buildscript {
	val kotlin_version: String by project
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register<GenerateGattClasses>("generateGattClasses")
{
    this.group = "Generate"
    this.description = "Generate Bluetooth Gatt attributes classes"
    this.packageName = "midoriiro.madfx.bluetooth.specifications"
    this.outputPath = "src/main/kotlin"
}

tasks.register("clean").configure {
    delete(rootProject.buildDir)
}