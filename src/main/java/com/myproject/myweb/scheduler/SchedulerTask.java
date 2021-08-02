package com.myproject.myweb.scheduler;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;

@Component
@Slf4j
public class SchedulerTask {

    @Autowired JobLauncher jobLauncher;
    Job job;

    // @Scheduled(cron = "0 */1 * * * *") // "0 */1 * * * *" "* * 9 15 * *"
    public void roleUpdateTask1() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        jobLauncher.run(job,
                new JobParametersBuilder()
                .addString("date", LocalDateTime.now().toString())
                .toJobParameters()
                );
    }
}

