package bg.softuni.onlinebookstorebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OnlineBookstoreBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineBookstoreBackendApplication.class, args);
	}

}
