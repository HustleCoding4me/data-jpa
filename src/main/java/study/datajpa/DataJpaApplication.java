package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
@EnableJpaAuditing
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}


	@Bean
	public AuditorAware<String> auditorProvider(){
		//Spring Security나 Session 등을 가져와 고유값을 넣어준다.
		return () -> Optional.of(UUID.randomUUID().toString());
	}

}
