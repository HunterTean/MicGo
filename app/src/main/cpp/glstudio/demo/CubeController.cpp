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
#include "../common/matrix.h"

#define LOG_TAG "CubeController"

CubeController::CubeController() {

    isInited = false;

    pthread_mutex_init(&mLock, NULL);
    pthread_cond_init(&mCondition, NULL);

}

CubeController::~CubeController() {

    pthread_mutex_destroy(&mLock);
    pthread_cond_destroy(&mCondition);

}

void CubeController::prepareEGLContext(ANativeWindow *window, int screenWidth, int screenHeight) {
    this->_window = window;
    this->screenWidth = screenWidth;
    this->screenHeight = screenHeight;

    _msg = MSG_EGL_THREAD_CREATE;

    pthread_create(&_threadId, 0, threadStartCallback, this);
}

void* CubeController::threadStartCallback(void *myself) {
    CubeController* controller = (CubeController*) myself;
    controller->renderLoop();
}

void CubeController::renderLoop() {
    bool renderingEnabled = true;
    while (renderingEnabled) {
//        pthread_mutex_lock(&mLock);
        /*process incoming messages*/
        switch (_msg) {
            case MSG_EGL_THREAD_CREATE:
                isInited = initialize();
                _msg = MSG_EGL_CUBE_SHOW;
                break;
            case MSG_EGL_CUBE_SHOW:
                if (isInited) {
                    draw();
                }
                break;
            case MSG_EGL_THREAD_EXIT:
                renderingEnabled = false;
                destroy();
                break;
            default:
                break;
        }
//        _msg = MSG_NONE;

//        pthread_cond_wait(&mCondition, &mLock);

//        pthread_mutex_unlock(&mLock);
    }
}

const char *vertexShaderSource = "#version 330 core\n"
        "layout (location = 0) in vec3 aPos;\n"
        "layout (location = 1) in vec3 aColor;\n"
        "out vec3 fragmentColor;\n"
        "uniform mat4 transform;"
        "void main()\n"
        "{\n"
        "   gl_Position = transform * vec4(aPos, 1.0);\n"
        "   fragmentColor = aColor;"
        "}\0";
const char *fragmentShaderSource = "#version 330 core\n"
        "in vec3 fragmentColor;"
        "out vec3 FragColor;\n"
        "void main()\n"
        "{\n"
//        "   FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n"
        "   FragColor = fragmentColor;\n"
        "}\n\0";

bool CubeController::initialize() {
    EGLint majorVersion;
    EGLint minorVersion;

    display = eglGetDisplay(EGL_DEFAULT_DISPLAY);
    if (display == EGL_NO_DISPLAY) {
        // unable to open connection to local windowing system
    }
    if (!eglInitialize(display, &majorVersion, &minorVersion)) {
        // unable to initialize EGL, Handle and recover
    }
    LOGI("initialize | majorVersion = %d | minorVersion = %d", majorVersion, minorVersion);
    // we need this config
    const EGLint attribs[] = { EGL_BUFFER_SIZE, 32, EGL_ALPHA_SIZE, 8, EGL_BLUE_SIZE, 8, EGL_GREEN_SIZE, 8, EGL_RED_SIZE, 8, EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
                               EGL_SURFACE_TYPE, EGL_WINDOW_BIT, EGL_NONE };

    const EGLint MaxConfigs = 10;
    EGLConfig configs[MaxConfigs]; // We'll only accept 10 configs
    EGLint numConfigs;
    if(!eglChooseConfig(display, attribs, configs, 1,
                        &numConfigs)) {
        // Something didn't work … handle error situation
    } else {
        // Everything's okay. Continue to create a rendering surface
    }

    EGLNativeWindowType windowType;
    surface = eglCreateWindowSurface(display, configs, windowType, NULL);
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

    mGLProgId = loadProgram(vertexShaderSource, fragmentShaderSource);

    return true;
}

void CubeController::draw() {

    eglMakeCurrent(display, surface, surface, context);

    //render
    drawCube();

    eglSwapBuffers(display, surface);

}

void CubeController::drawCube() {
    // Define the viewport dimensions
    glViewport(0, 0, screenWidth, screenHeight);

    float colors[] = {
            0.583f,  0.771f,  0.014f,
            0.609f,  0.115f,  0.436f,
            0.327f,  0.483f,  0.844f,
            0.822f,  0.569f,  0.201f,
            0.435f,  0.602f,  0.223f,
            0.310f,  0.747f,  0.185f,

            0.597f,  0.770f,  0.761f,
            0.559f,  0.436f,  0.730f,
            0.359f,  0.583f,  0.152f,
            0.483f,  0.596f,  0.789f,
            0.559f,  0.861f,  0.639f,
            0.195f,  0.548f,  0.859f,

            0.014f,  0.184f,  0.576f,
            0.771f,  0.328f,  0.970f,
            0.406f,  0.615f,  0.116f,
            0.676f,  0.977f,  0.133f,
            0.971f,  0.572f,  0.833f,
            0.140f,  0.616f,  0.489f,

            0.997f,  0.513f,  0.064f,
            0.945f,  0.719f,  0.592f,
            0.543f,  0.021f,  0.978f,
            0.279f,  0.317f,  0.505f,
            0.167f,  0.620f,  0.077f,
            0.347f,  0.857f,  0.137f,

            0.055f,  0.953f,  0.042f,
            0.714f,  0.505f,  0.345f,
            0.783f,  0.290f,  0.734f,
            0.722f,  0.645f,  0.174f,
            0.302f,  0.455f,  0.848f,
            0.225f,  0.587f,  0.040f,

            0.517f,  0.713f,  0.338f,
            0.053f,  0.959f,  0.120f,
            0.393f,  0.621f,  0.362f,
            0.673f,  0.211f,  0.457f,
            0.820f,  0.883f,  0.371f,
            0.982f,  0.099f,  0.879f
    };

    //三角形的三个顶点数据
    float vertices[] = {
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,

            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,

            -0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,

            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,

            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,

            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
    };

    unsigned int vboVertex, vboColor, VAO;
//    glGenVertexArrays(1, &VAO);
//    glBindVertexArray(VAO);

    glGenBuffers(1, &vboVertex);
    glBindBuffer(GL_ARRAY_BUFFER, vboVertex);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), (void*)0);

    glEnableVertexAttribArray(0);

    glGenBuffers(1, &vboColor);
    glBindBuffer(GL_ARRAY_BUFFER, vboColor);
    glBufferData(GL_ARRAY_BUFFER, sizeof(colors), colors, GL_STATIC_DRAW);
    glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), (void*)0);

    glEnableVertexAttribArray(1);

    // Enable depth test
    glEnable(GL_DEPTH_TEST);
    // Accept fragment if it closer to the camera than the former one
    glDepthFunc(GL_LESS);

    glClearColor(0.2f, 0.3f, 0.3f, 1.0f);

    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    glUseProgram(mGLProgId);

    float rotateMatrix[4 * 4];
    matrixSetIdentityM(rotateMatrix);
    matrixRotateM(rotateMatrix, 30, 1, 1, 0);

    unsigned int transformLoc = glGetUniformLocation(mGLProgId, "transform");
    glUniformMatrix4fv(transformLoc, 1, GL_FALSE, (GLfloat *) rotateMatrix);

//    glBindVertexArray(VAO);
    glDrawArrays(GL_TRIANGLES, 0, 36);

    eglSwapBuffers(display, surface);
}

void CubeController::destroy() {

}

void CubeController::destroyEGLContext() {

}
