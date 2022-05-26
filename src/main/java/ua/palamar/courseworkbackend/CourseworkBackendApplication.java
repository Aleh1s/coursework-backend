package ua.palamar.courseworkbackend;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class CourseworkBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseworkBackendApplication.class, args);
    }
}
