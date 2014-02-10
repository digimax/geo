package com.digimax.geo.components;

/**
 * Created by jon on 2/9/2014.
 */

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import java.util.Locale;

public class Globe {
    @Inject
    private AssetSource assetSource;

    @Inject
    private JavaScriptSupport javascriptRenderSupport;

    @Inject
    private Locale locale;

    @SetupRender
    private void setupRender() {
//        javascriptRenderSupport.importJavaScriptLibrary("///learningwebgl.com/lessons/lesson15/glMatrix-0.9.5.min.js");
//        javascriptRenderSupport.importJavaScriptLibrary("///learningwebgl.com/lessons/lesson15/webgl-utils.js");
    }

    public String getFsScript() {
        return "        <script id=\"per-fragment-lighting-fs\" type=\"x-shader/x-fragment\">\n" +
                "\n" +
                "            precision mediump float;\n" +
                "\n" +
                "            varying vec2 vTextureCoord;\n" +
                "            varying vec3 vTransformedNormal;\n" +
                "            varying vec4 vPosition;\n" +
                "\n" +
                "            uniform bool uUseColorMap;\n" +
                "            uniform bool uUseSpecularMap;\n" +
                "            uniform bool uUseLighting;\n" +
                "\n" +
                "            uniform vec3 uAmbientColor;\n" +
                "\n" +
                "            uniform vec3 uPointLightingLocation;\n" +
                "            uniform vec3 uPointLightingSpecularColor;\n" +
                "            uniform vec3 uPointLightingDiffuseColor;\n" +
                "\n" +
                "            uniform sampler2D uColorMapSampler;\n" +
                "            uniform sampler2D uSpecularMapSampler;\n" +
                "\n" +
                "\n" +
                "            void main(void) {\n" +
                "                vec3 lightWeighting;\n" +
                "                if (!uUseLighting) {\n" +
                "                    lightWeighting = vec3(1.0, 1.0, 1.0);\n" +
                "                } else {\n" +
                "                    vec3 lightDirection = normalize(uPointLightingLocation - vPosition.xyz);\n" +
                "                    vec3 normal = normalize(vTransformedNormal);\n" +
                "\n" +
                "                    float specularLightWeighting = 0.0;\n" +
                "                    float shininess = 32.0;\n" +
                "                    if (uUseSpecularMap) {\n" +
                "                        shininess = texture2D(uSpecularMapSampler, vec2(vTextureCoord.s, vTextureCoord.t)).r * 255.0;\n" +
                "                    }\n" +
                "                    if (shininess < 255.0) {\n" +
                "                        vec3 eyeDirection = normalize(-vPosition.xyz);\n" +
                "                        vec3 reflectionDirection = reflect(-lightDirection, normal);\n" +
                "\n" +
                "                        specularLightWeighting = pow(max(dot(reflectionDirection, eyeDirection), 0.0), shininess);\n" +
                "                    }\n" +
                "\n" +
                "                    float diffuseLightWeighting = max(dot(normal, lightDirection), 0.0);\n" +
                "                    lightWeighting = uAmbientColor\n" +
                "                            + uPointLightingSpecularColor * specularLightWeighting\n" +
                "                            + uPointLightingDiffuseColor * diffuseLightWeighting;\n" +
                "                }\n" +
                "\n" +
                "                vec4 fragmentColor;\n" +
                "                if (uUseColorMap) {\n" +
                "                    fragmentColor = texture2D(uColorMapSampler, vec2(vTextureCoord.s, vTextureCoord.t));\n" +
                "                } else {\n" +
                "                    fragmentColor = vec4(1.0, 1.0, 1.0, 1.0);\n" +
                "                }\n" +
                "                gl_FragColor = vec4(fragmentColor.rgb * lightWeighting, fragmentColor.a);\n" +
                "            }\n" +
                "\n" +
                "        </script>";
    }

    public String getVsScript() {
        return "        <script id=\"per-fragment-lighting-vs\" type=\"x-shader/x-vertex\">\n" +
                "            attribute vec3 aVertexPosition;\n" +
                "            attribute vec3 aVertexNormal;\n" +
                "            attribute vec2 aTextureCoord;\n" +
                "\n" +
                "            uniform mat4 uMVMatrix;\n" +
                "            uniform mat4 uPMatrix;\n" +
                "            uniform mat3 uNMatrix;\n" +
                "\n" +
                "            varying vec2 vTextureCoord;\n" +
                "            varying vec3 vTransformedNormal;\n" +
                "            varying vec4 vPosition;\n" +
                "\n" +
                "\n" +
                "            void main(void) {\n" +
                "                vPosition = uMVMatrix * vec4(aVertexPosition, 1.0);\n" +
                "                gl_Position = uPMatrix * vPosition;\n" +
                "                vTextureCoord = aTextureCoord;\n" +
                "                vTransformedNormal = uNMatrix * aVertexNormal;\n" +
                "            }\n" +
                "        </script>";
    }



    public String getMapUrl() {
        String mapUrl = assetSource.getContextAsset("img/earth.jpg", locale).toClientURL();
        return mapUrl;
    }

    public String getSpecularUrl() {
        String specularUrl = assetSource.getContextAsset("img/earth-specular.gif", locale).toClientURL();
        return specularUrl;
    }
}
