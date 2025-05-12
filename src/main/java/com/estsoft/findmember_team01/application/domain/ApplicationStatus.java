package com.estsoft.findmember_team01.application.domain;

public enum ApplicationStatus {
    PENDING("대기중"), ACCEPTED("승인됨"), REJECTED("거절됨");

    private final String displayName;

    ApplicationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

