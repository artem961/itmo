package arhr.tech.comp_math_lab2.services.solvers

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class SolveType(val id: Int, val label: String) {
    LEFT_RECTANGLES(0, "Метод левых прямоугольников"),
    RIGHT_RECTANGLES(1, "Метод правых прямоугольников"),
    MIDDLE_RECTANGLES(2, "Метод средних прямоугольников"),
    TRAPEZOID(3, "Метод трапеций"),
    SIMPSON(4, "Метод Симпсона");

    companion object {
        fun fromId(id: Int?): SolveType {
            return entries.find { it.id == id }
                ?: throw IllegalArgumentException("Метод с ID $id не поддерживается")
        }
    }
}