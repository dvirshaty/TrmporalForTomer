package com.dvir.temporal.poc.workflow;

import com.dvir.temporal.poc.activities.SampleActivity;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;
import org.springframework.util.StopWatch;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@WorkflowImpl(taskQueues = "TaskQueue")
public class SimpleFlowImpl implements SimpleFlow {
    private static final Logger logger = Workflow.getLogger(SimpleFlowImpl.class);


    private SampleActivity activity = Workflow.newActivityStub(SampleActivity.class,
            ActivityOptions.newBuilder().setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(1).build())
                    .setStartToCloseTimeout(Duration.ofSeconds(20)).build());
    SecureRandom secureRandom = new SecureRandom();

    StopWatch stopWatch;
    Boolean gotPing = Boolean.FALSE;

    @Override
    public void run(UUID id) {
        stopWatch = new StopWatch();
        stopWatch.start();
        logger.info("Start Workflow with Id {}", id);
        activity.step1(secureRandom.nextInt());
        Workflow.await(() -> gotPing);
        activity.step2(secureRandom.nextInt());
        activity.step3(id);
        stopWatch.stop();
        logger.info("Complete Workflow with Id {} , with Time {}", id, stopWatch.getTotalTime(TimeUnit.SECONDS));

    }

    @Override
    public void ping() {
        logger.info("got ping");
        gotPing = Boolean.TRUE;
    }

    @Override
    public Boolean getRunTime() {

        logger.info("getRunTime {}", stopWatch.isRunning());
        return stopWatch.isRunning();
    }
}
