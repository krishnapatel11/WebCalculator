package com.example;

// public class Main {
//     public static void main(String[] args) {
//         System.out.println("Hello, World!");
//     }
// }
//package main.java.com.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/calculate", new CalculatorHandler());
        server.createContext("/", new StaticFileHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started on port 8000");
    }

    static class CalculatorHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Parse query parameters from the request
                String query = new String(exchange.getRequestBody().readAllBytes());
                Map<String, String> params = parseQuery(query);

                // Retrieve numbers and operation
                double num1 = Double.parseDouble(params.get("num1"));
                double num2 = Double.parseDouble(params.get("num2"));
                String operator = params.get("operator");

                // Perform calculation
                double result = 0;
                switch (operator) {
                    case "+":
                        result = num1 + num2;
                        break;
                    case "-":
                        result = num1 - num2;
                        break;
                    case "*":
                        result = num1 * num2;
                        break;
                    case "/":
                        if (num2 != 0) {
                            result = num1 / num2;
                        } else {
                            sendResponse(exchange, "Error: Division by zero");
                            return;
                        }
                        break;
                    default:
                        sendResponse(exchange, "Error: Invalid operator");
                        return;
                }

                // Send response back to the client
                sendResponse(exchange, "Result: " + result);
            } else {
                sendResponse(exchange, "Error: Invalid request method");
            }
        }

        private void sendResponse(HttpExchange exchange, String responseText) throws IOException {
            exchange.sendResponseHeaders(200, responseText.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(responseText.getBytes());
            os.close();
        }

        private Map<String, String> parseQuery(String query) {
            Map<String, String> params = new HashMap<>();
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }
            return params;
        }
    }

    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                String path = exchange.getRequestURI().getPath();
                if ("/".equals(path)) {
                    path = "/index.html";
                }
                String filePath = "web" + path;
                byte[] response = readFile(filePath);
                if (response != null) {
                    exchange.sendResponseHeaders(200, response.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response);
                    os.close();
                } else {
                    String errorMessage = "404 Not Found";
                    exchange.sendResponseHeaders(404, errorMessage.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(errorMessage.getBytes());
                    os.close();
                }
            }
        }

        private byte[] readFile(String filePath) {
            try {
                return java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(filePath));
            } catch (IOException e) {
                return null;
            }
        }
    }
}
