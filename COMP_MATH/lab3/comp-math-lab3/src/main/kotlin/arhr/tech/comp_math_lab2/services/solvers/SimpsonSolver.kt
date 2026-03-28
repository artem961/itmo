package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.utils.Equation
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.MathContext

@Component
class SimpsonSolver : AbstractRungeIntegralSolver() {
    override val rungeCoff: Int
        get() = 4

    override fun methodImpl(
        eq: Equation,
        a: BigDecimal,
        b: BigDecimal,
        h: BigDecimal,
        n: Int
    ): BigDecimal {
        require(n % 2 == 0) { "Для метода Симпсона n должно быть чётным" }
        var sum = eq.f(a).add(eq.f(b))

        for (i in 1 until n) {
            val x = a.add((BigDecimal(i)).multiply(h))
            var k = BigDecimal("4")
            if (i%2 ==0)
                k = BigDecimal("2")
            sum = sum.add(eq.f(x).multiply(k))
        }

        val k = h.divide(BigDecimal("3"), MathContext.DECIMAL64)
        return sum.multiply(k)
    }

    override fun supports(type: SolveType): Boolean {
        return type == SolveType.SIMPSON
    }
}