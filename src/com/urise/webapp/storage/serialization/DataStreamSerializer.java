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
                AbstractSection section;
                switch (className) {
                    case "ListSection":
                        section = new ListSection(readList(dis));
                        break;
                    case "TextSection":
                        section = new TextSection(dis.readUTF());
                        break;
                    case "CompanySection":
                        section = new CompanySection(readCompanies(dis));
                        break;
                    default:
                        throw new IllegalStateException("Unexpected section type");
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
            writeContacts(dos, r.getContacts());
            writeSections(dos, r.getSections());
        }
    }

    private List<String> readList(DataInputStream dis) throws IOException {
        int listSize = dis.readInt();
        List<String> list = new ArrayList<>();
        for (int j = 0; j < listSize; j++) {
            String val = dis.readUTF();
            list.add(val);
        }
        return list;
    }

    private List<Company> readCompanies(DataInputStream dis) throws IOException {
        int companiesSize = dis.readInt();
        List<Company> companies = new ArrayList<>();
        for (int k = 0; k < companiesSize; k++) {
            String name = dis.readUTF();
            String websiteNullMarker = dis.readUTF();
            String website = websiteNullMarker.equals("null") ? null : dis.readUTF();
            List<Company.Period> periods = readPeriods(dis);
            companies.add(new Company(name, website, periods));
        }
        return companies;
    }

    private List<Company.Period> readPeriods(DataInputStream dis) throws IOException {
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
        return periods;
    }

    private void writeContacts(DataOutputStream dos, Map<ContactType, String> contacts) throws IOException {
        dos.writeInt(contacts.size());
        for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
            dos.writeUTF(entry.getKey().name());
            dos.writeUTF(entry.getValue());
        }
    }

    private void writeSections(DataOutputStream dos, Map<SectionType, AbstractSection> sections) throws IOException {
        dos.writeInt(sections.size());
        for (Map.Entry<SectionType, AbstractSection> entry : sections.entrySet()) {
            dos.writeUTF(entry.getKey().name());
            AbstractSection section = entry.getValue();
            switch (section.getClass().getSimpleName()) {
                case "ListSection":
                    writeListSection(dos, (ListSection) section);
                    break;
                case "TextSection":
                    writeTextSection(dos, (TextSection) section);
                    break;
                case "CompanySection":
                    writeCompanySection(dos, (CompanySection) section);
                    break;
                default:
                    throw new IllegalStateException("Unknown section type");
            }
        }
    }

    private void writeListSection(DataOutputStream dos, ListSection section) throws IOException {
        dos.writeUTF(ListSection.class.getSimpleName());
        List<String> list = section.getList();
        int listSize = list.size();
        dos.writeInt(listSize);
        for (String s : list) {
            dos.writeUTF(s);
        }
    }

    private void writeTextSection(DataOutputStream dos, TextSection section) throws IOException {
        dos.writeUTF(TextSection.class.getSimpleName());
        dos.writeUTF(section.getText());
    }

    private void writeCompanySection(DataOutputStream dos, CompanySection section) throws IOException {
        dos.writeUTF(CompanySection.class.getSimpleName());
        writeCompanies(dos, section.getCompanies());
    }

    private void writeCompanies(DataOutputStream dos, List<Company> companies) throws IOException {
        dos.writeInt(companies.size());
        for (Company company : companies) {
            dos.writeUTF(company.getName());
            writeNullableUTF(dos, company.getWebsite());
            writePeriods(dos, company.getPeriods());
        }
    }

    private void writePeriods(DataOutputStream dos, List<Company.Period> periods) throws IOException {
        dos.writeInt(periods.size());
        for (Company.Period period : periods) {
            dos.writeUTF(period.getStartDate().toString());
            writeNullableUTF(dos, period.getEndDate());
            dos.writeUTF(period.getTitle());
            writeNullableUTF(dos, period.getDescription());
        }
    }

    private void writeNullableUTF(DataOutputStream dos, Object o) throws IOException {
        dos.writeUTF(o == null ? "null" : "");
        if (o != null) {
            dos.writeUTF(o.toString());
        }
    }
}
