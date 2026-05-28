package arhr.tech.comp_math_lab2.solver


interface ODESolver {
    val name: String
    val isOneStep: Boolean
    val order: Int
    fun solve(f: ODEFunction, x0: Double, y0: Double, xn: Double, h: Double, eps: Double? = null): ODESolution
}
