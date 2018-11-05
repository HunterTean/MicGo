add_library(
    glstudio
    STATIC
    ${CMAKE_CURRENT_SOURCE_DIR}/gl_controller.cpp
    ${CMAKE_CURRENT_SOURCE_DIR}/common/matrix.cpp
    ${CMAKE_CURRENT_SOURCE_DIR}/demo/CubeController.cpp
)

target_include_directories(
    glstudio
    PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}
    PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/../libcommon
    PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/../libffmpeg
)

target_link_libraries(
    glstudio
    avfilter
    avformat
    avcodec
    swscale
    swresample
)