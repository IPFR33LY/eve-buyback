package de.bahr.eve;

import de.bahr.eve.entities.ApiEntry;
import de.bahr.eve.services.ApiLoader;
import de.bahr.eve.services.ApiProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;

@SpringBootApplication
@EnableAsync
public class Application /*extends AsyncConfigurerSupport*/ implements CommandLineRunner {

	@Autowired
    ApiLoader apiLoader;

	@Autowired
    ApiProcessor apiProcessor;

    public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    @Override
	public void run(String ... args) {
        List<ApiEntry> apiEntries = apiLoader.loadApis();
        for (ApiEntry apiEntry : apiEntries) {
            apiProcessor.process(apiEntry);
        }
    }

//    @Override
//    public Executor getAsyncExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(2);
//        executor.setMaxPoolSize(5);
//        executor.setQueueCapacity(500);
//        executor.setThreadNamePrefix("Parser-");
//        executor.initialize();
//        return executor;
//    }
}
