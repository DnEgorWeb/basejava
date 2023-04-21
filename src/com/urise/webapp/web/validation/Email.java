package com.urise.webapp.web.validation;

import org.apache.commons.validator.routines.EmailValidator;

public class Email implements Validator<String, String> {
    @Override
    public String validate(String name, String value) {
        if (!EmailValidator.getInstance().isValid(value)) {
            return name.toLowerCase() + " with value " + value + " is not a valid email";
        }
        return null;
    }
}
