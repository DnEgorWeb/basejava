package com.urise.webapp.model;

import java.time.LocalDate;

public class Period {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String title;
    private final String description;

    Period(LocalDate startDate, LocalDate endDate, String title, String description) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.description = description;
    }
}
