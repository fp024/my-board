package org.fp024.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.fp024.domain.generated.BoardAttachVO;
import org.fp024.domain.generated.BoardVO;

@Setter
@Getter
@ToString
public class BoardDTO {

  private BoardVO boardVO;

  private List<BoardAttachVO> attachList;

}
