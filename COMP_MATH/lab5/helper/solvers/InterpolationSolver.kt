package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.dto.InterpolationRequestDto
import arhr.tech.comp_math_lab2.dto.InterpolationResult
import arhr.tech.comp_math_lab2.dto.PointDto
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

interface InterpolationSolver {
    fun solve(request: InterpolationRequestDto): InterpolationResult
    fun supports(type: InterpolationType): Boolean

    fun normalizePoints(points: List<PointDto>): List<PointDto> {
        val sortedPoints = points.sortedBy { it.x }
        for (i in 1 until sortedPoints.size) {
            require(sortedPoints[i].x != sortedPoints[i - 1].x) { "В исходных данных обнаружены две одинаковые точки по x!" }
        }
        return sortedPoints
    }

    fun BigDecimal.round(): BigDecimal {
        if (this.abs() <= BigDecimal("0.0000001")) return BigDecimal.ZERO
        return this.round(MathContext(4, RoundingMode.HALF_UP))
    }

    fun checkEquidistant(points: List<PointDto>) {
        val n = points.size
        if (n < 2) return
        val h = points[1].x - points[0].x
        require(h != BigDecimal.ZERO) { "В исходных данных обнаружены две одинаковые точки по x!" }
        for (i in 2 until n) {
            require(points[i].x != points[i - 1].x) { "В исходных данных обнаружены две одинаковые точки по x!" }
            require(points[i].x - points[i - 1].x == h) { "Узлы должны быть равноотстоящими" }
        }
    }

    fun factorial(k: Int, mc: MathContext): BigDecimal {
        var result = BigDecimal.ONE
        for (i in 2..k) {
            result = result.multiply(BigDecimal(i), mc)
        }
        return result
    }

    companion object {
        fun calculateFiniteDifferences(points: List<PointDto>): List<List<BigDecimal>> {
            val n = points.size
            val diffs = mutableListOf(points.map { it.y })
            for (order in 1 until n) {
                val prev = diffs.last()
                diffs.add(prev.zipWithNext { a, b -> b - a })
            }
            return diffs
        }

        fun generateGraphicPoints(points: List<PointDto>, evaluate: (BigDecimal) -> BigDecimal): List<PointDto> {
            if (points.isEmpty()) return emptyList()
            val minX = points.minOf { it.x }
            val maxX = points.maxOf { it.x }
            val step = (maxX - minX).divide(BigDecimal(99), MathContext(16, RoundingMode.HALF_UP))
            if (step <= BigDecimal.ZERO) return listOf(PointDto(minX, evaluate(minX)))

            val graphicPoints = mutableListOf<PointDto>()
            var currentX = minX
            for (i in 0..99) {
                graphicPoints.add(PointDto(currentX, evaluate(currentX)))
                currentX = currentX.add(step)
            }
            return graphicPoints
        }
    }
}