package io.prediction;

import com.google.common.util.concurrent.ListenableFuture;
import com.ning.http.client.extra.ListenableFutureAdapter;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * APIResponse as a listenable future.
 *
 * @author The PredictionIO Team (<a href="http://prediction.io">http://prediction.io</a>)
 * @version 0.8.0
 * @since 0.2
 */

public class FutureAPIResponse implements ListenableFuture<APIResponse> {

    private ListenableFuture<APIResponse> apiResponse;

    public FutureAPIResponse(com.ning.http.client.ListenableFuture<APIResponse> apiResponse) {
        this.apiResponse = ListenableFutureAdapter.asGuavaFuture(apiResponse);
    }

    // implements ListenableFuture<APIResponse>

    public void addListener(Runnable listener, Executor executor) {
        this.apiResponse.addListener(listener, executor);
    }

    // implements Future<APIResponse>

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

    public ListenableFuture<APIResponse> getAPIResponse() {
        // get the underlying APIResponse
        return this.apiResponse;
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
