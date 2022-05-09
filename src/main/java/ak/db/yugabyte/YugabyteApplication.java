package ak.db.yugabyte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@EnableWebMvc
@SpringBootApplication
public class YugabyteApplication {

	public static void main(String[] args) {
		SpringApplication.run(YugabyteApplication.class, args);
	}
}