package com.urise.webapp.model;

import java.util.List;

public class Company {
    private final String title;
    private final String name;
    private final String website;
    private final List<Period> periods;

    Company(String title, String name, String website, List<Period> periods) {
        this.title = title;
        this.name = name;
        this.website = website;
        this.periods = periods;
    }
}
