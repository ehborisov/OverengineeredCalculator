package com.example.calc.client;

import com.example.calc.beans.ExpressionData;
import com.example.calc.beans.ExpressionValue;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface CalculatorService {

    @POST("rest/calculate")
    Call<ExpressionValue> evaluate(@Body ExpressionData expression);
}
