/**
 *             ,%%%%%%%%, 
 *           ,%%/\%%%%/\%% 
 *          ,%%%\c "" J/%%% 
 * %.       %%%%/ o  o \%%% 
 * `%%.     %%%%    _  |%%% 
 *  `%%     `%%%%(__Y__)%%' 
 *  //       ;%%%%`\-/%%%'
 * ((       /  `%%%%%%%' 
 *  \\    .'          | 
 *   \\  /       \  | | 
 *    \\/         ) | | 
 *     \         /_ | |__ 
 *     (___________)))))))
 *
 *
 * Created by 刘洪天 on 2018/12/24.
 *
 */

#include "wave_particular.h"

#define LOG_TAG "WaveParticular"

WaveParticular::WaveParticular() {

}

WaveParticular::~WaveParticular() {

}

void WaveParticular::reset() {
    angle = rand() % 360;
    offsetX = (float)(rand() % 200 + 0.5) / 100 - 1;
//    x = ramdomX();
    y = ramdomY();
//    LOGI("Tian point_pos x = %f | y = %f | angle = %d", x, y, angle);
    speed = ramdomSpeed();
    alpha = ramdomAlpha();
    size = ramdomSize();

    time = currentTimeMills();
}

void WaveParticular::move() {
    x = (float) (0.1 * cos(getRadian(angle))) + offsetX;
    angle++;
    LOGI("Tian | cos = %f", cos(getRadian(angle)));
//    x = offsetX;
    y = speed * (currentTimeMills() - time) + y;
    if (y > 1.0) {
        reset();
    }

    time = currentTimeMills();
}

float WaveParticular::getRadian(int angle) {
    return (float) (3.14159265358979323846 / 180 * angle);
}

//float WaveParticular::ramdomX() {
////    srand((unsigned)time(NULL));
//    float x = (float)(rand() % 20 + 0.5) / 10 - 1;
//    return x;
//}

float WaveParticular::ramdomY() {
    float y = (float) ((float)(rand() % 7) / 10 - 0.9);
    return y;
}

float WaveParticular::ramdomAlpha() {
    float alpha = (float) ((float)(rand() % 5) / 10 + 0.5);
    return alpha;
}

float WaveParticular::ramdomSize() {
    float size = (float)(rand() % 25) / 10 + 2;
    return size;
}

float WaveParticular::ramdomSpeed() {
    float speed = (float)(rand() % 5 + 4) / 10000;
    return speed;
}