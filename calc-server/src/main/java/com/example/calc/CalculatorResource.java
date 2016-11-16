package com.example.calc;

import com.example.calc.beans.ExpressionData;
import com.example.calc.beans.ExpressionValue;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/calculate")
public class CalculatorResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculate(final ExpressionData data) {
        final CalculatorService service = new CalculatorService();
        final ExpressionValue result = service.calculate(data);
        return Response
                .status(Response.Status.OK)
                .entity(result)
                .build();
    }
}
