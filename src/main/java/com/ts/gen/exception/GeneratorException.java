package com.ts.gen.exception;

public class GeneratorException extends Throwable {
    private String msg;

    public GeneratorException(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }
}
