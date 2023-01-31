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

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getWebsite() {
        return website;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    @Override
    public String toString() {
        return "Company{" +
                "title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", website='" + website + '\'' +
                ", periods=" + periods +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return title.equals(company.title);
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }
}
