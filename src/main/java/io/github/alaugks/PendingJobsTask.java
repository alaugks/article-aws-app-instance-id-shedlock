package io.github.alaugks;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PendingJobsTask {

	private static final Logger log = LoggerFactory.getLogger(PendingJobsTask.class);

	@Scheduled(cron = "*/10 * * * * *")
	@SchedulerLock(name = "processPendingJobs", lockAtLeastFor = "PT5S")
	public void processPendingJobs() {
		log.info("processPendingJobs runs");
	}
}
