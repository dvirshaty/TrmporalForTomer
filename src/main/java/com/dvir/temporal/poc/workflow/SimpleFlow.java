package com.dvir.temporal.poc.workflow;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import org.springframework.util.StopWatch;

import java.util.UUID;

@WorkflowInterface
public interface SimpleFlow {
    @WorkflowMethod
    void run(UUID id);

    @SignalMethod
    void ping();

    @QueryMethod
    Boolean getRunTime();

}
