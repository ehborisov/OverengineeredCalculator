package com.example.calc;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/rest")
public class CalculatorApp extends ResourceConfig {
    public CalculatorApp() {
        packages("com.example.calc");
    }
}
