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
 * Created by 刘洪天 on 2018/10/26.
 *
 */

#ifndef MICGO_CUBE_CONTROLLER_H
#define MICGO_CUBE_CONTROLLER_H

#include <common_tools.h>

#include <pthread.h>
#include <android/native_window.h>
#include <EGL/egl.h>
#include <EGL/eglext.h>

#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include <malloc.h>

class CubeController {

public:
    CubeController();
    virtual ~CubeController();

    /** 准备EGL Context与EGLThread **/
    void prepareEGLContext(ANativeWindow* window, int screenWidth, int screenHeight);

    /** 销毁EGLContext与EGLThread **/
    void destroyEGLContext();

private:
    ANativeWindow* _window;
    int screenWidth;
    int screenHeight;

    /** EGLThread关键的方法 **/
    pthread_t _threadId;
    static void* threadStartCallback(void *myself);
    void renderLoop();

    /** 调用线程与EGLThread通讯的消息类型与锁 **/
    pthread_mutex_t mLock;
    pthread_cond_t mCondition;
    enum RenderThreadMessage {
        MSG_NONE = 0,
        MSG_EGL_THREAD_CREATE,
        MSG_EGL_CUBE_SHOW,
        MSG_EGL_THREAD_EXIT
    };
    RenderThreadMessage _msg;

    /** 线程中核心的处理方法 **/
    //创建EGL资源以及调用Android创建Camera
//    EGLCore* eglCore;
//    EGLSurface previewSurface;
    EGLDisplay display;
    EGLSurface surface;
    EGLContext context;

    GLuint mGLProgId;

    bool isInited;

    bool initialize();
    void draw();
    void drawCube();
    void destroy();

    GLuint loadShader(GLenum shaderType, const char* pSource) {
        GLuint shader = glCreateShader(shaderType);
        if (shader) {
            glShaderSource(shader, 1, &pSource, NULL);
            glCompileShader(shader);
            GLint compiled = 0;
            glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
            if (!compiled) {
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
                    LOGI( "Guessing at GL_INFO_LOG_LENGTH size\n");
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
        GLuint vertexShader = loadShader(GL_VERTEX_SHADER, pVertexSource);
        if (!vertexShader) {
            return 0;
        }
        GLuint pixelShader = loadShader(GL_FRAGMENT_SHADER, pFragmentSource);
        if (!pixelShader) {
            return 0;
        }
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


#endif //MICGO_CUBE_CONTROLLER_H
