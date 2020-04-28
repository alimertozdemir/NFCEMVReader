package com.gt.alimert.emvnfclib.data.remote;

import com.gt.alimert.emvnfclib.data.model.TransactionRequest;
import com.gt.alimert.emvnfclib.data.model.TransactionResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author AliMertOzdemir
 * @class ApiService
 * @created 28.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public interface ApiService {

    @POST("softpos/provision")
    Single<TransactionResponse> startTransaction(@Body TransactionRequest transactionRequest);
}
