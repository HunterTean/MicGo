cmake_minimum_required(VERSION 3.4.1)

add_library(native-lib
            SHARED
            src/main/cpp/nativelib/native_lib.cpp)

include_directories( imported-lib/include/ )

find_library(log-lib log)

target_link_libraries(native-lib
    animat
    ${log-lib} )

ADD_SUBDIRECTORY(src/main/cpp/glstudio)