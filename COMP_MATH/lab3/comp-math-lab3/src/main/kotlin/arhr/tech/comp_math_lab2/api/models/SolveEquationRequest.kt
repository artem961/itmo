package arhr.tech.comp_math_lab2.api.models

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class SolveEquationRequest(
    @field:NotNull(message = "Не выбрано уравнение")
    val equationId: Int?,

    @field:NotNull(message = "Не выбран метод")
    val methodId: Int?,

    @field:NotNull(message = "граница a обязательна")
    //@field:DecimalMin("-200", message = "Слишком маленькая граница а")
    //@field:DecimalMax("200", message = "Слишком большая граница а")
    val a: BigDecimal?,

    @field:NotNull(message = "граница b обязательна")
    //@field:DecimalMin("-200", message = "Слишком маленькая граница b")
    //@field:DecimalMax("200", message = "Слишком большая граница b")
    val b: BigDecimal?,

    //@field:DecimalMin("0.0000000001", message = "Слишком большая точность")
    @field:DecimalMax("1.0", message = "Точность слишком грубая")
    val eps: BigDecimal?
    )