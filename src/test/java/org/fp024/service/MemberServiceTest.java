package org.fp024.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.fp024.config.RootConfig;
import org.fp024.domain.MemberDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@Slf4j
@SpringJUnitConfig(classes = {RootConfig.class})
class MemberServiceTest {

  @Autowired private MemberService memberService;

  @Test
  void testRead() {
    Optional<MemberDTO> member = memberService.read("admin90");

    assertTrue(member.isPresent());

    LOGGER.info("{}", member.get());
  }
}
