package com.example.calc;

import com.example.calc.beans.CalcErrorData;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class CalcExceptionMapper implements ExceptionMapper<CalculatorException> {

    public Response toResponse(final CalculatorException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new CalcErrorData().withDetails(ExceptionUtils.getStackTrace(e)))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
