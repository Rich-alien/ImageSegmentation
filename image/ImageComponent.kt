package org.example.image

import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.JComponent

class ImageComponent: JComponent {

    var img: BufferedImage? = null

    constructor() {
        try {
            val file = File({}.javaClass.getResource("/wine2.jpg").toURI())
            img = ImageIO.read(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun paint(graphics: Graphics) {
        if (img == null) return
        graphics.drawImage(img, 0, 0, null)
    }

    constructor(image: BufferedImage) {
        this.img = image
    }


}