package com.urise.webapp.model;

public enum SectionType {
    OBJECTIVE("Position"),
    PERSONAL("Personal qualities"),
    ACHIEVEMENT("Achievements"),
    QUALIFICATIONS("Qualification"),
    EXPERIENCE("Job experience"),
    EDUCATION("Education");

    private final String title;

    SectionType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Section{" +
                "title='" + title + '\'' +
                '}';
    }
}
