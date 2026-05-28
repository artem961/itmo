package arhr.tech.comp_math_lab2.solver

object RK4Starter {
    fun compute(f: ODEFunction, x0: Double, y0: Double, h: Double, steps: Int): ODESolution {
        val xs = DoubleArray(steps + 1)
        val ys = DoubleArray(steps + 1)
        xs[0] = x0
        ys[0] = y0
        for (i in 0 until steps) {
            val xi = xs[i]
            val yi = ys[i]
            val k1 = f(xi, yi)
            val k2 = f(xi + h / 2.0, yi + h * k1 / 2.0)
            val k3 = f(xi + h / 2.0, yi + h * k2 / 2.0)
            val k4 = f(xi + h, yi + h * k3)
            xs[i + 1] = x0 + (i + 1) * h
            ys[i + 1] = yi + h * (k1 + 2.0 * k2 + 2.0 * k3 + k4) / 6.0
        }
        return ODESolution(xs, ys)
    }
}
