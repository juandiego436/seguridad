package pe.gob.muniveintiseisdeoctubre.servicios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ServiciosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiciosApplication.class, args);
    }
}
