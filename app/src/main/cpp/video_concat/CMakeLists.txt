
add_library(
    videoconcat
    STATIC
    ${CMAKE_CURRENT_SOURCE_DIR}/libvideo_concat/video_concator.cpp
)

#target_compile_definitions(
#    videoconcat
#    PRIVATE -DHAVE_CONFIG_H
#    PRIVATE -DFPM_ARM
#)

#target_compile_options(
#    videoconcat
#    PRIVATE -ffast-math
#    PRIVATE -O3
#    PRIVATE -std=gnu++11
#)

target_include_directories(
    videoconcat
    PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}
    PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/../libcommon
    PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/../libffmpeg
)

target_link_libraries(
    videoconcat
    avfilter
    avformat
    avcodec
    swscale
    swresample
)