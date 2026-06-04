package org.fp024.domain.generated;

import org.fp024.domain.MemberAuthType;

public class AuthVO {
    private String userId;

    private MemberAuthType auth;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public MemberAuthType getAuth() {
        return auth;
    }

    public void setAuth(MemberAuthType auth) {
        this.auth = auth;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", auth=").append(auth);
        sb.append("]");
        return sb.toString();
    }
}