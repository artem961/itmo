package arhr.tech.comp_math_lab2.api.models

import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class InterpolationRequest(
    @field:NotNull(message = "Не заданы точки")
    val points: List<Point>,

    @field:NotNull(message = "Не задано значение аргумента X")
    val x: BigDecimal
) {
    @AssertTrue(message = "Количество точек должно быть от 2 до 20")
    fun isIntervalValid(): Boolean {
        return !(points.size < 2 || points.size > 20)
    }
}