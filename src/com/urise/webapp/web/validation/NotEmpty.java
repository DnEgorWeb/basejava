package com.urise.webapp.web.validation;

public class NotEmpty implements Validator<String, String> {
    @Override
    public String validate(String name, String value) {
        if (value.trim().length() == 0) {
            return name + " should not be empty";
        }
        return null;
    }
}
