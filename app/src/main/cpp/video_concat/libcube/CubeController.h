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

#include <pthread.h>
#include <android/native_window.h>

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

    bool initialize();
    void destroy();

};


#endif //MICGO_CUBE_CONTROLLER_H
