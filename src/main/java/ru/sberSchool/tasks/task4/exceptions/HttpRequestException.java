package ru.sberSchool.tasks.task4.exceptions;

public class HttpRequestException extends Exception {
    public HttpRequestException(String message) {
        super(message);
    }

    public HttpRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}

