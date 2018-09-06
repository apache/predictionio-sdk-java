/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.predictionio.sdk.java;

import com.google.common.util.concurrent.ListenableFuture;
import com.ning.http.client.extra.ListenableFutureAdapter;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * APIResponse as a listenable future.
 *
 * @version 0.8.3
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
