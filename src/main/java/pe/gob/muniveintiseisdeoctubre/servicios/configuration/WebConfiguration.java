package pe.gob.muniveintiseisdeoctubre.servicios.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import pe.gob.muniveintiseisdeoctubre.servicios.component.RequestTimeInterceptor;

@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {

    private final RequestTimeInterceptor requestTimeInterceptor;

    @Autowired
    public WebConfiguration(RequestTimeInterceptor requestTimeInterceptor) {
        this.requestTimeInterceptor = requestTimeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestTimeInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/storage/**").addResourceLocations("file:/opt/seguridadVeintiseis/");
        registry.addResourceHandler("/swagger-ui.html**").addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH").allowedOriginPatterns("*").exposedHeaders("Authorization").allowedHeaders("Authorization");
    }
}