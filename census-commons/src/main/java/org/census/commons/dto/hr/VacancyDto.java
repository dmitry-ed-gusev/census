package org.census.commons.dto.hr;

/**
 * hhpilot domain object -> vacancy
 * @author Gusev Dmitry (GusevD)
 * @version 1.0 (DATE: 22.10.12)
 *
 * update
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 03.12.12)
 */

public class VacancyDto {

    private int    id;
    private String header;
    private String description;
    private String type;
    private String comments;
    private int    status;
    private String timestamp;

    public VacancyDto(String header, String description, String comments, int status) {
        this.header = header;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
