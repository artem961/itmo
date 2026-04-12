package arhr.tech.comp_math_lab2.dto

import arhr.tech.comp_math_lab2.utils.Equation
import java.math.BigDecimal

data class SolveIntegralRequestDto(
    val equation: Equation,
    val a: BigDecimal,
    val b: BigDecimal,
    val eps: BigDecimal,
) {
}