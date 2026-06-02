package com.example.math

import org.apache.commons.math3.linear.*

class GaussSolver(private val aMatrix: MutableList<MutableList<Double>>, private val bVector: MutableList<Double>) {

    init {
        require(aMatrix.size == bVector.size) {
            "Matrix size must be equal b vector size"
        }
    }

    fun solve(): MutableList<Double> {
        val matrixArray = Array(aMatrix.size) { i ->
            DoubleArray(aMatrix[i].size) { j ->
                aMatrix[i][j]
            }
        }

        val bArray = DoubleArray(bVector.size) { i ->
            bVector[i]
        }

        val matrix = Array2DRowRealMatrix(matrixArray)
        val vector = ArrayRealVector(bArray)

        val luDecomposition = LUDecomposition(matrix)
        return luDecomposition.solver.solve(vector).toArray().toMutableList()
    }

    fun determinant(): Double {
        val matrixArray = Array(aMatrix.size) { i ->
            DoubleArray(aMatrix[i].size) { j ->
                aMatrix[i][j]
            }
        }

        val matrix = Array2DRowRealMatrix(matrixArray)
        val luDecomposition = LUDecomposition(matrix)
        return luDecomposition.determinant
    }
}