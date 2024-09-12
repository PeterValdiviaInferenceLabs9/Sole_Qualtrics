/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qualtricssole.beans;

/**
 *
 * @author Luis D
 */
public class HSMHeader {

    private String headerType;
    private String text;

    public HSMHeader(String headerType, String text) {
        this.headerType = headerType;
        this.text = text;
    }

    public String getHeaderType() {
        return headerType;
    }

    public void setHeaderType(String headerType) {
        this.headerType = headerType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextType() {
        headerType = "text";
    }

    public void setImageType() {
        headerType = "imageUrl";
    }

    public void setDocumentType() {
        headerType = "documentUrl";
    }
}
