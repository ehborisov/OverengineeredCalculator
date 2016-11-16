import com.example.calc.CalculatorException;
import com.example.calc.CalculatorService;
import com.example.calc.beans.ExpressionData;
import com.example.calc.beans.ExpressionValue;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class CalcServiceTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"2 + 2", true, "4"},
                {"0 + 0", true, "0"},
                {"0/1.01", true, "0"},
                {"(((1+1)))", true, "2.0"},
                {"3.5/(-8.6)", true, "-.4069767441860465"},
                {"(6431257.5634820 - 56436.7821)/((5-1))", true, "1593705.1953454998"},
                {"-8.2 / -12.1", true, "0.6776859504132231"},
                {"3 / 1", true, "3.0"},
                {"(13.4 - 4)/6/(((43.12-4.5)*(1.001 + (-0.5))))", true, "0.08097046025332387"},
                {"2/0", false, "Division by zero"},
                {"4 * 2 *", false, "no viable alternative at input '<EOF>'' at position 7"},
                {"a * 2", false, "token recognition error at: 'a'' at position 0"},
                {"(3 * 2))", false, "extraneous input ')'"},
                {"", false, "Empty expression"},
        });
    }

    private String expression;
    private Boolean isSuccess;
    private String expected;

    public CalcServiceTest(final String expression, final Boolean isSuccess, final String expected) {
        this.expression = expression;
        this.isSuccess = isSuccess;
        this.expected = expected;
    }

    @Test
    public void calcServiceTest() {
        final CalculatorService service = new CalculatorService();
        final ExpressionData expressionData = new ExpressionData().withExpression(expression);
        if (isSuccess) {
            final ExpressionValue value = service.calculate(expressionData);
            final Double result = Double.valueOf(value.getResult());
            Assert.assertEquals("Unexpected result of expression evaluation", Double.valueOf(expected),
                    result, Double.MIN_VALUE);
        } else {
            CalculatorException exception = null;
            try {
                service.calculate(expressionData);
            } catch (CalculatorException e) {
                exception = e;
            }
            Assert.assertNotNull("Calculation must result in an exception", exception);
            Assert.assertThat("Exception must contain specified reason", exception.getMessage(),
                    Matchers.containsString(expected));
        }
    }


}
