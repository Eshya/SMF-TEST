package com.eshya.test.payload.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskUpdateReq {
    private String name;

    private String description;

    @JsonProperty("is_completed")
    private boolean isCompleted;
}
