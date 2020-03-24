package me.enterman

import javafx.animation.*
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.value.ObservableValue
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.CacheHint
import javafx.scene.Node
import javafx.stage.Stage
import javafx.util.Duration


internal class ShakeTransition(private val node: Stage, event: EventHandler<ActionEvent?>?) : Transition() {
    private val WEB_EASE = Interpolator.SPLINE(0.25, 0.1, 0.25, 1.0)

    private val timeline: Timeline
    private var oldCache = false
    private var oldCacheHint = CacheHint.DEFAULT
    private val useCache = true
    private val xIni: Double
    private val x: DoubleProperty = SimpleDoubleProperty()

    /**
     * Called when the animation is starting
     */
    protected fun starting() {
        if (useCache) {
            //oldCache = node.isCache()
            //oldCacheHint = node.getCacheHint()
            //node.setCache(true)
            //node.setCacheHint(CacheHint.SPEED)
        }
    }

    /**
     * Called when the animation is stopping
     */
    protected fun stopping() {
        if (useCache) {
            //node.setCache(oldCache)
            //node.setCacheHint(oldCacheHint)
        }
    }

    override fun interpolate(d: Double) {
        timeline.playFrom(Duration.seconds(d))
        timeline.stop()
    }

    /**
     * Create new ShakeTransition
     *
     * @param node The node to affect
     */
    init {
        statusProperty().addListener { ov: ObservableValue<out Status?>?, t: Status?, newStatus: Status? ->
            when (newStatus) {
                Status.RUNNING -> starting()
                else -> stopping()
            }
        }
        timeline = Timeline(
                KeyFrame(Duration.millis(0.0), KeyValue(x, 0, WEB_EASE)),
                KeyFrame(Duration.millis(100.0), KeyValue(x, -10, WEB_EASE)),
                KeyFrame(Duration.millis(200.0), KeyValue(x, 10, WEB_EASE)),
                KeyFrame(Duration.millis(300.0), KeyValue(x, -10, WEB_EASE)),
                KeyFrame(Duration.millis(400.0), KeyValue(x, 10, WEB_EASE)),
                KeyFrame(Duration.millis(500.0), KeyValue(x, -10, WEB_EASE)),
                KeyFrame(Duration.millis(600.0), KeyValue(x, 10, WEB_EASE)),
                KeyFrame(Duration.millis(700.0), KeyValue(x, -10, WEB_EASE)),
                KeyFrame(Duration.millis(800.0), KeyValue(x, 10, WEB_EASE)),
                KeyFrame(Duration.millis(900.0), KeyValue(x, -10, WEB_EASE)),
                KeyFrame(Duration.millis(1000.0), KeyValue(x, 0, WEB_EASE))
        )
        xIni = node.x
        x.addListener { _: ObservableValue<out Number>?, _: Number?, n1: Number -> node.x = xIni + n1.toDouble() }
        cycleDuration = Duration.seconds(1.0)
        delay = Duration.seconds(0.2)
        onFinished = event
    }
}