package arhr.tech.comp_math_lab2.api.models

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class OdeRequest(
    @field:NotNull(message = "Equation ID is required")
    val equationId: Int,

    @field:NotNull(message = "x0 is required")
    val x0: Double,

    @field:NotNull(message = "y0 is required")
    val y0: Double,

    @field:NotNull(message = "xn is required")
    val xn: Double,

    @field:NotNull(message = "Step h is required")
    @field:Positive(message = "Step h must be positive")
    val h: Double,

    @field:NotNull(message = "Accuracy eps is required")
    @field:Positive(message = "Accuracy eps must be positive")
    val eps: Double
)

data class OdeResultSeries(
    val methodName: String,
    val points: List<Point2D>,
    val rungeError: Double? = null,
    val maxExactError: Double? = null,
    val localErrors: List<Double>? = null,
    val table: List<TableRow>? = null
)

data class Point2D(val x: Double, val y: Double)

data class TableRow(
    val x: Double,
    val y: Double,
    val yExact: Double?,
    val delta: Double?
)

data class OdeResponse(
    val exactPoints: List<Point2D>,
    val series: List<OdeResultSeries>
)
