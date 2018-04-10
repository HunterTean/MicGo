
add_library(
    extract
    STATIC
    ${CMAKE_CURRENT_SOURCE_DIR}/libextract/extractor.cpp
)

target_include_directories(
    extract
    PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}
    PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/../libcommon
    PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/../libffmpeg
)

target_link_libraries(
    extract
    avfilter
    avformat
    avcodec
    swscale
    swresample
)