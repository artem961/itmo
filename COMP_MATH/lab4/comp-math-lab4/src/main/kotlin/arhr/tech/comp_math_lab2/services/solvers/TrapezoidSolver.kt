package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.utils.Equation
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.MathContext

@Component
class TrapezoidSolver : AbstractRungeIntegralSolver() {
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

        for (i in 1 until n) {
            val x = a.add((BigDecimal(i)).multiply(h))
            sum = sum.add(eq.f(x))
        }
        val k = (eq.f(a).add(eq.f(b))).divide(BigDecimal.TWO, MathContext.DECIMAL64)
        return sum.add(k).multiply(h)
    }

    override fun supports(type: SolveType): Boolean {
        return type == SolveType.TRAPEZOID
    }
}