package org.fp024.domain;

import lombok.Getter;

/** 파일 타입 */
public enum FileType implements CodeOperation {
  NORMAL("N"),
  IMAGE("I");

  @Getter private final String code;

  FileType(String code) {
    this.code = code;
  }
}
