#version 150

varying vec2 uv;
uniform sampler2D tex;

void main() {
    gl_FragColor = texture(tex, uv * .5 + .5) * vec4(1.0,.5,.5,.5);
}