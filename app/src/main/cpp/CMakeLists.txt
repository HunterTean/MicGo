add_library(avcodec STATIC IMPORTED)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/prebuilt/libavcodec.a)

add_library(st_mobile SHARED IMPORTED)
set_target_properties(st_mobile PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/prebuilt/libavdevice.a)

add_library(avfilter STATIC IMPORTED)
set_target_properties(avfilter PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/prebuilt/libavfilter.a)

add_library(avformat STATIC IMPORTED)
set_target_properties(avformat PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/prebuilt/libavformat.a)

add_library(avutil STATIC IMPORTED)
set_target_properties(avutil PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/prebuilt/libavutil.a)

add_library(swresample STATIC IMPORTED)
set_target_properties(swresample PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/prebuilt/libswresample.a)

add_library(swscale STATIC IMPORTED)
set_target_properties(swscale PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/prebuilt/libswscale.a)


add_library(
    nativego
    SHARED
    native_jni.cpp
)

set(
    nativego_include_directories
    PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/libcommon
    PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/libffmpeg
)

#set(
#    nativego_link_libraries
#    videoduet
#    videoeffectprocessor
#    videopostprocessing
#)

set(
    nativego_definitions
    PRIVATE -D__STDC_CONSTANT_MACROS
)

target_include_directories(
    nativego
    ${nativego_include_directories}
)

target_compile_options(
    nativego
    PRIVATE -UHAVE_CONFIG_H
    PRIVATE -std=gnu++11
    PRIVATE -Wno-c++11-narrowing
    PRIVATE -fPIC
)

set_target_properties(
    nativego
    PROPERTIES CMAKE_ANDROID_STL_TYPE stlport_static
)

target_compile_definitions(
    nativego
    ${nativego_definitions}
)

target_link_libraries(
    nativego
    ${nativego_link_libraries}
    ${sles-lib}
    ${glesv2-lib}
    ${egl-lib}
    ${android-lib}
    ${log-lib}
    ${z-lib}
)

#ADD_SUBDIRECTORY(libbreakpad)
#ADD_SUBDIRECTORY(libpng)
#ADD_SUBDIRECTORY(libeditcore)