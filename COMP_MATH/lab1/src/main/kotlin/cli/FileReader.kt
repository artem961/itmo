package com.example.cli

import java.io.File

fun readMatrixFromFile(fileName: String): MutableList<MutableList<Double>> {
    var n: Int;

    val lines: List<String> = File(fileName).readLines()
    if (lines.isEmpty()) throw Exception("Empty file")

    var matrix: MutableList<MutableList<Double>> = lines
        .map { s ->
            s.trim().split(Regex("\\s+"))
                .map { s -> s.toDouble() }
                .toMutableList()
        }
        .toMutableList()
    n = matrix[0][0].toInt();
    matrix = matrix.subList(1, matrix.size).toMutableList()
    validate(matrix, n);

    return matrix
}

fun validate(matrix: MutableList<MutableList<Double>>, n: Int) {
    require(matrix.size == n) { "Rows count must be equal N" }
    matrix.forEach { it -> require(it.size == n + 1) { "Columns count must be equal N + 1" } }
    require(n > 0 && n <= 20) { "N range must be from 1 to 20" }
}