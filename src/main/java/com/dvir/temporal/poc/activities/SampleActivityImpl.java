package com.dvir.temporal.poc.activities;

import io.temporal.spring.boot.ActivityImpl;
import io.temporal.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.UUID;

@Component
@ActivityImpl(taskQueues = "TaskQueue")
public class SampleActivityImpl implements SampleActivity {


    private static final Logger logger = Workflow.getLogger(SampleActivityImpl.class);
    SecureRandom secureRandom = new SecureRandom();

    @Override
    public boolean step1(Integer value) {
        logger.info("Start Step 1 with Value {}", value);
        return secureRandom.nextBoolean();
    }

    @Override
    public boolean step2(Integer value) {
        logger.info("Start Step 2 with Value {}", value);
        return secureRandom.nextBoolean();
    }

    @Override
    public void step3(UUID id) {
        logger.info("Start Step 3 with id {}", id.toString());
    }
}
