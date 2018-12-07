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
 * Created by 刘洪天 on 2018/11/30.
 *
 */

#ifndef MICGO_WAVE_RENDER_H
#define MICGO_WAVE_RENDER_H

#include <common_tools.h>

#include <pthread.h>
#include <android/native_window.h>
#include <EGL/egl.h>
#include <EGL/eglext.h>

#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include <malloc.h>

class WaveRender {

    const char *vertexShaderSource = // "#version 330 core\n"0
            "attribute vec4 vPosition;\n"
            "attribute vec4 vTexCords;\n"
            "varying vec2 uvCoords;\n"
            "void main() {\n"
            "   uvCoords = vTexCords.xy;\n"
//            "   gl_PointSize = 14.0;\n"
            "   gl_Position = vPosition;\n"
            "}\n";
    const char *fragmentShaderSource = //"#version 330 core\n"
            "precision highp float;\n"
            "varying vec2 uvCoords;\n"
            "uniform float pointPosG[20];\n"
            "uniform float pointPosR[20];\n"
            "void main()\n"
            "{\n"
//            "   float minusX = uvCoords.x - pointPos[0];\n"
//            "   float minusY = uvCoords.y - pointPos[1];\n"
            "   int indexX = 0;\n"
            "   indexX = int(uvCoords.x * 10.0);\n"
            "   int indexY = indexX * 2 + 1;\n"
            "   float greenY = pointPosG[indexY];\n"
            "   float redY = pointPosR[indexY];\n"
            "   if (uvCoords.y > greenY - 0.02 && uvCoords.y > redY - 0.02) {\n" //红绿面交集
            "       gl_FragColor = vec4(0.41568627, 0.82745098, 0.74117647, 0.4);\n"
            "   } else if (uvCoords.y - greenY < 0.02 && uvCoords.y - greenY > -0.02) {\n" // 绿线
            "       gl_FragColor = vec4(0.14901961, 0.78431373, 0.65490196, 1.0);\n"
            "   } else if (uvCoords.y < redY - 0.02 && uvCoords.y > greenY + 0.02) {\n" // 绿面
            "       gl_FragColor = vec4(0.14901961, 0.78431373, 0.65490196, 0.4);\n"
            "   } else if (uvCoords.y == greenY) {\n" // 绿面盖红线
            "       gl_FragColor = vec4(0.10980392, 0.36078431, 0.33333333, 0.1);\n"
            "   } else if (uvCoords.y - redY < 0.02 && uvCoords.y - redY > -0.02) {\n" // 红线
            "       gl_FragColor = vec4(0.86666667, 0.14509804, 0.29803922, 1.0);\n"
            "   } else if (uvCoords.y > redY && uvCoords.y < greenY) {\n" // 红面
            "       gl_FragColor = vec4(0.86666667, 0.14509804, 0.29803922, 0.4);\n"
            "   } else {\n"
            "       gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);\n"
            "   }\n"
            "}\n";

    const char *fragmentShaderRed = //"#version 330 core\n"
            "precision highp float;\n"
                    "varying vec2 uvCoords;\n"
                    "uniform float pointPosR[20];\n"
                    "void main()\n"
                    "{\n"
                    "   int indexX = 0;\n"
                    "   indexX = int(uvCoords.x * 10.0);\n"
                    "   int indexY = indexX * 2 + 1;\n"
                    "   float redY = pointPosR[indexY];\n"
                    "   if (uvCoords.y - redY < 0.02 && uvCoords.y - redY > -0.02) {\n" // 红线
                    "       gl_FragColor = vec4(0.86666667, 0.14509804, 0.29803922, 1.0);\n"
                    "   } else if (uvCoords.y > redY + 0.02) {\n" // 红面
                    "       gl_FragColor = vec4(0.86666667, 0.14509804, 0.29803922, 0.4);\n"
                    "   } else {\n"
                    "       gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);\n"
                    "   }\n"
                    "}\n";

    const char *fragmentShaderGreen = //"#version 330 core\n"
            "precision highp float;\n"
                    "varying vec2 uvCoords;\n"
                    "uniform float pointPosG[20];\n"
                    "void main()\n"
                    "{\n"
                    "   int indexX = 0;\n"
                    "   indexX = int(uvCoords.x * 10.0);\n"
                    "   int indexY = indexX * 2 + 1;\n"
                    "   float greenY = pointPosG[indexY];\n"
                    "   if (uvCoords.y - greenY < 0.02 && uvCoords.y - greenY > -0.02) {\n" // 绿线
                    "       gl_FragColor = vec4(0.14901961, 0.78431373, 0.65490196, 1.0);\n"
                    "   } else if (uvCoords.y > greenY + 0.02) {\n" // 绿面
                    "       gl_FragColor = vec4(0.14901961, 0.78431373, 0.65490196, 0.4);\n"
                    "   } else {\n"
                    "       gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);\n"
                    "   }\n"
                    "}\n";

    const GLfloat GL_VERTEX_COORDS[8] = {
            -1.0f, 1.0f,    // 0 bottom right
            1.0f, 1.0f,	    // 1 top right
            -1.0f, -1.0f,	// 2 top left
            1.0f, -1.0f,	// 3 bottom left
    };

    const GLfloat GL_TEXTURE_COORDS[8] = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
    };

public:
    WaveRender();
    virtual ~WaveRender();

    /** 准备EGL Context与EGLThread **/
    void prepareEGL(ANativeWindow* window, int screenWidth, int screenHeight);

    /** 销毁EGLContext与EGLThread **/
    void destroyEGL();

private:
    ANativeWindow* _window;
    int screenWidth;
    int screenHeight;

    /** EGLThread关键的方法 **/
    pthread_t _threadId;
    static void* threadStartCallback(void *myself);
    void renderLoop();

    /** 线程中核心的处理方法 **/
    //创建EGL资源以及调用Android创建Camera
//    EGLCore* eglCore;
//    EGLSurface previewSurface;
    EGLDisplay display;
    EGLSurface surface;
    EGLContext context;

    GLuint mGLProgId;

    GLuint localtionPos;
    GLuint localtionTex;
    GLuint uniformGreenPoints;
    GLuint uniformRedPoints;

    GLuint mGLRedProgId;
    GLuint localtionPosRed;
    GLuint localtionTexRed;
    GLuint uniformPointsRed;

    GLuint mGLGreenProgId;
    GLuint localtionPosGreen;
    GLuint localtionTexGreen;
    GLuint uniformPointsGreen;

    GLuint vboVertex;

    bool isInited;
    bool isRunning;

    bool initialize();
    void draw();
    void drawRed();
    void drawGreen();
    void destroy();

    GLuint loadShader(GLenum shaderType, const char* pSource) {
        GLuint shader = glCreateShader(shaderType);
        if (shader) {
            glShaderSource(shader, 1, &pSource, NULL);
            glCompileShader(shader);
            GLint compiled = 0;
            glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
            if (!compiled) {
                LOGI("Tian loadShader !compiled 0");
                GLint infoLen = 0;
                glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);
                LOGI("Tian loadShader !compiled 1");
                if (infoLen) {
                    LOGI("Tian loadShader !compiled infoLen");
                    char* buf = (char*) malloc(infoLen);
                    if (buf) {
                        glGetShaderInfoLog(shader, infoLen, NULL, buf);
                        LOGI("Tian Could not compile shader %d:\n%s\n", shaderType, buf);
                        free(buf);
                    }
                } else {
                    LOGI( "Tian Guessing at GL_INFO_LOG_LENGTH size\n");
                    char* buf = (char*) malloc(0x1000);
                    if (buf) {
                        glGetShaderInfoLog(shader, 0x1000, NULL, buf);
                        LOGI("Tian Could not compile shader %d:\n%s\n", shaderType, buf);
                        free(buf);
                    }
                }
                glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    GLuint loadProgram(const char* pVertexSource, const char* pFragmentSource) {
        LOGI("Tian | loadProgram 0");
        GLuint vertexShader = loadShader(GL_VERTEX_SHADER, pVertexSource);
        LOGI("Tian | loadProgram 1");
        if (!vertexShader) {
            return 0;
        }
        LOGI("Tian | loadProgram 2");
        GLuint pixelShader = loadShader(GL_FRAGMENT_SHADER, pFragmentSource);
        LOGI("Tian | loadProgram 3");
        if (!pixelShader) {
            return 0;
        }
        LOGI("Tian | loadProgram 4");
        GLuint program = glCreateProgram();
        if (program) {
            glAttachShader(program, vertexShader);
            glAttachShader(program, pixelShader);
            glLinkProgram(program);
            GLint linkStatus = GL_FALSE;
            glGetProgramiv(program, GL_LINK_STATUS, &linkStatus);
            if (linkStatus != GL_TRUE) {
                GLint bufLength = 0;
                glGetProgramiv(program, GL_INFO_LOG_LENGTH, &bufLength);
                if (bufLength) {
                    char* buf = (char*) malloc(bufLength);
                    if (buf) {
                        glGetProgramInfoLog(program, bufLength, NULL, buf);
                        LOGI("Could not link program:\n%s\n", buf);
                        free(buf);
                    }
                }
                glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

};


#endif //MICGO_WAVE_RENDER_H
