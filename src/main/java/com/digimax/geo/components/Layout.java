package com.digimax.geo.components;

import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.SymbolConstants;

/**
 * Layout component for pages of application geo.
 */
@Import(stylesheet = "context:layout/layout.css")
public class Layout
{
    /**
     * The page title, for the <title> element and the <h1> element.
     */
    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String title;

    @Property
    private String pageName;

    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String sidebarTitle;

    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private Block sidebar;

    @Inject
    private ComponentResources resources;

    @Property
    @Inject
    @Symbol(SymbolConstants.APPLICATION_VERSION)
    private String appVersion;


    public String getClassForPageName()
    {
        return resources.getPageName().equalsIgnoreCase(pageName)
                ? "current_page_item"
                : null;
    }

    public String[] getPageNames()
    {
        return new String[]{"Index", "About", "Contact"};
    }

    public String getGlobeScript() {
        return "        <script id=\"per-fragment-lighting-fs\" type=\"x-shader/x-fragment\">\n" +
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
                "        </script>\n" +
                "\n" +
                "        <script id=\"per-fragment-lighting-vs\" type=\"x-shader/x-vertex\">\n" +
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
                "        </script>\n" +
                "\n" +
                "\n" +
                "        <script type=\"text/javascript\">\n" +
                "\n" +
                "        var gl;\n" +
                "\n" +
                "        function initGL(canvas) {\n" +
                "            try {\n" +
                "                gl = canvas.getContext(\"experimental-webgl\");\n" +
                "                gl.viewportWidth = canvas.width;\n" +
                "                gl.viewportHeight = canvas.height;\n" +
                "            } catch (e) {\n" +
                "            }\n" +
                "            if (!gl) {\n" +
                "                alert(\"Could not initialise WebGL, sorry :-(\");\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "        function getShader(gl, id) {\n" +
                "            var shaderScript = document.getElementById(id);\n" +
                "            if (!shaderScript) {\n" +
                "                return null;\n" +
                "            }\n" +
                "\n" +
                "            var str = \"\";\n" +
                "            var k = shaderScript.firstChild;\n" +
                "            while (k) {\n" +
                "                if (k.nodeType == 3) {\n" +
                "                    str += k.textContent;\n" +
                "                }\n" +
                "                k = k.nextSibling;\n" +
                "            }\n" +
                "\n" +
                "            var shader;\n" +
                "            if (shaderScript.type == \"x-shader/x-fragment\") {\n" +
                "                shader = gl.createShader(gl.FRAGMENT_SHADER);\n" +
                "            } else if (shaderScript.type == \"x-shader/x-vertex\") {\n" +
                "                shader = gl.createShader(gl.VERTEX_SHADER);\n" +
                "            } else {\n" +
                "                return null;\n" +
                "            }\n" +
                "\n" +
                "            gl.shaderSource(shader, str);\n" +
                "            gl.compileShader(shader);\n" +
                "\n" +
                "            if (!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {\n" +
                "                alert(gl.getShaderInfoLog(shader));\n" +
                "                return null;\n" +
                "            }\n" +
                "\n" +
                "            return shader;\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "        var shaderProgram;\n" +
                "\n" +
                "        function initShaders() {\n" +
                "            var fragmentShader = getShader(gl, \"per-fragment-lighting-fs\");\n" +
                "            var vertexShader = getShader(gl, \"per-fragment-lighting-vs\");\n" +
                "\n" +
                "            shaderProgram = gl.createProgram();\n" +
                "            gl.attachShader(shaderProgram, vertexShader);\n" +
                "            gl.attachShader(shaderProgram, fragmentShader);\n" +
                "            gl.linkProgram(shaderProgram);\n" +
                "\n" +
                "            if (!gl.getProgramParameter(shaderProgram, gl.LINK_STATUS)) {\n" +
                "                alert(\"Could not initialise shaders\");\n" +
                "            }\n" +
                "\n" +
                "            gl.useProgram(shaderProgram);\n" +
                "\n" +
                "            shaderProgram.vertexPositionAttribute = gl.getAttribLocation(shaderProgram, \"aVertexPosition\");\n" +
                "            gl.enableVertexAttribArray(shaderProgram.vertexPositionAttribute);\n" +
                "\n" +
                "            shaderProgram.vertexNormalAttribute = gl.getAttribLocation(shaderProgram, \"aVertexNormal\");\n" +
                "            gl.enableVertexAttribArray(shaderProgram.vertexNormalAttribute);\n" +
                "\n" +
                "            shaderProgram.textureCoordAttribute = gl.getAttribLocation(shaderProgram, \"aTextureCoord\");\n" +
                "            gl.enableVertexAttribArray(shaderProgram.textureCoordAttribute);\n" +
                "\n" +
                "            shaderProgram.pMatrixUniform = gl.getUniformLocation(shaderProgram, \"uPMatrix\");\n" +
                "            shaderProgram.mvMatrixUniform = gl.getUniformLocation(shaderProgram, \"uMVMatrix\");\n" +
                "            shaderProgram.nMatrixUniform = gl.getUniformLocation(shaderProgram, \"uNMatrix\");\n" +
                "            shaderProgram.colorMapSamplerUniform = gl.getUniformLocation(shaderProgram, \"uColorMapSampler\");\n" +
                "            shaderProgram.specularMapSamplerUniform = gl.getUniformLocation(shaderProgram, \"uSpecularMapSampler\");\n" +
                "            shaderProgram.useColorMapUniform = gl.getUniformLocation(shaderProgram, \"uUseColorMap\");\n" +
                "            shaderProgram.useSpecularMapUniform = gl.getUniformLocation(shaderProgram, \"uUseSpecularMap\");\n" +
                "            shaderProgram.useLightingUniform = gl.getUniformLocation(shaderProgram, \"uUseLighting\");\n" +
                "            shaderProgram.ambientColorUniform = gl.getUniformLocation(shaderProgram, \"uAmbientColor\");\n" +
                "            shaderProgram.pointLightingLocationUniform = gl.getUniformLocation(shaderProgram, \"uPointLightingLocation\");\n" +
                "            shaderProgram.pointLightingSpecularColorUniform = gl.getUniformLocation(shaderProgram, \"uPointLightingSpecularColor\");\n" +
                "            shaderProgram.pointLightingDiffuseColorUniform = gl.getUniformLocation(shaderProgram, \"uPointLightingDiffuseColor\");\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "        function handleLoadedTexture(texture) {\n" +
                "            gl.pixelStorei(gl.UNPACK_FLIP_Y_WEBGL, true);\n" +
                "            gl.bindTexture(gl.TEXTURE_2D, texture);\n" +
                "            gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, texture.image);\n" +
                "            gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR);\n" +
                "            gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR_MIPMAP_NEAREST);\n" +
                "            gl.generateMipmap(gl.TEXTURE_2D);\n" +
                "\n" +
                "            gl.bindTexture(gl.TEXTURE_2D, null);\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "        var earthColorMapTexture;\n" +
                "        var earthSpecularMapTexture;\n" +
                "\n" +
                "        function initTextures() {\n" +
                "            earthColorMapTexture = gl.createTexture();\n" +
                "            earthColorMapTexture.image = new Image();\n" +
                "            earthColorMapTexture.image.onload = function () {\n" +
                "                handleLoadedTexture(earthColorMapTexture)\n" +
                "            }\n" +
                "            earthColorMapTexture.image.src = \"/img/earth.jpg\";\n" +
                "\n" +
                "            earthSpecularMapTexture = gl.createTexture();\n" +
                "            earthSpecularMapTexture.image = new Image();\n" +
                "            earthSpecularMapTexture.image.onload = function () {\n" +
                "                handleLoadedTexture(earthSpecularMapTexture)\n" +
                "            }\n" +
                "            earthSpecularMapTexture.image.src = \"/img/earth-specular.gif\";\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "\n" +
                "        var mvMatrix = mat4.create();\n" +
                "        var mvMatrixStack = [];\n" +
                "        var pMatrix = mat4.create();\n" +
                "\n" +
                "        function mvPushMatrix() {\n" +
                "            var copy = mat4.create();\n" +
                "            mat4.set(mvMatrix, copy);\n" +
                "            mvMatrixStack.push(copy);\n" +
                "        }\n" +
                "\n" +
                "        function mvPopMatrix() {\n" +
                "            if (mvMatrixStack.length == 0) {\n" +
                "                throw \"Invalid popMatrix!\";\n" +
                "            }\n" +
                "            mvMatrix = mvMatrixStack.pop();\n" +
                "        }\n" +
                "\n" +
                "        function setMatrixUniforms() {\n" +
                "            gl.uniformMatrix4fv(shaderProgram.pMatrixUniform, false, pMatrix);\n" +
                "            gl.uniformMatrix4fv(shaderProgram.mvMatrixUniform, false, mvMatrix);\n" +
                "\n" +
                "            var normalMatrix = mat3.create();\n" +
                "            mat4.toInverseMat3(mvMatrix, normalMatrix);\n" +
                "            mat3.transpose(normalMatrix);\n" +
                "            gl.uniformMatrix3fv(shaderProgram.nMatrixUniform, false, normalMatrix);\n" +
                "        }\n" +
                "\n" +
                "        function degToRad(degrees) {\n" +
                "            return degrees * Math.PI / 180;\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "        var sphereVertexNormalBuffer;\n" +
                "        var sphereVertexTextureCoordBuffer;\n" +
                "        var sphereVertexPositionBuffer;\n" +
                "        var sphereVertexIndexBuffer;\n" +
                "\n" +
                "        function initBuffers() {\n" +
                "            var latitudeBands = 30;\n" +
                "            var longitudeBands = 30;\n" +
                "            var radius = 13;\n" +
                "\n" +
                "            var vertexPositionData = [];\n" +
                "            var normalData = [];\n" +
                "            var textureCoordData = [];\n" +
                "            for (var latNumber=0; latNumber <= latitudeBands; latNumber++) {\n" +
                "                var theta = latNumber * Math.PI / latitudeBands;\n" +
                "                var sinTheta = Math.sin(theta);\n" +
                "                var cosTheta = Math.cos(theta);\n" +
                "\n" +
                "                for (var longNumber=0; longNumber <= longitudeBands; longNumber++) {\n" +
                "                    var phi = longNumber * 2 * Math.PI / longitudeBands;\n" +
                "                    var sinPhi = Math.sin(phi);\n" +
                "                    var cosPhi = Math.cos(phi);\n" +
                "\n" +
                "                    var x = cosPhi * sinTheta;\n" +
                "                    var y = cosTheta;\n" +
                "                    var z = sinPhi * sinTheta;\n" +
                "                    var u = 1 - (longNumber / longitudeBands);\n" +
                "                    var v = 1 - (latNumber / latitudeBands);\n" +
                "\n" +
                "                    normalData.push(x);\n" +
                "                    normalData.push(y);\n" +
                "                    normalData.push(z);\n" +
                "                    textureCoordData.push(u);\n" +
                "                    textureCoordData.push(v);\n" +
                "                    vertexPositionData.push(radius * x);\n" +
                "                    vertexPositionData.push(radius * y);\n" +
                "                    vertexPositionData.push(radius * z);\n" +
                "                }\n" +
                "            }\n" +
                "\n" +
                "            var indexData = [];\n" +
                "            for (var latNumber=0; latNumber < latitudeBands; latNumber++) {\n" +
                "                for (var longNumber=0; longNumber < longitudeBands; longNumber++) {\n" +
                "                    var first = (latNumber * (longitudeBands + 1)) + longNumber;\n" +
                "                    var second = first + longitudeBands + 1;\n" +
                "                    indexData.push(first);\n" +
                "                    indexData.push(second);\n" +
                "                    indexData.push(first + 1);\n" +
                "\n" +
                "                    indexData.push(second);\n" +
                "                    indexData.push(second + 1);\n" +
                "                    indexData.push(first + 1);\n" +
                "                }\n" +
                "            }\n" +
                "\n" +
                "            sphereVertexNormalBuffer = gl.createBuffer();\n" +
                "            gl.bindBuffer(gl.ARRAY_BUFFER, sphereVertexNormalBuffer);\n" +
                "            gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(normalData), gl.STATIC_DRAW);\n" +
                "            sphereVertexNormalBuffer.itemSize = 3;\n" +
                "            sphereVertexNormalBuffer.numItems = normalData.length / 3;\n" +
                "\n" +
                "            sphereVertexTextureCoordBuffer = gl.createBuffer();\n" +
                "            gl.bindBuffer(gl.ARRAY_BUFFER, sphereVertexTextureCoordBuffer);\n" +
                "            gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(textureCoordData), gl.STATIC_DRAW);\n" +
                "            sphereVertexTextureCoordBuffer.itemSize = 2;\n" +
                "            sphereVertexTextureCoordBuffer.numItems = textureCoordData.length / 2;\n" +
                "\n" +
                "            sphereVertexPositionBuffer = gl.createBuffer();\n" +
                "            gl.bindBuffer(gl.ARRAY_BUFFER, sphereVertexPositionBuffer);\n" +
                "            gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertexPositionData), gl.STATIC_DRAW);\n" +
                "            sphereVertexPositionBuffer.itemSize = 3;\n" +
                "            sphereVertexPositionBuffer.numItems = vertexPositionData.length / 3;\n" +
                "\n" +
                "            sphereVertexIndexBuffer = gl.createBuffer();\n" +
                "            gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, sphereVertexIndexBuffer);\n" +
                "            gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(indexData), gl.STREAM_DRAW);\n" +
                "            sphereVertexIndexBuffer.itemSize = 1;\n" +
                "            sphereVertexIndexBuffer.numItems = indexData.length;\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "        var earthAngle = 180;\n" +
                "\n" +
                "        function drawScene() {\n" +
                "            gl.viewport(0, 0, gl.viewportWidth, gl.viewportHeight);\n" +
                "            gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);\n" +
                "\n" +
                "            mat4.perspective(45, gl.viewportWidth / gl.viewportHeight, 0.1, 100.0, pMatrix);\n" +
                "\n" +
                "            var useColorMap = document.getElementById(\"color-map\").checked;\n" +
                "            gl.uniform1i(shaderProgram.useColorMapUniform, useColorMap);\n" +
                "\n" +
                "            var useSpecularMap = document.getElementById(\"specular-map\").checked;\n" +
                "            gl.uniform1i(shaderProgram.useSpecularMapUniform, useSpecularMap);\n" +
                "\n" +
                "            var lighting = document.getElementById(\"lighting\").checked;\n" +
                "            gl.uniform1i(shaderProgram.useLightingUniform, lighting);\n" +
                "            if (lighting) {\n" +
                "                gl.uniform3f(\n" +
                "                        shaderProgram.ambientColorUniform,\n" +
                "                        parseFloat(document.getElementById(\"ambientR\").value),\n" +
                "                        parseFloat(document.getElementById(\"ambientG\").value),\n" +
                "                        parseFloat(document.getElementById(\"ambientB\").value)\n" +
                "                );\n" +
                "\n" +
                "                gl.uniform3f(\n" +
                "                        shaderProgram.pointLightingLocationUniform,\n" +
                "                        parseFloat(document.getElementById(\"lightPositionX\").value),\n" +
                "                        parseFloat(document.getElementById(\"lightPositionY\").value),\n" +
                "                        parseFloat(document.getElementById(\"lightPositionZ\").value)\n" +
                "                );\n" +
                "\n" +
                "                gl.uniform3f(\n" +
                "                        shaderProgram.pointLightingSpecularColorUniform,\n" +
                "                        parseFloat(document.getElementById(\"specularR\").value),\n" +
                "                        parseFloat(document.getElementById(\"specularG\").value),\n" +
                "                        parseFloat(document.getElementById(\"specularB\").value)\n" +
                "                );\n" +
                "\n" +
                "                gl.uniform3f(\n" +
                "                        shaderProgram.pointLightingDiffuseColorUniform,\n" +
                "                        parseFloat(document.getElementById(\"diffuseR\").value),\n" +
                "                        parseFloat(document.getElementById(\"diffuseG\").value),\n" +
                "                        parseFloat(document.getElementById(\"diffuseB\").value)\n" +
                "                );\n" +
                "            }\n" +
                "\n" +
                "            mat4.identity(mvMatrix);\n" +
                "\n" +
                "            mat4.translate(mvMatrix, [0, 0, -40]);\n" +
                "            mat4.rotate(mvMatrix, degToRad(23.4), [1, 0, -1]);\n" +
                "            mat4.rotate(mvMatrix, degToRad(earthAngle), [0, 1, 0]);\n" +
                "\n" +
                "            gl.activeTexture(gl.TEXTURE0);\n" +
                "            gl.bindTexture(gl.TEXTURE_2D, earthColorMapTexture);\n" +
                "            gl.uniform1i(shaderProgram.colorMapSamplerUniform, 0);\n" +
                "\n" +
                "            gl.activeTexture(gl.TEXTURE1);\n" +
                "            gl.bindTexture(gl.TEXTURE_2D, earthSpecularMapTexture);\n" +
                "            gl.uniform1i(shaderProgram.specularMapSamplerUniform, 1);\n" +
                "\n" +
                "            gl.bindBuffer(gl.ARRAY_BUFFER, sphereVertexPositionBuffer);\n" +
                "            gl.vertexAttribPointer(shaderProgram.vertexPositionAttribute, sphereVertexPositionBuffer.itemSize, gl.FLOAT, false, 0, 0);\n" +
                "\n" +
                "            gl.bindBuffer(gl.ARRAY_BUFFER, sphereVertexTextureCoordBuffer);\n" +
                "            gl.vertexAttribPointer(shaderProgram.textureCoordAttribute, sphereVertexTextureCoordBuffer.itemSize, gl.FLOAT, false, 0, 0);\n" +
                "\n" +
                "            gl.bindBuffer(gl.ARRAY_BUFFER, sphereVertexNormalBuffer);\n" +
                "            gl.vertexAttribPointer(shaderProgram.vertexNormalAttribute, sphereVertexNormalBuffer.itemSize, gl.FLOAT, false, 0, 0);\n" +
                "\n" +
                "            gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, sphereVertexIndexBuffer);\n" +
                "            setMatrixUniforms();\n" +
                "            gl.drawElements(gl.TRIANGLES, sphereVertexIndexBuffer.numItems, gl.UNSIGNED_SHORT, 0);\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "        var lastTime = 0;\n" +
                "\n" +
                "        function animate() {\n" +
                "            var timeNow = new Date().getTime();\n" +
                "            if (lastTime != 0) {\n" +
                "                var elapsed = timeNow - lastTime;\n" +
                "\n" +
                "                earthAngle += 0.05 * elapsed;\n" +
                "            }\n" +
                "            lastTime = timeNow;\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "\n" +
                "        function tick() {\n" +
                "            requestAnimFrame(tick);\n" +
                "            drawScene();\n" +
                "            animate();\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "        function webGLStart() {\n" +
                "            var canvas = document.getElementById(\"lesson15-canvas\");\n" +
                "            initGL(canvas);\n" +
                "            initShaders();\n" +
                "            initBuffers();\n" +
                "            initTextures();\n" +
                "\n" +
                "            gl.clearColor(0.0, 0.0, 0.0, 1.0);\n" +
                "            gl.enable(gl.DEPTH_TEST);\n" +
                "\n" +
                "            tick();\n" +
                "        }\n" +
                "\n" +
                "        </script>\n";
    }
}
