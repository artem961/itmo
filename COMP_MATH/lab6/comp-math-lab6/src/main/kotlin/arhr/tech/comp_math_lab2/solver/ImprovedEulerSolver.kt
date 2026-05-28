package arhr.tech.comp_math_lab2.solver

import org.springframework.stereotype.Component
import kotlin.math.floor

@Component
class ImprovedEulerSolver : ODESolver {
    override val name: String = "ImprovedEuler"
    override val isOneStep: Boolean = true
    override val order: Int = 2

    override fun solve(f: ODEFunction, x0: Double, y0: Double, xn: Double, h: Double, eps: Double?): ODESolution {
        require(h > 0)
        require(xn > x0)

        val n = floor((xn - x0) / h).toInt()
        val xs = DoubleArray(n + 1)
        val ys = DoubleArray(n + 1)
        xs[0] = x0
        ys[0] = y0
        for (i in 0 until n) {
            val xi = xs[i]
            val yi = ys[i]
            val k1 = f(xi, yi)
            val k2 = f(xi + h, yi + h * k1)
            xs[i + 1] = x0 + (i + 1) * h
            ys[i + 1] = yi + h * (k1 + k2) / 2.0
        }
        return ODESolution(xs, ys)
    }
}
