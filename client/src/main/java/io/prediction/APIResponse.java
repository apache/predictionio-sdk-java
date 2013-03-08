package io.prediction;

/**
 * API Response class for wrapping responses
 *
 * @author TappingStone (help@tappingstone.com)
 * @version 0.2
 * @since 0.2
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