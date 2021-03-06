val kotlin_version: String by project
val min_sdk_version: String by project
val target_sdk_version: String by project
val build_tools_version: String by project
val version_code: String by project
val version_name: String by project
plugins {
    this.id("com.android.application")
    this.id("kotlin-android")
    this.id("kotlin-android-extensions")
    this.id("kotlin-kapt")
}
apply {
    this.plugin("kotlin-android")
}
android {
    this.compileSdkVersion(target_sdk_version.toInt())
    this.buildToolsVersion(build_tools_version)
    this.defaultConfig {
        this.applicationId = "midoriiro.madfx"
        this.minSdkVersion(min_sdk_version.toInt())
        this.targetSdkVersion(target_sdk_version.toInt())
        this.versionCode = version_code.toInt()
        this.versionName = version_name
        this.multiDexEnabled = true
        this.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    this.buildTypes {
        this.getByName("release") {
            this.isMinifyEnabled = true
            this.proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        this.getByName("debug") {
            this.isMinifyEnabled = false
            this.proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    this.buildFeatures {
        this.dataBinding = true
    }
    this.compileOptions {
        this.coreLibraryDesugaringEnabled = true
        this.sourceCompatibility = JavaVersion.VERSION_1_8
        this.targetCompatibility = JavaVersion.VERSION_1_8
    }
    this.kotlinOptions {
        this.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}
dependencies {
    this.implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    this.implementation(this.project(":library:core"))
    this.implementation(this.project(":library:material:knob"))
    this.implementation(this.kotlin("stdlib", kotlin_version))
    this.implementation(this.kotlin("reflect", kotlin_version))
    this.implementation("androidx.fragment:fragment-ktx:1.2.5")
    this.implementation("androidx.core:core-ktx:1.3.1")
    this.implementation("androidx.appcompat:appcompat:1.2.0")
    this.implementation("androidx.constraintlayout:constraintlayout:2.0.1")
    this.implementation("androidx.recyclerview:recyclerview:1.1.0")
    this.implementation("androidx.cardview:cardview:1.0.0")
    this.implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    this.implementation("com.google.android.material:material:1.2.1")
    this.implementation("com.google.android.gms:play-services-location:17.0.0")
    this.implementation("com.mikepenz:iconics-core:4.0.2")
    this.implementation("com.mikepenz:iconics-views:4.0.2")
    this.implementation("com.mikepenz:community-material-typeface:5.3.45.1-kotlin@aar")
    this.implementation("com.codesgood:justifiedtextview:1.1.0")
    this.implementation("org.greenrobot:eventbus:3.2.0")
    this.androidTestImplementation("androidx.test.ext:junit:1.1.2")
    this.androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    this.testImplementation("junit:junit:4.12")
    this.coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.0.10")
}
repositories {
	this.mavenCentral()
}
android.sourceSets.all {
    this.java.srcDir("src/$name/kotlin")
}