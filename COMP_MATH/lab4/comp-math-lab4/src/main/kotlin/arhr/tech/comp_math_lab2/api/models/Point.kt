package arhr.tech.comp_math_lab2.api.models

import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class Point(
    val x: BigDecimal?,
    val y: BigDecimal?,
)