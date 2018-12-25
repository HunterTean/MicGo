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

#ifndef MICGO_WAVE_PARTICULAR_H
#define MICGO_WAVE_PARTICULAR_H

#include <stdlib.h>
#include <common_tools.h>
#include "time.h"
#include <math.h>

class WaveParticular {

private:
    int angle;
    float offsetX;
    float x;
    float y;
    float speed;
    float alpha;
    float size;

    int64_t time;

    float getRadian(int angle);

    float ramdomX();
    float ramdomY();
    float ramdomSpeed();
    float ramdomAlpha();
    float ramdomSize();

public:
    WaveParticular();
    virtual ~WaveParticular();

    void reset();

    float getX() {
        return x;
    }

    float getY() {
        return y;
    }

    float getSpeed() {
        return speed;
    }

    float getAlpha() {
        return alpha;
    }

    float getSize() {
        return size;
    }

    void move();
};


#endif //MICGO_WAVE_PARTICULAR_H
