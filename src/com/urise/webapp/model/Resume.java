package com.urise.webapp.model;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume> {

    private final String uuid;
    private String fullName;

    private Map<Contact, String> contacts;
    private Map<Section, AbstractSection> sections;

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        this.uuid = uuid;
        this.fullName = fullName;
        this.contacts = new HashMap<>();
        this.sections = new HashMap<>();
    }

    public Resume(String uuid, String fullName, Map<Contact, String> contacts, Map<Section, AbstractSection> sections) {
        this.uuid = uuid;
        this.fullName = fullName;
        this.contacts = contacts;
        this.sections = sections;
    }

    @Override
    public String toString() {
        return uuid + " (" + fullName + ")";
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Map<Contact, String> getContacts() {
        return new TreeMap<>(contacts);
    }

    public void setContacts(Map<Contact, String> contacts) {
        this.contacts = contacts;
    }

    public Map<Section, AbstractSection> getSections() {
        return new TreeMap<>(sections);
    }

    public void setSections(Map<Section, AbstractSection> sections) {
        this.sections = sections;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != getClass()) return false;

        Resume resume = (Resume) obj;

        if (!uuid.equals(resume.uuid)) return false;
        return fullName.equals(resume.fullName);
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        return 31 * result + fullName.hashCode();
    }

    @Override
    public int compareTo(Resume o) {
        int cmp = fullName.compareTo(o.fullName);
        return cmp == 0 ? uuid.compareTo(o.uuid) : cmp;
    }
}
