package io.github.devopMarkz.backend.shared.dto;

import java.time.Instant;

public class ErroDTO {

    private Instant timestamp;
    private Integer status;
    private String message;
    private String pat;

    public ErroDTO() {
    }

    public ErroDTO(Instant timestamp, Integer status, String message, String pat) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.pat = pat;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPat() {
        return pat;
    }

    public void setPat(String pat) {
        this.pat = pat;
    }
}
