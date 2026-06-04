package org.fp024.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.fp024.config.RootConfig;
import org.fp024.config.ServletConfig;
import org.fp024.domain.generated.ReplyVO;
import org.fp024.service.ReplyService;
import org.fp024.util.GsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringJUnitWebConfig(classes = {RootConfig.class, ServletConfig.class})
@Slf4j
class ReplyControllerTest {
  @Autowired private ReplyService service;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    this.mockMvc =
        MockMvcBuilders.standaloneSetup(new ReplyController(service))
            // 설정을 임의로 해주면, 기본 목록에 더해서 추가하는 것이 아니여서, 몇가지 사용하는 것을 써줘야한다.
            // 그런데 기본목록이 ServletConfig 에 사용자 정의한 내용이 추가 되는게 아님.
            .setMessageConverters(
                new StringHttpMessageConverter(), GsonHelper.gsonHttpMessageConverter())
            .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
            .build();
  }

  @Test
  void testCreate() throws Exception {
    ReplyVO reply = new ReplyVO();
    reply.setBno(1L);
    reply.setReply("Hello Reply");
    reply.setReplyer("user00");

    String jsonBody = GsonHelper.gson().toJson(reply);

    String responseContent =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/replies/new")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonBody))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertThat(responseContent, is("Success"));
  }

  @Test
  void testGetList() throws Exception {
    String responseContent =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/replies/pages/1/2").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    LOGGER.info("{}", responseContent);
  }

  @Test
  void testGet() throws Exception {
    String responseContent =
        mockMvc
            .perform(MockMvcRequestBuilders.get("/replies/6").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    LOGGER.info("{}", responseContent);
  }

  /** 응답을 Success 평문으로 받으므로.. /replies/43.json으로 요청 시도할 경우 406 응답을 받게된다. */
  @Test
  void testRemove() throws Exception {
    ReplyVO reply = new ReplyVO();
    reply.setRno(43L);
    reply.setReplyer("user00");

    String jsonBody = GsonHelper.gson().toJson(reply);

    String responseContent =
        mockMvc
            .perform(
                MockMvcRequestBuilders.delete("/replies/43")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonBody))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    LOGGER.info("{}", responseContent);
  }

  @Test
  void testModify() throws Exception {
    ReplyVO reply = new ReplyVO();
    reply.setRno(2L);
    reply.setReply(LocalDateTime.now().toString());

    String jsonBody = GsonHelper.toJson(reply);

    String responseContent =
        mockMvc
            .perform(
                MockMvcRequestBuilders.put("/replies/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonBody))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    LOGGER.info("{}", responseContent);
  }
}
