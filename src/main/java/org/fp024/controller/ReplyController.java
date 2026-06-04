package org.fp024.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fp024.domain.Criteria;
import org.fp024.domain.PageSize;
import org.fp024.domain.ReplyPageDTO;
import org.fp024.domain.generated.ReplyVO;
import org.fp024.service.ReplyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/replies/")
@RestController
@Slf4j
@RequiredArgsConstructor
public class ReplyController {
  private final ReplyService replyService;

  /**
   * 댓글 등록
   *
   * @param vo 댓글
   * @return 등록 결과 성공시 "Success" 반환
   */
  @PreAuthorize("isAuthenticated()")
  @PostMapping(
      value = "/new",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = {MediaType.TEXT_PLAIN_VALUE})
  public ResponseEntity<String> create(@RequestBody ReplyVO vo) {
    LOGGER.info("ReplyVO: {}", vo);
    try {
      replyService.register(vo);
      LOGGER.info("Reply INSERT Success");
      return new ResponseEntity<>("Success", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * 특정 게시물의 목록 확인
   *
   * @param page 댓글 페이지 번호
   * @param bno 게시물 번호
   * @return 특정 게시물 목록
   */
  @GetMapping(value = "/pages/{bno}/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ReplyPageDTO> getList(
      @PathVariable("page") int page, @PathVariable("bno") Long bno) {
    LOGGER.info("getList.....");
    Criteria cri = new Criteria(page, PageSize.SIZE_10);
    LOGGER.info("cri: {}", cri);
    return new ResponseEntity<>(replyService.getListPage(cri, bno), HttpStatus.OK);
  }

  /**
   * 댓글 조회
   *
   * @param rno 댓글 번호
   * @return 댓글
   */
  @GetMapping(value = "/{rno}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ReplyVO> get(@PathVariable("rno") Long rno) {
    LOGGER.info("get: {}", rno);
    return new ResponseEntity<>(replyService.get(rno), HttpStatus.OK);
  }

  /**
   * 댓글 삭제
   *
   * @param rno 댓글 번호
   * @return 삭제 성공 여부
   */
  @PreAuthorize("principal.username == #vo.replyer")
  @DeleteMapping(
      value = "/{rno}",
      produces = {MediaType.TEXT_PLAIN_VALUE})
  public ResponseEntity<String> remove(@RequestBody ReplyVO vo, @PathVariable("rno") Long rno) {
    LOGGER.info("remove: {}", rno);
    LOGGER.info("replyer: {}", vo.getReplyer());
    try {
      if (replyService.remove(vo.getRno()) == 1) {
        return new ResponseEntity<>("Success", HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Failure", HttpStatus.OK);
      }
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * 댓글 수정
   *
   * @param vo 댓글 내용
   * @param rno 댓글 번호
   * @return 수정 성공 여부
   */
  @PreAuthorize("principal.username == #vo.replyer")
  @RequestMapping(
      method = {RequestMethod.PUT, RequestMethod.PATCH},
      value = "/{rno}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> modify(@RequestBody ReplyVO vo, @PathVariable("rno") Long rno) {
    vo.setRno(rno);
    LOGGER.info("rno: {}", rno);
    try {
      if (replyService.modify(vo) == 1) {
        return new ResponseEntity<>("Success", HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
