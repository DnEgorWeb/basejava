package com.urise.webapp.storage.serialization;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {
    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            int contactsSize = dis.readInt();
            for (int i = 0; i < contactsSize; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }
            int sectionsSize = dis.readInt();
            for (int i = 0; i < sectionsSize; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                String className = dis.readUTF();
                AbstractSection section = null;
                if (className.equals(ListSection.class.getName())) {
                    int listSize = dis.readInt();
                    List<String> list = new ArrayList<>();
                    for (int j = 0; j < listSize; j++) {
                        String val = dis.readUTF();
                        list.add(val);
                    }
                    section = new ListSection(list);
                } else if (className.equals(TextSection.class.getName())) {
                    section = new TextSection(dis.readUTF());
                } else if (className.equals(CompanySection.class.getName())) {
                    int companiesSize = dis.readInt();
                    List<Company> companies = new ArrayList<>();
                    for (int k = 0; k < companiesSize; k++) {
                        String name = dis.readUTF();
                        String websiteNullMarker = dis.readUTF();
                        String website = websiteNullMarker.equals("null") ? null : dis.readUTF();
                        int periodsSize = dis.readInt();
                        List<Company.Period> periods = new ArrayList<>();
                        for (int l = 0; l < periodsSize; l++) {
                            LocalDate startDate = LocalDate.parse(dis.readUTF());
                            String endDateNullMarker = dis.readUTF();
                            LocalDate endDate = endDateNullMarker.equals("null") ? null : LocalDate.parse(dis.readUTF());
                            String title = dis.readUTF();
                            String descriptionNullMarker = dis.readUTF();
                            String description = descriptionNullMarker.equals("null") ? null : dis.readUTF();
                            periods.add(new Company.Period(startDate, endDate, title, description));
                        }
                        companies.add(new Company(name, website, periods));
                    }
                    section = new CompanySection(companies);
                }
                resume.addSection(sectionType, section);
            }
            return resume;
        }
    }

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }
            Map<SectionType, AbstractSection> sections = r.getSections();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, AbstractSection> entry : sections.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                AbstractSection section = entry.getValue();
                if (section instanceof ListSection) {
                    ListSection ls = (ListSection) section;
                    dos.writeUTF(ListSection.class.getName());
                    List<String> list = ls.getList();
                    int listSize = list.size();
                    dos.writeInt(listSize);
                    for (String s : list) {
                        dos.writeUTF(s);
                    }
                } else if (section instanceof TextSection) {
                    TextSection ts = (TextSection) section;
                    dos.writeUTF(TextSection.class.getName());
                    dos.writeUTF(ts.getText());
                } else if (section instanceof CompanySection) {
                    CompanySection cs = (CompanySection) section;
                    dos.writeUTF(CompanySection.class.getName());
                    List<Company> companies = cs.getCompanies();
                    dos.writeInt(companies.size());
                    for (Company company : companies) {
                        dos.writeUTF(company.getName());
                        boolean isWebsiteNull = company.getWebsite() == null;
                        dos.writeUTF(isWebsiteNull ? "null" : "not-null");
                        if (!isWebsiteNull) {
                            dos.writeUTF(company.getWebsite());
                        }
                        List<Company.Period> periods = company.getPeriods();
                        dos.writeInt(periods.size());
                        for (Company.Period period : periods) {
                            dos.writeUTF(period.getStartDate().toString());
                            boolean isEndDateNull = period.getEndDate() == null;
                            dos.writeUTF(isEndDateNull ? "null" : "not-null");
                            if (!isEndDateNull) {
                                dos.writeUTF(period.getEndDate().toString());
                            }
                            dos.writeUTF(period.getTitle());
                            boolean isDescriptionNull = period.getDescription() == null;
                            dos.writeUTF(isDescriptionNull ? "null" : "not-null");
                            if (!isDescriptionNull) {
                                dos.writeUTF(period.getDescription());
                            }
                        }
                    }
                }
            }
        }
    }
}
