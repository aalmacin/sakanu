package com.raidrin.sakanu.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemDetail {
    private String detail;

    public ProblemDetail(String detail) {
        this.detail = detail;
    }
}