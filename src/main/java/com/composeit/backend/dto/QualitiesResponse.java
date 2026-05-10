package com.composeit.backend.dto;

import java.util.List;

public class QualitiesResponse {

    private List<QualityEntry> qualities;

    public QualitiesResponse(List<QualityEntry> qualities) {
        this.qualities = qualities;
    }

    public List<QualityEntry> getQualities() {
        return qualities;
    }

    public void setQualities(List<QualityEntry> qualities) {
        this.qualities = qualities;
    }

    public static class QualityEntry {
        private String id;
        private String label;

        public QualityEntry(String id, String label) {
            this.id = id;
            this.label = label;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}
