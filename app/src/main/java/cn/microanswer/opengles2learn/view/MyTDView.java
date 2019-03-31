package cn.microanswer.opengles2learn.view;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.microanswer.opengles2learn.util.Triangle;

public class MyTDView extends GLSurfaceView {

    // 每次三角形旋转的角度。
    final float ANGLE_SPAN = 0.375f;

    public RotateThread rThread;
    SceneRenderer mRenderer;

    public MyTDView(Context context) {
        super(context);

        this.setEGLContextClientVersion(2);
        mRenderer = new SceneRenderer();
        this.setRenderer(mRenderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    }

    public class SceneRenderer implements GLSurfaceView.Renderer {
        Triangle tle;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0,0,0,1.0f);
            tle = new Triangle(MyTDView.this);
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            rThread = new RotateThread();
            rThread.start();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
            float ratio = (float) width / height;
            // 设置透视投影
            Matrix.frustumM(Triangle.mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
            // 设置摄像机
            Matrix.setLookAtM(Triangle.mVMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            tle.drawSelf();
        }
    }


    public class RotateThread extends Thread {
        public boolean flag = true;
        @Override
        public void run() {
            super.run();
            while (flag) {
                mRenderer.tle.xAngle += ANGLE_SPAN;
                SystemClock.sleep(20);
            }
        }
    }
}
