import com.example.calc.beans.ExpressionValue;
import com.example.calc.client.CalculatorServiceImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BasicTest {

    @Test
    public void simplePositiveEvaluate(){
        final ExpressionValue value = CalculatorServiceImpl.INSTANCE.evaluate("2*2");
        assertEquals("Unexpected result", 4.0, Double.parseDouble(value.getResult()), Double.MIN_VALUE);
    }

    @Test
    public void complexExpression(){
        final ExpressionValue value = CalculatorServiceImpl.INSTANCE.evaluate("(((3 + 4) + 7 * 2)/11*(3-2) - 1 - 9)/2");
        assertEquals("Unexpected result", -4.045454545454545, Double.parseDouble(value.getResult()), Double.MIN_VALUE);
    }

    @Test
    public void emptyData(){
        final ExpressionValue value = CalculatorServiceImpl.INSTANCE.evaluate("");
        assertEquals("Unexpected result for empty data in the form", "Empty expression data", value.getResult());
    }

    @Test
    public void divisionByZero(){
        final ExpressionValue value = CalculatorServiceImpl.INSTANCE.evaluate("42/0");
        assertTrue("Unexpected result for division by zero", value.getResult()
                .contains("ArithmeticException: Error: Division by zero"));
    }

    @Test
    public void unexpectedToken(){
        final ExpressionValue value = CalculatorServiceImpl.INSTANCE.evaluate("a * 2");
        assertTrue("Unexpected token in the input should not be parsed", value.getResult()
                .contains("CalculatorException: Syntax error: 'token recognition error at: 'a'' at position 0"));
    }

    @Test
    public void unmatchedBrace(){
        final ExpressionValue value = CalculatorServiceImpl.INSTANCE.evaluate("(2 + 2))");
        assertTrue("Unexpected token in the input should not be parsed",
                value.getResult().contains("CalculatorException: Syntax error: 'extraneous input ')'"));
    }
}
