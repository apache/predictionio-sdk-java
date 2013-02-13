package com.tappingstone.predictionio;

/**
 * API Response class for wrapping responses
 *
 * @author TappingStone (help@tappingstone.com)
 * @version 1.0
 * @since 1.0
 */

public class APIResponse {
    private int status;
    private String message;

    public APIResponse() {
        this.status = 0;
        this.message = "";
    }

    public APIResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}