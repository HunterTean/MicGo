cmake_minimum_required(VERSION 3.4.1)

find_library(log-lib log)
find_library(android-lib android)
find_library(glesv2-lib GLESv2)
find_library(egl-lib EGL)
find_library(sles-lib OpenSLES)
find_library(z-lib z)

ADD_SUBDIRECTORY(src/main/cpp)