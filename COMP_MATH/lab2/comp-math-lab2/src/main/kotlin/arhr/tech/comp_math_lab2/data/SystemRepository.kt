package arhr.tech.comp_math_lab2.data

import arhr.tech.comp_math_lab2.utils.BiVariableEquation
import org.springframework.stereotype.Repository
import arhr.tech.comp_math_lab2.utils.EquationSystem

@Repository
class SystemRepository {
    private val systems = mutableMapOf<Int, EquationSystem>()

    init {
        systems.put(
            0,
            EquationSystem(
                BiVariableEquation(
                    "sin(x+y) - 1.4*x",
                    "sin(x + y) + 1.4x"
                ),
                BiVariableEquation(
                    "x^2 + y^2 - 1",
                    "x² + y² - 1"
                ),
                0
            )
        )


        systems.put(
            1,
            EquationSystem(
                BiVariableEquation(
                    "tan(x*y) - x^2",
                    "tg(xy) - x²"
                ),
                BiVariableEquation(
                    "0.5*x^2 + 2*y^2 - 1",
                    "0.5x² + 2y² - 1"
                ),
                1
            )
        )

    }

    fun getById(id: Int?): EquationSystem {
        return systems[id] ?: throw Exception("Система с ID $id не найдена")
    }

    fun getAll(): List<EquationSystem> {
        return systems.values.toList()
    }
}