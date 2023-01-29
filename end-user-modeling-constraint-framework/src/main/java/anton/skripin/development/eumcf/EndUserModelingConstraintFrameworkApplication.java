package anton.skripin.development.eumcf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class EndUserModelingConstraintFrameworkApplication {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SpringApplication.run(EndUserModelingConstraintFrameworkApplication.class, args);
    }

}
