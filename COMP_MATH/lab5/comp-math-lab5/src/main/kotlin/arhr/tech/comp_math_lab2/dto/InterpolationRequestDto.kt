package arhr.tech.comp_math_lab2.dto

import java.math.BigDecimal

data class InterpolationRequestDto(
    val points: List<PointDto>,
    val x: BigDecimal
)