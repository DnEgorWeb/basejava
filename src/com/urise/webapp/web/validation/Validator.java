package com.urise.webapp.web.validation;

public interface Validator<T, R> {
    R validate(String name, T value);
}
