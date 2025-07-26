package com.asyncsite.studyservice.common;

public sealed interface Result<T, E> {
    record Success<T, E>(T data) implements Result<T, E> {}
    record Failure<T, E>(E error) implements Result<T, E> {}
    
    static <T, E> Result<T, E> success(T data) {
        return new Success<>(data);
    }
    
    static <T, E> Result<T, E> failure(E error) {
        return new Failure<>(error);
    }
    
    default boolean isSuccess() {
        return this instanceof Success;
    }
    
    default boolean isFailure() {
        return this instanceof Failure;
    }
    
    default T getData() {
        return switch (this) {
            case Success<T, E> success -> success.data();
            case Failure<T, E> failure -> throw new IllegalStateException("Cannot get data from failure result");
        };
    }
    
    default E getError() {
        return switch (this) {
            case Success<T, E> success -> throw new IllegalStateException("Cannot get error from success result");
            case Failure<T, E> failure -> failure.error();
        };
    }
}