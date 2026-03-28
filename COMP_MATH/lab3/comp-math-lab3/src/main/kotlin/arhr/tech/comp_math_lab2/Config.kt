package arhr.tech.comp_math_lab2

import org.mariuszgromada.math.mxparser.License
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config{
    @Bean
    fun runner(): ApplicationRunner =
        ApplicationRunner {
                args: ApplicationArguments? ->
            License.iConfirmNonCommercialUse("signature")
        }

}