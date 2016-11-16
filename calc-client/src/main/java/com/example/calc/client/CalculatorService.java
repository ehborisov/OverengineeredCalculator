package com.example.calc.client;

import com.example.calc.beans.CalcErrorData;
import com.example.calc.beans.ExpressionData;
import com.example.calc.beans.ExpressionValue;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.junit.Assume;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

public class CalculatorService {

    private static final long HTTP_CLIENT_TIMEOUT = 60;
    private static final String BASE_URL = "http://localhost:8080/";

    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .build();

    private static final Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build();

    private static final Calculator calculator = retrofit.create(Calculator.class);

    public static final CalculatorService instance = new CalculatorService();

    public ExpressionValue evaluate(final String expression) {
        try{
            final Response<ExpressionValue> response = calculator.evaluate(new ExpressionData()
                    .withExpression(expression))
                    .execute();
            Assume.assumeTrue("Successful response is expected", response.isSuccessful());
            return response.body();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CalcErrorData evaluateNegative(final String expression) {
        try {
            final Response<ExpressionValue> response = calculator.evaluate(new ExpressionData()
                    .withExpression(expression))
                    .execute();
            Assume.assumeFalse("Unsuccessful response is expected", response.isSuccessful());
            final Converter<ResponseBody, CalcErrorData> errorConverter = retrofit
                    .responseBodyConverter(CalcErrorData.class, new Annotation[0]);
            return errorConverter.convert(response.errorBody());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}