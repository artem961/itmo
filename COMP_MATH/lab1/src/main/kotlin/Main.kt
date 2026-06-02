package com.example

import com.example.cli.inputMatrix
import com.example.cli.inputNumber
import com.example.cli.printDeterminant
import com.example.cli.printHelp
import com.example.cli.printMatrix
import com.example.cli.printText
import com.example.cli.printTitle
import com.example.cli.printTriangularMatrix
import com.example.cli.printVector
import com.example.cli.readMatrixFromFile
import com.example.math.GaussSolver
import com.example.math.models.Matrix
import com.varabyte.kotter.foundation.input.Keys
import com.varabyte.kotter.foundation.input.onKeyPressed
import com.varabyte.kotter.foundation.runUntilSignal
import com.varabyte.kotter.foundation.session
import com.varabyte.kotter.foundation.text.ColorLayer
import com.varabyte.kotter.foundation.text.black
import com.varabyte.kotter.foundation.text.green
import com.varabyte.kotter.foundation.text.textLine
import com.varabyte.kotter.foundation.text.white
import com.varabyte.kotter.foundation.text.yellow
import com.varabyte.kotter.runtime.Session
import kotlin.system.exitProcess


fun main(args: Array<String>) {
    try {
        val arguments = parseArgs(args)

        when {
            arguments.containsKey("h") -> {
                printHelp()
                exitProcess(0)
            }

            arguments.containsKey("f") -> {
                startSessionWithFile(arguments.getValue("f"))
            }

            arguments.containsKey("r") -> {
                val n = arguments["n"]?.toIntOrNull()
                startSessionRandom(n)
            }

            else -> {
                val n = arguments["n"]?.toIntOrNull()
                startSessionInteractive(n)
            }
        }
    } catch (e: IllegalArgumentException) {
        println("Error: ${e.message}")
    } catch (e: NumberFormatException) {
        println("Error: Invalid number format for -n argument")
    } catch (e: Exception) {
        println("Unexpected error: ${e.message}")
    }
}

fun startSessionInteractive(n: Int? = null) {
    session {
        printTitle(this)
        do {

            val matrixSize = n ?: inputNumber(this)
            if (n != null) {
                printText(this, "Matrix size: $n\n")
            }

            val matrix = Matrix(inputMatrix(this, matrixSize))
            printResults(this, matrix)
        } while (repeatInput(this))
    }
}

fun startSessionWithFile(file: String) {
    val matrix = try {
        Matrix(readMatrixFromFile(file))
    } catch (e: Exception) {
        println(e.message)
        return
    }

    session {
        printTitle(this)
        printResults(this, matrix)
    }
}

fun startSessionRandom(n: Int? = null) {
    session {
        printTitle(this)

        do {
            val matrixSize = n ?: inputNumber(this)
            if (n != null) {
                printText(this, "Matrix size: $n\n")
            }

            val matrix = Matrix.randomMatrix(matrixSize)
            printResults(this, matrix)
        } while (repeatInput(this))
    }
}

fun parseArgs(args: Array<String>): Map<String, String> {
    try {
        val ans: MutableMap<String, String> = mutableMapOf()
        var i = 0

        while (i < args.size) {
            if (args[i].startsWith("-")) {
                val key = args[i].removePrefix("-")
                if (i + 1 < args.size && !args[i + 1].startsWith("-")) {
                    ans[key] = args[i + 1]
                    i += 2
                } else {
                    ans[key] = ""
                    i++
                }
            } else {
                i++
            }
        }

        return ans
    } catch (e: Exception) {
        throw Exception("Invalid program arguments: ${e.message}")
    }
}

fun printResults(session: Session, matrix: Matrix) {
    val originalMatrix = matrix.copy()
    try {
        printMatrix(session, originalMatrix)
        matrix.checkConsistency()
        matrix.makeTriangular()
        val det = matrix.determinant()
        val xVector = matrix.solve()
        val dis = matrix.discrepancy()

        printTriangularMatrix(session, matrix)
        printDeterminant(session, det)
        printVector(session, xVector, "x vector: ")
        printVector(session, dis, "discrepancy vector: ", symbol = "r")

        printText(session, "\n\n\nApache math solution: ")
        val gaussSolver = getGaussSolver(originalMatrix)
        printVector(session, gaussSolver.solve(), "x vector: ")
        printDeterminant(session, gaussSolver.determinant())
    } catch (e: Exception) {
        printText(session, e.message.toString())
    }
}

fun getGaussSolver(matrix: Matrix): GaussSolver {
    val n = matrix.matrix.size

    val aMatrix = MutableList(n) { i ->
        MutableList(n) { j ->
            matrix.matrix[i][j]
        }
    }

    val bVector = MutableList(n) { i ->
        matrix.matrix[i][n]
    }

    return GaussSolver(aMatrix, bVector)
}

fun repeatInput(session: Session): Boolean {
    var ex: Boolean = true
    var finished: Boolean = false
    session.section {
        if (finished) return@section
        if (ex) {
            black()
            green(layer = ColorLayer.BG, scopedBlock = { textLine("Exit") })
            white(scopedBlock = { textLine("Retry") })
        } else {
            white(scopedBlock = { textLine("Exit") })
            black()
            green(layer = ColorLayer.BG, scopedBlock = { textLine("Retry") })
        }
    }.runUntilSignal {
        onKeyPressed {
            when (key) {
                Keys.UP -> {
                    ex = true
                }

                Keys.DOWN -> {
                    ex = false
                }

                Keys.ENTER -> {
                    finished = true; rerender(); signal()
                }
            }
            rerender()
        }

    }
    return !ex
}