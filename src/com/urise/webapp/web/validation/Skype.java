package com.urise.webapp.web.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Skype implements Validator<String, String> {
    @Override
    public String validate(String name, String value) {
        Pattern pattern = Pattern.compile("^skype:[^\\\\?]+(\\\\?.+)?$");
        Matcher matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            return value + " is not a valid Skype link. Example: skype:nickname?add";
        }
        return null;
    }
}
