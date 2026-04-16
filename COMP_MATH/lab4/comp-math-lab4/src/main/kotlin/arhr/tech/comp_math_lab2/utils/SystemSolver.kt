package arhr.tech.comp_math_lab2.utils

import java.math.BigDecimal
import java.math.MathContext

/**
 * Решатель систем линейных уравнений методом Гаусса
 */
class SystemSolver(
    private val mc: MathContext = MathContext.DECIMAL128
) {

    /**
     * Решает систему линейных уравнений A * x = b
     * @param A матрица коэффициентов (n×n)
     * @param b вектор правых частей (n)
     * @return вектор неизвестных x (n)
     * @throws IllegalArgumentException если система не имеет единственного решения
     */
    fun solve(A: Array<Array<BigDecimal>>, b: Array<BigDecimal>): Array<BigDecimal> {
        val n = A.size
        require(n == b.size) { "Размер матрицы и вектора не совпадают" }

        // Создаём расширенную матрицу [A|b]
        val augmented = Array(n) { i ->
            Array(n + 1) { j ->
                if (j < n) A[i][j] else b[i]
            }
        }

        // Прямой ход метода Гаусса
        for (i in 0 until n) {
            // Поиск главного элемента (для устойчивости)
            var maxRow = i
            var maxAbs = augmented[i][i].abs()
            for (k in i + 1 until n) {
                val absVal = augmented[k][i].abs()
                if (absVal > maxAbs) {
                    maxAbs = absVal
                    maxRow = k
                }
            }

            // Если главный элемент почти ноль — система вырождена
            if (maxAbs < BigDecimal("1e-10")) {
                throw IllegalArgumentException("Матрица вырождена: нет единственного решения")
            }

            // Перестановка строк
            if (maxRow != i) {
                val temp = augmented[i]
                augmented[i] = augmented[maxRow]
                augmented[maxRow] = temp
            }

            // Нормализация строки i
            val pivot = augmented[i][i]
            for (j in i until n + 1) {
                augmented[i][j] = augmented[i][j].divide(pivot, mc)
            }

            // Исключение ниже и выше
            for (k in 0 until n) {
                if (k != i) {
                    val factor = augmented[k][i]
                    for (j in i until n + 1) {
                        augmented[k][j] = augmented[k][j] - factor * augmented[i][j]
                    }
                }
            }
        }

        // Извлекаем решение
        return Array(n) { i -> augmented[i][n] }
    }

    /**
     * Решает систему, где матрица задана списком списков
     */
    fun solve(A: List<List<BigDecimal>>, b: List<BigDecimal>): List<BigDecimal> {
        val AArray = A.map { it.toTypedArray() }.toTypedArray()
        val bArray = b.toTypedArray()
        return solve(AArray, bArray).toList()
    }
}