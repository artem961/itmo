package arhr.tech.comp_math_lab2.services.solvers

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class InterpolationType(val id: Int, val label: String) {
    LAGRANZH(0, "Многочлен Лагранжа"),
    NEWTON(1, "Многочлен Ньютона с конечными разностями"),
    GAUSS(2, "Многочлен Гаусса"),
    STIRLING(3, "Схема Стирлинга"),
    BESSEL(4, "Схема Бесселя");

    companion object {
        fun fromId(id: Int?): InterpolationType {
            return entries.find { it.id == id }
                ?: throw IllegalArgumentException("Метод с ID $id не существует")
        }
    }
}