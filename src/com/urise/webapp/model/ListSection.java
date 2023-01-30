package com.urise.webapp.model;

import java.util.List;

public class ListSection extends AbstractSection {
    private final List<String> list;

    ListSection(List<String> list) {
        this.list = list;
    }
}
