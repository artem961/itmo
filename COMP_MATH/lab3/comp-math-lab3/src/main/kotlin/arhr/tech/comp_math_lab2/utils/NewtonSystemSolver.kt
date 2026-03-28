package arhr.tech.comp_math_lab2.utils

import arhr.tech.comp_math_lab2.api.models.SolveSystemResponse
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

@Component
class NewtonSystemSolver {
    private val mc = MathContext(32, RoundingMode.HALF_UP)

    fun solve(
        system: EquationSystem,
        x0: BigDecimal,
        y0: BigDecimal,
        eps: BigDecimal
    ): SolveSystemResponse {
        var x = x0
        var y = y0
        var Dx: BigDecimal = BigDecimal.ZERO
        var Dy: BigDecimal = BigDecimal.ZERO
        var iterations = 0

        do {
            val det = det(system, x, y)

            if (det.signum() == 0) {
                throw RuntimeException("Определитель Якобиана равен нулю. Метод Ньютона не сошелся.")
            }

            val dX = deltaX(system, x, y, det)
            val dY = deltaY(system, x, y, det)

            if (dX.abs() > BigDecimal("1000") || dY.abs() > BigDecimal("1000")) {
                throw RuntimeException("Метод расходится. Попробуйте выбрать точку x0, y0 ближе к корню.")
            }


            val maxDiff = dX.abs().max(dY.abs())

            val nextX = x.add(dX)
            val nextY = y.add(dY)

            iterations++

            Dx = x.subtract(nextX)
            Dy = y.subtract(nextY)
            x = nextX
            y = nextY

            if (iterations > 1000) throw Exception("Достигнут лимит итераций. Метод не сошёлся")
        } while (maxDiff >= eps)


        val finalF = system.eq1.f(x, y).abs()
        val finalG = system.eq2.f(x, y).abs()

        if (finalF > eps || finalG > eps) {
            throw RuntimeException("Метод не сошелся к корню за отведенное число итераций. " +
                    "Невязка: f=${finalF.toPlainString()}, g=${finalG.toPlainString()}")
        }

        return SolveSystemResponse(
            x = x.setScale(eps.scale(), RoundingMode.HALF_UP),
            y = y.setScale(eps.scale(), RoundingMode.HALF_UP),
            dx = Dx.setScale(eps.scale(), RoundingMode.HALF_UP),
            dy = Dy.setScale(eps.scale(), RoundingMode.HALF_UP),
            iterations = iterations,
        )
    }


    private fun det(system: EquationSystem, x: BigDecimal, y: BigDecimal): BigDecimal {
        val first = system.eq1.dfdx(x, y).multiply(system.eq2.dfdy(x, y))
        val second = system.eq1.dfdy(x, y).multiply(system.eq2.dfdx(x, y))
        return first.subtract(second)
    }


    private fun deltaX(system: EquationSystem, x: BigDecimal, y: BigDecimal, det: BigDecimal): BigDecimal {
        val f = system.eq1.f(x, y)
        val g = system.eq2.f(x, y)
        val dfdy = system.eq1.dfdy(x, y)
        val dgdy = system.eq2.dfdy(x, y)

        val numerator = f.negate().multiply(dgdy).subtract(dfdy.multiply(g.negate()))
        return numerator.divide(det, mc)
    }


    private fun deltaY(system: EquationSystem, x: BigDecimal, y: BigDecimal, det: BigDecimal): BigDecimal {
        val f = system.eq1.f(x, y)
        val g = system.eq2.f(x, y)
        val dfdx = system.eq1.dfdx(x, y)
        val dgdx = system.eq2.dfdx(x, y)

        val numerator = dfdx.multiply(g.negate()).subtract(f.negate().multiply(dgdx))
        return numerator.divide(det, mc)
    }
}