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

#include "wave_render.h"

#define LOG_TAG "WaveRender"

WaveRender::WaveRender() {
    isInited = false;
    isRunning = false;
}

WaveRender::~WaveRender() {

}

void WaveRender::prepareEGL(ANativeWindow *window, int screenWidth, int screenHeight) {
    this->_window = window;
    this->screenWidth = screenWidth;
    this->screenHeight = screenHeight;

    this->isRunning = true;

    initParticulars();

    pthread_create(&_threadId, 0, threadStartCallback, this);
}

bool WaveRender::initialize() {
    isInited = true;

    EGLint majorVersion;
    EGLint minorVersion;

    display = eglGetDisplay(EGL_DEFAULT_DISPLAY);
    if (display == EGL_NO_DISPLAY) {
        // unable to open connection to local windowing system
    }
    if (!eglInitialize(display, &majorVersion, &minorVersion)) {
        // unable to initialize EGL, Handle and recover
    }
    LOGI("Tian initialize | majorVersion = %d | minorVersion = %d", majorVersion, minorVersion);
    // we need this config
    const EGLint attribs[] = { EGL_BUFFER_SIZE, 32, EGL_ALPHA_SIZE, 8, EGL_BLUE_SIZE, 8, EGL_GREEN_SIZE, 8, EGL_RED_SIZE, 8, EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
                               EGL_SURFACE_TYPE, EGL_WINDOW_BIT, EGL_NONE };

//    const EGLint iCfgAttrList[] = {
//                    EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
//                    EGL_SURFACE_TYPE, EGL_WINDOW_BIT,
//                    EGL_BUFFER_SIZE, 32,
//                    EGL_ALPHA_SIZE, 8,
//                    EGL_RED_SIZE, 8,
//                    EGL_GREEN_SIZE, 8,
//                    EGL_BLUE_SIZE, 8,
//                    EGL_SAMPLE_BUFFERS, 1,
//                    EGL_SAMPLES, 8,
//                    EGL_NONE
//            };

    EGLint iCfgAttrList[] =
            {
                    EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
                    EGL_SURFACE_TYPE, EGL_WINDOW_BIT,
                    EGL_BUFFER_SIZE, 16,
                    EGL_RED_SIZE, 5,
                    EGL_GREEN_SIZE, 6,
                    EGL_BLUE_SIZE, 5,
                    EGL_DEPTH_SIZE, 16,
                    EGL_SAMPLE_BUFFERS, 1,
                    EGL_SAMPLES, 8,
                    EGL_NONE
            };

//    const EGLint MaxConfigs = 10;
    EGLConfig configs; // We'll only accept 10 configs
    EGLint numConfigs;
    if(!eglChooseConfig(display, iCfgAttrList, &configs, 1, &numConfigs)) {
        // Something didn't work … handle error situation
        LOGI("Tian eglChooseConfig 0");
    } else {
        // Everything's okay. Continue to create a rendering surface
        LOGI("Tian eglChooseConfig 1");
    }

    EGLint format;
    if (!eglGetConfigAttrib(display, configs, EGL_NATIVE_VISUAL_ID, &format)) {
        LOGI("Tian eglGetConfigAttrib() returned error %d", eglGetError());
        return surface;
    }
    ANativeWindow_setBuffersGeometry(_window, 0, 0, format);
    surface = eglCreateWindowSurface(display, configs, _window, NULL);
    if (surface == EGL_NO_SURFACE) {
        switch(eglGetError())
        {
            case EGL_BAD_MATCH:
                // Check window and EGLConfig attributes to determine
                // compatibility, or verify that the EGLConfig
                // supports rendering to a window,
                break;
            case EGL_BAD_CONFIG:
                // Verify that provided EGLConfig is valid
                break;
            case EGL_BAD_NATIVE_WINDOW:
                // Verify that provided EGLNativeWindow is valid
                break;
            case EGL_BAD_ALLOC:
                // Not enough resources available. Handle and recover
                break;
        }
    }

    const EGLint attribList[] = {
            EGL_CONTEXT_CLIENT_VERSION, 2,
            EGL_NONE
    };

    context = eglCreateContext(display, configs, EGL_NO_CONTEXT, attribList);
    if(context == EGL_NO_CONTEXT)
    {
        EGLint error = eglGetError();
        if(error == EGL_BAD_CONFIG)
        {
            // Handle error and recover
        }
    }

    eglMakeCurrent(display, surface, surface, context);

    mGLProgId = loadProgram(vertexShaderSource, fragmentShaderSource);

    localtionPos = glGetAttribLocation(mGLProgId, "vPosition");
    localtionTex = glGetAttribLocation(mGLProgId, "vTexCords");

    uniformGreenPoints = glGetUniformLocation(mGLProgId, "pointPosG");
    uniformRedPoints = glGetUniformLocation(mGLProgId, "pointPosR");

    mGLRedProgId = loadProgram(vertexShaderSource, fragmentShaderRed);
    localtionPosRed = glGetAttribLocation(mGLRedProgId, "vPosition");
    localtionTexRed = glGetAttribLocation(mGLRedProgId, "vTexCords");
    uniformPointsRed = glGetUniformLocation(mGLRedProgId, "pointPosR");

    mGLGreenProgId = loadProgram(vertexShaderSource, fragmentShaderGreen);
    localtionPosGreen = glGetAttribLocation(mGLGreenProgId, "vPosition");
    localtionTexGreen = glGetAttribLocation(mGLGreenProgId, "vTexCords");
    uniformPointsGreen = glGetUniformLocation(mGLGreenProgId, "pointPosG");

    mGLParticularProgId = loadProgram(vertexShaderParticular, fragmentShaderParticular);
    locationParticularPos = glGetAttribLocation(mGLParticularProgId, "vPosition");
    locationParticularSize = glGetAttribLocation(mGLParticularProgId, "vPointSize");
    locationParticularCol = glGetAttribLocation(mGLParticularProgId, "aColor");

//    glGenBuffers(1, &vboVertex);
//    glBindBuffer(GL_ARRAY_BUFFER, vboVertex);
//    glBufferData(GL_ARRAY_BUFFER, sizeof(testVertex), testVertex, GL_STATIC_DRAW);
    LOGI("loadProgram done. | localtionPos = %d", localtionPos);

//    glBlendFunc(GL_SRC_ALPHA, GL_ONE);//混合函数
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    return true;
}

void* WaveRender::threadStartCallback(void *myself) {
    WaveRender* controller = (WaveRender*) myself;
    controller->renderLoop();
}

void WaveRender::renderLoop() {
    initialize();
    while (isRunning) {
        glViewport(0, 0, screenWidth, screenHeight);

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glEnable(GL_BLEND);
//        draw();
        drawRed();
        drawGreen();

        drawParticular();

        glDisable(GL_BLEND);

        eglSwapBuffers(display, surface);
    }
}

void WaveRender::draw() {
//    LOGI("Tian WaveRender::draw | screenWidth = %d | screenHeight = %d", screenWidth, screenHeight);

    glUseProgram(mGLProgId);

//    glEnableVertexAttribArray(localtionPos);
//    glBindBuffer(GL_ARRAY_BUFFER, vboVertex);
//    glVertexAttribPointer(localtionPos, 2, GL_FLOAT, GL_FALSE, 0, 0);

//    float testVertex[] = {-0.5f, -0.5f, 0.5f, -0.5f, 0.0f, 0.5f};
    float greenVertex[] = {
            0.0f, 1.0f, 0.01f, 0.1f, 0.02f, 0.2f, 0.03f, 0.3f, 0.04f, 0.4f, 0.05f, 0.5f, 0.06f, 0.6f, 0.07f, 0.7f, 0.08f, 0.8f, 0.09f, 0.9f
    };
    glUniform1fv(uniformGreenPoints, 20, greenVertex);
//      glUniformMatrix4fv(uniformPoints[i], 2, GL_FALSE, testVertex);

    float redVertex[] = {
            0.0f, 0.1f, 0.01f, 1.0f, 0.02f, 0.9f, 0.03f, 0.8f, 0.04f, 0.7f, 0.05f, 0.6f, 0.06f, 0.5f, 0.07f, 0.4f, 0.08f, 0.3f, 0.09f, 0.2f
    };
    glUniform1fv(uniformRedPoints, 20, redVertex);

    glVertexAttribPointer(localtionPos, 2, GL_FLOAT, GL_FALSE, 0, GL_VERTEX_COORDS);
    glEnableVertexAttribArray (localtionPos);

    glVertexAttribPointer(localtionTex, 2, GL_FLOAT, GL_FALSE, 0, GL_TEXTURE_COORDS);
    glEnableVertexAttribArray (localtionTex);

    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
//    glDrawArrays(GL_POINTS, 0, 3);

    glDisableVertexAttribArray(localtionPos);
    glDisableVertexAttribArray(localtionTex);


}

void WaveRender::drawRed() {
    glUseProgram(mGLRedProgId);

    float redVertex[] = {
            0.0f, 0.1f, 0.01f, 1.0f, 0.02f, 0.9f, 0.03f, 0.8f, 0.04f, 0.7f, 0.05f, 0.6f, 0.06f, 0.5f, 0.07f, 0.4f, 0.08f, 0.3f, 0.09f, 0.2f
    };
    glUniform1fv(uniformPointsRed, 20, redVertex);

    glVertexAttribPointer(localtionPosRed, 2, GL_FLOAT, GL_FALSE, 0, GL_VERTEX_COORDS);
    glEnableVertexAttribArray (localtionPosRed);

    glVertexAttribPointer(localtionTexRed, 2, GL_FLOAT, GL_FALSE, 0, GL_TEXTURE_COORDS);
    glEnableVertexAttribArray (localtionTexRed);

    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
//    glDrawArrays(GL_POINTS, 0, 3);

    glDisableVertexAttribArray(localtionPosRed);
    glDisableVertexAttribArray(localtionTexRed);

}

void WaveRender::drawGreen() {
    glUseProgram(mGLGreenProgId);
    float greenVertex[] = {
            0.0f, 1.0f, 0.01f, 0.1f, 0.02f, 0.2f, 0.03f, 0.3f, 0.04f, 0.4f, 0.05f, 0.5f, 0.06f, 0.6f, 0.07f, 0.7f, 0.08f, 0.8f, 0.09f, 0.9f
    };
    glUniform1fv(uniformPointsGreen, 20, greenVertex);

    glVertexAttribPointer(localtionPosGreen, 2, GL_FLOAT, GL_FALSE, 0, GL_VERTEX_COORDS);
    glEnableVertexAttribArray (localtionPosGreen);

    glVertexAttribPointer(localtionTexGreen, 2, GL_FLOAT, GL_FALSE, 0, GL_TEXTURE_COORDS);
    glEnableVertexAttribArray (localtionTexGreen);

    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
//    glDrawArrays(GL_POINTS, 0, 3);

    glDisableVertexAttribArray(localtionPosGreen);
    glDisableVertexAttribArray(localtionTexGreen);
}

#define PARTICULAR_S_NUM 80

void WaveRender::initParticulars() {
    for (int i = 0; i < PARTICULAR_S_NUM; i++) {
        WaveParticular* particular = new WaveParticular();
        particular->reset();
        particularVector.push_back(particular);
    }
}

void WaveRender::drawParticular() {
    glUseProgram(mGLParticularProgId);

    GLfloat point_pos[PARTICULAR_S_NUM*2];
    for (int i = 0; i < PARTICULAR_S_NUM; i++) {
        particularVector.at(i)->move();
        point_pos[i*2] = particularVector.at(i)->getX();
        point_pos[i*2+1] = particularVector.at(i)->getY();
    }

    GLfloat point_size[PARTICULAR_S_NUM];
    for (int i = 0; i < PARTICULAR_S_NUM; i++) {
        point_size[i] = particularVector.at(i)->getSize();
    }

    GLfloat point_color[PARTICULAR_S_NUM];
    for (int i = 0; i < PARTICULAR_S_NUM; i++) {
        point_color[i] = particularVector.at(i)->getAlpha();
    }

    glVertexAttribPointer(locationParticularPos, 2, GL_FLOAT, GL_FALSE, 0, point_pos);
    glEnableVertexAttribArray (locationParticularPos);

    glVertexAttribPointer(locationParticularSize, 1, GL_FLOAT, GL_FALSE, 0, point_size);
    glEnableVertexAttribArray (locationParticularSize);

    glVertexAttribPointer(locationParticularCol, 1, GL_FLOAT, GL_FALSE, 0, point_color);
    glEnableVertexAttribArray(locationParticularCol);

    glDrawArrays(GL_POINTS, 0, PARTICULAR_S_NUM);

}

void WaveRender::destroyEGL() {
    isRunning = false;
}

