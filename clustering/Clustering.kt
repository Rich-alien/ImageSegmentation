package org.example.clustering

import org.example.image.*
import java.awt.Color
import java.awt.image.BufferedImage
import javax.swing.JFrame
import kotlin.math.abs
import kotlin.math.pow
import kotlin.random.Random

class Clustering(
    val image: BufferedImage,
    clustersCount: Int = 3
) {

    private val pixelsList: Array<Array<Pixel?>> = ImageWorker.imgToPixel(image)
    private val clustersList = arrayListOf<Cluster>()
    private val mockColors = arrayOf(Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED)

    init {
        val delta = 100 / clustersCount
        val widthDelta = image.width / clustersCount
        val heightDelta = image.height / clustersCount
        var param = delta
        var width = widthDelta
        var height = heightDelta

        for (index in 0 until clustersCount) {
            this.clustersList.add(
                Cluster(
                    width,
                    height,
                    Color(
                        Random.nextInt(0, 255),
                        Random.nextInt(0, 255),
                        Random.nextInt(0, 255)
                    ),
                    LAB(
                        param,
                        param,
                        param
                    ),
                    mockColors[index]
                )
            )

            param += delta
            width += widthDelta
            height += heightDelta
        }
    }

    private val errorsList: ArrayList<Double> = arrayListOf(0.0)

    fun clustering() {
        do {
            this.setClusters()
            this.calculateNewClustersChar()
            this.errorsList.add(getE())
        } while (this.errorsList[this.errorsList.size - 1] != this.errorsList[this.errorsList.size - 2])

        val imageComponent = ImageComponent(this.getConvertedImage())

        val frame = ImageFrame(imageComponent)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.isVisible = true
    }

    private fun setClusters() {
        this.pixelsList.forEach { col ->

            col.forEach lit@{ pixel ->
                if (pixel == null) return@lit

                var minSize = 100.00
                var selectedCluster: Cluster = this.clustersList[0]

                this.clustersList.forEach { cluster ->
                    val minSizeBuffer = this.metric(pixel, cluster)

                    if (minSize > minSizeBuffer) {
                        minSize = minSizeBuffer
                        selectedCluster = cluster
                    }
                }

                pixel.cluster = selectedCluster
            }
        }
    }

    private fun getConvertedImage(): BufferedImage {
        this.pixelsList.forEach { col ->
            col.forEach lit@{
                if (it?.cluster == null) return@lit
                it.rgbColor = it.cluster!!.displayColor
            }
        }

        return ImageWorker.setColors(image, this.pixelsList)
    }

    private fun calculateNewClustersChar() {

        this.clustersList.forEach { cluster ->
            var x = 0
            var y = 0
            val labColor = LAB(0, 0, 0)

            val clusterPointsList = this.pixelsList.map { it.filter { pixel -> pixel?.cluster != null && pixel.cluster == cluster } }

            var size = 0

            clusterPointsList.forEach { col ->
                col.forEach lit@{ row ->
                    if (row == null) return@lit
                    x += row.x
                    y += row.y
                    labColor.a += row.labColor.a
                    labColor.b += row.labColor.b

                    size++
                }
            }

            if (size == 0) return

            cluster.x = x / size
            cluster.y = y / size
            cluster.colorLAB.b = labColor.b / size
            cluster.colorLAB.a = labColor.a / size
        }
    }


    private fun metric(pixel: Pixel, cluster: Cluster): Double {
        return 1 * (
                abs(this.linear(0, image.width, pixel.x) - this.linear(0, image.width, cluster.x)) +
                        abs(this.linear(0, image.height, pixel.y) - this.linear(0, image.height, cluster.y))
                ) +
                5 * (
                    abs(this.linear(-120, 120, pixel.labColor.a) - this.linear(-120, 120, cluster.colorLAB.a)) +
                             abs(this.linear(-120, 120, pixel.labColor.b) - this.linear(-120, 120, cluster.colorLAB.b))
                )
    }

    private fun linear(min: Int, max: Int, value: Int): Double {
        return (value - min) / ((max - min) * 1.00)
    }

    private fun getE(): Double {
        var e = 0.0

        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                val pixel = this.pixelsList[x][y] ?: continue
                e += this.metric(pixel, pixel.cluster!!).pow(2)
            }
        }

        return e
    }


}