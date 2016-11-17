package net.corda.explorer.views

import javafx.application.Platform
import javafx.event.EventTarget
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.scene.text.TextAlignment
import javafx.util.StringConverter
import tornadofx.gridpane
import tornadofx.label

/**
 *  Helper method to reduce boiler plate code
 */
fun <T> stringConverter(fromStringFunction: ((String?) -> T)? = null, toStringFunction: (T) -> String): StringConverter<T> {
    val converter = object : StringConverter<T>() {
        override fun fromString(string: String?): T {
            return fromStringFunction?.invoke(string) ?: throw UnsupportedOperationException("not implemented")
        }

        override fun toString(o: T): String {
            return toStringFunction(o)
        }
    }
    return converter
}

/**
 * Format Number to string with metric prefix.
 */
fun Number.toStringWithSuffix(precision: Int = 1): String {
    if (this.toDouble() < 1000) return "$this"
    val scales = "kMBT"
    val exp = Math.min(scales.length, (Math.log(this.toDouble()) / Math.log(1000.0)).toInt())
    return "${(this.toDouble() / Math.pow(1000.0, exp.toDouble())).format(precision)}${scales[exp - 1]}"
}

fun Double.format(precision: Int) = String.format("%.${precision}f", this)

/**
 * Helper method to make sure block runs in FX thread
 */
fun runInFxApplicationThread(block: () -> Unit) {
    if (Platform.isFxApplicationThread()) {
        block()
    } else {
        Platform.runLater(block)
    }
}

fun EventTarget.underConstruction(): Parent {
    return gridpane {
        label("Under Construction...") {
            maxWidth = Double.MAX_VALUE
            textAlignment = TextAlignment.CENTER
            alignment = Pos.CENTER
            GridPane.setVgrow(this, Priority.ALWAYS)
            GridPane.setHgrow(this, Priority.ALWAYS)
        }
    }
}