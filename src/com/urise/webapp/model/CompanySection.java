package com.urise.webapp.model;

import java.util.List;

public class CompanySection extends AbstractSection {
    private final List<Company> companies;

    CompanySection(List<Company> companies) {
        this.companies = companies;
    }
}
