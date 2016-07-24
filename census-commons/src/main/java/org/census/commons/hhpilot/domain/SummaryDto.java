package org.census.commons.hhpilot.domain;

/**
 * hhpilot domain object -> summary
 * @author Gusev Dmitry (GusevD)
 * @version 1.0 (DATE: 22.10.12)
 *
 * update
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 03.12.12)
 */

public class SummaryDto {

    private int    id;
    private String header;
    private String type;
    private String file;
    private String summaryTextRus;
    private String summaryTextEng;
    private String comments;
    private int    status;
    private String timestamp;

    public SummaryDto(String header, String summaryTextRus, String summaryTextEng, String comments, int status) {
        this.header = header;
        this.summaryTextRus = summaryTextRus;
        this.summaryTextEng = summaryTextEng;
        this.comments = comments;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getSummaryTextRus() {
        return summaryTextRus;
    }

    public void setSummaryTextRus(String summaryTextRus) {
        this.summaryTextRus = summaryTextRus;
    }

    public String getSummaryTextEng() {
        return summaryTextEng;
    }

    public void setSummaryTextEng(String summaryTextEng) {
        this.summaryTextEng = summaryTextEng;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpdTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}