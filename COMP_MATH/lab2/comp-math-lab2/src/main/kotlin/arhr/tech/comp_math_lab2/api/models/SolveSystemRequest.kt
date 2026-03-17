package arhr.tech.comp_math_lab2.api.models

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class SolveSystemRequest(
    @field:NotNull(message = "Не выбрана система уравнений")
    val systemId: Int?,

    @field:NotNull(message = "Начальное приближение x0 обязательно")
    @field:DecimalMin("-200", message = "Начальное x0 слишком мало")
    @field:DecimalMax("200", message = "Начальное x0 слишком велико")
    val x0: BigDecimal?,

    @field:NotNull(message = "Начальное приближение y0 обязательно")
    @field:DecimalMin("-200", message = "Начальное y0 слишком мало")
    @field:DecimalMax("200", message = "Начальное y0 слишком велико")
    val y0: BigDecimal?,

    @field:NotNull(message = "Точность должна быть указана")
    @field:DecimalMin("0.0000000001", message = "Слишком высокая точность")
    @field:DecimalMax("1.0", message = "Точность слишком грубая")
    val eps: BigDecimal?
)