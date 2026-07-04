package cc.kertaskerja.cetakservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CetakserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CetakserviceApplication.class, args);
	}

}
