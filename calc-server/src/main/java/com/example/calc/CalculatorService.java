package com.example.calc;

import com.example.calc.antlr4.SimpleArithmeticBaseVisitor;
import com.example.calc.antlr4.SimpleArithmeticLexer;
import com.example.calc.antlr4.SimpleArithmeticParser;
import com.example.calc.beans.ExpressionData;
import com.example.calc.beans.ExpressionValue;
import org.antlr.v4.runtime.*;

import java.util.ArrayList;
import java.util.List;


public class CalculatorService {

    public ExpressionValue calculate(final ExpressionData data) throws CalculatorException {
        if (data.getExpression().isEmpty()) {
            throw new CalculatorException("Error: Empty expression data");
        }

        final SimpleArithmeticLexer lexer = new SimpleArithmeticLexer(new ANTLRInputStream(data.getExpression()));
        final ErrorListener errorListener = new ErrorListener();
        lexer.addErrorListener(errorListener);
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final SimpleArithmeticParser parser = new SimpleArithmeticParser(tokens);
        parser.addErrorListener(errorListener);

        final SimpleArithmeticParser.ExpressionContext parsed = parser.expression();
        if (!errorListener.getExceptions().isEmpty()) {
            throw errorListener.getExceptions().get(0);
        }

        final Visitor visitor = new Visitor();
        final Double result = visitor.visit(parsed);
        return new ExpressionValue().withResult(result.toString());
    }

    private static final class Visitor extends SimpleArithmeticBaseVisitor<Double> {

        @Override
        public Double visitPlus(final SimpleArithmeticParser.PlusContext context) {
            return visit(context.expression()) + visit(context.term());
        }

        @Override
        public Double visitMinus(final SimpleArithmeticParser.MinusContext context) {
            return visit(context.expression()) - visit(context.term());
        }

        @Override
        public Double visitMultiplication(final SimpleArithmeticParser.MultiplicationContext context) {
            return visit(context.term()) * visit(context.unary());
        }

        @Override
        public Double visitDivision(final SimpleArithmeticParser.DivisionContext context) {
            final Double divider = visit(context.unary());
            if (Math.abs(divider) < Double.MIN_VALUE) {
                throw new CalculatorException("Error: Division by zero");
            }
            return visit(context.term()) / visit(context.unary());
        }

        @Override
        public Double visitUnaryMinus(final SimpleArithmeticParser.UnaryMinusContext context) {
            return -1 * visit(context.unary());
        }

        @Override
        public Double visitNumber(final SimpleArithmeticParser.NumberContext context) {
            return Double.parseDouble(context.getText());
        }

        @Override
        public Double visitSubexpression(final SimpleArithmeticParser.SubexpressionContext context) {
            return visit(context.expression());
        }
    }

    private static final class ErrorListener extends BaseErrorListener {

        private final List<CalculatorException> exceptions = new ArrayList<>();

        @Override
        public void syntaxError(final Recognizer<?, ?> recognizer, final Object offendingSymbol,
                                final int line, final int charPositionInLine,
                                final String msg, final RecognitionException e) {
            final String message = String.format("Syntax error: '%s' at position %d", msg, charPositionInLine);
            exceptions.add(new CalculatorException(message, e));
        }

        List<CalculatorException> getExceptions() {
            return exceptions;
        }
    }
}
