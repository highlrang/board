package com.myproject.myweb.batch.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@SpringBatchTest
public class UserRoleUpdateJobTest {

    Job job;
    @Autowired JobLauncherTestUtils jobLauncher;

    @Test
    public void 배치() throws Exception {

        jobLauncher.launchJob(
                new JobParametersBuilder()
                        .addString("date", LocalDateTime.now().toString())
                        .toJobParameters()

        );

    }
}