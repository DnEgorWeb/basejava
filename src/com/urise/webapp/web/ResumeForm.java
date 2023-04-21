package com.urise.webapp.web;

import com.urise.webapp.model.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ResumeForm implements Serializable {
    private String fullName;
    private Map<ContactType, String> contacts;
    private Map<SectionType, String> stringSections;

    private Map<SectionType, List<String>> listSections;
    private Map<SectionType, List<CompanyForm>> companySections;

    // for returning initial create form
    public static ResumeForm buildEmptyForm() {
        ResumeForm form = new ResumeForm();
        form.setFullName(null);
        form.setContacts(new HashMap<ContactType, String>() {{
            for (ContactType ct : ContactType.values()) {
                put(ct, null);
            }
        }});
        form.setStringSections(new HashMap<SectionType, String>() {{
            put(SectionType.OBJECTIVE, null);
            put(SectionType.PERSONAL, null);
        }});
        form.setListSections(new HashMap<SectionType, List<String>>() {{
            put(SectionType.ACHIEVEMENT, null);
            put(SectionType.QUALIFICATIONS, null);
        }});
        form.setCompanySections(new HashMap<SectionType, List<CompanyForm>>() {{
            put(SectionType.EXPERIENCE, Collections.singletonList(new CompanyForm(null, null, Collections.singletonList(new CompanyForm.PeriodForm(null, null, null, null)))));
            put(SectionType.EDUCATION, Collections.singletonList(new CompanyForm(null, null, Collections.singletonList(new CompanyForm.PeriodForm(null, null, null, null)))));
        }});
        return form;
    }

    // for returning existing resumes (view / put)
    public static ResumeForm buildFrom(Resume r) {
        ResumeForm form = new ResumeForm();
        form.setFullName(r.getFullName());
        form.setContacts(r.getContacts());
        form.setStringSections(new HashMap<SectionType, String>() {{
            put(SectionType.PERSONAL, ((TextSection) r.getSection(SectionType.PERSONAL)).getText());
            put(SectionType.OBJECTIVE, ((TextSection) r.getSection(SectionType.OBJECTIVE)).getText());
        }});
        form.setListSections(new HashMap<SectionType, List<String>>() {{
            put(SectionType.ACHIEVEMENT, ((ListSection) r.getSection(SectionType.ACHIEVEMENT)).getList());
            put(SectionType.QUALIFICATIONS, ((ListSection) r.getSection(SectionType.QUALIFICATIONS)).getList());
        }});
        form.setCompanySections(new HashMap<SectionType, List<CompanyForm>>() {{
            put(SectionType.EXPERIENCE, ((CompanySection) r.getSection(SectionType.EXPERIENCE)).getCompanies().stream().map(CompanyForm::buildFrom).collect(Collectors.toList()));
            put(SectionType.EDUCATION, ((CompanySection) r.getSection(SectionType.EXPERIENCE)).getCompanies().stream().map(CompanyForm::buildFrom).collect(Collectors.toList()));
        }});
        return form;
    }

    public Resume buildResume() {
        Resume r = new Resume(fullName);
        r.setContacts(contacts);
        stringSections.forEach((key, value) -> r.addSection(key, new TextSection(value)));
        listSections.forEach((key, value) -> {
            r.addSection(key, new ListSection(value));
        });
        companySections.forEach((key, companyForms) -> {
            List<Company> companies = new ArrayList<>();
            companyForms.forEach(company -> {
                List<Company.Period> periods = new ArrayList<>();
                company.getPeriods().forEach(period -> {
                    periods.add(new Company.Period(LocalDate.parse(period.startDate),
                            LocalDate.parse(period.endDate),
                            period.getTitle(),
                            period.getDescription()));
                });

                companies.add(new Company(company.getName(),
                        company.getWebsite(),
                        periods));
            });

            r.addSection(key, new CompanySection(companies));
        });

        return r;
    }

    public boolean isEmpty() {
        if (fullName != null) {
            return false;
        }
        for (String value : stringSections.values()) {
            if (value != null) {
                return false;
            }
        }
        for (List<String> list : listSections.values()) {
            for (String value : list) {
                if (value != null) {
                    return false;
                }
            }
        }
        for (List<CompanyForm> companyForms : companySections.values()) {
            for (CompanyForm companyForm : companyForms) {
                if (companyForm.getName() != null || companyForm.getWebsite() != null) {
                    return false;
                }
                for (CompanyForm.PeriodForm period : companyForm.getPeriods()) {
                    if (period.getTitle() != null || period.getDescription() != null || period.getStartDate() != null || period.getEndDate() != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public String getContact(ContactType ct) {
        return contacts.get(ct);
    }

    public void setContact(ContactType ct, String value) {
        contacts.put(ct, value);
    }

    public String getFullName() {
        return fullName;
    }

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public void setContacts(Map<ContactType, String> contacts) {
        this.contacts = contacts;
    }

    public Map<SectionType, String> getStringSections() {
        return stringSections;
    }

    public void setStringSections(Map<SectionType, String> stringSections) {
        this.stringSections = stringSections;
    }

    public Map<SectionType, List<String>> getListSections() {
        return listSections;
    }

    public void setListSections(Map<SectionType, List<String>> listSections) {
        this.listSections = listSections;
    }

    public String getStringSection(SectionType sectionType) {
        return stringSections.get(sectionType);
    }

    public List<String> getListSection(SectionType sectionType) {
        return listSections.get(sectionType);
    }

    public void setStringSection(SectionType sectionType, String value) {
        stringSections.put(sectionType, value);
    }

    public void setListSection(SectionType sectionType, List<String> values) {
        listSections.put(sectionType, values);
        listSections.size();
    }

    public Map<SectionType, List<CompanyForm>> getCompanySections() {
        return companySections;
    }

    public void setCompanySections(HashMap<SectionType, List<CompanyForm>> companySections) {
        this.companySections = companySections;
    }

    public List<CompanyForm> getCompanySection(SectionType sectionType) {
        return companySections.get(sectionType);
    }

    public void setCompanySection(SectionType sectionType, List<CompanyForm> companyForms) {
        companySections.put(sectionType, companyForms);
    }

    public List<CompanyForm> getCompanies(SectionType sectionType) {
        return companySections.get(sectionType);
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public static class CompanyForm {
        private String name;
        private String website;
        private List<PeriodForm> periods;

        public CompanyForm(String name, String website, List<PeriodForm> periods) {
            this.name = name;
            this.website = website;
            this.periods = periods;
        }

        public static CompanyForm buildFrom(Company company) {
            return new CompanyForm(company.getName(),
                    company.getWebsite(),
                    company.getPeriods().stream().map(PeriodForm::buildFrom).collect(Collectors.toList()));
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public List<PeriodForm> getPeriods() {
            return periods;
        }

        public void setPeriods(List<PeriodForm> periods) {
            this.periods = periods;
        }

        public static class PeriodForm {
            private String title;
            private String description;
            private String startDate;
            private String endDate;

            public PeriodForm(String title, String description, String startDate, String endDate) {
                this.title = title;
                this.description = description;
                this.startDate = startDate;
                this.endDate = endDate;
            }

            public static PeriodForm buildFrom(Company.Period period) {
                return new PeriodForm(period.getTitle(),
                        period.getDescription(),
                        period.getStartDate().toString(),
                        period.getEndDate().toString());
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getStartDate() {
                return startDate;
            }

            public void setStartDate(String startDate) {
                this.startDate = startDate;
            }

            public String getEndDate() {
                return endDate;
            }

            public void setEndDate(String endDate) {
                this.endDate = endDate;
            }
        }
    }
}
