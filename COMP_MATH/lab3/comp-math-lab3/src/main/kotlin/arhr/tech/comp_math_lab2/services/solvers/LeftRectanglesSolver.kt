package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.utils.Equation
import java.math.BigDecimal

class LeftRectanglesSolver: AbstractRungeIntegralSolver() {
    override val rungeCoff: Int
        get() = 1

    override fun methodImpl(
        eq: Equation,
        a: BigDecimal,
        b: BigDecimal,
        h: BigDecimal
    ): BigDecimal {
        TODO("Not yet implemented")
    }

    override fun supports(type: SolveType): Boolean {
        return type == SolveType.LEFT_RECTANGLES
    }

}