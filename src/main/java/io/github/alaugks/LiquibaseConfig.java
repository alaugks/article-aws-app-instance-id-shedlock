package io.github.alaugks;

import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class LiquibaseConfig {

	@Bean
	//@Profile({"prod", "stage"})
	public SpringLiquibase liquibase(DataSource dataSource) {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.xml");
		liquibase.setClearCheckSums(true);
		return liquibase;
	}
}
