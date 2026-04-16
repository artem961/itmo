package arhr.tech.comp_math_lab2.api.models

import jakarta.validation.Valid
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.NotNull

data class ApproximationRequest(
    @field:NotNull(message = "Не заданы точки")
    val points: List<Point>
) {
    @AssertTrue(message = "Количество точек должно быть от 8 до 12")
    fun isIntervalValid(): Boolean {
        return !(points.size < 8 || points.size > 12)
    }
}