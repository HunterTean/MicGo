
add_library(
    videoconcat
    STATIC
    ${CMAKE_CURRENT_SOURCE_DIR}/libvideo_concat/video_concator.cpp
)

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