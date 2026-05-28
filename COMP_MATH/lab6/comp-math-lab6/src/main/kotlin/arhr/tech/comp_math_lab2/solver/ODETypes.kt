package arhr.tech.comp_math_lab2.solver

typealias ODEFunction = (Double, Double) -> Double

data class ODESolution(
	val xs: DoubleArray,
	val ys: DoubleArray
)
