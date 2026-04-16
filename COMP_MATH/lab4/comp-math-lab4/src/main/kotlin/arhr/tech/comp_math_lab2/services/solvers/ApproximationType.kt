package arhr.tech.comp_math_lab2.services.solvers

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ApproximationType(val id: Int, val label: String) {
    LINEAR(0, "Линейная"),
    QUADRATIC(1, "Квадратичная"),
    CUBIC(2, "Кубическая"),
    EXPONENTIAL(3, "Экспоненциальная"),
    LOGARITHMIC(4, "Логарифмическая"),
    POWER(5, "Степенная");

    companion object {
        fun fromId(id: Int?): ApproximationType {
            return entries.find { it.id == id }
                ?: throw IllegalArgumentException("Метод с ID $id не существует")
        }
    }
}