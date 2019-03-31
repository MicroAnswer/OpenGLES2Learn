package cn.microanswer.opengles2learn.util;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import cn.microanswer.opengles2learn.view.MyTDView;

public class Triangle {
    // 4x4的投影矩阵
    public static float[] mProjMatrix = new float[16];
    // 摄像机位置朝向的参数矩阵
    public static float[] mVMatrix = new float[16];
    // 总变换矩阵
    public static float[] mMVPMatrix = new float[16];

    // 总变换矩阵引用。
    int muMVPMatrixHandle;
    // 顶点位置属性引用
    int maPositionHandle;
    // 顶点颜色属性引用。
    int maColorHandle;
    // 顶点着色器代码脚本。，
    String mVertexShader;
    // 片源着色器代码脚本
    String mFragmentShader;

    // 渲染管线着色器程序id。
    private int mProgram;

    // 具体物体的3d变换矩阵，包括旋转平移缩放
    static float[] mMMatrix = new float[16];

    // 顶点坐标数据缓冲。
    FloatBuffer mVertexBuffer;
    // 顶点着色器缓冲。
    FloatBuffer mColorBuffer;

    // 顶点数量
    int vcount = 0;
    // 绕X轴旋转的角度。
    public float xAngle = 0;

    public Triangle(MyTDView myTDView) {
        initVertexData();
        intShader(myTDView);
    }

    // 初始化顶点数据的方法
    private void initVertexData() {
        vcount = 3;
        final float UNIT_SIZE = 0.2f;
        // 顶点坐标数组。
        float vertices[] = new float[]{
                -4 * UNIT_SIZE, 0, 0,
                0, -4 * UNIT_SIZE, 0,
                4 * UNIT_SIZE, 0, 0
        };
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder()); // 设置字节顺序为本地操作系统顺序。
        mVertexBuffer = vbb.asFloatBuffer(); // 转换为浮点型缓冲
        mVertexBuffer.put(vertices); // 在缓冲区内写入数据
        mVertexBuffer.position(0); // 设置缓冲区起始位置

        // 顶点颜色数组。
        float colors[] = new float[]{
             // r  g  b  a
                1, 0, 0, 0,
                0, 0, 1, 0,
                0, 1, 0, 0
        };
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);
    }

    // 产生最终变换矩阵的方法。
    public static float[] getFinalMatrix(float[] spec) {
        mMVPMatrix = new float[16]; // 初始化总变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    // 创建并初始化着色器方法
    private void intShader(MyTDView mv) {
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.glsl", mv.getResources());
        mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.glsl", mv.getResources());
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);

        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    // 绘制三角形方法。
    public void drawSelf() {
        GLES20.glUseProgram(mProgram);

        // 初始化变换矩阵
        Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);
        // 设置沿着Z轴正向位移
        Matrix.translateM(mMMatrix, 0, 0, 0, 1);
        // 设置绕x轴旋转。
        Matrix.rotateM(mMMatrix, 0, xAngle, 1, 0, 0);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, Triangle.getFinalMatrix(mMMatrix), 0);
        // 将顶点位置数据传送给渲染管线。。
        GLES20.glVertexAttribPointer(maPositionHandle,3, GLES20.GL_FLOAT, false,3*4,mVertexBuffer);
        // 将顶点颜色数据传送给渲染管线。
        GLES20.glVertexAttribPointer(maColorHandle, 4, GLES20.GL_FLOAT, false, 4*4, mColorBuffer);
        // 启用顶点位置数据和颜色数据
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maColorHandle);

        // 执行绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vcount);
    }
}
