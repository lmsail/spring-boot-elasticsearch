package com.example.elasticsearch.entity;

import lombok.Data;

@Data
public class UserAccountDocument {

    private String account;

    private String userId;

    private String userName;

    private String createdAt;
}
