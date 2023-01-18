#version 150

attribute vec2 pos;

uniform vec2 center;
uniform float scale;
uniform float aspect;

void main() {

    vec2 x = (pos + center) * vec2(1.0,-1.0) * vec2(1.0 * aspect, 1.0) / scale;

    gl_Position = vec4(x, 0.0, 1.0);
}