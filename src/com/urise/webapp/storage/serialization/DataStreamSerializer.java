package com.urise.webapp.storage.serialization;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataStreamSerializer implements StreamSerializer {
    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readWithException(dis, () -> {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
                return null;
            });
            readWithException(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                AbstractSection section;
                switch (sectionType) {
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        section = new ListSection(readList(dis, dis::readUTF));
                        break;
                    case PERSONAL:
                    case OBJECTIVE:
                        section = new TextSection(dis.readUTF());
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        section = new CompanySection(readList(dis, () -> {
                            String companyName = dis.readUTF();
                            String websiteNullMarker = dis.readUTF();
                            String website = websiteNullMarker.equals("null") ? null : dis.readUTF();
                            return new Company(companyName, website, readList(dis, () -> {
                                LocalDate startDate = LocalDate.parse(dis.readUTF());
                                String endDateNullMarker = dis.readUTF();
                                LocalDate endDate = endDateNullMarker.equals("null") ? null : LocalDate.parse(dis.readUTF());
                                String title = dis.readUTF();
                                String descriptionNullMarker = dis.readUTF();
                                String description = descriptionNullMarker.equals("null") ? null : dis.readUTF();
                                return new Company.Period(startDate, endDate, title, description);
                            }));
                        }));
                        break;
                    default:
                        throw new IllegalStateException("Unexpected section type");
                }
                resume.addSection(sectionType, section);
                return null;
            });
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

    private <T> List<T> readList(DataInputStream dis, ThrowingReader<T> reader) throws IOException {
        int listSize = dis.readInt();
        List<T> list = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            list.add(reader.read());
        }
        return list;
    }

    private void writeContacts(DataOutputStream dos, Map<ContactType, String> contacts) throws IOException {
        writeWithException(dos, contacts.entrySet(), (Map.Entry<ContactType, String> entry) -> {
            dos.writeUTF(entry.getKey().name());
            dos.writeUTF(entry.getValue());
        });
    }

    private void writeSections(DataOutputStream dos, Map<SectionType, AbstractSection> sections) throws IOException {
        writeWithException(dos, sections.entrySet(), (Map.Entry<SectionType, AbstractSection> entry) -> {
            SectionType sectionType = entry.getKey();
            dos.writeUTF(entry.getKey().name());
            AbstractSection section = entry.getValue();
            switch (sectionType) {
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    writeListSection(dos, (ListSection) section);
                    break;
                case PERSONAL:
                case OBJECTIVE:
                    writeTextSection(dos, (TextSection) section);
                    break;
                case EXPERIENCE:
                case EDUCATION:
                    writeCompanySection(dos, (CompanySection) section);
                    break;
                default:
                    throw new IllegalStateException("Unknown section type");
            }
        });
    }

    private void writeListSection(DataOutputStream dos, ListSection section) throws IOException {
        List<String> list = section.getList();
        writeWithException(dos, list, dos::writeUTF);
    }

    private void writeTextSection(DataOutputStream dos, TextSection section) throws IOException {
        dos.writeUTF(section.getText());
    }

    private void writeCompanySection(DataOutputStream dos, CompanySection section) throws IOException {
        writeCompanies(dos, section.getCompanies());
    }

    private void writeCompanies(DataOutputStream dos, List<Company> companies) throws IOException {
        writeWithException(dos, companies, (Company company) -> {
            dos.writeUTF(company.getName());
            writeNullableUTF(dos, company.getWebsite());
            writePeriods(dos, company.getPeriods());
        });
    }

    private void writePeriods(DataOutputStream dos, List<Company.Period> periods) throws IOException {
        writeWithException(dos, periods, (Company.Period period) -> {
            dos.writeUTF(period.getStartDate().toString());
            writeNullableUTF(dos, period.getEndDate());
            dos.writeUTF(period.getTitle());
            writeNullableUTF(dos, period.getDescription());
        });
    }

    private void writeNullableUTF(DataOutputStream dos, Object o) throws IOException {
        dos.writeUTF(o == null ? "null" : "");
        if (o != null) {
            dos.writeUTF(o.toString());
        }
    }

    private <T> void readWithException(DataInputStream dis, ThrowingSupplier<T> supplier) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            supplier.get();
        }
    }

    private <T> void writeWithException(DataOutputStream dos, Collection<T> collection, ThrowingConsumer<? super T> action) throws IOException {
        Objects.requireNonNull(action);
        dos.writeInt(collection.size());
        for (T entry : collection) {
            action.cb(entry);
        }
    }

    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        T get() throws IOException;
    }

    @FunctionalInterface
    interface ThrowingConsumer<T> {
        void cb(T action) throws IOException;
    }

    @FunctionalInterface
    interface ThrowingReader<T> {
        T read() throws IOException;
    }
}
