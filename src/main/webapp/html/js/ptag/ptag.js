	var xhr = new XMLHttpRequest();
    xhr.open("POST", "http://pacl.pchome.com.tw/api/collect2", true);
    xhr.withCredentials = true;
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {}
    };
    xhr.send();
    
    
	(function(name, context, definition) {
        'use strict';
        if (typeof window.define === 'function' && window.define.amd) {
            window.define(definition)
        } else if (typeof module !== 'undefined' && module.exports) {
            module.exports = definition()
        } else if (context.exports) {
            context.exports = definition()
        } else {
            context[name] = definition()
        }
    })('PCHOMEFingerprint', this, function() {
        'use strict';
        var PCHOMEFingerprint = function(options) {
            if (!(this instanceof PCHOMEFingerprint)) {
                return new PCHOMEFingerprint(options)
            }
            var defaultOptions = {
                swfContainerId: 'PCHOMEFingerprint',
                swfPath: 'flash/compiled/FontList.swf',
                detectScreenOrientation: true,
                sortPluginsFor: [/palemoon/i],
                userDefinedFonts: []
            };
            this.options = this.extend(options, defaultOptions);
            this.nativeForEach = Array.prototype.forEach;
            this.nativeMap = Array.prototype.map
        };
        
        
        
        PCHOMEFingerprint.prototype = {
            extend: function(source, target) {
                if (source == null) {
                    return target
                }
                for (var k in source) {
                    if (source[k] != null && target[k] !== source[k]) {
                        target[k] = source[k]
                    }
                }
                return target
            },
            getWebglFp: function() {
                var gl;
                var fa2s = function(fa) {
                    gl.clearColor(0.0, 0.0, 0.0, 1.0);
                    gl.enable(gl.DEPTH_TEST);
                    gl.depthFunc(gl.LEQUAL);
                    gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
                    return '[' + fa[0] + ', ' + fa[1] + ']'
                };
                var maxAnisotropy = function(gl) {
                    var ext = gl.getExtension('EXT_texture_filter_anisotropic') || gl.getExtension('WEBKIT_EXT_texture_filter_anisotropic') || gl.getExtension('MOZ_EXT_texture_filter_anisotropic');
                    if (ext) {
                        var anisotropy = gl.getParameter(ext.MAX_TEXTURE_MAX_ANISOTROPY_EXT);
                        if (anisotropy === 0) {
                            anisotropy = 2
                        }
                        return anisotropy
                    } else {
                        return null
                    }
                };
                gl = this.getWebglCanvas();
                if (!gl) {
                    return null
                }
                var result = [];
                var vShaderTemplate = 'attribute vec2 attrVertex;varying vec2 varyinTexCoordinate;uniform vec2 uniformOffset;void main(){varyinTexCoordinate=attrVertex+uniformOffset;gl_Position=vec4(attrVertex,0,1);}';
                var fShaderTemplate = 'precision mediump float;varying vec2 varyinTexCoordinate;void main() {gl_FragColor=vec4(varyinTexCoordinate,0,1);}';
                var vertexPosBuffer = gl.createBuffer();
                gl.bindBuffer(gl.ARRAY_BUFFER, vertexPosBuffer);
                var vertices = new Float32Array([-0.2, -0.9, 0, 0.4, -0.26, 0, 0, 0.732134444, 0]);
                gl.bufferData(gl.ARRAY_BUFFER, vertices, gl.STATIC_DRAW);
                vertexPosBuffer.itemSize = 3;
                vertexPosBuffer.numItems = 3;
                var program = gl.createProgram();
                var vshader = gl.createShader(gl.VERTEX_SHADER);
                gl.shaderSource(vshader, vShaderTemplate);
                gl.compileShader(vshader);
                var fshader = gl.createShader(gl.FRAGMENT_SHADER);
                gl.shaderSource(fshader, fShaderTemplate);
                gl.compileShader(fshader);
                gl.attachShader(program, vshader);
                gl.attachShader(program, fshader);
                gl.linkProgram(program);
                gl.useProgram(program);
                program.vertexPosAttrib = gl.getAttribLocation(program, 'attrVertex');
                program.offsetUniform = gl.getUniformLocation(program, 'uniformOffset');
                gl.enableVertexAttribArray(program.vertexPosArray);
                gl.vertexAttribPointer(program.vertexPosAttrib, vertexPosBuffer.itemSize, gl.FLOAT, !1, 0, 0);
                gl.uniform2f(program.offsetUniform, 1, 1);
                gl.drawArrays(gl.TRIANGLE_STRIP, 0, vertexPosBuffer.numItems);
                try {
                    result.push(gl.canvas.toDataURL())
                } catch (e) {}
                result.push('extensions:' + (gl.getSupportedExtensions() || []).join(';'));
                result.push('webgl aliased line width range:' + fa2s(gl.getParameter(gl.ALIASED_LINE_WIDTH_RANGE)));
                result.push('webgl aliased point size range:' + fa2s(gl.getParameter(gl.ALIASED_POINT_SIZE_RANGE)));
                result.push('webgl alpha bits:' + gl.getParameter(gl.ALPHA_BITS));
                result.push('webgl antialiasing:' + (gl.getContextAttributes().antialias ? 'yes' : 'no'));
                result.push('webgl blue bits:' + gl.getParameter(gl.BLUE_BITS));
                result.push('webgl depth bits:' + gl.getParameter(gl.DEPTH_BITS));
                result.push('webgl green bits:' + gl.getParameter(gl.GREEN_BITS));
                result.push('webgl max anisotropy:' + maxAnisotropy(gl));
                result.push('webgl max combined texture image units:' + gl.getParameter(gl.MAX_COMBINED_TEXTURE_IMAGE_UNITS));
                result.push('webgl max cube map texture size:' + gl.getParameter(gl.MAX_CUBE_MAP_TEXTURE_SIZE));
                result.push('webgl max fragment uniform vectors:' + gl.getParameter(gl.MAX_FRAGMENT_UNIFORM_VECTORS));
                result.push('webgl max render buffer size:' + gl.getParameter(gl.MAX_RENDERBUFFER_SIZE));
                result.push('webgl max texture image units:' + gl.getParameter(gl.MAX_TEXTURE_IMAGE_UNITS));
                result.push('webgl max texture size:' + gl.getParameter(gl.MAX_TEXTURE_SIZE));
                result.push('webgl max varying vectors:' + gl.getParameter(gl.MAX_VARYING_VECTORS));
                result.push('webgl max vertex attribs:' + gl.getParameter(gl.MAX_VERTEX_ATTRIBS));
                result.push('webgl max vertex texture image units:' + gl.getParameter(gl.MAX_VERTEX_TEXTURE_IMAGE_UNITS));
                result.push('webgl max vertex uniform vectors:' + gl.getParameter(gl.MAX_VERTEX_UNIFORM_VECTORS));
                result.push('webgl max viewport dims:' + fa2s(gl.getParameter(gl.MAX_VIEWPORT_DIMS)));
                result.push('webgl red bits:' + gl.getParameter(gl.RED_BITS));
                result.push('webgl renderer:' + gl.getParameter(gl.RENDERER));
                result.push('webgl shading language version:' + gl.getParameter(gl.SHADING_LANGUAGE_VERSION));
                result.push('webgl stencil bits:' + gl.getParameter(gl.STENCIL_BITS));
                result.push('webgl vendor:' + gl.getParameter(gl.VENDOR));
                result.push('webgl version:' + gl.getParameter(gl.VERSION));
                try {
                    var extensionDebugRendererInfo = gl.getExtension('WEBGL_debug_renderer_info');
                    if (extensionDebugRendererInfo) {
                        result.push('webgl unmasked vendor:' + gl.getParameter(extensionDebugRendererInfo.UNMASKED_VENDOR_WEBGL));
                        result.push('webgl unmasked renderer:' + gl.getParameter(extensionDebugRendererInfo.UNMASKED_RENDERER_WEBGL))
                    }
                } catch (e) {}
                if (!gl.getShaderPrecisionFormat) {
                    return result.join('~')
                }
                result.push('webgl vertex shader high float precision:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.HIGH_FLOAT).precision);
                result.push('webgl vertex shader high float precision rangeMin:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.HIGH_FLOAT).rangeMin);
                result.push('webgl vertex shader high float precision rangeMax:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.HIGH_FLOAT).rangeMax);
                result.push('webgl vertex shader medium float precision:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.MEDIUM_FLOAT).precision);
                result.push('webgl vertex shader medium float precision rangeMin:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.MEDIUM_FLOAT).rangeMin);
                result.push('webgl vertex shader medium float precision rangeMax:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.MEDIUM_FLOAT).rangeMax);
                result.push('webgl vertex shader low float precision:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.LOW_FLOAT).precision);
                result.push('webgl vertex shader low float precision rangeMin:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.LOW_FLOAT).rangeMin);
                result.push('webgl vertex shader low float precision rangeMax:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.LOW_FLOAT).rangeMax);
                result.push('webgl fragment shader high float precision:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.HIGH_FLOAT).precision);
                result.push('webgl fragment shader high float precision rangeMin:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.HIGH_FLOAT).rangeMin);
                result.push('webgl fragment shader high float precision rangeMax:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.HIGH_FLOAT).rangeMax);
                result.push('webgl fragment shader medium float precision:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.MEDIUM_FLOAT).precision);
                result.push('webgl fragment shader medium float precision rangeMin:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.MEDIUM_FLOAT).rangeMin);
                result.push('webgl fragment shader medium float precision rangeMax:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.MEDIUM_FLOAT).rangeMax);
                result.push('webgl fragment shader low float precision:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.LOW_FLOAT).precision);
                result.push('webgl fragment shader low float precision rangeMin:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.LOW_FLOAT).rangeMin);
                result.push('webgl fragment shader low float precision rangeMax:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.LOW_FLOAT).rangeMax);
                result.push('webgl vertex shader high int precision:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.HIGH_INT).precision);
                result.push('webgl vertex shader high int precision rangeMin:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.HIGH_INT).rangeMin);
                result.push('webgl vertex shader high int precision rangeMax:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.HIGH_INT).rangeMax);
                result.push('webgl vertex shader medium int precision:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.MEDIUM_INT).precision);
                result.push('webgl vertex shader medium int precision rangeMin:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.MEDIUM_INT).rangeMin);
                result.push('webgl vertex shader medium int precision rangeMax:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.MEDIUM_INT).rangeMax);
                result.push('webgl vertex shader low int precision:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.LOW_INT).precision);
                result.push('webgl vertex shader low int precision rangeMin:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.LOW_INT).rangeMin);
                result.push('webgl vertex shader low int precision rangeMax:' + gl.getShaderPrecisionFormat(gl.VERTEX_SHADER, gl.LOW_INT).rangeMax);
                result.push('webgl fragment shader high int precision:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.HIGH_INT).precision);
                result.push('webgl fragment shader high int precision rangeMin:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.HIGH_INT).rangeMin);
                result.push('webgl fragment shader high int precision rangeMax:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.HIGH_INT).rangeMax);
                result.push('webgl fragment shader medium int precision:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.MEDIUM_INT).precision);
                result.push('webgl fragment shader medium int precision rangeMin:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.MEDIUM_INT).rangeMin);
                result.push('webgl fragment shader medium int precision rangeMax:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.MEDIUM_INT).rangeMax);
                result.push('webgl fragment shader low int precision:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.LOW_INT).precision);
                result.push('webgl fragment shader low int precision rangeMin:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.LOW_INT).rangeMin);
                result.push('webgl fragment shader low int precision rangeMax:' + gl.getShaderPrecisionFormat(gl.FRAGMENT_SHADER, gl.LOW_INT).rangeMax);
                return result.join('~')
            },
            getWebglCanvas: function() {
                var canvas = document.createElement('canvas');
                var gl = null;
                try {
                    gl = canvas.getContext('webgl') || canvas.getContext('experimental-webgl')
                } catch (e) {}
                if (!gl) {
                    gl = null
                }
                return gl
            },
            getCanvasFp: function() {
                var result = [];
                var canvas = document.createElement('canvas');
                canvas.width = 2000;
                canvas.height = 200;
                canvas.style.display = 'inline';
                var ctx = canvas.getContext('2d');
                ctx.rect(0, 0, 10, 10);
                ctx.rect(2, 2, 6, 6);
                result.push('canvas winding:' + ((ctx.isPointInPath(5, 5, 'evenodd') === false) ? 'yes' : 'no'));
                ctx.textBaseline = 'alphabetic';
                ctx.fillStyle = '#f60';
                ctx.fillRect(125, 1, 62, 20);
                ctx.fillStyle = '#069';
                if (this.options.dontUseFakeFontInCanvas) {
                    ctx.font = '11pt Arial'
                } else {
                    ctx.font = '11pt no-real-font-123'
                }
                ctx.fillText('Cwm fjordbank glyphs vext quiz, ??', 2, 15);
                ctx.fillStyle = 'rgba(102, 204, 0, 0.2)';
                ctx.font = '18pt Arial';
                ctx.fillText('Cwm fjordbank glyphs vext quiz, ??', 4, 45);
                ctx.globalCompositeOperation = 'multiply';
                ctx.fillStyle = 'rgb(255,0,255)';
                ctx.beginPath();
                ctx.arc(50, 50, 50, 0, Math.PI * 2, true);
                ctx.closePath();
                ctx.fill();
                ctx.fillStyle = 'rgb(0,255,255)';
                ctx.beginPath();
                ctx.arc(100, 50, 50, 0, Math.PI * 2, true);
                ctx.closePath();
                ctx.fill();
                ctx.fillStyle = 'rgb(255,255,0)';
                ctx.beginPath();
                ctx.arc(75, 100, 50, 0, Math.PI * 2, true);
                ctx.closePath();
                ctx.fill();
                ctx.fillStyle = 'rgb(255,0,255)';
                ctx.arc(75, 75, 75, 0, Math.PI * 2, true);
                ctx.arc(75, 75, 25, 0, Math.PI * 2, true);
                ctx.fill('evenodd');
                if (canvas.toDataURL) {
                    result.push('canvas fp:' + canvas.toDataURL())
                }
                return result.join('~')
            },
            x64Add: function(m, n) {
                m = [m[0] >>> 16, m[0] & 0xffff, m[1] >>> 16, m[1] & 0xffff];
                n = [n[0] >>> 16, n[0] & 0xffff, n[1] >>> 16, n[1] & 0xffff];
                var o = [0, 0, 0, 0];
                o[3] += m[3] + n[3];
                o[2] += o[3] >>> 16;
                o[3] &= 0xffff;
                o[2] += m[2] + n[2];
                o[1] += o[2] >>> 16;
                o[2] &= 0xffff;
                o[1] += m[1] + n[1];
                o[0] += o[1] >>> 16;
                o[1] &= 0xffff;
                o[0] += m[0] + n[0];
                o[0] &= 0xffff;
                return [(o[0] << 16) | o[1], (o[2] << 16) | o[3]]
            },
            x64Multiply: function(m, n) {
                m = [m[0] >>> 16, m[0] & 0xffff, m[1] >>> 16, m[1] & 0xffff];
                n = [n[0] >>> 16, n[0] & 0xffff, n[1] >>> 16, n[1] & 0xffff];
                var o = [0, 0, 0, 0];
                o[3] += m[3] * n[3];
                o[2] += o[3] >>> 16;
                o[3] &= 0xffff;
                o[2] += m[2] * n[3];
                o[1] += o[2] >>> 16;
                o[2] &= 0xffff;
                o[2] += m[3] * n[2];
                o[1] += o[2] >>> 16;
                o[2] &= 0xffff;
                o[1] += m[1] * n[3];
                o[0] += o[1] >>> 16;
                o[1] &= 0xffff;
                o[1] += m[2] * n[2];
                o[0] += o[1] >>> 16;
                o[1] &= 0xffff;
                o[1] += m[3] * n[1];
                o[0] += o[1] >>> 16;
                o[1] &= 0xffff;
                o[0] += (m[0] * n[3]) + (m[1] * n[2]) + (m[2] * n[1]) + (m[3] * n[0]);
                o[0] &= 0xffff;
                return [(o[0] << 16) | o[1], (o[2] << 16) | o[3]]
            },
            x64Rotl: function(m, n) {
                n %= 64;
                if (n === 32) {
                    return [m[1], m[0]]
                } else if (n < 32) {
                    return [(m[0] << n) | (m[1] >>> (32 - n)), (m[1] << n) | (m[0] >>> (32 - n))]
                } else {
                    n -= 32;
                    return [(m[1] << n) | (m[0] >>> (32 - n)), (m[0] << n) | (m[1] >>> (32 - n))]
                }
            },
            x64LeftShift: function(m, n) {
                n %= 64;
                if (n === 0) {
                    return m
                } else if (n < 32) {
                    return [(m[0] << n) | (m[1] >>> (32 - n)), m[1] << n]
                } else {
                    return [m[1] << (n - 32), 0]
                }
            },
            x64Xor: function(m, n) {
                return [m[0] ^ n[0], m[1] ^ n[1]]
            },
            x64Fmix: function(h) {
                h = this.x64Xor(h, [0, h[0] >>> 1]);
                h = this.x64Multiply(h, [0xff51afd7, 0xed558ccd]);
                h = this.x64Xor(h, [0, h[0] >>> 1]);
                h = this.x64Multiply(h, [0xc4ceb9fe, 0x1a85ec53]);
                h = this.x64Xor(h, [0, h[0] >>> 1]);
                return h
            },
            
            
            
            
            
            
            
            
            
            jsFontsKey : function() {
            	var options = {
                		fonts: {extendedJsFonts: true}, 
                    	excludes: {userAgent: true,language: true}
                }
            	
                var baseFonts = ['monospace', 'sans-serif', 'serif'];
                var fontList = [
                    'Andale Mono', 'Arial', 'Arial Black', 'Arial Hebrew', 'Arial MT', 'Arial Narrow', 'Arial Rounded MT Bold', 'Arial Unicode MS',
                    'Bitstream Vera Sans Mono', 'Book Antiqua', 'Bookman Old Style',
                    'Calibri', 'Cambria', 'Cambria Math', 'Century', 'Century Gothic', 'Century Schoolbook', 'Comic Sans', 'Comic Sans MS', 'Consolas', 'Courier', 'Courier New',
                    'Geneva', 'Georgia',
                    'Helvetica', 'Helvetica Neue',
                    'Impact',
                    'Lucida Bright', 'Lucida Calligraphy', 'Lucida Console', 'Lucida Fax', 'LUCIDA GRANDE', 'Lucida Handwriting', 'Lucida Sans', 'Lucida Sans Typewriter', 'Lucida Sans Unicode',
                    'Microsoft Sans Serif', 'Monaco', 'Monotype Corsiva', 'MS Gothic', 'MS Outlook', 'MS PGothic', 'MS Reference Sans Serif', 'MS Sans Serif', 'MS Serif', 'MYRIAD', 'MYRIAD PRO',
                    'Palatino', 'Palatino Linotype',
                    'Segoe Print', 'Segoe Script', 'Segoe UI', 'Segoe UI Light', 'Segoe UI Semibold', 'Segoe UI Symbol',
                    'Tahoma', 'Times', 'Times New Roman', 'Times New Roman PS', 'Trebuchet MS',
                    'Verdana', 'Wingdings', 'Wingdings 2', 'Wingdings 3'
                ];

                if (options.fonts.extendedJsFonts) {
                    var extendedFontList = [
                        'Abadi MT Condensed Light', 'Academy Engraved LET', 'ADOBE CASLON PRO', 'Adobe Garamond', 'ADOBE GARAMOND PRO', 'Agency FB', 'Aharoni', 'Albertus Extra Bold', 'Albertus Medium', 'Algerian', 'Amazone BT', 'American Typewriter',
                        'American Typewriter Condensed', 'AmerType Md BT', 'Andalus', 'Angsana New', 'AngsanaUPC', 'Antique Olive', 'Aparajita', 'Apple Chancery', 'Apple Color Emoji', 'Apple SD Gothic Neo', 'Arabic Typesetting', 'ARCHER',
                        'ARNO PRO', 'Arrus BT', 'Aurora Cn BT', 'AvantGarde Bk BT', 'AvantGarde Md BT', 'AVENIR', 'Ayuthaya', 'Bandy', 'Bangla Sangam MN', 'Bank Gothic', 'BankGothic Md BT', 'Baskerville',
                        'Baskerville Old Face', 'Batang', 'BatangChe', 'Bauer Bodoni', 'Bauhaus 93', 'Bazooka', 'Bell MT', 'Bembo', 'Benguiat Bk BT', 'Berlin Sans FB', 'Berlin Sans FB Demi', 'Bernard MT Condensed', 'BernhardFashion BT', 'BernhardMod BT', 'Big Caslon', 'BinnerD',
                        'Blackadder ITC', 'BlairMdITC TT', 'Bodoni 72', 'Bodoni 72 Oldstyle', 'Bodoni 72 Smallcaps', 'Bodoni MT', 'Bodoni MT Black', 'Bodoni MT Condensed', 'Bodoni MT Poster Compressed',
                        'Bookshelf Symbol 7', 'Boulder', 'Bradley Hand', 'Bradley Hand ITC', 'Bremen Bd BT', 'Britannic Bold', 'Broadway', 'Browallia New', 'BrowalliaUPC', 'Brush Script MT', 'Californian FB', 'Calisto MT', 'Calligrapher', 'Candara',
                        'CaslonOpnface BT', 'Castellar', 'Centaur', 'Cezanne', 'CG Omega', 'CG Times', 'Chalkboard', 'Chalkboard SE', 'Chalkduster', 'Charlesworth', 'Charter Bd BT', 'Charter BT', 'Chaucer',
                        'ChelthmITC Bk BT', 'Chiller', 'Clarendon', 'Clarendon Condensed', 'CloisterBlack BT', 'Cochin', 'Colonna MT', 'Constantia', 'Cooper Black', 'Copperplate', 'Copperplate Gothic', 'Copperplate Gothic Bold',
                        'Copperplate Gothic Light', 'CopperplGoth Bd BT', 'Corbel', 'Cordia New', 'CordiaUPC', 'Cornerstone', 'Coronet', 'Cuckoo', 'Curlz MT', 'DaunPenh', 'Dauphin', 'David', 'DB LCD Temp', 'DELICIOUS', 'Denmark',
                        'DFKai-SB', 'Didot', 'DilleniaUPC', 'DIN', 'DokChampa', 'Dotum', 'DotumChe', 'Ebrima', 'Edwardian Script ITC', 'Elephant', 'English 111 Vivace BT', 'Engravers MT', 'EngraversGothic BT', 'Eras Bold ITC', 'Eras Demi ITC', 'Eras Light ITC', 'Eras Medium ITC',
                        'EucrosiaUPC', 'Euphemia', 'Euphemia UCAS', 'EUROSTILE', 'Exotc350 Bd BT', 'FangSong', 'Felix Titling', 'Fixedsys', 'FONTIN', 'Footlight MT Light', 'Forte',
                        'FrankRuehl', 'Fransiscan', 'Freefrm721 Blk BT', 'FreesiaUPC', 'Freestyle Script', 'French Script MT', 'FrnkGothITC Bk BT', 'Fruitger', 'FRUTIGER',
                        'Futura', 'Futura Bk BT', 'Futura Lt BT', 'Futura Md BT', 'Futura ZBlk BT', 'FuturaBlack BT', 'Gabriola', 'Galliard BT', 'Gautami', 'Geeza Pro', 'Geometr231 BT', 'Geometr231 Hv BT', 'Geometr231 Lt BT', 'GeoSlab 703 Lt BT',
                        'GeoSlab 703 XBd BT', 'Gigi', 'Gill Sans', 'Gill Sans MT', 'Gill Sans MT Condensed', 'Gill Sans MT Ext Condensed Bold', 'Gill Sans Ultra Bold', 'Gill Sans Ultra Bold Condensed', 'Gisha', 'Gloucester MT Extra Condensed', 'GOTHAM', 'GOTHAM BOLD',
                        'Goudy Old Style', 'Goudy Stout', 'GoudyHandtooled BT', 'GoudyOLSt BT', 'Gujarati Sangam MN', 'Gulim', 'GulimChe', 'Gungsuh', 'GungsuhChe', 'Gurmukhi MN', 'Haettenschweiler', 'Harlow Solid Italic', 'Harrington', 'Heather', 'Heiti SC', 'Heiti TC', 'HELV',
                        'Herald', 'High Tower Text', 'Hiragino Kaku Gothic ProN', 'Hiragino Mincho ProN', 'Hoefler Text', 'Humanst 521 Cn BT', 'Humanst521 BT', 'Humanst521 Lt BT', 'Imprint MT Shadow', 'Incised901 Bd BT', 'Incised901 BT',
                        'Incised901 Lt BT', 'INCONSOLATA', 'Informal Roman', 'Informal011 BT', 'INTERSTATE', 'IrisUPC', 'Iskoola Pota', 'JasmineUPC', 'Jazz LET', 'Jenson', 'Jester', 'Jokerman', 'Juice ITC', 'Kabel Bk BT', 'Kabel Ult BT', 'Kailasa', 'KaiTi', 'Kalinga', 'Kannada Sangam MN',
                        'Kartika', 'Kaufmann Bd BT', 'Kaufmann BT', 'Khmer UI', 'KodchiangUPC', 'Kokila', 'Korinna BT', 'Kristen ITC', 'Krungthep', 'Kunstler Script', 'Lao UI', 'Latha', 'Leelawadee', 'Letter Gothic', 'Levenim MT', 'LilyUPC', 'Lithograph', 'Lithograph Light', 'Long Island',
                        'Lydian BT', 'Magneto', 'Maiandra GD', 'Malayalam Sangam MN', 'Malgun Gothic',
                        'Mangal', 'Marigold', 'Marion', 'Marker Felt', 'Market', 'Marlett', 'Matisse ITC', 'Matura MT Script Capitals', 'Meiryo', 'Meiryo UI', 'Microsoft Himalaya', 'Microsoft JhengHei', 'Microsoft New Tai Lue', 'Microsoft PhagsPa', 'Microsoft Tai Le',
                        'Microsoft Uighur', 'Microsoft YaHei', 'Microsoft Yi Baiti', 'MingLiU', 'MingLiU_HKSCS', 'MingLiU_HKSCS-ExtB', 'MingLiU-ExtB', 'Minion', 'Minion Pro', 'Miriam', 'Miriam Fixed', 'Mistral', 'Modern', 'Modern No. 20', 'Mona Lisa Solid ITC TT', 'Mongolian Baiti',
                        'MONO', 'MoolBoran', 'Mrs Eaves', 'MS LineDraw', 'MS Mincho', 'MS PMincho', 'MS Reference Specialty', 'MS UI Gothic', 'MT Extra', 'MUSEO', 'MV Boli',
                        'Nadeem', 'Narkisim', 'NEVIS', 'News Gothic', 'News GothicMT', 'NewsGoth BT', 'Niagara Engraved', 'Niagara Solid', 'Noteworthy', 'NSimSun', 'Nyala', 'OCR A Extended', 'Old Century', 'Old English Text MT', 'Onyx', 'Onyx BT', 'OPTIMA', 'Oriya Sangam MN',
                        'OSAKA', 'OzHandicraft BT', 'Palace Script MT', 'Papyrus', 'Parchment', 'Party LET', 'Pegasus', 'Perpetua', 'Perpetua Titling MT', 'PetitaBold', 'Pickwick', 'Plantagenet Cherokee', 'Playbill', 'PMingLiU', 'PMingLiU-ExtB',
                        'Poor Richard', 'Poster', 'PosterBodoni BT', 'PRINCETOWN LET', 'Pristina', 'PTBarnum BT', 'Pythagoras', 'Raavi', 'Rage Italic', 'Ravie', 'Ribbon131 Bd BT', 'Rockwell', 'Rockwell Condensed', 'Rockwell Extra Bold', 'Rod', 'Roman', 'Sakkal Majalla',
                        'Santa Fe LET', 'Savoye LET', 'Sceptre', 'Script', 'Script MT Bold', 'SCRIPTINA', 'Serifa', 'Serifa BT', 'Serifa Th BT', 'ShelleyVolante BT', 'Sherwood',
                        'Shonar Bangla', 'Showcard Gothic', 'Shruti', 'Signboard', 'SILKSCREEN', 'SimHei', 'Simplified Arabic', 'Simplified Arabic Fixed', 'SimSun', 'SimSun-ExtB', 'Sinhala Sangam MN', 'Sketch Rockwell', 'Skia', 'Small Fonts', 'Snap ITC', 'Snell Roundhand', 'Socket',
                        'Souvenir Lt BT', 'Staccato222 BT', 'Steamer', 'Stencil', 'Storybook', 'Styllo', 'Subway', 'Swis721 BlkEx BT', 'Swiss911 XCm BT', 'Sylfaen', 'Synchro LET', 'System', 'Tamil Sangam MN', 'Technical', 'Teletype', 'Telugu Sangam MN', 'Tempus Sans ITC',
                        'Terminal', 'Thonburi', 'Traditional Arabic', 'Trajan', 'TRAJAN PRO', 'Tristan', 'Tubular', 'Tunga', 'Tw Cen MT', 'Tw Cen MT Condensed', 'Tw Cen MT Condensed Extra Bold',
                        'TypoUpright BT', 'Unicorn', 'Univers', 'Univers CE 55 Medium', 'Univers Condensed', 'Utsaah', 'Vagabond', 'Vani', 'Vijaya', 'Viner Hand ITC', 'VisualUI', 'Vivaldi', 'Vladimir Script', 'Vrinda', 'Westminster', 'WHITNEY', 'Wide Latin',
                        'ZapfEllipt BT', 'ZapfHumnst BT', 'ZapfHumnst Dm BT', 'Zapfino', 'Zurich BlkEx BT', 'Zurich Ex BT', 'ZWAdobeF'
                    ];
                    fontList = fontList.concat(extendedFontList);
                };

                fontList = fontList.concat(options.fonts.userDefinedFonts);

                fontList = fontList.filter(function(font, position) {
                    return fontList.indexOf(font) === position;
                });

                var testString = 'mmmmmmmmmmlli';

                var testSize = '72px';

                var h = document.getElementsByTagName('body')[0];

                var baseFontsDiv = document.createElement('div');

                var fontsDiv = document.createElement('div');

                var defaultWidth = {};
                var defaultHeight = {};

                var createSpan = function() {
                    var s = document.createElement('span');
                    s.style.position = 'absolute';
                    s.style.left = '-9999px';
                    s.style.fontSize = testSize;
                    s.style.fontStyle = 'normal';
                    s.style.fontWeight = 'normal';
                    s.style.letterSpacing = 'normal';
                    s.style.lineBreak = 'auto';
                    s.style.lineHeight = 'normal';
                    s.style.textTransform = 'none';
                    s.style.textAlign = 'left';
                    s.style.textDecoration = 'none';
                    s.style.textShadow = 'none';
                    s.style.whiteSpace = 'normal';
                    s.style.wordBreak = 'normal';
                    s.style.wordSpacing = 'normal';
                    s.innerHTML = testString;
                    return s;
                }
            },
            
            
            x64hash128: function(key, seed) {
                key = key || '';
                seed = seed || 0;
                var remainder = key.length % 16;
                var bytes = key.length - remainder;
                var h1 = [0, seed];
                var h2 = [0, seed];
                var k1 = [0, 0];
                var k2 = [0, 0];
                var c1 = [0x87c37b91, 0x114253d5];
                var c2 = [0x4cf5ad43, 0x2745937f];
                for (var i = 0; i < bytes; i = i + 16) {
                    k1 = [((key.charCodeAt(i + 4) & 0xff)) | ((key.charCodeAt(i + 5) & 0xff) << 8) | ((key.charCodeAt(i + 6) & 0xff) << 16) | ((key.charCodeAt(i + 7) & 0xff) << 24), ((key.charCodeAt(i) & 0xff)) | ((key.charCodeAt(i + 1) & 0xff) << 8) | ((key.charCodeAt(i + 2) & 0xff) << 16) | ((key.charCodeAt(i + 3) & 0xff) << 24)];
                    k2 = [((key.charCodeAt(i + 12) & 0xff)) | ((key.charCodeAt(i + 13) & 0xff) << 8) | ((key.charCodeAt(i + 14) & 0xff) << 16) | ((key.charCodeAt(i + 15) & 0xff) << 24), ((key.charCodeAt(i + 8) & 0xff)) | ((key.charCodeAt(i + 9) & 0xff) << 8) | ((key.charCodeAt(i + 10) & 0xff) << 16) | ((key.charCodeAt(i + 11) & 0xff) << 24)];
                    k1 = this.x64Multiply(k1, c1);
                    k1 = this.x64Rotl(k1, 31);
                    k1 = this.x64Multiply(k1, c2);
                    h1 = this.x64Xor(h1, k1);
                    h1 = this.x64Rotl(h1, 27);
                    h1 = this.x64Add(h1, h2);
                    h1 = this.x64Add(this.x64Multiply(h1, [0, 5]), [0, 0x52dce729]);
                    k2 = this.x64Multiply(k2, c2);
                    k2 = this.x64Rotl(k2, 33);
                    k2 = this.x64Multiply(k2, c1);
                    h2 = this.x64Xor(h2, k2);
                    h2 = this.x64Rotl(h2, 31);
                    h2 = this.x64Add(h2, h1);
                    h2 = this.x64Add(this.x64Multiply(h2, [0, 5]), [0, 0x38495ab5])
                };
                k1 = [0, 0];
                k2 = [0, 0];
                switch (remainder) {
                    case 15:
                        k2 = this.x64Xor(k2, this.x64LeftShift([0, key.charCodeAt(i + 14)], 48));
                    case 14:
                        k2 = this.x64Xor(k2, this.x64LeftShift([0, key.charCodeAt(i + 13)], 40));
                    case 13:
                        k2 = this.x64Xor(k2, this.x64LeftShift([0, key.charCodeAt(i + 12)], 32));
                    case 12:
                        k2 = this.x64Xor(k2, this.x64LeftShift([0, key.charCodeAt(i + 11)], 24));
                    case 11:
                        k2 = this.x64Xor(k2, this.x64LeftShift([0, key.charCodeAt(i + 10)], 16));
                    case 10:
                        k2 = this.x64Xor(k2, this.x64LeftShift([0, key.charCodeAt(i + 9)], 8));
                    case 9:
                        k2 = this.x64Xor(k2, [0, key.charCodeAt(i + 8)]);
                        k2 = this.x64Multiply(k2, c2);
                        k2 = this.x64Rotl(k2, 33);
                        k2 = this.x64Multiply(k2, c1);
                        h2 = this.x64Xor(h2, k2);
                    case 8:
                        k1 = this.x64Xor(k1, this.x64LeftShift([0, key.charCodeAt(i + 7)], 56));
                    case 7:
                        k1 = this.x64Xor(k1, this.x64LeftShift([0, key.charCodeAt(i + 6)], 48));
                    case 6:
                        k1 = this.x64Xor(k1, this.x64LeftShift([0, key.charCodeAt(i + 5)], 40));
                    case 5:
                        k1 = this.x64Xor(k1, this.x64LeftShift([0, key.charCodeAt(i + 4)], 32));
                    case 4:
                        k1 = this.x64Xor(k1, this.x64LeftShift([0, key.charCodeAt(i + 3)], 24));
                    case 3:
                        k1 = this.x64Xor(k1, this.x64LeftShift([0, key.charCodeAt(i + 2)], 16));
                    case 2:
                        k1 = this.x64Xor(k1, this.x64LeftShift([0, key.charCodeAt(i + 1)], 8));
                    case 1:
                        k1 = this.x64Xor(k1, [0, key.charCodeAt(i)]);
                        k1 = this.x64Multiply(k1, c1);
                        k1 = this.x64Rotl(k1, 31);
                        k1 = this.x64Multiply(k1, c2);
                        h1 = this.x64Xor(h1, k1)
                };
                h1 = this.x64Xor(h1, [0, key.length]);
                h2 = this.x64Xor(h2, [0, key.length]);
                h1 = this.x64Add(h1, h2);
                h2 = this.x64Add(h2, h1);
                h1 = this.x64Fmix(h1);
                h2 = this.x64Fmix(h2);
                h1 = this.x64Add(h1, h2);
                h2 = this.x64Add(h2, h1);
                return (('00000000' + (h1[0] >>> 0).toString(16)).slice(-8) + ('00000000' + (h1[1] >>> 0).toString(16)).slice(-8) + ('00000000' + (h2[0] >>> 0).toString(16)).slice(-8) + ('00000000' + (h2[1] >>> 0).toString(16)).slice(-8))
            }
        };
        return PCHOMEFingerprint
    });
    var ptagParamater = window.dataLayer;
    var page_view_opt1 = "";
    var page_view_opt2 = "";
    var convert_opt1 = "";
    var convert_opt2 = "";
    var tracking_opt1 = "";
    var tracking_opt2 = "";
    var convert_id = "";
    var convert_price = "";
    var tracking_id = "";
    var prod_id = "";
    var prod_price = "";
    var prod_dis = "";
    var ec_stock_status = "";
    var pa_em_value = "";
    var pa_id = "";
    
    
    
    
    
    
    
    
    (function() {
        var click = null;
        var keys = [];
        var getCanvasFp = PCHOMEFingerprint().getCanvasFp();
        var getWebglFp = PCHOMEFingerprint().getWebglFp();
        var windowScreenColorDepth = window.screen.colorDepth;
        var devicePixelRatio = window.devicePixelRatio;
        var userAgent = navigator.userAgent;
        var language = navigator.language;
        var deviceMemory = navigator.deviceMemory || -1;
        var resolution = (window.screen.height > window.screen.width) ? [window.screen.height, window.screen.width] : [window.screen.width, window.screen.height];
        var cpu;
        if (navigator.cpuClass) {
            cpu = navigator.cpuClass
        } else {
            cpu = 'unknown'
        }
        keys.push(getCanvasFp);
        keys.push(getWebglFp);
        keys.push(windowScreenColorDepth);
        keys.push(devicePixelRatio);
        keys.push(userAgent);
        keys.push(language);
        keys.push(resolution);
        keys.push(cpu);
        
        
//        var x64Add = PCHOMEFingerprint().x64Add;
//        keys.push(getCanvasFp);
//        keys.push(getWebglFp);
//        keys.push(windowScreenColorDepth);
//        keys.push(devicePixelRatio);
//        keys.push(userAgent);
//        keys.push(language);
//        keys.push(resolution);
//        keys.push(cpu);
//        keys.push(x64Add);
        fig = PCHOMEFingerprint().x64hash128(keys.join('~~~'), 32);
        referer = encodeURIComponent(document.referrer);
        screen_x = screen.availWidth;
        screen_y = screen.availHeight;
        webUrl = encodeURIComponent(location.href);
        convert_click_flag = false;
        
        do_a(function(){
        	doInitData();
        	doSendPaclData();
        });
    })();
var referer = "";
var screen_x = "";
var screen_y = "";
var webUrl = "";
var fig = "";
var convert_click_flag = null;
var paclCodeObject = new Object();
var ptagParamater = window.dataLayer;
var paclCodeJson = null;


var paclCodeObject = null;
var ptagParamater = null;
function do_a(callback){
	paclCodeObject = new Object();
	paclCodeObject["data"] = {};
	ptagParamater = window.dataLayer;
	if( typeof callback === 'function' ){
		callback();
	}
}

function doInitData() {
    var pa_id = "";
    ptagParamater.forEach(function(element) {
        var ptagType = element[0];
        if ((element.length == 1 && element[0].paid == undefined) || (element.length == undefined)) {
            return
        }
        if (element.length == 1 && element[0].paid != undefined) {
            pa_id = ptagType.paid
        }
        if (element[0] == undefined) {
            return
        }
        if (element[1] == 'convert' || element[1] == 'page_view' || element[1] == 'tracking') {
            var eventType = element[1];
            if (eventType == "page_view") {
                page_view_opt1 = element[2].hasOwnProperty('op1') ? element[2].op1 : '';
                page_view_opt2 = element[2].hasOwnProperty('op2') ? element[2].op2 : '';
                pa_em_value = element[2].hasOwnProperty('pa_em_value') ? element[2].pa_em_value : '';
                paclCodeObject.data['page_view'] = {
                    'page_view_opt1': page_view_opt1,
                    'page_view_opt2': page_view_opt2,
                    'pa_em_value': pa_em_value,
                    'pa_id': pa_id
                }
            }
            if (eventType == "convert") {
                convert_id = element[2].hasOwnProperty('convert_id') ? element[2].convert_id : '';
                convert_price = element[2].hasOwnProperty('convert_price') ? element[2].convert_price : '';
                convert_opt1 = element[2].hasOwnProperty('op1') ? element[2].op1 : '';
                convert_opt2 = element[2].hasOwnProperty('op2') ? element[2].op2 : '';
                pa_em_value = element[2].hasOwnProperty('pa_em_value') ? element[2].pa_em_value : '';
                convert_click_flag = element[3] === 'click' ? true : false;
                paclCodeObject.data['convert_' + convert_id] = {
                    'convert_id': convert_id,
                    'convert_price': convert_price,
                    'convert_opt1': convert_opt1,
                    'convert_opt2': convert_opt2,
                    'pa_em_value': pa_em_value,
                    'pa_id': pa_id,
                    'convert_click_flag': convert_click_flag
                }
            }
            if (eventType == "tracking") {
                tracking_id = element[2].hasOwnProperty('tracking_id') ? element[2].tracking_id : '';
                prod_id = element[2].hasOwnProperty('prod_id') ? element[2].prod_id : '';
                prod_price = element[2].hasOwnProperty('prod_price') ? element[2].prod_price : '';
                prod_dis = element[2].hasOwnProperty('prod_dis') ? element[2].prod_dis : '';
                tracking_opt1 = element[2].hasOwnProperty('op1') ? element[2].op1 : '';
                tracking_opt2 = element[2].hasOwnProperty('op2') ? element[2].op2 : '';
                ec_stock_status = element[2].hasOwnProperty('ec_stock_status') ? element[2].ec_stock_status : '';
                pa_em_value = element[2].hasOwnProperty('pa_em_value') ? element[2].pa_em_value : '';
                paclCodeObject.data['tracking_' + tracking_id] = {
                    'tracking_id': tracking_id,
                    'prod_id': prod_id,
                    'prod_price': prod_price,
                    'prod_dis': prod_dis,
                    'tracking_opt1': tracking_opt1,
                    'tracking_opt2': tracking_opt2,
                    'ec_stock_status': ec_stock_status,
                    'pa_em_value': pa_em_value,
                    'pa_id': pa_id
                }
            }
        }
    })
};

function doSendPaclData() {
    for (var key in paclCodeObject.data) {
        if (key.includes('convert')) {
            convert_id = paclCodeObject.data[key].convert_id;
            convert_price = paclCodeObject.data[key].convert_price;
            convert_opt1 = paclCodeObject.data[key].convert_opt1;
            convert_opt2 = paclCodeObject.data[key].convert_opt2;
            pa_em_value = paclCodeObject.data[key].pa_em_value;
            pa_id = paclCodeObject.data[key].pa_id;
            if (!paclCodeObject.data[key].convert_click_flag) {
            	doConvert()
            }
        }
        if (key.includes('tracking')) {
            tracking_id = paclCodeObject.data[key].tracking_id;
            prod_id = paclCodeObject.data[key].prod_id;
            prod_price = paclCodeObject.data[key].prod_price;
            prod_dis = paclCodeObject.data[key].prod_dis;
            tracking_opt1 = paclCodeObject.data[key].tracking_opt1;
            tracking_opt2 = paclCodeObject.data[key].tracking_opt2;
            ec_stock_status = paclCodeObject.data[key].ec_stock_status;
            pa_em_value = paclCodeObject.data[key].pa_em_value;
            pa_id = paclCodeObject.data[key].pa_id;
            doTracking()
        }
        if (key.includes('page_view')) {
            page_view_opt1 = paclCodeObject.data[key].page_view_opt1;
            page_view_opt2 = paclCodeObject.data[key].page_view_opt2;
            pa_em_value = paclCodeObject.data[key].pa_em_value;
            pa_id = paclCodeObject.data[key].pa_id;
            doPageView()
        }
    }
};

function doConvert() {
	var paclUrl = "https://pacl.pchome.com.tw/api/collect";
	if(location.href.indexOf("http://")){
		paclUrl = "http://pacl.pchome.com.tw/api/collect";
	}
    var xhr = new XMLHttpRequest();
    xhr.open("POST", paclUrl, true);
    xhr.withCredentials = true;
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {}
    };
    xhr.send("fingerId=" + fig + "&paId=" + pa_id + "&screenX=" + screen_x + "&screenY=" + screen_y + "&paEmValue=" + pa_em_value + "&url=" + webUrl + "&paEvent=convert&convertId=" + convert_id + "&convertPrice=" + convert_price + "&op1=" + convert_opt1 + "&op2=" + convert_opt2 + "&referer=" + referer)
};

function doPageView() {
	var paclUrl = "https://pacl.pchome.com.tw/api/collect";
	if(location.href.indexOf("http://")){
		paclUrl = "http://pacl.pchome.com.tw/api/collect";
	}
    var xhr = new XMLHttpRequest();
    xhr.open("POST", paclUrl, true);
    xhr.withCredentials = true;
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {}
    };
    xhr.send("fingerId=" + fig + "&paId=" + pa_id + "&screenX=" + screen_x + "&screenY=" + screen_y + "&paEmValue=" + pa_em_value + "&url=" + webUrl + "&paEvent=page_view&op1=" + page_view_opt1 + "&op2=" + page_view_opt2 + "&referer=" + referer)
};

function doTracking() {
	var paclUrl = "https://pacl.pchome.com.tw/api/collect";
	if(location.href.indexOf("http://")){
		paclUrl = "http://pacl.pchome.com.tw/api/collect";
	}
    var xhr = new XMLHttpRequest();
    xhr.open("POST", paclUrl, true);
    xhr.withCredentials = true;
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {}
    };
    xhr.send("fingerId=" + fig + "&paId=" + pa_id + "&screenX=" + screen_x + "&screenY=" + screen_y + "&paEmValue=" + pa_em_value + "&url=" + webUrl + "&paEvent=tracking&trackingId=" + tracking_id + "&prodId=" + prod_id + "&prodPrice=" + prod_price + "&prodDis=" + prod_dis + "&op1=" + tracking_opt1 + "&op2=" + tracking_opt2 + "&referer=" + referer + "&ecStockStatus=" + ec_stock_status)
};

function pchome_click(link_url, blank_flag) {
    if (link_url == null || link_url.length == 0 || link_url == '') {
        alert('link_url 是空值，link_url is null');
        return false
    }
    var blank = false;
    if (typeof blank_flag === "boolean") {
        blank = blank_flag
    }
    for (var key in paclCodeObject.data) {
        if (key.includes('convert')) {
            convert_id = paclCodeObject.data[key].convert_id;
            convert_price = paclCodeObject.data[key].convert_price;
            convert_opt1 = paclCodeObject.data[key].convert_opt1;
            convert_opt2 = paclCodeObject.data[key].convert_opt2;
            pa_em_value = paclCodeObject.data[key].pa_em_value;
            pa_id = paclCodeObject.data[key].pa_id;
            if (paclCodeObject.data[key].convert_click_flag) {
                doConvert()
            }
        }
    }
    if (blank) {
        window.open(link_url, '_0')
    } else {
        location.href = link_url
    }
}

function pchome_click() {
	do_a(function(){
		doInitData();
	});
    for (var key in paclCodeObject.data) {
        if (key.includes('convert')) {
            convert_id = paclCodeObject.data[key].convert_id;
            convert_price = paclCodeObject.data[key].convert_price;
            convert_opt1 = paclCodeObject.data[key].convert_opt1;
            convert_opt2 = paclCodeObject.data[key].convert_opt2;
            pa_em_value = paclCodeObject.data[key].pa_em_value;
            pa_id = paclCodeObject.data[key].pa_id;
            if (paclCodeObject.data[key].convert_click_flag) {
                doConvert()
            }
        }
    }
}