package org.fp024.domain.generated;

import java.time.LocalDateTime;

public class BoardVO {
    private Long bno;

    private String title;

    private String writer;

    private LocalDateTime regdate;

    private LocalDateTime updateDate;

    private int replyCount;

    private String content;

    public Long getBno() {
        return bno;
    }

    public void setBno(Long bno) {
        this.bno = bno;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer == null ? null : writer.trim();
    }

    public LocalDateTime getRegdate() {
        return regdate;
    }

    public void setRegdate(LocalDateTime regdate) {
        this.regdate = regdate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", bno=").append(bno);
        sb.append(", title=").append(title);
        sb.append(", writer=").append(writer);
        sb.append(", regdate=").append(regdate);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", replyCount=").append(replyCount);
        sb.append(", content=").append(content);
        sb.append("]");
        return sb.toString();
    }
}