package com.example.calc;

import com.example.calc.antlr4.SimpleArithmeticBaseVisitor;
import com.example.calc.antlr4.SimpleArithmeticLexer;
import com.example.calc.antlr4.SimpleArithmeticParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

class ExpressionParser {

    Double parse(final String expression) {
        final SimpleArithmeticLexer lexer = new SimpleArithmeticLexer(new ANTLRInputStream(expression));
        final ErrorListener lexicErrors = new ErrorListener();
        lexer.addErrorListener(lexicErrors);
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final SimpleArithmeticParser parser = new SimpleArithmeticParser(tokens);
        final ErrorListener parsingErrors = new ErrorListener();
        parser.addErrorListener(parsingErrors);

        final SimpleArithmeticParser.ExpressionContext parsed = parser.expression();
        if (!lexicErrors.getExceptions().isEmpty()) {
            throw lexicErrors.getExceptions().get(0);
        }
        if (!parsingErrors.getExceptions().isEmpty()) {
            throw parsingErrors.getExceptions().get(0);
        }

        final Visitor visitor = new Visitor();
        final Double result = visitor.visit(parsed);
        if(result != null) {
            return result;
        } else {
            throw new CalculatorException("Cannot evaluate the result of expression");
        }
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
                throw new ArithmeticException("Error: Division by zero");
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

        private final List<RuntimeException> exceptions = new ArrayList<>();

        @Override
        public void syntaxError(final Recognizer<?, ?> recognizer, final Object offendingSymbol,
                                final int line, final int charPositionInLine,
                                final String msg, final RecognitionException e) {
            final String message = String.format("Syntax error: '%s' at position %d", msg, charPositionInLine);
            exceptions.add(new CalculatorException(message, e));
        }

        List<RuntimeException> getExceptions() {
            return exceptions;
        }
    }
}
