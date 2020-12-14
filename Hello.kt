package org.example

import org.example.clustering.Clustering
import org.example.image.ImageComponent
import org.example.image.ImageFrame
import org.example.image.ImageWorker
import javax.swing.JFrame

fun main(args: Array<String>) {

    val imageComponent = ImageComponent()

    val frame = ImageFrame(imageComponent)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.isVisible = true

    val image = imageComponent.img ?: return
    val clustering = Clustering(image, 4)
    clustering.clustering()
}

