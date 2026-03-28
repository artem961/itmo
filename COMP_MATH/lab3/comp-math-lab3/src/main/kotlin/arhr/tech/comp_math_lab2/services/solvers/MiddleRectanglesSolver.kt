package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.utils.Equation
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class MiddleRectanglesSolver : AbstractRungeIntegralSolver() {
    override val rungeCoff: Int
        get() = 2

    override fun methodImpl(
        eq: Equation,
        a: BigDecimal,
        b: BigDecimal,
        h: BigDecimal,
        n: Int
    ): BigDecimal {
        var sum = BigDecimal.ZERO

        for (i in 1 .. n) {
            val x = a.add((BigDecimal(i).subtract(BigDecimal("0.5"))).multiply(h))
            sum = sum.add(eq.f(x))
        }
        return sum.multiply(h)
    }

    override fun supports(type: SolveType): Boolean {
        return type == SolveType.MIDDLE_RECTANGLES
    }
}