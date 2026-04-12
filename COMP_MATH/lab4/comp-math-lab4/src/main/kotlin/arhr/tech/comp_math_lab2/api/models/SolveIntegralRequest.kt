package arhr.tech.comp_math_lab2.api.models

import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class SolveIntegralRequest(
    @field:NotNull(message = "Не выбрано уравнение")
    val equationId: Int?,

    @field:NotNull(message = "Не выбран метод")
    val methodId: Int?,

    @field:NotNull(message = "Граница a обязательна")
    val a: BigDecimal?,

    @field:NotNull(message = "Граница b обязательна")
    val b: BigDecimal?,

    @field:NotNull(message = "Точность не указана")
    @field:DecimalMax("1.0", message = "Точность слишком грубая")
    @field:DecimalMin("0", message = "Точность должна быть положительной")
    val eps: BigDecimal?
    )
{
    @AssertTrue(message = "Граница a должна быть меньше границы b")
    fun isIntervalValid(): Boolean {
        if (a == null || b == null) return true
        return a < b
    }

    @AssertTrue(message = "Длина интервала [a, b] не должна превышать 10000")
    fun isIntervalLengthValid(): Boolean {
        if (a == null || b == null) return true
        val length = b.subtract(a).abs()
        return length <= BigDecimal("10000")
    }
}