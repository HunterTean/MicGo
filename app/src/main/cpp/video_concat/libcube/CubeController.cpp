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

#include "CubeController.h"

CubeController::CubeController() {

}

CubeController::~CubeController() {

}

void CubeController::prepareEGLContext(ANativeWindow *window, int screenWidth, int screenHeight) {
    this->_window = window;
    this->screenWidth = screenWidth;
    this->screenHeight = screenHeight;

    pthread_create(&_threadId, 0, threadStartCallback, this);
}

void* CubeController::threadStartCallback(void *myself) {
    CubeController* controller = (CubeController*) myself;
    controller->renderLoop();
}

void CubeController::renderLoop() {
    bool renderingEnabled = true;
    while (renderingEnabled) {
        pthread_mutex_lock(&mLock);
        /*process incoming messages*/
        switch (_msg) {
            case MSG_EGL_THREAD_CREATE:
                initialize();
                break;
            case MSG_EGL_CUBE_SHOW:
                this->releaseCamera();
                this->configCamera();
                renderer->setDegress(degress,facingId == CAMERA_FACING_FRONT);
                this->startCameraPreview();
                cameraTextureRefresh = true;
                break;
            case MSG_EGL_THREAD_EXIT:
                renderingEnabled = false;
                destroy();
                break;
            default:
                break;
        }
        _msg = MSG_NONE;
        if (NULL != eglCore) {

            if (cameraTextureRefresh ) {
                cameraTextureRefresh = false;
            }else{
                this->processVideoFrame();
                this->draw();
            }

            pthread_cond_wait(&mCondition, &mLock);
        }
        pthread_mutex_unlock(&mLock);
    }
}

void CubeController::destroyEGLContext() {

}
