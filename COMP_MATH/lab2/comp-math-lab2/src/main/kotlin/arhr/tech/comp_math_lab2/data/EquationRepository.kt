package arhr.tech.comp_math_lab2.data

import arhr.tech.comp_math_lab2.utils.Equation
import org.springframework.stereotype.Repository

@Repository
class EquationRepository {
    private val equations = mutableMapOf<Int, Equation>()

    init {
        equations.put(0, Equation(
            formula = "-0.38*x^3 - 3.42*x^2 + 2.51*x + 8.75",
            view = "-0.38x³ - 3.42x² - 2.51x + 8.75",
            id = 0))

        equations.put(1, Equation(
            formula = "x^3 + 2.64*x^2 - 5.41*x - 11.76",
            view = "x³ + 2.64x² - 5.41x - 11.76",
            id = 1))

        equations.put(2, Equation(
            formula = "sin(2*x) + 0.8",
            view = "sin(2x) + 0.8",
            id = 2))

    }

    fun getById(id: Int?): Equation {
        return equations[id] ?: throw Exception("Выражение с ID $id не найдено")
    }

    fun getAll(): List<Equation> {
        return equations.values.toList()
    }
}