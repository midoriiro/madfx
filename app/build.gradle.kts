val kotlin_version: String by extra
val kotlinVersion: String by project

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}
apply {
	plugin("kotlin-android")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.2")

    defaultConfig {
        applicationId = "midoriiro.madfx"
        minSdkVersion(23)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        dataBinding = true
    }

    compileOptions {
        coreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib", kotlinVersion))
    implementation("androidx.fragment:fragment-ktx:1.2.5")
    implementation("androidx.core:core-ktx:1.3.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.1")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("com.google.android.gms:play-services-location:17.0.0")
    implementation("com.mikepenz:iconics-core:4.0.2")
    implementation("com.mikepenz:iconics-views:4.0.2")
    implementation("com.mikepenz:community-material-typeface:5.3.45.1-kotlin@aar")
    implementation("com.codesgood:justifiedtextview:1.1.0")
    implementation("org.greenrobot:eventbus:3.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.0.10")
    testImplementation("junit:junit:4.12")
}

repositories {
	mavenCentral()
}

tasks.register<GenerateGattClasses>("generateGattClasses")
{
    this.group = "Generate"
    this.description = "Generate Bluetooth Gatt attributes classes"
    this.packageName = "midoriiro.madfx.bluetooth.specifications"
    this.outputPath = "src/main/kotlin"
}

android.sourceSets.all {
    java.srcDir("src/$name/kotlin")
}