package arhr.tech.comp_math_lab2.solver

import kotlin.math.floor
import kotlin.math.min
import org.springframework.stereotype.Component

@Component
class AdamsSolver : ODESolver {
    override val isOneStep: Boolean = false
    override val order: Int = 4
    override val name: String = "Adams(order=$order)"

    override fun solve(f: ODEFunction, x0: Double, y0: Double, xn: Double, h: Double, eps: Double?): ODESolution {
        require(h > 0)
        require(xn > x0)
        val n = floor((xn - x0) / h).toInt()
        if (n <= 0) return ODESolution(doubleArrayOf(x0), doubleArrayOf(y0))

        val m = order
        val starter = RK4Starter.compute(f, x0, y0, h, m - 1)
        val xs = DoubleArray(n + 1)
        val ys = DoubleArray(n + 1)
        val stepsToCopy = min(starter.xs.size, n + 1)
        for (i in 0 until stepsToCopy) {
            xs[i] = starter.xs[i]
            ys[i] = starter.ys[i]
        }
        val epsLocal = eps ?: 1e-9

        val fs = DoubleArray(n + 1)
        for (i in 0 until stepsToCopy) fs[i] = f(xs[i], ys[i])

        for (i in stepsToCopy - 1 until n) {
            val yPred = if (i >= 3) {
                ys[i] + h / 24.0 * (55.0 * fs[i] - 59.0 * fs[i - 1] + 37.0 * fs[i - 2] - 9.0 * fs[i - 3])
            } else {
                ys[i] + h * fs[i]
            }

            val xNext = xs[i] + h
            var yCorr = yPred
            var iteration = 0
            while (true) {
                val fNext = f(xNext, yCorr)
                val yNew = if (i >= 2) {
                    ys[i] + h / 24.0 * (9.0 * fNext + 19.0 * fs[i] - 5.0 * fs[i - 1] + fs[i - 2])
                } else {
                    ys[i] + h * fNext
                }
                if (kotlin.math.abs(yNew - yCorr) < epsLocal) {
                    yCorr = yNew
                    break
                }
                yCorr = yNew
                if (++iteration > 20) break
            }

            xs[i + 1] = x0 + (i + 1) * h
            ys[i + 1] = yCorr
            fs[i + 1] = f(xs[i + 1], ys[i + 1])
        }

        return ODESolution(xs, ys)
    }
}
