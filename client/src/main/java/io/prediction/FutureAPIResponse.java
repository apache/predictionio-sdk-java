package io.prediction;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * APIResponse as a future.
 *
 * @author The PredictionIO Team (<a href="http://prediction.io">http://prediction.io</a>)
 * @version 0.2
 * @since 0.2
 */

public class FutureAPIResponse implements Future {
    private Future<APIResponse> apiResponse;

    public FutureAPIResponse(Future<APIResponse> apiResponse) {
        this.apiResponse = apiResponse;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return this.apiResponse.cancel(mayInterruptIfRunning);
    }

    public APIResponse get() throws ExecutionException, InterruptedException {
        return this.apiResponse.get();
    }

    public APIResponse get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        return this.apiResponse.get(timeout, unit);
    }

    public boolean isCancelled() {
        return this.apiResponse.isCancelled();
    }

    public boolean isDone() {
        return this.apiResponse.isDone();
    }

    public int getStatus() {
        try {
            return this.apiResponse.get().getStatus();
        } catch (InterruptedException e) {
            return 0;
        } catch (ExecutionException e) {
            return 0;
        }
    }

    public String getMessage() {
        try {
            return this.apiResponse.get().getMessage();
        } catch (InterruptedException e) {
            return e.getMessage();
        } catch (ExecutionException e) {
            return e.getMessage();
        }
    }
}