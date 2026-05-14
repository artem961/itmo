package arhr.tech.comp_math_lab2.services.solvers

import arhr.tech.comp_math_lab2.dto.InterpolationRequestDto
import arhr.tech.comp_math_lab2.dto.PointDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import java.math.BigDecimal

class InterpolationSolversTest {

    private val polynomial: (BigDecimal) -> BigDecimal = { x ->
        x.pow(3).subtract(x.pow(2).multiply(BigDecimal("2"))).add(x).add(BigDecimal("3"))
    }

    private fun pointsOf(xs: List<String>): List<PointDto> {
        return xs.map { value ->
            val x = BigDecimal(value)
            PointDto(x, polynomial(x))
        }
    }

    private fun assertClose(actual: BigDecimal?, expected: BigDecimal, epsilon: String = "0.0000001") {
        require(actual != null)
        assertTrue((actual - expected).abs() <= BigDecimal(epsilon), "Expected $expected but was $actual")
    }

    @Test
    fun `newton matches cubic polynomial`() {
        val solver = NewtonSolver()
        val request = InterpolationRequestDto(pointsOf(listOf("-1", "0", "1", "2")), BigDecimal("1.5"))
        val result = solver.solve(request)
        assertClose(result.y, polynomial(BigDecimal("1.5")))
    }

    @Test
    fun `lagrange matches cubic polynomial with unsorted points`() {
        val solver = LagranzhSolver()
        val request = InterpolationRequestDto(pointsOf(listOf("2", "-1", "1", "0")), BigDecimal("1.5"))
        val result = solver.solve(request)
        assertClose(result.y, polynomial(BigDecimal("1.5")))
    }

    @Test
    fun `gauss matches cubic polynomial at every node`() {
        val solver = GaussSolver()
        val points = pointsOf(listOf("-2", "-1", "0", "1", "2"))
        for (point in points) {
            val request = InterpolationRequestDto(points, point.x)
            val result = solver.solve(request)
            assertClose(result.y, point.y)
        }
    }

    @Test
    fun `stirling matches cubic polynomial at every node`() {
        val solver = StirlingSolver()
        val points = pointsOf(listOf("-2", "-1", "0", "1", "2"))
        for (point in points) {
            val request = InterpolationRequestDto(points, point.x)
            val result = solver.solve(request)
            assertClose(result.y, point.y)
        }
    }

    @Test
    fun `bessel matches cubic polynomial`() {
        val solver = BesselSolver()
        val request = InterpolationRequestDto(pointsOf(listOf("-2", "-1", "0", "1", "2", "3")), BigDecimal("0.5"))
        val result = solver.solve(request)
        assertClose(result.y, polynomial(BigDecimal("0.5")))
    }
}