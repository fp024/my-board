package org.fp024.domain;

import lombok.Getter;

/**
 * Y, N에 대한 제한을 두고 싶어서, Enum으로 따로 만들었다. <br>
 * Enum에 형식을 강제하고 싶을 때는, 인터페이스 구현하게 하면 되나보다.
 */
public enum EnabledType implements CodeOperation {
  YES("Y"),
  NO("N");

  @Getter private final String code;

  EnabledType(String code) {
    this.code = code;
  }
}
