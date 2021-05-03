package ml.mixko.shopkoapi.SpringConfig;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
class MvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:3000","http://shopko.mixko.ml").allowCredentials(true).allowedMethods("GET", "POST","PUT","DELETE","PATCH");
    }
}
