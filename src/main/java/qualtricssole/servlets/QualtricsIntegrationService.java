/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package qualtricssole.servlets;

import desktopframework.Aplicacion;
import desktopframework.ConexionBDConfiguracion;
import desktopframework.Configuracion;
import desktopframework.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.validator.EmailValidator;
import qualtricssole.beans.DistributionResponse;
import qualtricssole.beans.HSMButton;
import qualtricssole.beans.HSMHeader;
import qualtricssole.beans.HSMRequestResult;
import qualtricssole.beans.HSMTemplate;
import qualtricssole.beans.HSMUser;
import qualtricssole.beans.MailingListResponse;
import qualtricssole.beans.QualtricsRequest;
import qualtricssole.beans.SoleNpsRegistry;
import qualtricssole.controllers.HSMSender;
import qualtricssole.controllers.QualtricsController;
import qualtricssole.controllers.QualtricsRequestController;
import qualtricssole.tools.StringTools;

/**
 *
 * @author Luis D
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, //1 MB
        maxFileSize = 1024 * 1024 * 100, //10 MB
        maxRequestSize = 1024 * 1024 * 100 //100 MB
)

public class QualtricsIntegrationService extends HttpServlet {

    private static final String CONTENT_DISPOSITION_KEY = "content-disposition";
    private static final String FILE_NAME_KEY = "filename";
    private static final int BUFFER_SIZE = 2048;
    private final String SERVLET = "QualtricsIntegrationService";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws org.apache.commons.fileupload.FileUploadException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileUploadException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        String uuid = UUID.randomUUID().toString() + "-";
        final String SUBHEADER = ".processRequest() - ";
        final String LOG_HEADER = uuid + SERVLET + SUBHEADER;

        String part_filename, part_content = "";
        MailingListResponse refMailingListResponse;
        DistributionResponse refDistributionResponse;
        List<SoleNpsRegistry> npsList = new ArrayList<>();
        List<String> linksList = new ArrayList<>();
        HSMRequestResult refHSMRequestResult;

        QualtricsRequest refQualtricsRequest = new QualtricsRequest();

        boolean rs = true;
        String rs_body = "";
        int status = 200;

        //Configuracion de directorio de aplicacion, log y bd
        Aplicacion.directorio = "C:\\home\\luisInference\\qualtricssole";
        Configuracion.actualizar();
        ConexionBDConfiguracion.actualizar();
        Log.actualizar();

        Log.info(LOG_HEADER + "***************** " + SERVLET + " -- Inicio de Servicio *****************", SERVLET);
        Log.debug(LOG_HEADER + "ContextPath = " + request.getContextPath(), SERVLET);
        Log.debug(LOG_HEADER + "ContentType = " + request.getContentType(), SERVLET);
        Log.debug(LOG_HEADER + "Method = " + request.getMethod(), SERVLET);
        Log.debug(LOG_HEADER + "QueryString = " + request.getQueryString(), SERVLET);
        Log.debug(LOG_HEADER + "PathInfo = " + request.getPathInfo(), SERVLET);

        String id_encuesta = request.getParameter("idEncuesta");
        String mailingList = request.getParameter("mailingList");
        //String exp_num = request.getParameter("name_exp");//EXPIRATION DATE OF QUALTRICS SURVEY
        String file = request.getParameter("filecsv");

        Log.info(LOG_HEADER + "INPUT: id_encuesta = " + id_encuesta, SERVLET);
        Log.info(LOG_HEADER + "INPUT: MailingList = " + mailingList, SERVLET);

        //Extracting part by part of CSV file
        for (Part part : request.getParts()) {
            part_filename = getFileName(part);
            part_content = getTextFromPart(part);

            if (part_filename != null) {
                file = part_filename;
            }
        }

        Log.info(LOG_HEADER + "INPUT: file = " + file, SERVLET);

        npsList = getNpsList(part_content, uuid, SERVLET);
        Log.info(LOG_HEADER + "La npsList contiene " + npsList.size() + " clientes registrados.", SERVLET);

        //Set fileName, idMailingList, idSurvey and idRequest on refQualtricsRequest and create first registry on table masivos
        refQualtricsRequest.setFileName(file);
        refQualtricsRequest.setIdMailingList(mailingList);
        refQualtricsRequest.setIdSurvey(id_encuesta);
        refQualtricsRequest.setIdRequest(uuid);
        refQualtricsRequest.setNumClients(npsList.size());//Set number of contacts on npsList
        refQualtricsRequest.setStatus("Creado");
        QualtricsRequestController.createQualtricsRequestRegistry(refQualtricsRequest, uuid, SERVLET);

        if (!npsList.isEmpty()) {//npsList has contacts
            //Step 1: Create Mailing List
            refMailingListResponse = QualtricsController.createMailingList(mailingList, uuid, SERVLET);

            if (refMailingListResponse.isResult()) {

                //Step 2: Create Contact on Mailing List
                if (QualtricsController.createContactOnMailingList(npsList, refMailingListResponse.getMailingList_id(), uuid, SERVLET)) {

                    //Step 3: Create Distribution Mailing List
                    refDistributionResponse = QualtricsController.createDistributionOnMailingList(refMailingListResponse.getMailingList_id(), id_encuesta, uuid, SERVLET);
                    if (refDistributionResponse.isResult()) {
                        //Step 4: Get Generated Qualtrics Links
                        npsList = QualtricsController.generateDistributionLinks(npsList, refDistributionResponse.getDistribution_id(), id_encuesta, uuid, SERVLET);

                        for (SoleNpsRegistry linkPivot : npsList) {
                            linksList.add(linkPivot.getLinkSurvey());
                        }

                        Log.info(LOG_HEADER + "Size of LinkList = " + linksList.size(), SERVLET);

                        //Inserting on DAO 1
                        if (linksList.isEmpty()) {
                            Log.info(LOG_HEADER + "Process failed on Step 4!", SERVLET);
                            refQualtricsRequest.setStatus("Fallido");
                            status = 400;
                            response.sendRedirect("https://web-prd.inferencelabs9.com/QualtricsSole-1.0/qualtricssoleresult.html?status=" + "COMPLETADO" + "&fileName=" + file
                                    + "&surveyId=" + id_encuesta + "&mailingListId=" + mailingList + "&qtyClients=" + npsList.size() + "&linksGenerated=NINGUNO" + "&hsmSent=NINGUNO");
                        } else {//Step 5: Send links by HSM Survey
                            refQualtricsRequest.setNumQualtricsLinks(linksList.size());
                            refQualtricsRequest.setStatus("Links Cargados");
                            QualtricsRequestController.updateQualtricsRequestRegistry(refQualtricsRequest, uuid, SERVLET);//Updating status and size of numQualtricsLinks

                            HSMUser refHSMUser = new HSMUser();//Set WS Sender Config
                            HSMHeader refHSMHeader = new HSMHeader("imageUrl", "https://web-prd.inferencelabs9.com/QualtricsSole-1.0/css/img/logo.png");
                            ArrayList<HSMTemplate> refHSMTemplateList = new ArrayList<>();

                            //Loop
                            for (int x = 0; x < npsList.size(); x++) {
                                SoleNpsRegistry pivot = npsList.get(x);
                                String pivot_telefono = "51" + pivot.getTelefono();
                                String pivot_link = pivot.getLinkSurvey();
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
                                refQualtricsRequest.setStatus("COMPLETADO");
                                refQualtricsRequest.setNumWSSent(refHSMRequestResult.getCountersucess());;//Updating status and size of numWSSent
                                response.sendRedirect("https://web-prd.inferencelabs9.com/QualtricsSole-1.0/qualtricssoleresult.html?status=" + "COMPLETADO" + "&fileName=" + file
                                        + "&surveyId=" + id_encuesta + "&mailingListId=" + mailingList + "&qtyClients=" + npsList.size() + "&linksGenerated=" + linksList.size() + "&hsmSent=" + refHSMRequestResult.getCountersucess());
                            } else {//Failure
                                Log.info(LOG_HEADER + "Process failed on Step 5!", SERVLET);
                                refQualtricsRequest.setStatus("FALLIDO");
                                refQualtricsRequest.setNumWSSent(refHSMRequestResult.getCountersucess());;//Updating status and size of numWSSent
                                response.sendRedirect("https://web-prd.inferencelabs9.com/QualtricsSole-1.0/qualtricssoleresult.html?status=" + "COMPLETADO" + "&fileName=" + file
                                        + "&surveyId=" + id_encuesta + "&mailingListId=" + mailingList + "&qtyClients=" + npsList.size() + "&linksGenerated=" + linksList.size() + "&hsmSent=" + refHSMRequestResult.getCountersucess());
                            }
                            Log.info(LOG_HEADER + "############# QualtricsIntegrationService Envío de HSM  - Fin #############", SERVLET);
                        }
                    } else {
                        Log.info(LOG_HEADER + "Process failed on Step 3!", SERVLET);
                        refQualtricsRequest.setStatus("FALLIDO");
                        status = 400;
                        response.sendRedirect("https://web-prd.inferencelabs9.com/QualtricsSole-1.0/qualtricssoleresult.html?status=" + "FALLIDO" + "&fileName=" + file
                                + "&surveyId=" + id_encuesta + "&mailingListId=" + mailingList + "&qtyClients=" + npsList.size() + "&linksGenerated=NINGUNO" + "&hsmSent=NINGUNO");
                    }
                } else {
                    Log.info(LOG_HEADER + "Process failed on Step 2!", SERVLET);
                    refQualtricsRequest.setStatus("FALLIDO");
                    status = 400;
                    response.sendRedirect("https://web-prd.inferencelabs9.com/QualtricsSole-1.0/qualtricssoleresult.html?status=" + "FALLIDO" + "&fileName=" + file
                            + "&surveyId=" + id_encuesta + "&mailingListId=" + mailingList + "&qtyClients=" + npsList.size() + "&linksGenerated=NINGUNO" + "&hsmSent=NINGUNO");
                }
            } else {
                Log.info(LOG_HEADER + "Process failed on Step 1!", SERVLET);
                refQualtricsRequest.setStatus("FALLIDO");
                status = 400;
                response.sendRedirect("https://web-prd.inferencelabs9.com/QualtricsSole-1.0/qualtricssoleresult.html?status=" + "FALLIDO" + "&fileName=" + file
                        + "&surveyId=" + id_encuesta + "&mailingListId=" + mailingList + "&qtyClients=" + npsList.size() + "&linksGenerated=NINGUNO" + "&hsmSent=NINGUNO");
            }
        } else {//npsList is empty
            Log.info(LOG_HEADER + "npsList is Empty!", SERVLET);
            refQualtricsRequest.setStatus("FALLIDO");
            status = 400;
        }

        QualtricsRequestController.updateQualtricsRequestRegistry(refQualtricsRequest, uuid, SERVLET);

        //Return of response
        PrintWriter out = response.getWriter();
        response.setStatus(status);
        out.print("NULL");
        out.flush();
        Log.info(LOG_HEADER + "***************** " + SERVLET + " -- Fin de Servicio *****************", SERVLET);
    }

    private List<SoleNpsRegistry> getNpsList(String csvdata, String uuid, String SERVLET) {
        String LOG_HEADER = uuid + SERVLET + ".getNpsList() - ";
        List<SoleNpsRegistry> rs = new ArrayList<>();
        List<String> nps_rows = new ArrayList<>();
        String temp = csvdata;

        //Obteniendo rows
        Scanner scanner = new Scanner(csvdata);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            nps_rows.add(line);
        }

        Log.info(LOG_HEADER + "Se han obtenido " + nps_rows.size() + " filas del archivo csv.", SERVLET);

        //Obteniendo campos
        for (int cnt = 0; cnt < nps_rows.size(); cnt++) {//Recorriendo row by row
            String[] strarray = nps_rows.get(cnt).split(",");
            SoleNpsRegistry refSoleNpsRegistry = new SoleNpsRegistry();
            boolean duplicado = false;

            if (cnt == 0) {//Headers
                Log.info(LOG_HEADER + "Se ha verificado que existen " + strarray.length + " headers en el csv.", SERVLET);
            } else {//Valores
                for (int cnt2 = 0; cnt2 < strarray.length; cnt2++) {//Recorriendo col by col
                    String col_value = strarray[cnt2].trim();
                    col_value = col_value.replace("\n", "\\n");
                    col_value = col_value.replace("\r", "\\r");
                    col_value = col_value.replace("\t", "");
                    col_value = col_value.replace("\"", "\\\"");

                    switch (cnt2) {
                        case 0:
                            refSoleNpsRegistry.setOrden_servicio(col_value);
                            break;
                        case 1:
                            refSoleNpsRegistry.setAño(col_value);
                            break;
                        case 2:
                            refSoleNpsRegistry.setMes(col_value);
                            break;
                        case 3:
                            refSoleNpsRegistry.setExternal_data_reference(col_value);
                            break;
                        case 4:
                            String firstName = StringTools.getFirstName(col_value);
                            String lastName = StringTools.getLastName(col_value);

                            if (lastName.isEmpty()) {
                                lastName = firstName;
                            }
                            refSoleNpsRegistry.setNombre(firstName.trim());
                            refSoleNpsRegistry.setApellido(lastName.trim());
                            break;
                        case 5:
                            EmailValidator refEmailValidator = EmailValidator.getInstance();

                            if (!refEmailValidator.isValid(col_value)) {//If original email is not valid, use placeholder
                                col_value = "tempoEmail@placeholder.com";
                            }
                            //col_value = col_value.replaceAll(",", ".");
                            refSoleNpsRegistry.setCorreo(col_value);
                            break;
                        case 6:
                            refSoleNpsRegistry.setTelefono(col_value);
                            break;
                        case 7:
                            refSoleNpsRegistry.setPedido(col_value);
                            break;
                        case 8:
                            refSoleNpsRegistry.setFecha(col_value);
                            break;
                        case 9:
                            refSoleNpsRegistry.setHora_cierre_tarea(col_value);
                            break;
                        case 10:
                            refSoleNpsRegistry.setTienda(col_value);
                            break;
                        case 11:
                            refSoleNpsRegistry.setProducto(col_value);
                            break;
                        case 12:
                            refSoleNpsRegistry.setGrupo_material_1(col_value);
                            break;
                        case 13:
                            refSoleNpsRegistry.setGrupo_material_2(col_value);
                            break;
                        case 14:
                            refSoleNpsRegistry.setS_collection(col_value);
                            break;
                        case 15:
                            refSoleNpsRegistry.setServicio(col_value);
                            break;
                        case 16:
                            refSoleNpsRegistry.setMotivo_instalacion(col_value);
                            break;
                        case 17:
                            refSoleNpsRegistry.setSegmento_asignado(col_value);
                            break;
                        case 18:
                            refSoleNpsRegistry.setDistrito(col_value);
                            break;
                        case 19:
                            refSoleNpsRegistry.setCod_tecnico(col_value);
                            break;
                        case 20:
                            refSoleNpsRegistry.setTecnico(col_value);
                            break;
                        case 21:
                            refSoleNpsRegistry.setEmpresa(col_value);
                            break;
                        case 22:
                            refSoleNpsRegistry.setEstado(col_value);
                            break;
                        case 23:
                            refSoleNpsRegistry.setVisita_realizada(col_value);
                            break;
                        case 24:
                            refSoleNpsRegistry.setTrabajo_efectuado(col_value);
                            break;
                        case 25:
                            refSoleNpsRegistry.setArea(col_value);
                            break;
                        case 26:
                            refSoleNpsRegistry.setCreador(col_value);
                            break;
                        case 27:
                            refSoleNpsRegistry.setResultado(col_value);
                            break;
                        case 28:
                            refSoleNpsRegistry.setCantidad(col_value);
                            break;
                        case 29:
                            refSoleNpsRegistry.setExitoso(col_value);
                            break;
                        case 30:
                            refSoleNpsRegistry.setCosto_servicio(col_value);
                            break;
                        case 31:
                            refSoleNpsRegistry.setNueva_visita(col_value);
                            break;
                        case 32:
                            refSoleNpsRegistry.setF_nueva_visita(col_value);
                            break;
                        case 33:
                            refSoleNpsRegistry.setNuevo_tipo_servicio(col_value);
                            break;
                        case 34:
                            refSoleNpsRegistry.setMotivo_no_atencion(col_value);
                            break;
                        case 35:
                            refSoleNpsRegistry.setUsuario_de_creacion(col_value);
                            break;
                        case 36:
                            refSoleNpsRegistry.setObservacion_tecnico(col_value);
                            break;
                        case 37:
                            refSoleNpsRegistry.setNum_servicio(col_value);
                            break;
                        case 38:
                            refSoleNpsRegistry.setTipo_cliente(col_value);
                            break;
                        case 39:
                            refSoleNpsRegistry.setNum_tipo(col_value);
                            break;
                        case 40:
                            refSoleNpsRegistry.setNum_servicio_diferente(col_value);
                            break;
                    }
                }

                for (int y = 0; y < rs.size(); y++) {
                    SoleNpsRegistry pivotRs = rs.get(y);

                    if (pivotRs.getNombre().equals(refSoleNpsRegistry.getNombre())) {
                        if (pivotRs.getApellido().equals(refSoleNpsRegistry.getApellido())) {
                            duplicado = true;//If it is a duplicated 
                        }
                    }
                }

                if (duplicado == false) {
                    rs.add(refSoleNpsRegistry);//Agregando cliente a lista rs del tipo SoleNpsRegistry
                    Log.info(LOG_HEADER + "Se guardado el registro con nombre de cliente " + refSoleNpsRegistry.getNombre() + " en lista.", SERVLET);
                } else {
                    Log.info(LOG_HEADER + "No se guardo el registro con nombre de cliente " + refSoleNpsRegistry.getNombre() + " ya que es un duplicado.", SERVLET);
                }
            }
        }
        return rs;
    }

    private String getFileName(Part part) {
        for (String contentDisposition : part.getHeader(CONTENT_DISPOSITION_KEY).split(";")) {
            if (contentDisposition.trim().startsWith(FILE_NAME_KEY)) {
                return contentDisposition.substring(contentDisposition.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    private String getTextFromPart(Part part) throws IOException {
        BufferedReader reader
                = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"));
        StringBuilder value = new StringBuilder();
        char[] buffer = new char[BUFFER_SIZE];
        for (int length = 0; (length = reader.read(buffer)) > 0;) {
            value.append(buffer, 0, length);
        }
        return value.toString();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(QualtricsIntegrationService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(QualtricsIntegrationService.class.getName()).log(Level.SEVERE, null, ex);
        }
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
