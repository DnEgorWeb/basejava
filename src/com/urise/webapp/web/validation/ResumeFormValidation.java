package com.urise.webapp.web.validation;

import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.SectionType;
import com.urise.webapp.web.ResumeForm;

import java.util.*;

public class ResumeFormValidation implements Validator<ResumeForm, ResumeForm> {
    private final NotEmpty notEmpty = new NotEmpty();
    private final NotEmptyList notEmptyList = new NotEmptyList();
    private final CompanyForm companyValidator = new CompanyForm();
    private final Map<ContactType, List<Validator<String, String>>> contactValidators = new HashMap<ContactType, List<Validator<String, String>>>() {{
        Validator<String, String> urlValidator = new AllowEmpty(new Url());
        put(ContactType.EMAIL, Collections.singletonList(new AllowEmpty(new Email())));
        put(ContactType.SKYPE, Collections.singletonList(new AllowEmpty(new Skype())));
        put(ContactType.LINKEDIN, Collections.singletonList(urlValidator));
        put(ContactType.GITHUB, Collections.singletonList(urlValidator));
        put(ContactType.STACKOVERFLOW, Collections.singletonList(urlValidator));
        put(ContactType.HOMEPAGE, Collections.singletonList(urlValidator));
    }};

    @Override
    public ResumeForm validate(String name, ResumeForm form) {
        ResumeForm errors = ResumeForm.buildEmptyForm();

        errors.setFullName(notEmpty.validate("name", form.getFullName()));
        Map<ContactType, String> contactErrors = new HashMap<>();
        for (ContactType contactType : ContactType.values()) {
            contactErrors.put(contactType, validateContact(contactType, form.getContact(contactType)));
        }
        errors.setContacts(contactErrors);
        Validator<String, String> notEmptyWhitespaces = new AllowEmpty(new NotEmpty());
        errors.setStringSection(SectionType.PERSONAL, notEmptyWhitespaces.validate(SectionType.PERSONAL.getTitle(), form.getStringSection(SectionType.PERSONAL)));
        errors.setStringSection(SectionType.OBJECTIVE, notEmptyWhitespaces.validate(SectionType.OBJECTIVE.getTitle(), form.getStringSection(SectionType.OBJECTIVE)));

        errors.setListSection(SectionType.ACHIEVEMENT, Collections.singletonList(null));
        errors.setListSection(SectionType.QUALIFICATIONS, Collections.singletonList(null));

        List<ResumeForm.CompanyForm> companyFormErrors = new ArrayList<>();
        for (int i = 0; i < form.getCompanySection(SectionType.EXPERIENCE).size(); i++) {
            companyFormErrors.add(companyValidator.validate(SectionType.EXPERIENCE.getTitle(), form.getCompanySection(SectionType.EXPERIENCE).get(i)));
        }
        errors.setCompanySection(SectionType.EXPERIENCE, companyFormErrors);

        List<ResumeForm.CompanyForm> educationFormErrors = new ArrayList<>();
        for (int i = 0; i < form.getCompanySection(SectionType.EDUCATION).size(); i++) {
            educationFormErrors.add(companyValidator.validate(SectionType.EDUCATION.getTitle(), form.getCompanySection(SectionType.EDUCATION).get(i)));
        }
        errors.setCompanySection(SectionType.EDUCATION, educationFormErrors);

        return errors;
    }

    private String validateContact(ContactType key, String value) {
        List<Validator<String, String>> validators = contactValidators.get(key);
        if (validators == null) {
            return null;
        }
        return validators.stream()
                .map(v -> v.validate(key.getTitle(), value))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
