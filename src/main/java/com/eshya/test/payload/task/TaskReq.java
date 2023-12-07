package com.eshya.test.payload.task;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class TaskReq {

    private String name;

    private String description;

}
