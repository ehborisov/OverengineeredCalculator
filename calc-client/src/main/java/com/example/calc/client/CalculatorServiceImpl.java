package com.example.calc.client;

import com.example.calc.beans.ExpressionData;
import com.example.calc.beans.ExpressionValue;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

import java.io.IOException;
import java.lang.annotation.Annotation;

public enum CalculatorServiceImpl {

    INSTANCE;

    private final CalculatorService service = ServiceProvider.INSTANCE.buildService();

    public ExpressionValue evaluate(final String expression) {
        try {
            final Response<ExpressionValue> response = service.evaluate(new ExpressionData()
                    .withExpression(expression))
                    .execute();
            if (!response.isSuccessful() && response.errorBody() != null) {
                final Converter<ResponseBody, ExpressionValue> errorConverter = ServiceProvider.INSTANCE.getRetrofit()
                                .responseBodyConverter(ExpressionValue.class, new Annotation[0]);
                return errorConverter.convert(response.errorBody());
            }
            return response.body();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
