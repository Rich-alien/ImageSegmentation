package org.example.clustering

import org.example.image.LAB
import java.awt.Color

data class Cluster(
    var x: Int,
    var y: Int,
    var colorRGB: Color,
    var colorLAB: LAB,
    val displayColor: Color
)