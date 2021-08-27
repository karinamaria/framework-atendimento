package br.ufrn.PDSgrupo5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class PdSgrupo5Application {

	public static void main(String[] args) {
//		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
//		System.out.println(bc.encode("karina"));
		SpringApplication.run(PdSgrupo5Application.class, args);
	}

}
