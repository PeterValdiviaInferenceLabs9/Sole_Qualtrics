/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qualtricssole.beans;

/**
 *
 * @author Luis D
 */
public class MailingListResponse {

    private boolean result;
    private String mailingList_id;

    public MailingListResponse(boolean result, String mailingList_id) {
        this.result = result;
        this.mailingList_id = mailingList_id;
    }

    public MailingListResponse() {
    }
    
    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMailingList_id() {
        return mailingList_id;
    }

    public void setMailingList_id(String mailingList_id) {
        this.mailingList_id = mailingList_id;
    }
}
