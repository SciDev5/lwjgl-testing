#version 150

attribute vec2 pos;
varying vec2 uv;
void main() {
    uv = pos * vec2(1.0,-1.0);
    gl_Position = vec4(pos,0.0,1.0);
}