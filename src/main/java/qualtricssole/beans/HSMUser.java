/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qualtricssole.beans;

import desktopframework.Configuracion;

/**
 *
 * @author Luis D
 */
public class HSMUser {

    private String apikey_ws;
    private String cascadeId_ws;
    private static final String endpoint_ws = "https://app.edna.io/api/cascade/schedule";

    public HSMUser() {
        apikey_ws = Configuracion.getValueString("API_KEY_WHATSAPP");
        cascadeId_ws = Configuracion.getValueString("CASCADE_WHATSAPP");
    }

    public HSMUser(String apikey_ws, String cascade_ws) {
        this.apikey_ws = apikey_ws;
        this.cascadeId_ws = cascade_ws;
    }

    public String getApikey_ws() {
        return apikey_ws;
    }

    public void setApikey_ws(String apikey_ws) {
        this.apikey_ws = apikey_ws;
    }

    public String getCascadeId_ws() {
        return cascadeId_ws;
    }

    public void setCascadeId_ws(String cascade_ws) {
        this.cascadeId_ws = cascade_ws;
    }

    public static String getEndpoint_ws() {
        return endpoint_ws;
    }
}
