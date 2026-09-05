package com.google.main.to;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseTO<T> {

    private String status;
    private T data;
    // metadata

    public ResponseTO(String status, T data) {
        this.status = status;
        this.data = data;
    }

    public static ResponseEntity<ResponseTO<Map<String, Object>>> handleResponse(Map<String, Object> data) {
        return ResponseEntity.ok(new ResponseTO<>("success", data));
    }

    ResponseTO<T> fail(T data) {
        return new ResponseTO<>("fail", data);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
