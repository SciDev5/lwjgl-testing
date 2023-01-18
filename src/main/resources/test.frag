#version 150

varying vec2 uv;
uniform vec2 center;
uniform float scale;
uniform float aspect;

uniform int fractalIndex;
uniform bool enableColor;

out vec4 color;

vec2 cMul(vec2 a, vec2 b) {
    return vec2(
    a.x * b.x - a.y * b.y,
    a.x * b.y + a.y * b.x
    );
}
vec2 cSquare(vec2 a) {
    return cMul(a,a);
}
vec2 cExp(vec2 x) {
    return vec2(
            cos(x.y), sin(x.y)
    ) * exp(x.x);
}
vec2 cDiv(vec2 a, vec2 b) {
    float denom = 1.0 / (b.x*b.x + b.y*b.y);
    return vec2(a.x*b.x + a.y*b.y, a.y*b.x - a.x*b.y) * denom;
}


void mandelbrot(inout vec2 z, vec2 c) {
    z = cMul(z, z) + c;
}
void thingy(inout vec2 z, vec2 c) {
    z = cMul(z, z + c) + cMul(z,c);
}
void burningShip(inout vec2 z, vec2 c) {
    z = cSquare(abs(z)) + c;
}
void shit(inout vec2 z, vec2 c) {
    z = cSquare(z) + abs(c-z);
}
void shit2(inout vec2 z, vec2 c) {
    z = cSquare(z)*z + abs(c-z) * cExp(z);
}
void sfx(inout vec2 z, vec2 c) {
    z = z * dot(z,z) - cMul(z, c*c);
}

void fractalStep(inout vec2 z, vec2 c) {
    switch (fractalIndex) {
        case 0: mandelbrot(z,c); break;
        case 1: burningShip(z,c); break;
        case 2: shit(z,c); break;
        case 3: thingy(z,c); break;
        case 4: shit2(z,c); break;
        case 5: sfx(z,c); break;
    }
}

const int MAX_ITER = 700;
const int COLOR_ITER = 30;

vec4 trace(vec2 z, vec2 c) {
    int lastI = -1;
    float angle = 0.0;
    vec2 z0 = z;
    vec2 z1 = z;
    vec3 color = vec3(0.0,0.0,0.0);
    float zeroFrac = 0.0;
    for (int i = 0; i < MAX_ITER; i++) {
        z0 = z;
        fractalStep(z,c);
        if (length(z) > 2.0) {
            lastI = i;
            break;
        }
        if (length(z-z0) < 0.01) {
            zeroFrac ++;
        }
    }
    zeroFrac /= MAX_ITER;
    if (enableColor) {
        for (int i = MAX_ITER; i < MAX_ITER+COLOR_ITER; i++) {
            z1 = z0; z0 = z;
            fractalStep(z, c);
            if (length(z) > 2.0) {
                break;
            }
            if (length(z - z0) > 0.0001 && length(z0 - z1) > 0.0001) {
                angle = acos(dot(normalize(z - z0), normalize(z0 - z1)) * .9999);
                color += vec3(
                cos(angle * 3.0) * .5 + .5,
                cos(angle * 3.0 + 1.0) * .5 + .5,
                cos(angle * 4.0 + 2.0) * .5 + .5
                );
                //            color += vec3(
                //            dot(z - z0, z0 - z1),
                //            dot(z - z0, z - z0),
                //            dot(z - z1, z - z1)
                //            );
            } else {
                color += vec3(.5, .5, .5);
            }
        }
    } else {
        zeroFrac = 0.0;
    }
    if (lastI == -1) {
//        if (length(z-z0) < 0.001) return vec4(.5,.5,.5,.5);
        return vec4(color / float(COLOR_ITER) * (1.0-zeroFrac) + vec3(.5,.5,.5) * zeroFrac, 1.0);
    } else {
        float k = float(lastI) * 0.01;
        return vec4(
        1.0 - exp(-k / .2) + sin(6*k)*.2-.2,
        0.8 - exp(-k / .9) + sin(4*k)*.2-.2,
        1.0 - exp(-k / .3),
        0.0
        );
    }
}


void main() {
    vec2 x = uv * vec2(1.0 / aspect, 1.0) * scale - center;
    color = trace(x, x);
}