package arhr.tech.comp_math_lab2.utils

import java.math.BigDecimal

class EquationSystem(
    val eq1: BiVariableEquation,
    val eq2: BiVariableEquation,
    val id: Int
) {

    fun calculateValues(x: BigDecimal, y: BigDecimal): Pair<BigDecimal, BigDecimal> {
        return Pair(eq1.f(x, y), eq2.f(x, y))
    }

    fun getJacobianMatrix(x: BigDecimal, y: BigDecimal): List<List<BigDecimal>> {
        return listOf(
            listOf(eq1.dfdx(x, y), eq1.dfdy(x, y)),
            listOf(eq2.dfdx(x, y), eq2.dfdy(x, y))
        )
    }
}