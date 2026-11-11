package io.github.alaugks;

import io.github.alaugks.aws.app.instance.id.InstanceService;
import io.github.alaugks.aws.app.instance.id.service.FargateInstanceService;
import io.github.alaugks.aws.app.instance.id.service.MockInstanceService;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AwsInstanceConfig {

	private static final Logger log = LoggerFactory.getLogger(AwsInstanceConfig.class);

	@Bean
	@Profile({"prod", "stage"})
	public InstanceService awsInstanceService() {
		return InstanceService
			.builder(
				FargateInstanceService.builder().build()
			)
			.onInstanceId(id -> log.info("InstanceId: {}", id))
			.fallback(e -> {
				String fallbackId = "fallback-" + UUID.randomUUID();
				log.warn("Could not determine instanceId, using {}: {}", fallbackId, e.getMessage());
				return fallbackId;
			})
			.build();
	}

	@Bean
	@Profile({"dev", "test"})
	public InstanceService awsMockInstanceService() {
		return InstanceService
			.builder(
				MockInstanceService.builder("mock-" + UUID.randomUUID()).build()
			)
			.onInstanceId(id -> log.info("InstanceId: {}", id))
			.build();
	}
}
