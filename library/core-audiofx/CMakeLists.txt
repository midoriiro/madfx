cmake_minimum_required(VERSION 3.4.1)

add_library(
	"cpufeatures"
	STATIC
	"${ANDROID_NDK}/sources/android/cpufeatures/cpu-features.c"
)
set_source_files_properties("${ANDROID_NDK}/sources/android/cpufeatures/cpu-features.c" PROPERTIES COMPILE_FLAGS "-Wno-sign-conversion -Wno-gnu-statement-expression")

include_directories(src/main/cpp)
include_directories("${ANDROID_NDK}/sources/android/cpufeatures")
include_directories(libs/Juce)
include_directories(libs/Juce/modules)
file(GLOB_RECURSE sources src/main/cpp/**.cpp src/main/cpp/**.h)
file(GLOB_RECURSE juce_sources libs/Juce/modules/**.cpp libs/Juce/modules/**.h libs/Juce/modules/**.mm)
file(GLOB_RECURSE include_sources libs/Juce/include_*.cpp)

enable_language(ASM)

if(JUCE_BUILD_CONFIGURATION MATCHES "DEBUG")
	add_definitions("-DJUCE_ANDROID=1" "-DJUCER_ANDROIDSTUDIO_7F0E4A25=1" "-DJUCE_GLOBAL_MODULE_SETTINGS_INCLUDED=1" "-DJUCE_STRICT_REFCOUNTEDPOINTER=1" "-DJUCE_STANDALONE_APPLICATION=0" "-DDEBUG=1" "-D_DEBUG=1")
elseif(JUCE_BUILD_CONFIGURATION MATCHES "RELEASE")
	add_definitions("-DJUCE_ANDROID=1" "-DJUCER_ANDROIDSTUDIO_7F0E4A25=1" "-DJUCE_GLOBAL_MODULE_SETTINGS_INCLUDED=1" "-DJUCE_STRICT_REFCOUNTEDPOINTER=1" "-DJUCE_STANDALONE_APPLICATION=0" "-DNDEBUG=1")
else()
	message( FATAL_ERROR "No matching build-configuration found." )
endif()

set(BINARY_NAME "core-audiofx")

add_library(
	${BINARY_NAME}
	SHARED
	${juce_sources}
	${include_sources}
	libs/Juce/JuceHeader.h
	${sources}
)

set_property(SOURCE ${juce_sources} PROPERTY HEADER_FILE_ONLY TRUE)

target_compile_options( ${BINARY_NAME} PRIVATE "-fsigned-char" )
target_compile_options( ${BINARY_NAME} PRIVATE)

find_library(log "log")
find_library(android "android")
find_library(glesv2 "GLESv2")
find_library(egl "EGL")

target_link_libraries(
	${BINARY_NAME}
	${log}
	${android}
	${glesv2}
	${egl}
	"cpufeatures"
)