package com.erat.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class Submission {
    private int submissionId;
    private String studentId;
    private String experimentId;
    private boolean isSubmitted;
    private LocalDateTime submissionTime;
}