package com.urise.webapp.web.validation;

import org.apache.commons.validator.routines.UrlValidator;

public class Url implements Validator<String, String> {
    @Override
    public String validate(String name, String value) {
        if (new UrlValidator().isValid(value)) {
            return null;
        }
        return name.toLowerCase() + " with value " + value + " is not a valid url";
    }
}
