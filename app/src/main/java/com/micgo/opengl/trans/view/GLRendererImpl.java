package com.micgo.opengl.trans.view;//package com.changba.record.recording.view.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by liuhongtian on 17/12/23.
 */

public class GLRendererImpl implements GLRenderThread.GLRenderer {

    private int mProgramObject;
    private int mWidth;
    private int mHeight;
    private FloatBuffer mVertices;
    private ShortBuffer mTexCoords;
    private Context mContext;
    private int[] mTexID;
    private final float[] mVerticesData = { -1f, -1f, 0, 1f, -1f, 0, -1f, 1f, 0, 1f, 1f, 0 };
    private final short[] mTexCoordsData = {0, 1, 1, 1, 0, 0, 1, 0};

    private int mStep = 0;

    private int mUniformLocationTexA;
    private int mUniformLocationTexB;
    private int mUniformLocationTexC;

    private int mUniformLocationStep;

    private int mUniformLocationAlpha;

    public GLRendererImpl(Context ctx) {
        mVertices = ByteBuffer.allocateDirect(mVerticesData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertices.put(mVerticesData).position(0);

        mTexCoords = ByteBuffer.allocateDirect(mTexCoordsData.length * 2)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        mTexCoords.put(mTexCoordsData).position(0);

        mContext = ctx;
    }

    public void setViewport(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    public void initGL(int res) {
        comipleAndLinkProgram();
        loadTexture(new int[]{res});
        GLES20.glClearColor(0,  0, 0, 0);

        mUniformLocationTexA = GLES20.glGetUniformLocation(mProgramObject, "a_Texture");
        mUniformLocationTexB = GLES20.glGetUniformLocation(mProgramObject, "b_Texture");
        mUniformLocationTexC = GLES20.glGetUniformLocation(mProgramObject, "c_Texture");

        mUniformLocationStep = GLES20.glGetUniformLocation(mProgramObject, "image_step");
        mUniformLocationAlpha = GLES20.glGetUniformLocation(mProgramObject, "image_alpha");
    }

    public void resize(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    @Override
    public void drawFirstFrame() {
        // TODO Auto-generated method stub
        GLES20.glViewport(0, 0, mWidth, mHeight);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(mProgramObject);

        GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 0, mVertices);
        GLES20.glEnableVertexAttribArray(0);
        GLES20.glVertexAttribPointer(1, 2, GLES20.GL_SHORT, false, 0, mTexCoords);
        GLES20.glEnableVertexAttribArray(1);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexID[0]);
        GLES20.glUniform1i(mUniformLocationTexA, 0);

        GLES20.glUniform1i(mUniformLocationStep, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    @Override
    public void drawFrame() {
        // TODO Auto-generated method stub
        GLES20.glViewport(0, 0, mWidth, mHeight);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(mProgramObject);

        GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 0, mVertices);
        GLES20.glEnableVertexAttribArray(0);
        GLES20.glVertexAttribPointer(1, 2, GLES20.GL_SHORT, false, 0, mTexCoords);
        GLES20.glEnableVertexAttribArray(1);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexID[0]);
        GLES20.glUniform1i(mUniformLocationTexA, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexID[1]);
        GLES20.glUniform1i(mUniformLocationTexB, 1);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexID[2]);
        GLES20.glUniform1i(mUniformLocationTexC, 2);

        mStep++;
        if (mStep > 99) {
            mStep = 0;
        }
        GLES20.glUniform1i(mUniformLocationStep, mStep);

        float mAlpha;
        if (mStep < 25) {
            mAlpha = (float)mStep / 25;
        } else if (mStep < 50) {
            mAlpha = (float)(mStep - 25) / 25;
        } else if (mStep < 75) {
            mAlpha = (float)(mStep - 50) / 25;
        } else {
            mAlpha = (float)(mStep - 75) / 25;
        }
        GLES20.glUniform1f(mUniformLocationAlpha, mAlpha);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    public void loadTexture(int[] res) {
        int index = 0;
        if (mTexID == null) {
            mTexID = new int[res.length];
        } else {
            index = mTexID.length;
            int[] texIDbak = new int[index];
            for (int i = 0; i < index; i++) {
                texIDbak[i] = mTexID[i];
            }
            mTexID = new int[index+res.length];
            for (int i = 0; i < texIDbak.length; i++) {
                mTexID[i] = texIDbak[i];
            }
        }
        for (int i = 0; i < res.length; i++) {
            Bitmap b = BitmapFactory.decodeResource(mContext.getResources(), res[i]);
            if (b != null) {
                int []texID = new int[1];
                GLES20.glGenTextures(1, texID, 0);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texID[0]);
                mTexID[i+index] = texID[0];

                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                        GLES20.GL_LINEAR);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                        GLES20.GL_LINEAR);

                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                        GLES20.GL_REPEAT);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                        GLES20.GL_REPEAT);

                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, b, 0);
                b.recycle();
            }
        }
    }

    private void comipleAndLinkProgram() {
        String vShaderStr = "attribute vec4 a_position;    \n"
                +"attribute vec2 a_texCoords; \n"
                +"varying vec2 v_texCoords; \n"
                + "void main()                  \n"
                + "{                            \n"
                + "   gl_Position = a_position;  \n"
                +"    v_texCoords = a_texCoords; \n"
                + "}                            \n";
        String fShaderStr = "precision mediump float;\n"
                +"uniform int image_step;\n"
                +"uniform float image_alpha;\n"
                +"uniform sampler2D a_Texture; \n"
                +"uniform sampler2D b_Texture; \n"
                +"uniform sampler2D c_Texture; \n"
                +"varying vec2 v_texCoords; \n"
                + "void main()\n"
                + "{\n"
                + "  vec4 color;\n"
                + "  if(image_step == 0){ \n"
                + "     color = texture2D(a_Texture, v_texCoords);\n"
                + "  } else if (image_step > 0 && image_step < 25) {\n"
                + "     vec4 a_color = texture2D(a_Texture, v_texCoords);\n"
                + "     vec4 b_color = texture2D(b_Texture, v_texCoords);\n"
                + "     color = a_color * (1.0 - image_alpha) + b_color * image_alpha;\n"
                + "  } else if (image_step == 25) {\n"
                + "     color = texture2D(b_Texture, v_texCoords);\n"
                + "  } else if (image_step > 25 && image_step < 50) {\n"
                + "     vec4 b_color = texture2D(b_Texture, v_texCoords);\n"
                + "     vec4 c_color = texture2D(c_Texture, v_texCoords);\n"
                + "     color = b_color * (1.0 - image_alpha) + c_color * image_alpha;\n"
                + "  } else if (image_step == 50) {\n"
                + "     color = texture2D(c_Texture, v_texCoords);\n"
                + "  } else if (image_step > 50 && image_step < 75) {\n"
                + "     vec4 b_color = texture2D(b_Texture, v_texCoords);\n"
                + "     vec4 c_color = texture2D(c_Texture, v_texCoords);\n"
                + "     color = b_color * image_alpha + c_color * (1.0 - image_alpha);\n"
                + "  } else if (image_step == 75) {\n"
                + "     color = texture2D(b_Texture, v_texCoords);\n"
                + "  } else if (image_step > 75) {\n"
                + "     vec4 a_color = texture2D(a_Texture, v_texCoords);\n"
                + "     vec4 b_color = texture2D(b_Texture, v_texCoords);\n"
                + "     color = a_color * image_alpha + b_color * (1.0 - image_alpha);\n"
                + "  }\n"
                + "  gl_FragColor = vec4(color.rgb, 1.0);\n"
                + "}\n";
        int vertexShader;
        int fragmentShader;
        int programObject;
        int[] linked = new int[1];
        // Load the vertex/fragment shaders
        vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vShaderStr);
        fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fShaderStr);
        // Create the program object
        programObject = GLES20.glCreateProgram();
        if (programObject == 0)
            return ;
        GLES20.glAttachShader(programObject, vertexShader);
        GLES20.glAttachShader(programObject, fragmentShader);
        // Bind vPosition to attribute 0
        GLES20.glBindAttribLocation(programObject, 0, "a_position");
        GLES20.glBindAttribLocation(programObject, 1, "a_texCoords");
        // Link the program
        GLES20.glLinkProgram(programObject);
        // Check the link status
        GLES20.glGetProgramiv(programObject, GLES20.GL_LINK_STATUS, linked, 0);
        if (linked[0] == 0) {
            GLES20.glDeleteProgram(programObject);
            return  ;
        }
        mProgramObject = programObject;
    }

    private int loadShader(int shaderType, String shaderSource) {
        int shader;
        int[] compiled = new int[1];
        // Create the shader object
        shader = GLES20.glCreateShader(shaderType);
        if (shader == 0)
            return 0;
        // Load the shader source
        GLES20.glShaderSource(shader, shaderSource);
        // Compile the shader
        GLES20.glCompileShader(shader);
        // Check the compile status
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            GLES20.glDeleteShader(shader);
            return 0;
        }
        return shader;
    }
}
