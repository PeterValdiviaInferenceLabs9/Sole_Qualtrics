/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qualtricssole.beans;

/**
 *
 * @author Luis D
 */
public class QualtricsIndividualRequest {

    private String idRequest;
    private String phoneNumber;
    private String idMailingList;
    private String idSurvey;
    private String surveyLink;
    private String status;

    public QualtricsIndividualRequest() {
    }

    public String getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(String idRequest) {
        this.idRequest = idRequest;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdMailingList() {
        return idMailingList;
    }

    public void setIdMailingList(String idMailingList) {
        this.idMailingList = idMailingList;
    }

    public String getIdSurvey() {
        return idSurvey;
    }

    public void setIdSurvey(String idSurvey) {
        this.idSurvey = idSurvey;
    }

    public String getSurveyLink() {
        return surveyLink;
    }

    public void setSurveyLink(String surveyLink) {
        this.surveyLink = surveyLink;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
