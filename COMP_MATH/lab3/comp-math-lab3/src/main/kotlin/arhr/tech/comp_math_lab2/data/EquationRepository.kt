package arhr.tech.comp_math_lab2.data

import arhr.tech.comp_math_lab2.utils.Equation
import org.springframework.stereotype.Repository
import kotlin.math.abs
import kotlin.math.sqrt

@Repository
class EquationRepository {
    private val equations = mutableMapOf<Int, Equation>()
    init {
        equations.put(0, Equation(
            formula = "3*x^3 - 4*x^2 + 7*x - 17",
            view = "3x³ - 4x² + 7x - 17",
            lambda = { x: Double -> 3 * x * x * x - 4 * x * x + 7 * x - 17 },
            id = 0
        ))

        equations.put(1, Equation(
            formula = "1/sqrt(abs(x))",
            view = "1/√|x|",
            lambda = { x: Double -> 1 / sqrt(abs(x)) },
            id = 1
        ))

        equations.put(2, Equation(
            formula = "1/x",
            view = "1/x",
            lambda = { x: Double -> 1 / (x) },
            id = 2
        ))

        equations.put(3, Equation(
            formula = "sqrt(abs(2*x))/abs(5*x + x^2)",
            view = "√|2x|/(5x + x²)",
            lambda = { x: Double -> sqrt(abs(2*x))/abs(5*x + x*x) },
            id = 3
        ))
    }

    fun getById(id: Int?): Equation {
        return equations[id] ?: throw Exception("Выражение с ID $id не найдено")
    }

    fun getAll(): List<Equation> {
        return equations.values.toList()
    }
}