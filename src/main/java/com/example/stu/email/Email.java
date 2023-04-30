package com.example.stu.email;

import lombok.Builder;
import lombok.Data;

import java.util.Map;
@Builder
@Data
public class Email {
    private String recipientEmail;
    private String subject;
    private String templateName;
    private Map<String, Object> emailVariables;
}
