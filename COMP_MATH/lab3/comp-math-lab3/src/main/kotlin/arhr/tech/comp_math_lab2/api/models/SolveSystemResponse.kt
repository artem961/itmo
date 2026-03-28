package arhr.tech.comp_math_lab2.api.models

import java.math.BigDecimal

data class SolveSystemResponse(
    val x: BigDecimal? = null,
    val y: BigDecimal? = null,
    val dx: BigDecimal? = null,
    val dy: BigDecimal? = null,
    val iterations: Int? = null,
    val error: String? = null
) {

}