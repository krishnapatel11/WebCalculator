package com.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CalculatorHandlerTest {

    @Test
    public void testAddition() throws IOException {
        HttpExchange exchange = mock(HttpExchange.class);
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream("num1=5&num2=3&operator=+".getBytes()));
        when(exchange.getResponseBody()).thenReturn(responseStream);

        CalculatorHandler handler = new CalculatorHandler();
        handler.handle(exchange);

        verify(exchange).sendResponseHeaders(200, "Result: 8.0".getBytes().length);
        assertEquals("Result: 8.0", responseStream.toString());
    }

    @Test
    public void testSubtraction() throws IOException {
        HttpExchange exchange = mock(HttpExchange.class);
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream("num1=5&num2=3&operator=-".getBytes()));
        when(exchange.getResponseBody()).thenReturn(responseStream);

        CalculatorHandler handler = new CalculatorHandler();
        handler.handle(exchange);

        verify(exchange).sendResponseHeaders(200, "Result: 2.0".getBytes().length);
        assertEquals("Result: 2.0", responseStream.toString());
    }

    @Test
    public void testInvalidOperator() throws IOException {
        HttpExchange exchange = mock(HttpExchange.class);
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream("num1=5&num2=3&operator=%".getBytes()));
        when(exchange.getResponseBody()).thenReturn(responseStream);

        CalculatorHandler handler = new CalculatorHandler();
        handler.handle(exchange);

        verify(exchange).sendResponseHeaders(200, "Error: Invalid operator".getBytes().length);
        assertEquals("Error: Invalid operator", responseStream.toString());
    }

    @Test
    public void testDivisionByZero() throws IOException {
        HttpExchange exchange = mock(HttpExchange.class);
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream("num1=5&num2=0&operator=/".getBytes()));
        when(exchange.getResponseBody()).thenReturn(responseStream);

        CalculatorHandler handler = new CalculatorHandler();
        handler.handle(exchange);

        verify(exchange).sendResponseHeaders(200, "Error: Division by zero".getBytes().length);
        assertEquals("Error: Division by zero", responseStream.toString());
    }
}
