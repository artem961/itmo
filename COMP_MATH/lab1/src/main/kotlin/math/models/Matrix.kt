package com.example.math.models

class Matrix(val matrix: MutableList<MatrixRow>) {
    val size = matrix.size
    private var permutations = 0

    constructor(matrix: List<MutableList<Double>>, tmp: String = "") : this(
        matrix.map { MatrixRow(it) }.toMutableList()
    )

    fun print() {
        matrix.forEach { it.print() }
    }

    private fun swapRows(from: Int, to: Int) {
        val tmp = matrix[from]
        matrix[from] = matrix[to]
        matrix[to] = tmp
        permutations++
    }

    fun makeTriangular() {
        if (size == 1) return

        for (i in 0 until size) {

            if (matrix[i][i] == 0.0) {
                for (j in i + 1 until size) {
                    if (matrix[j][i] != 0.0) {
                        swapRows(i, j)
                        break
                    }
                }
            }

            val mainRow = matrix[i]

            for (row in i + 1 until size) {
                val k = -matrix[row][i] / mainRow[i]
                matrix[row] = matrix[row] + (mainRow * k)
            }
        }
    }

    fun solve(): List<Double> {
        val matrixCopy = copy()
        matrixCopy.makeTriangular()
        matrixCopy.checkConsistency()

        val xVector = MutableList(size) { 0.0 }

        for (i in size - 1 downTo 0) {
            val b = matrixCopy.matrix[i][size]
            val a = matrixCopy.matrix[i][i]

            var sum = 0.0
            for (j in i + 1 until size) {
                sum += matrixCopy.matrix[i][j] * xVector[j]
            }

            xVector[i] = (b - sum) / a
        }

        return xVector
    }

    fun determinant(): Double {
        val matrixCopy = copy()
        matrixCopy.makeTriangular()

        val sign = if (matrixCopy.permutations % 2 == 0) 1.0 else -1.0
        var det = sign

        for (i in 0 until size) {
            det *= matrixCopy.matrix[i][i]
        }
        return det
    }

    fun discrepancy(): List<Double> {
        val xVector = solve()
        val disVector = MutableList(size) { 0.0 }

        for (i in 0 until size) {
            var sum = 0.0
            for (j in 0 until size) {
                sum += xVector[j] * matrix[i][j]
            }
            disVector[i] = sum - matrix[i][size]
        }
        return disVector
    }

    fun checkConsistency() {
        val rankA = rank(withoutBColumn())
        val rankAB = rank(this)

        when {
            rankAB != rankA -> throw IllegalStateException("Matrix has 0 solutions")
            rankA < size -> throw IllegalStateException("Matrix has infinite solutions")
            rankA == 0 -> throw IllegalStateException("Matrix has all zeros")
        }
    }

    private fun rank(matrix: Matrix): Int {
        val matrixCopy = matrix.copy()
        matrixCopy.makeTriangular()

        return matrixCopy.matrix.count { !it.isNullable() }
    }

    private fun withoutBColumn(): Matrix {
        val newRows = matrix.map { row ->
            MatrixRow(row.row.dropLast(1).toMutableList())
        }.toMutableList()
        return Matrix(newRows)
    }

    fun copy(): Matrix {
        val newRows = matrix.map { row ->
            MatrixRow(row.row.toMutableList())
        }.toMutableList()
        return Matrix(newRows)
    }


    companion object {
        fun randomMatrix(n: Int): Matrix {
            val rows: MutableList<MatrixRow> = mutableListOf()
            (0 until n).forEach { _ -> rows.add(MatrixRow.randomRow(n + 1)) }

            return Matrix(rows)
        }
    }
}