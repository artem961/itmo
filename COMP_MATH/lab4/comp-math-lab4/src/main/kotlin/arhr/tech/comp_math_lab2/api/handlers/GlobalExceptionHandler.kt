package arhr.tech.comp_math_lab2.api.handlers

import arhr.tech.comp_math_lab2.api.models.SolveIntegralResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<SolveIntegralResponse> {
        val errors = e.bindingResult.fieldErrors.map { it.defaultMessage }
        val message = errors.joinToString("\n")

        return ResponseEntity(
            SolveIntegralResponse(error = message),
            HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException::class)
    fun handleJsonException(e: Exception): ResponseEntity<String> {
        return ResponseEntity(
            "Некорректный формат данных в запросе",
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<SolveIntegralResponse> {
        return ResponseEntity(
            SolveIntegralResponse(error =  e.message),
            HttpStatus.BAD_REQUEST)
    }
}