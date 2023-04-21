package com.urise.webapp.web.validation;

import com.urise.webapp.web.ResumeForm;

import java.util.ArrayList;
import java.util.List;

public class CompanyForm implements Validator<ResumeForm.CompanyForm, ResumeForm.CompanyForm> {
    private final NotEmpty notEmpty = new NotEmpty();
    @Override
    public ResumeForm.CompanyForm validate(String name, ResumeForm.CompanyForm form) {
        boolean hasPeriods = !form.getPeriods().isEmpty();
        List<ResumeForm.CompanyForm.PeriodForm> companyPeriodErrors = new ArrayList<>();
        if (hasPeriods) {
            for (int i = 0; i < form.getPeriods().size(); i++) {
                companyPeriodErrors.add(new ResumeForm.CompanyForm.PeriodForm(null, null, null, null));
            }
        } else {
            companyPeriodErrors.add(new ResumeForm.CompanyForm.PeriodForm(null, null, null, null));
        }
        ResumeForm.CompanyForm companyErrors = new ResumeForm.CompanyForm(null, null, companyPeriodErrors);

        if (form.getName().isEmpty()) {
            companyErrors.setName("name should be fulfilled");
        }
        String urlViolation = new Url().validate("website", form.getWebsite());
        if (form.getWebsite().isEmpty()) {
            companyErrors.setWebsite("website should be fulfilled");
        } else if (urlViolation != null) {
            companyErrors.setWebsite(urlViolation);
        }
        if (form.getPeriods().isEmpty()) {
            companyErrors.getPeriods().get(0).setTitle("Periods are required");
        }

        for (int i = 0; i < form.getPeriods().size(); i++) {
            ResumeForm.CompanyForm.PeriodForm formPeriod = form.getPeriods().get(i);
            ResumeForm.CompanyForm.PeriodForm periodError = companyPeriodErrors.get(i);

            String emptyTitleError = notEmpty.validate("title", formPeriod.getTitle());
            if (emptyTitleError != null) {
                periodError.setTitle(emptyTitleError);
            }
            String emptyDescriptionError = notEmpty.validate("description", formPeriod.getDescription());
            if (emptyDescriptionError != null) {
                periodError.setDescription(emptyDescriptionError);
            }
            String emptyStartDateError = notEmpty.validate("start date", formPeriod.getStartDate());
            if (emptyStartDateError != null) {
                periodError.setStartDate(emptyStartDateError);
            }
            String emptyEndDateError = notEmpty.validate("end date", formPeriod.getEndDate());
            if (emptyEndDateError != null) {
                periodError.setEndDate(emptyEndDateError);
            }
        }

        return companyErrors;
    }
}
