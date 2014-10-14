package penghuangcom.gles2demo;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by penghuang on 10/10/14.
 */
public class GLES20Renderer implements GLSurfaceView.Renderer {

    private static final String TAG = "GLES20Renderer";
    private final String kVertexShader = new StringBuilder()
            .append("attribute vec4 aPosition;  \n")
            .append("attribute vec4 aColor;     \n")
            .append("varying vec4 vColor;       \n")
            .append("void main() {              \n")
            .append("  gl_Position = aPosition; \n")
            .append("  vColor = aColor;         \n")
            .append("}\n")
            .toString();

    private final String kFragmentShader = new StringBuilder()
            .append("varying vec4 vColor;      \n")
            .append("void main() {             \n")
            .append("  gl_FragColor = vColor;  \n")
            .append("}\n")
            .toString();

    private final float kVertex[] = {
            -1f, -1f, -1f, 1f, 1f, -1f, 1f, 1f,
    };

    private final float kColor[] = {
            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f,
            1f, 1f, 1f, 1f,
    };

    private int mWidth;
    private int mHeight;
    private int mProgram;
    private FloatBuffer mBufferVertex;
    private FloatBuffer mBufferColor;

    public GLES20Renderer() {
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        mWidth = -1;
        mHeight = -1;
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        mWidth = width;
        mHeight = height;
        GLES20.glClearColor(0f, 0f, 0f, 1f);
        initGL();
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(
                GLES20.GL_COLOR_BUFFER_BIT |
                        GLES20.GL_DEPTH_BUFFER_BIT);
        drawFrame();
    }

    void initGL() {
        mProgram = GLES20.glCreateProgram();
        int vShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vShader, kVertexShader);
        GLES20.glCompileShader(vShader);
        GLES20.glAttachShader(mProgram, vShader);
        GLES20.glDeleteShader(vShader);

        int fShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fShader, kFragmentShader);
        GLES20.glCompileShader(fShader);
        GLES20.glAttachShader(mProgram, fShader);
        GLES20.glDeleteShader(fShader);
        GLES20.glLinkProgram(mProgram);

        mBufferVertex = ByteBuffer.allocateDirect(4 * kVertex.length)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mBufferVertex.put(kVertex).position(0);

        mBufferColor = ByteBuffer.allocateDirect(4 * kColor.length)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mBufferColor.put(kColor).position(0);

/*
        int[] buffers = new int[1];
        GLES20.glGenBuffers(1, buffers, 0);
        mBuffer = buffers[0];
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mBuffer);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, kData.length * 4, FloatBuffer.wrap(kData), GLES20.GL_DYNAMIC_DRAW);
*/
    }

    void drawFrame() {
        GLES20.glUseProgram(mProgram);

        int position = GLES20.glGetAttribLocation(mProgram, "aPosition");
        GLES20.glEnableVertexAttribArray(position);
        GLES20.glVertexAttribPointer(position, 2, GLES20.GL_FLOAT, false, 0, mBufferVertex);

        int color = GLES20.glGetAttribLocation(mProgram, "aColor");
        GLES20.glEnableVertexAttribArray(color);
        GLES20.glVertexAttribPointer(color, 4, GLES20.GL_FLOAT, false, 0, mBufferColor);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
