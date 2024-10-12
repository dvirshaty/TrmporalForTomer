package com.dvir.temporal.poc.activities;

import io.temporal.activity.ActivityInterface;

import java.util.UUID;

@ActivityInterface
public interface SampleActivity {
    public boolean step1(Integer value);

    public boolean step2(Integer value);

    public void step3(UUID id);

}
