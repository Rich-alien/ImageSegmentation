package org.example.image

import org.example.clustering.Cluster
import java.awt.Color

data class Pixel(
    val x: Int,
    val y: Int,
    var rgbColor: Color,
    var labColor: LAB,
    var cluster: Cluster? = null
)