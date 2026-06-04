package org.fp024.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AttachFileDTO {
  /** 파일 명 */
  private String fileName;

  /** 파일 경로 */
  private String uploadPath;

  /** UUID */
  private String uuid;

  /** 파일 타입 */
  private FileType fileType;

  /** 업로드 성공 여부 */
  private boolean success;
}
