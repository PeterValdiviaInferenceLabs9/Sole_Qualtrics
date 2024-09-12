/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package qualtricssole.servlets;

import desktopframework.Aplicacion;
import desktopframework.ConexionBDConfiguracion;
import desktopframework.Configuracion;
import desktopframework.Log;
import desktopframework.web.ServicioWeb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import qualtricssole.beans.DistributionResponse;
import qualtricssole.beans.HSMButton;
import qualtricssole.beans.HSMHeader;
import qualtricssole.beans.HSMRequestResult;
import qualtricssole.beans.HSMTemplate;
import qualtricssole.beans.HSMUser;
import qualtricssole.beans.MailingListResponse;
import qualtricssole.beans.QualtricsIndividualRequest;
import qualtricssole.beans.SoleNpsRegistry;
import qualtricssole.controllers.HSMSender;
import qualtricssole.controllers.QualtricsController;
import qualtricssole.controllers.QualtricsRequestController;
import qualtricssole.tools.StringTools;

/**
 * @author Luis D
 */
public class SendQualtricsByWs extends HttpServlet {

    private final String SERVLET = "SendQualtricsByWs";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String uuid = UUID.randomUUID().toString() + "-";
        final String SUBHEADER = ".processRequest() - ";
        final String LOG_HEADER = uuid + SERVLET + SUBHEADER;

        //Configuracion de directorio de aplicacion, log y bd
        Aplicacion.directorio = "C:\\home\\luisInference\\qualtricssole";
        Configuracion.actualizar();
        ConexionBDConfiguracion.actualizar();
        Log.actualizar();

        QualtricsIndividualRequest refQualtricsIndividualRequest = new QualtricsIndividualRequest();

        MailingListResponse refMailingListResponse;
        DistributionResponse refDistributionResponse;
        List<SoleNpsRegistry> npsList = new ArrayList<>();
        List<String> linksList = new ArrayList<>();
        HSMRequestResult refHSMRequestResult;
        JSONObject refJSONObject = null;

        boolean rs = true;
        String rs_body = "NULL";
        int status = 200;

        Log.info(LOG_HEADER + "***************** " + SERVLET + " -- Inicio de Servicio *****************", SERVLET);
        Log.debug(LOG_HEADER + "ContextPath = " + request.getContextPath(), SERVLET);
        Log.debug(LOG_HEADER + "ContentType = " + request.getContentType(), SERVLET);
        Log.debug(LOG_HEADER + "Method = " + request.getMethod(), SERVLET);
        Log.debug(LOG_HEADER + "QueryString = " + request.getQueryString(), SERVLET);
        Log.debug(LOG_HEADER + "PathInfo = " + request.getPathInfo(), SERVLET);

        refJSONObject = ServicioWeb.leerJsonDelRequest(request);//Getting POST request JSON

        try {
            //Step 1: Getting values from request
            String mailingList = refJSONObject.getString("mailingList");
            String id_encuesta = refJSONObject.getString("id_encuesta");
            String orden_servicio = refJSONObject.getString("orden_servicio");
            String año = refJSONObject.getString("año");
            String mes = refJSONObject.getString("mes");
            String nombre = refJSONObject.getString("nombre");
            String apellido = refJSONObject.getString("apellido");
            String correo = refJSONObject.getString("correo");
            String telefono = refJSONObject.getString("telefono");
            String pedido = refJSONObject.getString("pedido");
            String fecha = refJSONObject.getString("fecha");
            String tienda = refJSONObject.getString("tienda");
            String producto = refJSONObject.getString("producto");
            String grupo_material_1 = refJSONObject.getString("grupo_material_1");
            String grupo_material_2 = refJSONObject.getString("grupo_material_2");
            String s_collection = refJSONObject.getString("s_collection");
            String servicio = refJSONObject.getString("servicio");
            String motivo_instalacion = refJSONObject.getString("motivo_instalacion");
            String segmento_asignado = refJSONObject.getString("segmento_asignado");
            String distrito = refJSONObject.getString("distrito");
            String cod_tecnico = refJSONObject.getString("cod_tecnico");
            String tecnico = refJSONObject.getString("tecnico");
            String empresa = refJSONObject.getString("empresa");
            String estado = refJSONObject.getString("estado");
            String visita_realizada = refJSONObject.getString("visita_realizada");
            String trabajo_efectuado = refJSONObject.getString("trabajo_efectuado");
            String area = refJSONObject.getString("area");
            String creador = refJSONObject.getString("creador");
            String resultado = refJSONObject.getString("resultado");
            String cantidad = refJSONObject.getString("cantidad");
            String exitoso = refJSONObject.getString("exitoso");
            String costo_servicio = refJSONObject.getString("costo_servicio");
            String nueva_visita = refJSONObject.getString("nueva_visita");
            String f_nueva_visita = refJSONObject.getString("f_nueva_visita");
            String nuevo_tipo_servicio = refJSONObject.getString("nuevo_tipo_servicio");
            String motivo_no_atencion = refJSONObject.getString("motivo_no_atencion");
            String usuario_de_creacion = refJSONObject.getString("usuario_de_creacion");
            String observacion_tecnico = refJSONObject.optString("observacion_tecnico");//Requested on 22/12/23 updated
            String num_servicio = refJSONObject.getString("num_servicio");
            String tipo_cliente = refJSONObject.getString("tipo_cliente");
            String num_tipo = refJSONObject.getString("num_tipo");
            String num_servicio_diferente = refJSONObject.getString("num_servicio_diferente");

            npsList.add(new SoleNpsRegistry(orden_servicio, año, mes, num_servicio_diferente, nombre, apellido, correo, telefono, pedido, fecha, tipo_cliente, tienda, producto, grupo_material_1, grupo_material_2, s_collection, servicio, motivo_instalacion, segmento_asignado, distrito, cod_tecnico, tecnico, empresa, estado, visita_realizada, trabajo_efectuado, area, creador, resultado, cantidad, exitoso, costo_servicio, nueva_visita, f_nueva_visita, nuevo_tipo_servicio, motivo_no_atencion, usuario_de_creacion, observacion_tecnico, num_servicio, tipo_cliente, num_tipo, num_servicio_diferente));

            refQualtricsIndividualRequest.setIdMailingList(mailingList);
            refQualtricsIndividualRequest.setIdRequest(uuid);
            refQualtricsIndividualRequest.setIdSurvey(id_encuesta);
            refQualtricsIndividualRequest.setPhoneNumber(telefono);
            refQualtricsIndividualRequest.setStatus("CREADO");
            refQualtricsIndividualRequest.setSurveyLink("Ninguno");
            QualtricsRequestController.createQualtricsIndividualRequestRegistry(refQualtricsIndividualRequest, uuid, SERVLET);//Creating request on table

            //Step 2: Create MailingList
            refMailingListResponse = QualtricsController.createMailingList(mailingList, uuid, SERVLET);

            //Step 3: Create Contact on Mailing List
            if (QualtricsController.createContactOnMailingList(npsList, refMailingListResponse.getMailingList_id(), uuid, SERVLET)) {

                //Step 4: Create Distribution 
                refDistributionResponse = QualtricsController.createDistributionOnMailingList(refMailingListResponse.getMailingList_id(), id_encuesta, uuid, SERVLET);

                if (refDistributionResponse.isResult()) {
                    //Step 5: Get Generated Qualtrics Links
                    npsList = QualtricsController.generateDistributionLinks(npsList, refDistributionResponse.getDistribution_id(), id_encuesta, uuid, SERVLET);

                    for (SoleNpsRegistry linkPivot : npsList) {
                        linksList.add(linkPivot.getLinkSurvey());
                    }

                    Log.info(LOG_HEADER + "Size of LinkList = " + linksList.size(), SERVLET);
                    if (!linksList.isEmpty()) {
                        refQualtricsIndividualRequest.setSurveyLink(linksList.get(0));
                        refQualtricsIndividualRequest.setStatus("LINK GENERADO");
                        QualtricsRequestController.updateQualtricsIndividualRequestRegistry(refQualtricsIndividualRequest, uuid, SERVLET);//Updating status and link on table

                        //Step 6: Send links by HSM Survey
                        HSMUser refHSMUser = new HSMUser();//Set WS Sender Config
                        HSMHeader refHSMHeader = new HSMHeader("imageUrl", "https://web-prd.inferencelabs9.com/QualtricsSole-1.0/css/img/logo.png");
                        ArrayList<HSMTemplate> refHSMTemplateList = new ArrayList<>();

                        //Loop
                        for (int x = 0; x < npsList.size(); x++) {
                            SoleNpsRegistry pivot = npsList.get(x);
                            String pivot_telefono = "51" + pivot.getTelefono();
                            String pivot_link = linksList.get((npsList.size() - 1) - x);//Getting pivot survey link

                            HSMButton refHSMButton1 = new HSMButton("Realizar Encuesta", "URL", pivot_link);
                            HSMTemplate refHSMTemplate = new HSMTemplate("¡Hola " + pivot.getNombre() + "! Porque valoramos tu opinión, agradeceríamos que puedas completar la siguiente encuesta \uD83D\uDC99", pivot_telefono);
                            refHSMTemplate.addButton(refHSMButton1);
                            refHSMTemplate.setHsmheader(refHSMHeader);
                            refHSMTemplateList.add(refHSMTemplate);
                        }

                        Log.info(LOG_HEADER + "############# QualtricsIntegrationService Envío de HSM - Inicio#############", SERVLET);
                        Log.info(LOG_HEADER + "INPUT: apikey_ws = " + refHSMUser.getApikey_ws(), SERVLET);
                        Log.info(LOG_HEADER + "INPUT: cascadeid_ws = " + refHSMUser.getCascadeId_ws(), SERVLET);

                        refHSMRequestResult = HSMSender.sendHSM(refHSMUser, refHSMTemplateList, uuid, SERVLET);

                        //Sending HSMs
                        if (refHSMRequestResult.isResult()) {//Success
                            rs_body = "{\n"
                                    + "\"result\": \"" + "An HSM Survey Qualtrics Message request was sent to the following phone number " + npsList.get(0).getTelefono() + ".\"\n,"
                                    + "\"link\": \"" + linksList.get(0) + "\"\n"
                                    + "}";
                            status = 200;
                            refQualtricsIndividualRequest.setStatus("COMPLETADO");
                            QualtricsRequestController.updateQualtricsIndividualRequestRegistry(refQualtricsIndividualRequest, uuid, SERVLET);//Updating status and link on table

                        } else {//Failure
                            Log.info(LOG_HEADER + "Process failed on Step 6!", SERVLET);
                            status = 400;
                        }

                        Log.info(LOG_HEADER + "############# QualtricsIntegrationService Envío de HSM  - Fin #############", SERVLET);
                    } else {
                        Log.info(LOG_HEADER + "Process failed on Step 5!", SERVLET);
                        status = 400;
                    }
                } else {
                    Log.info(LOG_HEADER + "Process failed on Step 4!", SERVLET);
                    status = 400;
                }

            } else {
                Log.info(LOG_HEADER + "Process failed on Step 3!", SERVLET);
                status = 400;
            }

        } catch (JSONException ee) {
            Log.info(LOG_HEADER + "Process failed on Step 1!", SERVLET);
            Log.debug(LOG_HEADER + ee, SERVLET);
            rs_body = ee.toString();
        }

        //Return of response
        PrintWriter out = response.getWriter();

        response.setStatus(status);
        out.print(rs_body);
        out.flush();
        Log.info(LOG_HEADER + "***************** " + SERVLET + " -- Fin de Servicio *****************", SERVLET);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
