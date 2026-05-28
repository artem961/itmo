package arhr.tech.comp_math_lab2.equations

/**
 * Представление ОДУ в коде.
 *
 * Реализация описывает конкретное дифференциальное уравнение y' = f(x,y) и его аналитическое решение (если известно).
 * - `id` — числовой идентификатор уравнения (используется в API)
 * - `stringRepresentation` — читаемое представление уравнения
 *
 * Методы:
 * - `f(x,y)` — правая часть уравнения y' = f(x,y)
 * - `exactSolution(x, x0, y0)` — аналитическое решение y(x) с начальными условиями (для сравнения/контроля ошибок)
 */
interface OdeEquation {
    val id: Int
    val stringRepresentation: String

    fun f(x: Double, y: Double): Double
    fun exactSolution(x: Double, x0: Double, y0: Double): Double
}
