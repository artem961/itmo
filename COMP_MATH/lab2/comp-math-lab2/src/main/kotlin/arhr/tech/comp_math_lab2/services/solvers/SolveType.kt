package arhr.tech.comp_math_lab2.services.solvers

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class SolveType(val id: Int, val label: String) {
    CHORD(0, "Метод хорд"),
    NEWTON(1, "Метод Ньютона"),
    ITERATIONS(2, "Метод простых итераций");

    companion object {
        fun fromId(id: Int?): SolveType {
            return entries.find { it.id == id }
                ?: throw IllegalArgumentException("Метод с ID $id не поддерживается")
        }
    }
}