//
// Created by 刘洪天 on 18/4/10.
//

#ifndef MICGO_EXTRACTOR_H
#define MICGO_EXTRACTOR_H

#include <common_tools.h>

class Extractor {

public:
    Extractor();
    ~Extractor();

    void processMP3ToMP3(const char* inputPath, const char* outputPath, int startSecond, int endSecond);
    void processMP4ToMP3(const char* inputPath, const char* outputPath, int startSecond, int endSecond);

private:


};


#endif //MICGO_EXTRACTOR_H
