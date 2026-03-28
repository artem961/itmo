package arhr.tech.comp_math_lab2.api.models

import jakarta.validation.constraints.DecimalMax
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
    val eps: BigDecimal?
    )