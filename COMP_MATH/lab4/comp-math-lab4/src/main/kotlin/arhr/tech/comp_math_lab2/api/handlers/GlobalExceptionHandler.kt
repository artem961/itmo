package arhr.tech.comp_math_lab2.api.handlers

import arhr.tech.comp_math_lab2.api.models.ApproximationResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ApproximationResponse> {
        val errors = e.bindingResult.fieldErrors.map { it.defaultMessage }
        val message = errors.joinToString("\n")

        return ResponseEntity(
            ApproximationResponse(error = message, best = null, results = null),
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
    fun handleException(e: Exception): ResponseEntity<ApproximationResponse> {
        return ResponseEntity(
            ApproximationResponse(error =  e.message, best =  null, results = null),
            HttpStatus.BAD_REQUEST)
    }
}