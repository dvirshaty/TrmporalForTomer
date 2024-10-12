/*
 *  Copyright (c) 2020 Temporal Technologies, Inc. All Rights Reserved
 *
 *  Copyright 2012-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 *  Modifications copyright (C) 2017 Uber Technologies, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"). You may not
 *  use this file except in compliance with the License. A copy of the License is
 *  located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 *  or in the "license" file accompanying this file. This file is distributed on
 *  an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *  express or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 */

package com.dvir.temporal.poc.controller;

import com.dvir.temporal.poc.workflow.SimpleFlow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.RetryOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;



@Controller
@RequestMapping("/submit")
public class SamplesController {
    Logger logger = LoggerFactory.getLogger(SamplesController.class);
    public static final WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
    public static final WorkflowClient client = WorkflowClient.newInstance(service);

    @PostMapping("/start")
    ResponseEntity<Boolean> submit() {
        logger.info("Start Submit");

        UUID uuid = UUID.randomUUID();

        SimpleFlow workflow = client.newWorkflowStub(SimpleFlow.class, WorkflowOptions.newBuilder().setTaskQueue("TaskQueue").setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(1).build()).setWorkflowId(uuid.toString()).build());
        WorkflowClient.start(workflow::run, uuid);

        logger.info("Done Submit");
        return ResponseEntity.ok(true);
    }


}


