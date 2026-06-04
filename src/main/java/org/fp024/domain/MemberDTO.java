package org.fp024.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.fp024.domain.generated.AuthVO;
import org.fp024.domain.generated.MemberVO;

@Getter
@Setter
@ToString
public class MemberDTO {
  private MemberVO memberVO;

  private List<AuthVO> authList;
}
