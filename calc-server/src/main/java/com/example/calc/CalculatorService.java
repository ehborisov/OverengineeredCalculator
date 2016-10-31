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
public class CalculatorService {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculate(final ExpressionData data) {
        if(data.getExpression().isEmpty()){
            return prepareResponse(Response.Status.BAD_REQUEST, "Empty expression data");
        }
        final ExpressionParser parser = new ExpressionParser();
        try {
            final Double result = parser.parse(data.getExpression());
            return prepareResponse(Response.Status.OK, result.toString());
        } catch (final ArithmeticException | CalculatorException ex) {
            return prepareResponse(Response.Status.BAD_REQUEST, ex.toString());
        } catch (final Exception e){
            return prepareResponse(Response.Status.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    private static Response prepareResponse(final Response.Status status, final String result){
        return Response
                .status(status)
                .entity(new ExpressionValue()
                .withResult(result))
                .build();
    }
}
