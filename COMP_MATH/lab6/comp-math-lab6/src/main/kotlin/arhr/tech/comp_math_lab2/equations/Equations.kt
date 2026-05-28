package arhr.tech.comp_math_lab2.equations

import org.springframework.stereotype.Component
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin

@Component
class Equation1 : OdeEquation {
    override val id = 1
    override val stringRepresentation = "y' = y"

    override fun f(x: Double, y: Double): Double = y
    override fun exactSolution(x: Double, x0: Double, y0: Double): Double = y0 * exp(x - x0)
}

@Component
class Equation2 : OdeEquation {
    override val id = 2
    override val stringRepresentation = "y' = y * cos(x)"

    override fun f(x: Double, y: Double): Double = y * cos(x)
    // solution: y = y0 * exp(sin x - sin x0)
    override fun exactSolution(x: Double, x0: Double, y0: Double): Double = y0 * exp(sin(x) - sin(x0))
}

@Component
class Equation3 : OdeEquation {
    override val id = 3
    override val stringRepresentation = "y' = y * (1 - y)" // logistic

    override fun f(x: Double, y: Double): Double = y * (1.0 - y)
    // logistic solution with K=1, r=1: y = 1 / (1 + ((1 - y0)/y0) * exp(-(x - x0)))
    override fun exactSolution(x: Double, x0: Double, y0: Double): Double {
        if (y0 == 0.0) return 0.0
        val a = (1.0 - y0) / y0
        return 1.0 / (1.0 + a * exp(-(x - x0)))
    }
}

@Component
class Equation4 : OdeEquation {
    override val id = 4
    override val stringRepresentation = "y' = sin(x)"

    override fun f(x: Double, y: Double): Double = sin(x)
    // integral of sin is -cos: y = y0 - cos(x) + cos(x0)
    override fun exactSolution(x: Double, x0: Double, y0: Double): Double = y0 - kotlin.math.cos(x) + kotlin.math.cos(x0)
}
