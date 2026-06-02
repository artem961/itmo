package com.example.cli

import com.example.math.models.Matrix
import com.varabyte.kotter.foundation.session
import com.varabyte.kotter.foundation.text.*
import com.varabyte.kotter.runtime.Session
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt

private fun Double.fmt(precision: Int = 5): String {
    val threshold = 10.0.pow(-precision) / 2

    return when {
        this > 0 && this < threshold -> "0.${"0".repeat(precision - 1)}1"

        this < 0 && abs(this) < threshold -> "-0.${"0".repeat(precision - 1)}1"

        abs(this - this.roundToInt()) < threshold -> this.roundToInt().toString()

        else -> "%.${precision}f".format(this).trimEnd('0').trimEnd('.').trimEnd(',')
    }
}

private fun Double.fmtAligned(precision: Int = 5): String {
    val formatted = this.fmt(precision)
    return if (this >= 0) " $formatted" else formatted
}

fun printTitle(session: Session) {
    val title = """
  ╔══════════════════════════╗
  ║     MATRIX  SOLVER       ║
  ╚══════════════════════════╝
    """.trimIndent()

    session.section {
        textLine()
        cyan { bold { textLine(title) } }
        textLine()
    }.run()
}

private fun printSectionHeader(session: Session, title: String) {
    session.section {
        cyan { bold { text("  ▸ ") } }
        yellow { bold { underline { textLine(title) } } }
    }.run()
}

fun printMatrix(session: Session, matrix: Matrix) {
    printSectionHeader(session, "Source matrix")
    session.section {
        matrix.matrix.forEach { rowObj ->
            val row = rowObj.row
            val coeffs = row.subList(0, row.size - 1)
            val rhs = row.last()

            cyan { text("  │") }
            coeffs.forEach { v ->
                white { text(v.fmt().padStart(13)) }
            }
            cyan { text("  │") }
            yellow { bold { text(rhs.fmt().padStart(13)) } }
            cyan { textLine("  │") }
        }
        textLine()
    }.run()
}

fun printTriangularMatrix(session: Session, matrix: Matrix) {
    printSectionHeader(session, "Triangular matrix")
    session.section {
        matrix.matrix.forEach { rowObj ->
            val row = rowObj.row
            val coeffs = row.subList(0, row.size - 1)
            val rhs = row.last()

            cyan { text("  │") }
            coeffs.forEach { v ->
                if (v == 0.0) {
                    white { text(v.fmt().padStart(13)) }
                } else {
                    green { bold { text(v.fmt().padStart(13)) } }
                }
            }
            cyan { text("  ║") }
            yellow { bold { text(rhs.fmt().padStart(13)) } }
            cyan { textLine("  │") }
        }
        textLine()
    }.run()
}

fun printDeterminant(session: Session, det: Double) {
    session.section {
        textLine()
        cyan { textLine("  ╔════════════════════════════════════╗") }
        cyan { text("  ║  ") }
        yellow { bold { text("Determinant:  ") } }
        white { bold { text(det.fmt().padEnd(20)) } }
        cyan { textLine("║") }
        cyan { textLine("  ╚════════════════════════════════════╝") }
        textLine()
    }.run()
}

fun printVector(session: Session, vector: List<Double>, title: String = "", symbol: String = "x") {

    if (title.isNotBlank()) printSectionHeader(session, title)
    session.section {
        vector.forEachIndexed { i, v ->
            cyan { text("  ${symbol}${i + 1}") }
            white { text(" = ") }
            yellow { bold { textLine(v.toString()) } }
        }
        textLine()
    }.run()
}

fun printText(session: Session, text: String) {
    session.section {
        scopedState {
            invert()
            cyan { bold { textLine("$text") } }
        }
        textLine()
    }.run()
}

fun printHelp() {
    session {
        printTitle(this)
        section {
            textLine(
                """
                 
                 | Usage: java -jar app.jar [OPTIONS]
                 |
                 | Options:
                 |   -h                   Show help message
                 |   -f <filename>        Read matrix from file (ignores other options)
                 |   -r                   Generate random matrix
                 |   -n <number>          Set matrix size
                 |
                 | File format:
                 |   First line: matrix size n
                 |   Next n lines: n + 1 numbers per line A-coffs and B-value
                 
            """.trimIndent()
            )

        }.run()
    }
}