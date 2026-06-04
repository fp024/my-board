package org.fp024.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fp024.util.CommonUtil.getFolderYesterday;
import static org.fp024.util.CommonUtil.getFolderYesterdayUnixPath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.fp024.domain.FileType;
import org.fp024.domain.generated.BoardAttachVO;
import org.fp024.mapper.generated.BoardAttachMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mybatis.dynamic.sql.dsl.SelectDSLCompleter;

@Slf4j
@ExtendWith(MockitoExtension.class)
class FileCheckTaskTest {
  @TempDir private File uploadDirRoot;

  private FileCheckTask task;

  @Mock private BoardAttachMapper attachMapper;

  private MockedStatic<LocalDateTime> mockedJSONContext;

  // 연관된 게시물이 있는 파일 정보
  private static final String FILE_UUID = "5c96644a-fbd3-457c-ab1f-50061153d375";
  private static final String FILE_NAME_ONLY = "연관된_게시물이_있는_이미지_파일.jpg";
  private static final String IMAGE_FILE_NAME = FILE_UUID + "_" + FILE_NAME_ONLY;

  // 연관된 게시물이 없는 파일 정보
  private static final String BROKEN_FILE_UUID = "1d96644a-fbd3-457c-ab1f-50061153d374";
  private static final String BROKEN_FILE_NAME_ONLY = "연관된_게시물이_삭제된_이미지_파일.jpg";
  private static final String BROKEN_IMAGE_FILE_NAME =
      BROKEN_FILE_UUID + "_" + BROKEN_FILE_NAME_ONLY;

  @BeforeEach
  public void beforeEach() throws IOException {
    mockedJSONContext = mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
    LocalDateTime testCurrentNow = LocalDateTime.of(2022, 12, 2, 2, 0, 0, 0);
    mockedJSONContext.when(LocalDateTime::now).thenReturn(testCurrentNow);

    task = new FileCheckTask(uploadDirRoot.getAbsolutePath(), attachMapper);

    makeFile(IMAGE_FILE_NAME);
    makeFile(BROKEN_IMAGE_FILE_NAME);
  }

  /** 임시 폴더 경로에 첨부파일 경로 생성 */
  private void makeFile(String imageFileName) throws IOException {
    new File(uploadDirRoot, getFolderYesterday() + File.separator).mkdirs();
    File attachFile =
        new File(uploadDirRoot, getFolderYesterday() + File.separator + imageFileName);
    File thumbnailAttachFile =
        new File(uploadDirRoot, getFolderYesterday() + File.separator + "s_" + imageFileName);

    attachFile.createNewFile();
    thumbnailAttachFile.createNewFile();

    assertThat(attachFile.exists()).isTrue();
    assertThat(thumbnailAttachFile.exists()).isTrue();
  }

  @AfterEach
  public void afterEach() {
    if (mockedJSONContext != null) {
      mockedJSONContext.close();
    }
  }

  @Test
  void testCheckFiles() {

    List<BoardAttachVO> list = getOldFilesTestData();
    File attachFile =
        new File(uploadDirRoot, getFolderYesterday() + File.separator + IMAGE_FILE_NAME);
    File brokenAttachFile =
        new File(uploadDirRoot, getFolderYesterday() + File.separator + BROKEN_IMAGE_FILE_NAME);

    /*
      // 코드 상에서 매퍼에다가 아래 람다식을 전달해주는데...
      // 이것을 매처로 전달해주게되면 실제 메서드에서 생성되는 람다 인스턴스하고 아래 인스턴스는 같은 것이 아니니 비교가 안된다.
      //
        c ->
            c.where(
                BoardAttachVODynamicSqlSupport.uploadPath, isEqualTo(getFolderYesterdayUnixPath()));
    */
    when(attachMapper.select(any(SelectDSLCompleter.class))).thenReturn(list);

    // when
    task.checkFiles();

    // then
    assertThat(attachFile.exists()).isTrue().as("게시물과 연관된 첨부파일은 삭제하지 않음");
    assertThat(brokenAttachFile.exists()).isFalse().as("게시물과 연관이 끊긴 첨부파일은 삭제함");
  }

  private List<BoardAttachVO> getOldFilesTestData() {
    List<BoardAttachVO> list = new ArrayList<>();
    BoardAttachVO associatedFile = new BoardAttachVO();
    associatedFile.setUuid(FILE_UUID);
    associatedFile.setUploadPath(getFolderYesterdayUnixPath());
    associatedFile.setFileName(FILE_NAME_ONLY);
    associatedFile.setFileType(FileType.IMAGE);
    associatedFile.setBno(1L);
    list.add(associatedFile);
    return list;
  }
}
