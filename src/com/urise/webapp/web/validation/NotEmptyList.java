package com.urise.webapp.web.validation;

import java.util.List;

public class NotEmptyList implements Validator<List<String>, String> {
    @Override
    public String validate(String name, List<String> list) {
        if (String.join("", list).trim().length() == 0) {
            return name + " should not be empty";
        }
        return null;
    }
}
