add_library(avformat STATIC IMPORTED)
set_target_properties(avformat PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/prebuilt/libavformat.a)

add_library(avcodec STATIC IMPORTED)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/prebuilt/libavcodec.a)

add_library(avutil STATIC IMPORTED)
set_target_properties(avutil PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/prebuilt/libavutil.a)

add_library(avfilter STATIC IMPORTED)
set_target_properties(avfilter PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/prebuilt/libavfilter.a)

add_library(swresample STATIC IMPORTED)
set_target_properties(swresample PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/prebuilt/libswresample.a)

add_library(swscale STATIC IMPORTED)
set_target_properties(swscale PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/prebuilt/libswscale.a)


add_library(
    nativego
    SHARED
    native_jni.cpp
    video_concat/VideoConcatProcessor.cpp
    extract/ExtractProcessor.cpp
    glstudio/gl_controller.cpp
)

include_directories(
    ${CMAKE_CURRENT_SOURCE_DIR}/libcommon
    ${CMAKE_CURRENT_SOURCE_DIR}/libffmpeg
)

target_include_directories(
    nativego

    PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/libcommon
    PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/libffmpeg
    PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/video_concat/libvideo_concat
    PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/extract/libextract
)

target_link_libraries(
    nativego

    videoconcat
    extract
    glstudio

    avfilter
    avformat
    avcodec
    avutil
    swresample
    swscale

    ${sles-lib}
    ${glesv2-lib}
    ${egl-lib}
    ${android-lib}
    ${log-lib}
    ${z-lib}
)

ADD_SUBDIRECTORY(video_concat)
ADD_SUBDIRECTORY(extract)
ADD_SUBDIRECTORY(glstudio)