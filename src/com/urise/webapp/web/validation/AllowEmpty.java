package com.urise.webapp.web.validation;

public class AllowEmpty implements Validator<String, String> {
    private final Validator<String, String> validator;

    public AllowEmpty(Validator<String, String> validator) {
        this.validator = validator;
    }

    @Override
    public String validate(String name, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return validator.validate(name, value);
    }
}
