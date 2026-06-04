package org.fp024.domain;

import lombok.Getter;

/** Enum이름 그대로 DB에 저장하기 때문에, CodeOperation을 구현하진 않았다. */
public enum MemberAuthType {
  ROLE_USER(Groups.USER, RoleNames.ROLE_USER),
  ROLE_MEMBER(Groups.MEMBER, RoleNames.ROLE_MEMBER),
  ROLE_ADMIN(Groups.ADMIN, RoleNames.ROLE_ADMIN);

  private static final class Groups {
    private static final String USER = "USER";
    private static final String MEMBER = "MEMBER";
    private static final String ADMIN = "ADMIN";
  }

  public static final class RoleNames {
    private static final String PREFIX = "ROLE_";
    public static final String ROLE_USER = PREFIX + Groups.USER;
    public static final String ROLE_MEMBER = PREFIX + Groups.MEMBER;
    public static final String ROLE_ADMIN = PREFIX + Groups.ADMIN;
  }

  /** 권한 그룹 이름 */
  @Getter
  private final String groupName;

  /** 권한 전체 이름 */
  @Getter private final String roleName;

  MemberAuthType(String groupName, String roleName) {
    this.groupName = groupName;
    this.roleName = roleName;
  }
}