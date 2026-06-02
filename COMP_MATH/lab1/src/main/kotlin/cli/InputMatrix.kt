package com.example.cli

import com.varabyte.kotter.foundation.input.Keys
import com.varabyte.kotter.foundation.input.onKeyPressed
import com.varabyte.kotter.foundation.runUntilSignal
import com.varabyte.kotter.foundation.text.*
import com.varabyte.kotter.runtime.Session

private const val CELL_WIDTH = 10

private val DIGIT_MAP = mapOf(
    Keys.DIGIT_0 to '0', Keys.DIGIT_1 to '1', Keys.DIGIT_2 to '2',
    Keys.DIGIT_3 to '3', Keys.DIGIT_4 to '4', Keys.DIGIT_5 to '5',
    Keys.DIGIT_6 to '6', Keys.DIGIT_7 to '7', Keys.DIGIT_8 to '8',
    Keys.DIGIT_9 to '9'
)

fun formatNumber(value: Double): String = when {
    value == 0.0       -> "0"
    value % 1.0 == 0.0 -> value.toInt().toString()
    else               -> "%.10f".format(value).trimEnd('0').trimEnd('.')
}

fun formatCurrent(value: Double): String {
    val s = formatNumber(value)
    return if (s == "0") "" else s
}

fun isValidInput(input: String): Boolean {
    if (input.isEmpty() || input == "-") return true
    if (input.count { it == '.' } > 1) return false
    val digits = input.removePrefix("-")
    if (digits.length > 1 && digits.startsWith("0") && digits[1].isDigit()) return false
    return true
}

fun centreInCell(text: String, width: Int = CELL_WIDTH): String =
    text.padStart((text.length + width) / 2).padEnd(width)

private class MatrixState(val rows: Int, val cols: Int) {
    val matrix  = Array(rows) { DoubleArray(cols) { 0.0 } }
    val touched = Array(rows) { BooleanArray(cols) { false } }
    var curRow      = 0
    var curCol      = 0
    var inputBuffer = ""
    var finishing   = false

    fun commitBuffer() {
        val buf = inputBuffer.replace(',', '.')
        if (buf.isEmpty() || buf == "-") return
        val parsed = buf.trimEnd('.').toDoubleOrNull() ?: return
        matrix[curRow][curCol] = if (parsed == 0.0) 0.0 else parsed
        touched[curRow][curCol] = true
    }

    fun moveTo(row: Int, col: Int) {
        commitBuffer()
        curRow = row
        curCol = col
        inputBuffer = formatCurrent(matrix[curRow][curCol])
    }

    fun jumpTo(row: Int, col: Int) {
        curRow = row
        curCol = col
        inputBuffer = formatCurrent(matrix[curRow][curCol])
    }

    fun appendDecimal() {
        if ('.' !in inputBuffer) {
            inputBuffer += "."
            touched[curRow][curCol] = true
        }
    }

    fun appendDigit(ch: Char) {
        val safeChar = if (ch == ',') '.' else ch
        val candidate = inputBuffer + safeChar
        val digitCount = candidate.count { it.isDigit() }
        if (digitCount <= 6 && isValidInput(candidate)) {
            inputBuffer = candidate
            touched[curRow][curCol] = true
        }
    }

    fun backspace() {
        val buf = inputBuffer
        if (buf.isNotEmpty() && buf != "-") {
            inputBuffer = buf.dropLast(1)
        }
    }

    fun toMutableResult(): MutableList<MutableList<Double>> =
        matrix.map { row -> row.map { it }.toMutableList() }.toMutableList()
}

fun inputMatrix(session: Session, n: Int): MutableList<MutableList<Double>> {
    val state = MatrixState(rows = n, cols = n + 1)

    session.section {

        if (state.finishing) return@section

        val tableWidth = state.cols * (CELL_WIDTH + 2) + 1

        cyan()
        textLine("  ┌──────────────────────────────────────┐")
        textLine("  │              CONTROLS                │")
        textLine("  │──────────────────────────────────────│")
        textLine("  │  ↑ ↓ ← →   Navigate cells            │")
        textLine("  │  Enter     Next cell                 │")
        textLine("  │  Esc       Finish input              │")
        textLine("  └──────────────────────────────────────┘")
        white()

        textLine()

        val title = "── MATRIX INPUT ──"
        textLine(title.padStart((title.length + tableWidth) / 2).padEnd(tableWidth))
        textLine()


        for (i in 0 until state.rows) {
            for (j in 0 until state.cols) {
                val isActive  = (i == state.curRow && j == state.curCol)
                val isTouched = state.touched[i][j]
                val isZero    = state.matrix[i][j] == 0.0
                val rawText   = if (isActive) state.inputBuffer
                else formatNumber(state.matrix[i][j])
                val cellText  = centreInCell(rawText.ifEmpty { "0" })

                when {
                    isActive             -> { yellow(layer = ColorLayer.BG); black() }
                    !isTouched && isZero -> { black(layer = ColorLayer.BG); black(isBright = true) }
                    else                 -> { black(layer = ColorLayer.BG); white() }
                }

                text(" $cellText ")

                black(layer = ColorLayer.BG); white()

                if (j == state.cols - 2) { cyan(); text("│"); white() }
            }
            textLine()
        }

    }.runUntilSignal {
        onKeyPressed {
            with(state) {
                when (key) {
                    Keys.UP    -> if (curRow > 0)        moveTo(curRow - 1, curCol)
                    Keys.DOWN  -> if (curRow < rows - 1) moveTo(curRow + 1, curCol)
                    Keys.LEFT  -> if (curCol > 0)        moveTo(curRow, curCol - 1)
                    Keys.RIGHT -> if (curCol < cols - 1) moveTo(curRow, curCol + 1)

                    Keys.MINUS -> {
                        inputBuffer = if ('-' in inputBuffer)
                            inputBuffer.removePrefix("-") else "-$inputBuffer"
                        touched[curRow][curCol] = true
                    }

                    Keys.PERIOD, Keys.COMMA -> appendDecimal()

                    Keys.BACKSPACE -> backspace()

                    Keys.ENTER -> {
                        commitBuffer()
                        when {
                            curCol < cols - 1 -> jumpTo(curRow, curCol + 1)
                            curRow < rows - 1 -> jumpTo(curRow + 1, 0)
                            else              -> { finishing = true; rerender(); signal() }
                        }
                    }

                    Keys.ESC -> {
                        commitBuffer()
                        finishing = true
                        rerender()
                        signal()
                    }

                    else -> {
                        val ch = DIGIT_MAP[key]
                        if (ch != null) {
                            appendDigit(ch)
                        } else {
                            val raw = key.toString()
                            if (raw == "." || raw == ",") appendDecimal()
                        }
                    }
                }
            }
            rerender()
        }
    }

    return state.toMutableResult()
}