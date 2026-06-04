package org.fp024.domain.generated;

import java.time.LocalDateTime;
import org.fp024.domain.EnabledType;

public class MemberVO {
    private String userId;

    private String userPassword;

    private String userName;

    private LocalDateTime registerDate;

    private LocalDateTime updateDate;

    private EnabledType enabled;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword == null ? null : userPassword.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public EnabledType getEnabled() {
        return enabled;
    }

    public void setEnabled(EnabledType enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", userPassword=").append(userPassword);
        sb.append(", userName=").append(userName);
        sb.append(", registerDate=").append(registerDate);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", enabled=").append(enabled);
        sb.append("]");
        return sb.toString();
    }
}