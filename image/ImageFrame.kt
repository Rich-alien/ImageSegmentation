package org.example.image

import javax.swing.JFrame

class ImageFrame(
        component: ImageComponent
): JFrame() {

    init {
        title = "Image test"

        val img = component.img

        if (img != null) {
            setSize(img.width, img.height)
            add(component)
        }
    }
}