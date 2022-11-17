package com.example.ninhdemo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    private int code;
    private String msg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int totalPage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long count;

    private Object data;

    public ResponseDTO(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
