package cn.microanswer.opengles2learn.util;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;

public class ShaderUtil {
    private static final String CHAR_SET = "UTF-8";

    // 加载指定着色器的方法。
    public static int loadShader(int shaderType, String source) {

        // 创建一个 shader 并保存其id
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0) {

            // 加载着色器的源代码
            GLES20.glShaderSource(shader, source);

            // 编译
            GLES20.glCompileShader(shader);

            // 获取 编辑情况。
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (0 == compiled[0]) {
                // 如果编译失败则显示错误日志然后删除此shader。
                Log.e("EL20_ERROR", "Could not compile shader " + shaderType + ":");
                Log.e("EL20_ERROR", GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }

        return shader;
    }

    // 创建着色器程序的方法。
    public static int createProgram(String vertexSource, String fragmentSource) {

        // 加载顶点着色器。
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (0 == vertexShader) return 0;

        // 加载片元着色器。
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (0 == pixelShader) return 0;

        // 创建程序。
        int program = GLES20.glCreateProgram();
        if (0 != program) {
            // 向程序中加入顶点着色器。
            GLES20.glAttachShader(program, vertexShader); checkGlError("glAttachShader");
            // 向程序中加入片源着色器。
            GLES20.glAttachShader(program, pixelShader); checkGlError("glAttachShader");
            // 链接程序
            GLES20.glLinkProgram(program); checkGlError("glLinkProgram");

            // 获取链接情况
            int []linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);

            if (GLES20.GL_TRUE != linkStatus[0]) {
                // 若链接时报则报错并删除程序。
                Log.e("EL20_ERROR", "Could not link program:");
                Log.e("EL20_ERROR", GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }

        }

        return program;
    }

    // 检查每一步操作是否有错误的方法。
    public static void checkGlError(String op) {
        int error;
        /*while*/
        if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            String errMsg = op + ": glError " + error;
            Log.e("EL20_ERROR", errMsg);
            throw new RuntimeException(errMsg);
        }
    }

    // 从sh脚本中加载着色器内容的方法。
    public static String loadFromAssetsFile(String fname, Resources r) {
        InputStreamReader inputStreamReader = null;
        try {
            InputStream in = r.getAssets().open(fname);
            inputStreamReader = new InputStreamReader(in, CHAR_SET);
            StringBuilder sbu = new StringBuilder();
            char[] chars = new char[128];
            int size;

            while ((size = inputStreamReader.read(chars)) != -1) {
                sbu.append(chars, 0, size);
            }

            return sbu.toString().replace("\r\n", "\n");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStreamReader != null)
                try {
                    inputStreamReader.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
        }
        return null;
    }
}
