/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qualtricssole.controllers;

import desktopframework.Log;
import desktopframework.web.Header;
import desktopframework.web.ServicioWeb;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import qualtricssole.beans.HSMRequestResult;
import qualtricssole.beans.HSMTemplate;
import qualtricssole.beans.HSMUser;

/**
 *
 * @author Luis D
 */
public class HSMSender {

    final static String className = "HSMSender";

    public static HSMRequestResult sendHSM(HSMUser refHSMUser, ArrayList<HSMTemplate> rerHSMTemplateList, String uuid, String SERVLET) {
        String LOGHEADER = uuid + className + ".sendHSM() - ";
        List<Header> listaHeaders = new ArrayList();
        HttpResponse<String> refHttpResponse;
        boolean result = true;
        int counterfails = 0;
        int countersucess = 0;

        String hsm_body = "";
        String url = HSMUser.getEndpoint_ws();
        String apikey_ws = refHSMUser.getApikey_ws();

        int statusCode;

        listaHeaders.add(new Header("X-API-KEY", apikey_ws));
        listaHeaders.add(new Header("Content-Type", "application/json"));

        for (HSMTemplate refHSMTemplate : rerHSMTemplateList) {
            Log.info(LOGHEADER + "######## HSM #" + countersucess + " ########", SERVLET);
            Log.info(LOGHEADER + "INPUT: url = " + url, SERVLET);

            hsm_body = refHSMTemplate.generateTemplateBody(refHSMUser, uuid, SERVLET);
            Log.info(LOGHEADER + "INPUT: data = " + hsm_body, SERVLET);

            refHttpResponse = ServicioWeb.postWithHeaders(url, listaHeaders, hsm_body);
            statusCode = refHttpResponse.statusCode();
            Log.info(LOGHEADER + "OUTPUT: statusCode = " + statusCode, SERVLET);
            Log.info(LOGHEADER + "OUTPUT: body = " + refHttpResponse.body(), SERVLET);

            if (statusCode == 200) {
                countersucess++;
            } else {
                counterfails++;
            }
        }

        if (counterfails > 0) {
            result = false;
        }

        return new HSMRequestResult(result, countersucess, counterfails);
    }
}
