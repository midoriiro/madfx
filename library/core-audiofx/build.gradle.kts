val kotlin_version: String by project
val min_sdk_version: String by project
val target_sdk_version: String by project
val build_tools_version: String by project
val version_code: String by project
val version_name: String by project

plugins {
    this.id("com.android.library")
    this.id("kotlin-android")
    this.id("kotlin-android-extensions")
}

apply {
    this.plugin("kotlin-android")
}

android {
    this.compileSdkVersion(target_sdk_version.toInt())
    this.buildToolsVersion(build_tools_version)
    this.externalNativeBuild {
        cmake {
            this.setPath("CMakeLists.txt")
        }
    }
    this.defaultConfig {
        this.minSdkVersion(min_sdk_version.toInt())
        this.targetSdkVersion(target_sdk_version.toInt())
        this.versionCode = version_code.toInt()
        this.versionName = version_name
        this.multiDexEnabled = true
        this.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        externalNativeBuild {
            cmake {
                this.arguments(
                    "-DANDROID_TOOLCHAIN=clang",
                    "-DANDROID_PLATFORM=android-${min_sdk_version}",
                    "-DANDROID_STL=c++_static",
                    "-DANDROID_CPP_FEATURES=exceptions rtti",
                    "-DANDROID_ARM_MODE=arm",
                    "-DANDROID_ARM_NEON=TRUE",
                    "-DCMAKE_CXX_STANDARD=14",
                    "-DCMAKE_CXX_EXTENSIONS=OFF"
                )
            }
        }
    }
    this.buildTypes {
        this.getByName("release") {
            this.initWith(this)
            this.isDebuggable = false
            this.isJniDebuggable = false
            this.isMinifyEnabled = true
            this.proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        this.getByName("debug") {
            this.initWith(this)
            this.isDebuggable = true
            this.isJniDebuggable = true
            this.isMinifyEnabled = false
            this.proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    this.flavorDimensions("default")
    this.productFlavors {
        this.create("debug_") {
            this.ndk {
                this.abiFilters("armeabi-v7a", "x86", "arm64-v8a", "x86_64")
            }
            this.externalNativeBuild {
                this.cmake {
                    this.arguments(
                        "-DJUCE_BUILD_CONFIGURATION=DEBUG",
                        "-DCMAKE_CXX_FLAGS_DEBUG=-O0",
                        "-DCMAKE_C_FLAGS_DEBUG=-O0"
                    )
                }
            }
            this.dimension = "default"
        }
        this.create("release_") {
            this.externalNativeBuild {
                this.cmake {
                    this.arguments(
                        "-DJUCE_BUILD_CONFIGURATION=RELEASE",
                        "-DCMAKE_CXX_FLAGS_RELEASE=-O3",
                        "-DCMAKE_C_FLAGS_RELEASE=-O3"
                    )
                }
            }
            this.dimension = "default"
        }
    }
    this.variantFilter {
        val names = this.flavors.map { it.name }
        if(names.contains("debug_") && this.buildType.name != "debug")
        {
            this.ignore = true
        }
        if(names.contains("release_") && this.buildType.name != "release")
        {
            this.ignore = true
        }
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
    this.implementation(kotlin("stdlib", kotlin_version))
    this.implementation(kotlin("reflect", kotlin_version))
    this.implementation("androidx.core:core-ktx:1.3.1")
    this.implementation("androidx.appcompat:appcompat:1.2.0")
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