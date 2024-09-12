/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qualtricssole.beans;

import desktopframework.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Luis D
 */
public class HSMTemplate {

    private String text;
    private String phonenumber;
    private HSMHeader hsmheader;
    private List<HSMButton> buttonList = new ArrayList<>();
    private final String HEADER = "HSMTemplate() - ";

    public HSMTemplate(String text, String phonenumber) {
        this.text = text;
        this.phonenumber = phonenumber.replace("+", "");
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void addButton(HSMButton refHSMButton) {
        buttonList.add(refHSMButton);
    }

    public void setHsmheader(HSMHeader hsmheader) {
        this.hsmheader = hsmheader;
    }

    public String generateTemplateBody(HSMUser refHSMUser, String uuid, String SERVLET) {
        String LOGHEADER = uuid + HEADER;

        Log.info(LOGHEADER + "IN: Telefono = " + phonenumber, SERVLET);
        Log.info(LOGHEADER + "IN: Text = " + text, SERVLET);
        Log.info(LOGHEADER + "Texto contiene saltos de línea (n) = " + text.contains("\n"), SERVLET);
        Log.info(LOGHEADER + "Texto contiene saltos de línea (r) = " + text.contains("\r"), SERVLET);
        Log.info(LOGHEADER + "Texto contiene saltos de línea (t) = " + text.contains("\t"), SERVLET);
        Log.info(LOGHEADER + "Texto contiene saltos de línea (\") = " + text.contains("\""), SERVLET);

        text = text.replace("\n", "\\n");
        text = text.replace("\r", "\\r");
        text = text.replace("\t", "\\t");
        text = text.replace("\"", "\\\"");

        String template_body = "{\n"
                + "    \"requestId\": \"" + UUID.randomUUID() + "\",\n"
                + "    \"cascadeId\": \"" + refHSMUser.getCascadeId_ws() + "\",\n"
                + "    \"subscriberFilter\": {\n"
                + "         \"address\": \"" + phonenumber + "\",\n"
                + "         \"type\": \"PHONE\"\n"
                + "     },\n"
                + "    \"content\": {\n"
                + "         \"whatsappContent\": {\n"
                + "             \"contentType\": \"TEXT\",\n"
                + "             \"text\": \"" + text + "\",\n";

        if (hsmheader != null) {
            template_body
                    += " \"header\": {\n"
                    + " \"" + hsmheader.getHeaderType() + "\": \"" + hsmheader.getText() + "\"\n"
                    + "},";
        }

        template_body
                += "\"keyboard\": {\n"
                + "\"rows\": [\n"
                + "{\n"
                + " \"buttons\": [\n";;

        for (int x = 0; x < buttonList.size(); x++) {
            if (x != 0) {
                template_body += ",\n";
            }

            template_body += "{\n"
                    + "  \"text\": \"" + buttonList.get(x).getText() + "\",\n"
                    + "  \"buttonType\": \"" + buttonList.get(x).getButtonType() + "\",\n";

            if (buttonList.get(x).getButtonType() == "QUICK_REPLY") {
                template_body += "\"payload\": \"";
            } else {
                template_body += "\"url\": \"";
            }
            template_body += buttonList.get(x).getPayload() + "\"\n}";
        }

        template_body += "                     ]"
                + "                     }\n"
                + "                 ]\n"
                + "             }\n"
                + "         }\n"
                + "     }\n"
                + " }";

        return template_body;
    }
}
