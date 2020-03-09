package nl.politie.predev.notes.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import nl.politie.predev.notes.api.repository.RefreshableRepositoryImpl;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = RefreshableRepositoryImpl.class)
public class NotesApi {

	public static void main(String[] args) {
	
		SpringApplication.run(NotesApi.class, args);
		
	}
}
