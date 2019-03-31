uniform mat4 uMVPMatrix; // 总变换矩阵
attribute vec3 aPosition; // 顶点位置
attribute vec4 aColor; // 顶点颜色
varying vec4 vColor; // 用于传递给片源着色器的易变变量
void main() {
    gl_Position = uMVPMatrix * vec4(aPosition, 1); // 根据总变换矩阵计算此次变换矩阵的位置。
    vColor = aColor;
}