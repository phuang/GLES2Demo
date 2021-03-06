package penghuangcom.gles2demo;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

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

    private final int kOne = 0x10000;
    private final int kVertex[] = {
            -kOne, -kOne, -kOne, kOne, kOne, -kOne, kOne, kOne
    };

    private final int kColor[] = {
            kOne, 0, 0, kOne,
            0, kOne, 0, kOne,
            0, 0, kOne, kOne,
            kOne, kOne, kOne, kOne,
    };

    private int mWidth;
    private int mHeight;
    private int mProgram;
    private IntBuffer mBufferVertex;
    private IntBuffer mBufferColor;

    private int mFrameCount = 0;

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
        for (int i = 0; i < 1; i++) {
            GLES20.glClear(
                    GLES20.GL_COLOR_BUFFER_BIT |
                            GLES20.GL_DEPTH_BUFFER_BIT);
            drawFrame();
        }
        mFrameCount++;
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
                .asIntBuffer();
        mBufferVertex.put(kVertex).position(0);

        mBufferColor = ByteBuffer.allocateDirect(4 * kColor.length)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer();
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
        GLES20.glVertexAttribPointer(position, 2, GLES20.GL_FIXED, false, 0, mBufferVertex);

        int color = GLES20.glGetAttribLocation(mProgram, "aColor");
        GLES20.glEnableVertexAttribArray(color);
        GLES20.glVertexAttribPointer(color, 4, GLES20.GL_FIXED, false, 0, mBufferColor);

/*
        final double factor = Math.abs(Math.sin((Math.PI / 90.0) * mFrameCount)) * 0.5 + 0.5;
        final float[] matrix = {
                (float) factor, 0, 0, 0,
                0, (float) factor, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };
        GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(mProgram, "uFactor"), 1, false,
                matrix, 0);
*/



        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
