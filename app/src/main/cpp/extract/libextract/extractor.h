//
// Created by 刘洪天 on 18/4/10.
//

#ifndef MICGO_EXTRACTOR_H
#define MICGO_EXTRACTOR_H

#include <common_tools.h>

extern "C"
{
#include <libavformat/avformat.h>
}

class Extractor {

public:
    Extractor();
    ~Extractor();

    int processMP3ToMP3(const char* inputPath, const char* outputPath, int startSecond, int endSecond);
    int processMP4ToMP3(const char* inputPath, const char* outputPath, int startSecond, int endSecond);

private:


};


#endif //MICGO_EXTRACTOR_H
