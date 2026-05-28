package arhr.tech.comp_math_lab2.solver

import kotlin.math.floor
import org.springframework.stereotype.Component

@Component
class EulerSolver : ODESolver {
    override val name: String = "Euler"
    override val isOneStep: Boolean = true
    override val order: Int = 1

    override fun solve(f: ODEFunction, x0: Double, y0: Double, xn: Double, h: Double, eps: Double?): ODESolution {
        require(h > 0) { "h must be > 0" }
        require(xn > x0) { "xn must be > x0" }

        val n = floor((xn - x0) / h).toInt()
        val xs = DoubleArray(n + 1)
        val ys = DoubleArray(n + 1)
        xs[0] = x0
        ys[0] = y0
        for (i in 0 until n) {
            val xi = xs[i]
            val yi = ys[i]
            xs[i + 1] = x0 + (i + 1) * h
            ys[i + 1] = yi + h * f(xi, yi)
        }
        return ODESolution(xs, ys)
    }
}
