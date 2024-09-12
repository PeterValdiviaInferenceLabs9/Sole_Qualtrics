/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qualtricssole.controllers;

import desktopframework.Configuracion;
import desktopframework.Fecha;
import desktopframework.web.GetResponse;
import desktopframework.web.Header;
import desktopframework.web.ServicioWeb;
import desktopframework.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import qualtricssole.beans.DistributionResponse;
import qualtricssole.beans.MailingListResponse;
import qualtricssole.beans.SoleNpsRegistry;

/**
 * @author Luis D
 */
public class QualtricsController {

    private static String className = "QualtricsController";
    private static String apikey = Configuracion.getValueString("APIKEY");
    private static String ownerId = Configuracion.getValueString("OWNERID");

    public static MailingListResponse createMailingList(String mailingList_name, String uuid, String SERVLET) {
        String LOG_HEADER = uuid + className + ".createMailingList() - ";
        HttpResponse<String> refHttpResponse;
        List<Header> listaHeaders = new ArrayList();
        MailingListResponse ml_rs = new MailingListResponse();
        JSONObject rs_body;
        boolean rs = true;
        int status;

        String url = "https://sole.yul1.qualtrics.com/API/v3/directories/POOL_1CsDYTWRqHFTxD3/mailinglists?X-API-TOKEN=" + apikey;

        listaHeaders.add(new Header("X-API-TOKEN", apikey));
        listaHeaders.add(new Header("Content-Type", "application/json"));

        String body = "{\n"
                + "\"name\": \"" + mailingList_name + "\",\n"
                + "\"ownerId\": \"" + ownerId + "\"\n"
                + "}";

        Log.info(LOG_HEADER + "INPUT: name = " + mailingList_name, SERVLET);
        Log.info(LOG_HEADER + "INPUT: ownerId = " + ownerId, SERVLET);

        refHttpResponse = ServicioWeb.postWithHeaders(url, listaHeaders, body);
        status = refHttpResponse.statusCode();

        Log.info(LOG_HEADER + "OUTPUT: status = " + status, SERVLET);
        Log.info(LOG_HEADER + "OUTPUT: body = " + refHttpResponse.body(), SERVLET);

        if (status == 200) {
            rs = true;
            try {
                rs_body = new JSONObject(refHttpResponse.body());
                JSONObject result = rs_body.getJSONObject("result");
                String result_id = result.getString("id");
                ml_rs.setMailingList_id(result_id);
            } catch (JSONException ee) {
                Log.info(ee.toString(), SERVLET);
            }
        } else {
            rs = false;
        }
        ml_rs.setResult(rs);
        return ml_rs;
    }

    public static boolean createContactOnMailingList(List<SoleNpsRegistry> refNpsList, String id_mailingList, String uuid, String SERVLET) {
        String LOG_HEADER = uuid + className + ".createContactOnMailingList() - ";
        HttpResponse<String> refHttpResponse;
        List<Header> listaHeaders = new ArrayList();
        String language = "es-PE";
        JSONObject rs_body;
        boolean rs = true;
        int status, checker = 0;

        String url = "https://sole.yul1.qualtrics.com/API/v3/directories/POOL_1CsDYTWRqHFTxD3/mailinglists/" + id_mailingList + "/contacts";

        listaHeaders.add(new Header("X-API-TOKEN", apikey));
        listaHeaders.add(new Header("Content-Type", "application/json"));

        for (SoleNpsRegistry refSoleNpsRegistry : refNpsList) {

            //Basic Data
            String firstname = refSoleNpsRegistry.getNombre();
            String lastname = refSoleNpsRegistry.getApellido();
            String email = refSoleNpsRegistry.getCorreo();
            String phone = refSoleNpsRegistry.getTelefono();
            String extRef = refSoleNpsRegistry.getExternal_data_reference();
            String unsubscribed = "false";

            //Embedded Data
            String orden_servicio = refSoleNpsRegistry.getOrden_servicio();
            String año = refSoleNpsRegistry.getAño();
            String mes = refSoleNpsRegistry.getMes();
            String pedido = refSoleNpsRegistry.getPedido();
            String fecha = refSoleNpsRegistry.getFecha();
            String hora_cierre_tarea = refSoleNpsRegistry.getHora_cierre_tarea();
            String tienda = refSoleNpsRegistry.getTienda();
            String producto = refSoleNpsRegistry.getProducto();
            String grupo_material_1 = refSoleNpsRegistry.getGrupo_material_1();
            String grupo_material_2 = refSoleNpsRegistry.getGrupo_material_2();
            String s_collection = refSoleNpsRegistry.getS_collection();
            String servicio = refSoleNpsRegistry.getServicio();
            String motivo_instalacion = refSoleNpsRegistry.getMotivo_instalacion();
            String segmento_asignado = refSoleNpsRegistry.getSegmento_asignado();
            String distrito = refSoleNpsRegistry.getDistrito();
            String cod_tecnico = refSoleNpsRegistry.getCod_tecnico();
            String tecnico = refSoleNpsRegistry.getTecnico();
            String empresa = refSoleNpsRegistry.getEmpresa();
            String estado = refSoleNpsRegistry.getEstado();
            String visita_realizada = refSoleNpsRegistry.getVisita_realizada();
            String trabajo_efectuado = refSoleNpsRegistry.getTrabajo_efectuado();
            String area = refSoleNpsRegistry.getArea();
            String creador = refSoleNpsRegistry.getCreador();
            String resultado = refSoleNpsRegistry.getResultado();
            String cantidad = refSoleNpsRegistry.getCantidad();
            String exitoso = refSoleNpsRegistry.getExitoso();
            String costo_servicio = refSoleNpsRegistry.getCosto_servicio();
            String nueva_visita = refSoleNpsRegistry.getNueva_visita();
            String f_nueva_visita = refSoleNpsRegistry.getF_nueva_visita();
            String nuevo_tipo_servicio = refSoleNpsRegistry.getNuevo_tipo_servicio();
            String motivo_no_atencion = refSoleNpsRegistry.getMotivo_no_atencion();
            String usuario_de_creacion = refSoleNpsRegistry.getUsuario_de_creacion();
            String observacion_tecnico = refSoleNpsRegistry.getObservacion_tecnico();//Requested on 22/12/23 updated
            String num_servicio = refSoleNpsRegistry.getNum_servicio();
            String tipo_cliente = refSoleNpsRegistry.getTipo_cliente();
            String num_tipo = refSoleNpsRegistry.getNum_tipo();
            String num_servicio_diferente = refSoleNpsRegistry.getNum_servicio_diferente();

            String body = "{\n"
                    + "  \"firstName\": \"" + firstname + "\",\n"
                    + "  \"lastName\": \"" + lastname + "\",\n"
                    + "  \"email\": \"" + email + "\",\n"
                    + "  \"phone\": \"" + phone + "\",\n"
                    + "  \"extRef\": \"" + extRef + "\",\n"
                    + "  \"embeddedData\": {\n"
                    + "     \"ORDENDESERVICIO\" : \"" + orden_servicio + "\",\n"
                    + "     \"ANIO\": \"" + año + "\",\n"
                    + "     \"MESN\": \"" + mes + "\",\n"
                    + "     \"PEDIDO\": \"" + pedido + "\",\n"
                    + "     \"FECHA_DE_VISITA\": \"" + fecha + "\",\n"
                    + "     \"HORA_DE_CIERRE_DE_TAREA\": \"" + hora_cierre_tarea + "\",\n"
                    + "     \"TIENDA\": \"" + tienda + "\",\n"
                    + "     \"PRODUCTO\": \"" + producto + "\",\n"
                    + "     \"GRUPO_MATERIAL_1\": \"" + grupo_material_1 + "\",\n"
                    + "     \"GRUPO_MATERIAL_2\": \"" + grupo_material_2 + "\",\n"
                    + "     \"S_COLLECTION\": \"" + s_collection + "\",\n"
                    + "     \"SERVICIO\": \"" + servicio + "\",\n"
                    + "     \"MOTIVO_DE_DESINSTALACION\": \"" + motivo_instalacion + "\",\n"
                    + "     \"SEGMENTO_ASIGNADO\": \"" + segmento_asignado + "\",\n"
                    + "     \"DISTRITO\": \"" + distrito + "\",\n"
                    + "     \"COD_TECNICO\": \"" + cod_tecnico + "\",\n"
                    + "     \"TECNICO\": \"" + tecnico + "\",\n"
                    + "     \"EMPRESA\": \"" + empresa + "\",\n"
                    + "     \"ESTADO\": \"" + estado + "\",\n"
                    + "     \"VISITA_REALIZADA\": \"" + visita_realizada + "\",\n"
                    + "     \"TRABAJO_EFECTUADO\": \"" + trabajo_efectuado + "\",\n"
                    + "     \"AREA\": \"" + area + "\",\n"
                    + "     \"CREADOR\": \"" + creador + "\",\n"
                    + "     \"RESULTADO\": \"" + resultado + "\",\n"
                    + "     \"Q_CANT\": \"" + cantidad + "\",\n"
                    + "     \"Q_EXITOSO\": \"" + exitoso + "\",\n"
                    + "     \"COSTO_DE_SERVICIO\": \"" + costo_servicio + "\",\n"
                    + "     \"NUEVA_VISITA\": \"" + nueva_visita + "\",\n"
                    + "     \"F_NUEVA_VISITA\": \"" + f_nueva_visita + "\",\n"
                    + "     \"NUEVO_TIPO_SERVICIO\": \"" + nuevo_tipo_servicio + "\",\n"
                    + "     \"MOTIVO_NO_ATENCION\": \"" + motivo_no_atencion + "\",\n"
                    + "     \"USUARIO_DE_CREACION_OS\": \"" + usuario_de_creacion + "\",\n"
                    + "     \"OBSERVACION_TECNICO\":    \"" + observacion_tecnico + "\",\n"
                    + "     \"NUM SERVICIO\": \"" + num_servicio + "\",\n"
                    + "     \"CLIENTE TIPO\": \"" + tipo_cliente + "\",\n"
                    + "     \"NUM TIPO\": \"" + num_tipo + "\",\n"
                    + "     \"NUM SERVICIO DIFERENTE\": \"" + num_servicio_diferente + "\"\n"
                    + "},\n"
                    + "  \"language\": \"" + language + "\",\n"
                    + "  \"unsubscribed\": " + unsubscribed + "\n"
                    + "}";

            refHttpResponse = ServicioWeb.postWithHeaders(url, listaHeaders, body);
            status = refHttpResponse.statusCode();

            Log.info(LOG_HEADER + "OUTPUT: status = " + status, SERVLET);
            Log.info(LOG_HEADER + "OUTPUT: body = " + refHttpResponse.body(), SERVLET);

            if (status != 200) {
                Log.info(LOG_HEADER + "INPUT (failed): body = " + body, SERVLET);
                checker++;
            }
        }

        if (checker == 0) {//If any response that is not 200 results on iterations, returns false. Otherwise, return true
            rs = true;
            Log.info(LOG_HEADER + "Contacts created on mailingList Qualtrics successfully.", SERVLET);
        } else {
            rs = false;
            Log.info(LOG_HEADER + "Contacts created on mailingList Qualtrics failed (on at least one iteration)", SERVLET);
        }

        return rs;
    }

    public static DistributionResponse createDistributionOnMailingList(String id_mailingList, String id_survey, String uuid, String SERVLET) {
        String LOG_HEADER = uuid + className + ".createDistributionOnMailingList() - ";
        HttpResponse<String> refHttpResponse;
        List<Header> listaHeaders = new ArrayList();
        DistributionResponse refDistributionResponse = new DistributionResponse();
        String language = "es-PE";
        JSONObject rs_body;
        boolean rs = true;
        int status;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatterTwo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();//Current date
        Date expDate = Fecha.sumarDias(date, 5);//Time period before survey expires

        String url = "https://sole.yul1.qualtrics.com/API/v3/distributions";

        listaHeaders.add(new Header("X-API-TOKEN", apikey));
        listaHeaders.add(new Header("Content-Type", "application/json"));

        Log.info(LOG_HEADER + "INPUT: id_survey = " + id_survey, SERVLET);
        Log.info(LOG_HEADER + "INPUT: id_mailingList = " + id_mailingList, SERVLET);

        String body = "{\n"
                + "        \"surveyId\": \"" + id_survey + "\",\n"
                + "        \"linkType\": \"Individual\",\n"
                + "        \"description\": \"Distribucion " + formatter.format(date) + "\",\n"
                + "        \"action\": \"CreateDistribution\",\n"
                + "        \"expirationDate\": \"" + formatterTwo.format(expDate) + "\",\n"
                + "        \"mailingListId\": \"" + id_mailingList + "\"\n"
                + "}";

        refHttpResponse = ServicioWeb.postWithHeaders(url, listaHeaders, body);
        status = refHttpResponse.statusCode();

        Log.info(LOG_HEADER + "OUTPUT: status = " + status, SERVLET);
        Log.info(LOG_HEADER + "OUTPUT: body = " + refHttpResponse.body(), SERVLET);

        if (status == 200) {
            rs = true;
            try {
                rs_body = new JSONObject(refHttpResponse.body());
                JSONObject result = rs_body.getJSONObject("result");
                String result_id = result.getString("id");
                refDistributionResponse.setDistribution_id(result_id);
            } catch (JSONException ee) {
                Log.info(ee.toString(), SERVLET);
            }
        } else {
            rs = false;
        }

        refDistributionResponse.setResult(rs);

        return refDistributionResponse;
    }

    public static List<SoleNpsRegistry> generateDistributionLinks(List<SoleNpsRegistry> npsList, String id_distribution, String id_survey, String uuid, String SERVLET) {
        String LOG_HEADER = uuid + className + ".generateDistributionLinks() - ";
        HttpURLConnection refHttpURLConnection = null;
        GetResponse refGetResponse = null;
        List<Header> listaHeaders = new ArrayList();
        //List<String> rs = new ArrayList<>();
        boolean rs_status = true, nextPage = false;
        String rs_body_str = null;
        JSONObject rs_body;
        int status;

        List<SoleNpsRegistry> rs = new ArrayList<>();
        rs.addAll(npsList);

        String url = "https://sole.yul1.qualtrics.com/API/v3/distributions/" + id_distribution + "/links?surveyId=" + id_survey;
        listaHeaders.add(new Header("X-API-TOKEN", apikey));
        listaHeaders.add(new Header("Content-Type", "application/json"));

        try {
            do {
                Log.info(LOG_HEADER + "INPUT: url = " + url, SERVLET);
                Log.info(LOG_HEADER + "INPUT: id_distribution= " + id_distribution, SERVLET);
                Log.info(LOG_HEADER + "INPUT: id_survey = " + id_survey, SERVLET);

                refGetResponse = ServicioWeb.getWithHeaders(url, listaHeaders);
                refHttpURLConnection = refGetResponse.getRefHttpURLConnection();

                status = refHttpURLConnection.getResponseCode();
                rs_body_str = refGetResponse.getBody_response();

                Log.info(LOG_HEADER + "OUTPUT: ResponseCode = " + status, SERVLET);
                Log.info(LOG_HEADER + "OUTPUT: Message = " + refHttpURLConnection.getResponseMessage(), SERVLET);

                if (status == 200) {
                    JSONObject refJSONObject = new JSONObject(rs_body_str);
                    JSONObject result = refJSONObject.getJSONObject("result");
                    JSONArray elements = result.getJSONArray("elements");

                    for (int i = 0; i < elements.length(); i++) {//Iterate over JSONArray elements
                        JSONObject pivot = elements.getJSONObject(i);//Getting pivot by loop index
                        String temp_link = pivot.getString("link");
                        String temp_nombre = pivot.getString("firstName");
                        String temp_apellido = pivot.getString("lastName");

                        for (int x = 0; x < rs.size(); x++) {
                            if (rs.get(x).getNombre().equals(temp_nombre)) {
                                if (rs.get(x).getApellido().equals(temp_apellido)) {
                                    rs.get(x).setLinkSurvey(temp_link);
                                }
                            }
                        }
                    }

                    if (result.has("nextPage") && !result.isNull("nextPage")) {
                        Object nextPage_obj = result.get("nextPage");

                        if (nextPage_obj instanceof String) {
                            String nextPageString = (String) nextPage_obj;
                            url = nextPageString;
                            nextPage = true;
                        } else {
                            Log.info(LOG_HEADER + "No more distribution link pages found.", SERVLET);
                            nextPage = false;
                        }
                    } else {
                        Log.info(LOG_HEADER + "No more distribution link pages found.", SERVLET);
                        nextPage = false;
                    }
                }
            } while (nextPage);

        } catch (IOException | JSONException ee) {
            Log.info(ee.toString(), SERVLET);
        }

        return rs;
    }
}
