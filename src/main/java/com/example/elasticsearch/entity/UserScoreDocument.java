package com.example.elasticsearch.entity;

import lombok.Data;

@Data
public class UserScoreDocument {

    private String account;

    private String userId;

    private Integer operationType;

    private Double scoreBefore;

    private Double scoreAfter;

    private String createdAt;
}
