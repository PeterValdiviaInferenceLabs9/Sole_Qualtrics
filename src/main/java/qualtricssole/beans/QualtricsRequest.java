/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qualtricssole.beans;

/**
 *
 * @author Luis D
 */
public class QualtricsRequest {

    private String idRequest;
    private String fileName;
    private String idSurvey;
    private String idMailingList;
    private int numClients = 0;
    private int numQualtricsLinks = 0;
    private int numWSSent = 0;
    private String status;

    public QualtricsRequest() {
    }

    public String getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(String idRequest) {
        this.idRequest = idRequest;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getIdSurvey() {
        return idSurvey;
    }

    public void setIdSurvey(String idSurvey) {
        this.idSurvey = idSurvey;
    }

    public String getIdMailingList() {
        return idMailingList;
    }

    public void setIdMailingList(String idMailingList) {
        this.idMailingList = idMailingList;
    }

    public int getNumClients() {
        return numClients;
    }

    public void setNumClients(int numClients) {
        this.numClients = numClients;
    }

    public int getNumQualtricsLinks() {
        return numQualtricsLinks;
    }

    public void setNumQualtricsLinks(int numQualtricsLinks) {
        this.numQualtricsLinks = numQualtricsLinks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNumWSSent() {
        return numWSSent;
    }

    public void setNumWSSent(int numWSSent) {
        this.numWSSent = numWSSent;
    }
}
