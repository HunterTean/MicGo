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
            "uniform float pointPos[20];\n"
            "void main()\n"
            "{\n"
//            "   float minusX = uvCoords.x - pointPos[0];\n"
//            "   float minusY = uvCoords.y - pointPos[1];\n"
            "   int indexX = 0;\n"
            "   indexX = int(uvCoords.x * 10.0);\n"
            "   int pointY = indexX * 2 + 1;\n"
            "   float minusY = uvCoords.y - pointPos[pointY];\n"
//            "   if (minusX < 0.0014 && minusX > -0.0014 && minusY > 0.0) {\n"
            "   if (minusY > 0.0) {\n"
            "       gl_FragColor = vec4(0.8, 0.8, 0.8, 1.0);\n"
            "   } else {\n"
            "       gl_FragColor = vec4(0.2, 0.2, 0.2, 1.0);\n"
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
    GLuint uniformPoints;

    GLuint vboVertex;

    bool isInited;
    bool isRunning;

    bool initialize();
    void draw();
    void destroy();

    GLuint loadShader(GLenum shaderType, const char* pSource) {
        GLuint shader = glCreateShader(shaderType);
        if (shader) {
            glShaderSource(shader, 1, &pSource, NULL);
            glCompileShader(shader);
            GLint compiled = 0;
            glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
            if (!compiled) {
                LOGI("Tian loadShader !compiled");
                GLint infoLen = 0;
                glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);
                if (infoLen) {
                    char* buf = (char*) malloc(infoLen);
                    if (buf) {
                        glGetShaderInfoLog(shader, infoLen, NULL, buf);
                        LOGI("Could not compile shader %d:\n%s\n", shaderType, buf);
                        free(buf);
                    }
                } else {
                    LOGI( "Tian Guessing at GL_INFO_LOG_LENGTH size\n");
                    char* buf = (char*) malloc(0x1000);
                    if (buf) {
                        glGetShaderInfoLog(shader, 0x1000, NULL, buf);
                        LOGI("Could not compile shader %d:\n%s\n", shaderType, buf);
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
