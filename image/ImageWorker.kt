package org.example.image

import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.pow
import kotlin.math.roundToInt

class ImageWorker {
    companion object {
        fun imgToPixel(image: BufferedImage): Array<Array<Pixel?>> {
            val arrayPX = Array(image.width) { arrayOfNulls<Pixel>(image.height) }
            for (x in 0 until image.width) {
                for (y in 0 until image.height) {
                    arrayPX[x][y] = cordInsidePixel(image, x, y)
                }
            }

            return arrayPX
        }
        fun setColors(image: BufferedImage, pixels: Array<Array<Pixel?>>): BufferedImage {
            val result = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)

            for (x in 0 until image.width) {
                for (y in 0 until image.height) {
                    val color = pixels[x][y]?.rgbColor ?: continue
                    result.setRGB(x, y, getIntFromColor(color))
                }
            }
            return result
        }
        fun cordInsidePixel(image: BufferedImage, x: Int, y: Int): Pixel {
            val color = Color(image.getRGB(x, y), true)
            val labColor = generateLAB(color)
            return Pixel(x, y, color, labColor)
        }
        // RGB -> XYZ -> LAB
        fun generateLAB(color: Color): LAB {
            val Xr = 95.047
            val Yr = 100.0
            val Zr = 108.883
            var r: Double = color.red / 255.0
            var g: Double = color.green / 255.0
            var b: Double = color.blue / 255.0
            r = if (r > 0.04045) ((r + 0.055) / 1.055).pow(2.4) else r / 12.92
            g = if (g > 0.04045) ((g + 0.055) / 1.055).pow(2.4) else g / 12.92
            b = if (b > 0.04045) ((b + 0.055) / 1.055).pow(2.4) else b / 12.92
            r *= 100.0
            g *= 100.0
            b *= 100.0
            val X = 0.4124 * r + 0.3576 * g + 0.1805 * b
            val Y = 0.2126 * r + 0.7152 * g + 0.0722 * b
            val Z = 0.0193 * r + 0.1192 * g + 0.9505 * b
            var xr = X / Xr
            var yr = Y / Yr
            var zr = Z / Zr
            xr = if (xr > 0.008856) {
                xr.pow(1 / 3.0)
            } else {
                ((7.787 * xr)+16 / 116.0)
            }

            yr = if (yr > 0.008856) {
                yr.pow(1 / 3.0)
            } else {
                ((7.787 * yr)+16 / 116.0)
            }

            zr = if (zr > 0.008856) {
                zr.pow(1 / 3.0)
            } else {
                ((7.787 * zr)+16 / 116.0)
            }
            return LAB((116 * yr - 16).roundToInt(), (500 * (xr - yr)).roundToInt(), (200 * (yr - zr)).roundToInt())
        }
        private fun getIntFromColor(color: Color): Int {
            val red = (color.red shl 16) and 0x00FF0000
            val green = (color.green shl 8) and 0x0000FF00
            val blue = color.blue and 0x000000FF
            return (0xFF000000 or red.toLong() or green.toLong() or blue.toLong()).toInt()
        }
    }
}