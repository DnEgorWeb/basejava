package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class CompanySection extends AbstractSection {
    private final List<Company> companies;

    CompanySection(List<Company> companies) {
        this.companies = companies;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    @Override
    public String toString() {
        return "CompanySection{" +
                "companies=" + companies +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanySection that = (CompanySection) o;
        List<Company> differences = new ArrayList<>(that.companies);
        companies.forEach(differences::remove);
        return differences.size() == 0;
    }

    @Override
    public int hashCode() {
        return companies.hashCode();
    }
}
