package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.api.models.SolveIntegralResponse
import arhr.tech.comp_math_lab2.dto.SolveIntegralRequestDto
import arhr.tech.comp_math_lab2.utils.Equation
import arhr.tech.comp_math_lab2.utils.SingularityPoint
import arhr.tech.comp_math_lab2.utils.SingularityType
import java.math.BigDecimal
import java.math.MathContext

abstract class AbstractRungeIntegralSolver : IntegralSolver {
    abstract val rungeCoff: Int
    private val delta = BigDecimal("0.00001")

    override fun solve(
        request: SolveIntegralRequestDto
    ): SolveIntegralResponse {
        val equation = request.equation
        val a = request.a
        val b = request.b
        val eps = request.eps

        val singularPoints = findSingularities(equation, a, b)
        validateConvergence(singularPoints, equation)

        val segments = splitToSegments(a, b, singularPoints)

        segments.forEach { segment -> println(segment) }
        var n = 4
        var value = calculateOnSegments(equation, segments, n)

        while (true) {
            println("$n")
            n *= 2
            val newValue = calculateOnSegments(equation, segments, n)

            val denominator = BigDecimal.TWO.pow(rungeCoff).subtract(BigDecimal.ONE)
            val error = newValue.subtract(value).abs().divide(denominator, MathContext.DECIMAL64)

            value = newValue

            if (error < eps)
                return createResponse(value, n)

            if (n > 1000000)
                throw RuntimeException("Достигнут лимит разбиений")
        }
    }


    abstract fun methodImpl(
        eq: Equation,
        a: BigDecimal,
        b: BigDecimal,
        h: BigDecimal,
        n: Int
    ): BigDecimal

    private fun splitToSegments(
        a: BigDecimal,
        b: BigDecimal,
        points: List<SingularityPoint>
    ): List<Pair<BigDecimal, BigDecimal>> {
        val coords = (points.map { it.x } + a + b)
            .distinct()
            .sorted()

        val singularityCoords = points.map { it.x }.toSet()
        val segments = mutableListOf<Pair<BigDecimal, BigDecimal>>()

        for (i in 0 until coords.size - 1) {
            var start = coords[i]
            var end = coords[i + 1]

            if (singularityCoords.contains(start))
                start = start.add(delta)


            if (singularityCoords.contains(end))
                end = end.subtract(delta)


            if (start < end)
                segments.add(start to end)
        }

        return segments
    }

    private fun calculateOnSegments(
        eq: Equation,
        segments: List<Pair<BigDecimal, BigDecimal>>,
        n: Int
    ): BigDecimal {
        var totalSum = BigDecimal.ZERO
        for (seg in segments) {
            val h = calcH(seg.first, seg.second, n)
            totalSum = totalSum.add(methodImpl(eq, seg.first, seg.second, h, n))
        }
        return totalSum
    }

    fun findSingularities(
        eq: Equation,
        a: BigDecimal,
        b: BigDecimal
    ): List<SingularityPoint> {
        val points = mutableListOf<SingularityPoint>()

        if (eq.isSingularity(a))
            points.add(SingularityPoint(a, SingularityType.LEFT))
        if (eq.isSingularity(b))
            points.add(SingularityPoint(b, SingularityType.RIGHT))

        val step = BigDecimal("0.001")
        var x = a.add(step)
        while (x < b) {
            if (eq.isSingularity(x))
                points.add(SingularityPoint(x, SingularityType.INTERIOR))
            x = x.add(step)
        }

        return points
    }

    fun validateConvergence(points: List<SingularityPoint>, equation: Equation) {
        val breakPoints = points
            .filter { !checkConvergence(equation, it) }
        if (breakPoints.isNotEmpty()) {
            val pointsString = breakPoints.joinToString("") { "x = ${it.x}" }
            throw RuntimeException("Интеграл расходится. Точки разрыва: $pointsString")
        }
    }

    fun checkConvergence(eq: Equation, point: SingularityPoint): Boolean {
        val eps = BigDecimal("0.01")

        fun calcArea(start: BigDecimal, end: BigDecimal): BigDecimal {
            val width = end.subtract(start).abs()
            val mid = start.add(end).divide(BigDecimal.TWO, MathContext.DECIMAL64)
            return try {
                val fMid = eq.f(mid).abs()
                fMid.multiply(width, MathContext.DECIMAL64)
            } catch (e: Exception) {
                BigDecimal.valueOf(Double.MAX_VALUE)
            }
        }

        var a = point.x.add(delta)
        var b = point.x.add(delta).add(delta)
        return when (point.type) {
            SingularityType.LEFT -> {
                val area = calcArea(a, b)
                area < eps
            }

            SingularityType.RIGHT -> {
                a = point.x.subtract(delta)
                b = point.x.subtract(delta).subtract(delta)
                val area = calcArea(a, b)
                area < eps
            }

            SingularityType.INTERIOR -> {
                val areaLeft = calcArea(a, b)
                val aNew = point.x.subtract(delta).subtract(delta)
                val bNew = point.x.subtract(delta)
                val areaRight = calcArea(aNew, bNew)
                areaLeft < eps && areaRight < eps
            }
        }
    }

    fun calcH(
        a: BigDecimal,
        b: BigDecimal,
        n: Int
    ): BigDecimal {
        return b.subtract(a).abs()
            .divide(BigDecimal(n), MathContext.DECIMAL64)
    }

    fun createResponse(value: BigDecimal, splits: Int): SolveIntegralResponse {
        return SolveIntegralResponse(value = value, splits = splits)
    }
}