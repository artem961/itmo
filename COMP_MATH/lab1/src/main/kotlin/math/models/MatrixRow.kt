package com.example.math.models

import kotlin.random.Random

class MatrixRow(val row: MutableList<Double>) {

    init {
        require(row.isNotEmpty()) { "Row cannot be empty" }
    }

    fun print() {
        println(row.joinToString(" "))
    }

    operator fun times(n: Double): MatrixRow {
        return MatrixRow(row.map { it * n }.toMutableList())
    }

    operator fun plus(row: MatrixRow): MatrixRow {
        require(this.row.size == row.row.size) { "Rows must be same size" }
        val newRow = this.row.indices.map { this.row[it] + row.row[it] }.toMutableList()
        return MatrixRow(newRow)
    }

    operator fun minus(row: MatrixRow): MatrixRow {
        require(this.row.size == row.row.size) { "Rows must be same size" }
        val newRow = this.row.indices.map { this.row[it] - row.row[it] }.toMutableList()
        return MatrixRow(newRow)
    }

    operator fun get(index: Int): Double {
        return row[index]
    }

    fun isNullable(): Boolean {
        return row.all { it == 0.0 }
    }

    companion object {
        fun randomRow(size: Int): MatrixRow {
            var random: MutableList<Double> = mutableListOf()
            for (i in 0 until size) {
                random.add(Random.nextInt(1, 10).toDouble())
            }
            return MatrixRow(random)
        }
    }
}