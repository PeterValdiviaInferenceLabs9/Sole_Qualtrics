/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package qualtricssole.servlets;

import desktopframework.Aplicacion;
import desktopframework.ConexionBDConfiguracion;
import desktopframework.Configuracion;
import desktopframework.Fecha;
import desktopframework.Log;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import qualtricssole.beans.QualtricsRequestReport;
import qualtricssole.controllers.QualtricsRequestController;
import qualtricssole.beans.QualtricsIndividualRequestReport;

/**
 *
 * @author Luis D
 */
public class QualtricsReports extends HttpServlet {

    private final String SERVLET = "QualtricsReports";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String uuid = UUID.randomUUID().toString() + "-";
        final String SUBHEADER = ".processRequest() - ";
        final String LOG_HEADER = uuid + SERVLET + SUBHEADER;
        String rutaTemplate = null;

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

        String reportOption = request.getParameter("opcionReporte");
        String fechaInicio_Parameter = request.getParameter("FechaStart");
        String fechaFin_Parameter = request.getParameter("FechaEnd");

        Log.info(LOG_HEADER + "INPUT: reportOption = " + reportOption, SERVLET);
        Log.info(LOG_HEADER + "INPUT: fechaInicio_Parameter = " + fechaInicio_Parameter, SERVLET);
        Log.info(LOG_HEADER + "INPUT: fechaFin_Parameter = " + fechaFin_Parameter, SERVLET);

        String nameOutputXlsx = "reporteQualtrics";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "inline; filename=\"" + nameOutputXlsx + ".xlsx" + "\"");

        if (reportOption.equals("MASIVO")) {
            rutaTemplate = Configuracion.getValueString("UBIC_TEMPLATE_MASIVOS");
        } else {
            rutaTemplate = Configuracion.getValueString("UBIC_TEMPLATE_INDIVIDUAL");
        }

        XSSFWorkbook libro = new XSSFWorkbook(rutaTemplate);
        XSSFSheet page = libro.getSheetAt(0);

        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);

        try {
            int horasDiff = Configuracion.getValueInt("HORAS_DIFERENCIA");
            Date fechaInicio = new SimpleDateFormat("yyyy-MM-dd").parse(fechaInicio_Parameter);
            Date fechaFin = new SimpleDateFormat("yyyy-MM-dd").parse(fechaFin_Parameter);
            fechaInicio = Fecha.sumarHoras(fechaInicio, 5);
            fechaFin = Fecha.sumarHoras(fechaFin, 29);

            String fechaInicioString = df.format(fechaInicio);
            String fechaFinString = df.format(fechaFin);

            Log.info(LOG_HEADER + "INPUT: fechaInicio = " + fechaInicioString, SERVLET);
            Log.info(LOG_HEADER + "INPUT: fechaFin = " + fechaFinString, SERVLET);

            if (reportOption.equals("MASIVO")) {//MASIVO
                ArrayList<QualtricsRequestReport> ref = QualtricsRequestController.getRegistriesByDateRange(fechaInicioString, fechaFinString, uuid, SERVLET);

                Row fila;
                Cell celda;
                int y = 1;

                for (QualtricsRequestReport pivot : ref) {
                    for (int x = 0; x < 10; x++) {
                        String cellValue;
                        int cellValueNum;
                        switch (x) {
                            case 0:
                                cellValue = pivot.getIdRequest();
                                fila = page.createRow(y);
                                celda = fila.createCell(x);
                                celda.setCellValue(cellValue);
                                celda.setCellType(CellType.STRING);
                                break;

                            case 1:
                                cellValue = df.format(Fecha.restarHoras(pivot.getCreatedAt(), horasDiff));
                                fila = page.getRow(y);
                                celda = fila.createCell(x);
                                celda.setCellValue(cellValue);
                                celda.setCellType(CellType.STRING);
                                break;

                            case 2:
                                cellValue = df.format(Fecha.restarHoras(pivot.getUpdatedAt(), horasDiff));
                                fila = page.getRow(y);
                                celda = fila.createCell(x);
                                celda.setCellValue(cellValue);
                                celda.setCellType(CellType.STRING);
                                break;

                            case 3:
                                cellValue = pivot.getFileName();
                                fila = page.getRow(y);
                                celda = fila.createCell(x);
                                celda.setCellValue(cellValue);
                                celda.setCellType(CellType.STRING);
                                break;

                            case 4:
                                cellValue = pivot.getIdSurvey();
                                fila = page.getRow(y);
                                celda = fila.createCell(x);
                                celda.setCellValue(cellValue);
                                celda.setCellType(CellType.STRING);
                                break;

                            case 5:
                                cellValue = pivot.getIdMailingList();
                                fila = page.getRow(y);
                                celda = fila.createCell(x);
                                celda.setCellValue(cellValue);
                                celda.setCellType(CellType.STRING);
                                break;

                            case 6:
                                cellValueNum = pivot.getNumClients();
                                fila = page.getRow(y);
                                celda = fila.createCell(x);
                                celda.setCellValue(cellValueNum);
                                celda.setCellType(CellType.NUMERIC);
                                break;

                            case 7:
                                cellValueNum = pivot.getNumQualtricsLinks();
                                fila = page.getRow(y);
                                celda = fila.createCell(x);
                                celda.setCellValue(cellValueNum);
                                celda.setCellType(CellType.NUMERIC);
                                break;

                            case 8:
                                cellValueNum = pivot.getNumWSSent();
                                fila = page.getRow(y);
                                celda = fila.createCell(x);
                                celda.setCellValue(cellValueNum);
                                celda.setCellType(CellType.NUMERIC);
                                break;

                            case 9:
                                cellValue = pivot.getStatus();
                                fila = page.getRow(y);
                                celda = fila.createCell(x);
                                celda.setCellValue(cellValue);
                                celda.setCellType(CellType.STRING);
                                break;
                        }
                    }
                    y++;
                    Log.info(LOG_HEADER + "Registro con Fecha: " + pivot.getCreatedAt() + " y tickets atendidos: " + ref.size() + " fue agregado al reporte.", SERVLET);
                }
            } else {//INDIVIDUAL
                ArrayList<QualtricsIndividualRequestReport> ref = QualtricsRequestController.getIndividualRegistriesByDateRange(fechaInicioString, fechaFinString, uuid, SERVLET);
                Row fila;
                Cell celda;
                int y = 1;

                for (QualtricsIndividualRequestReport pivot : ref) {
                    for (int x = 0; x < 8; x++) {
                        String cellValue;
                        int cellValueNum;
                        switch (x) {
                            case 0:
                                cellValue = pivot.getIdRequest();
                                fila = page.createRow(y);
                                celda = fila.createCell(x);
                                celda.setCellValue(cellValue);
                                celda.setCellType(CellType.STRING);
                                break;

                            case 1:
                                cellValue = df.format(Fecha.restarHoras(pivot.getCreatedAt(), horasDiff));
                                fila = page.getRow(y);
                                celda = fila.createCell(x);
                                celda.setCellValue(cellValue);
                                celda.setCellType(CellType.STRING);
                                break;

                            case 2:
                                cellValue = df.format(Fecha.restarHoras(pivot.getUpdatedAt(), horasDiff));
                                fila = page.getRow(y);
                                celda = fila.createCell(x);
                                celda.setCellValue(cellValue);
                                celda.setCellType(CellType.STRING);
                                break;

                            case 3:
                                cellValue = pivot.getPhoneNumber();
                                fila = page.getRow(y);
                                celda = fila.createCell(x);
                                celda.setCellValue(cellValue);
                                celda.setCellType(CellType.STRING);
                                break;

                            case 4:
                                cellValue = pivot.getIdMailingList();
                                fila = page.getRow(y);
                                celda = fila.createCell(x);
                                celda.setCellValue(cellValue);
                                celda.setCellType(CellType.STRING);
                                break;

                            case 5:
                                cellValue = pivot.getIdSurvey();
                                fila = page.getRow(y);
                                celda = fila.createCell(x);
                                celda.setCellValue(cellValue);
                                celda.setCellType(CellType.STRING);
                                break;

                            case 6:
                                cellValue = pivot.getSurveyLink();
                                fila = page.getRow(y);
                                celda = fila.createCell(x);
                                celda.setCellValue(cellValue);
                                celda.setCellType(CellType.STRING);
                                break;

                            case 7:
                                cellValue = pivot.getStatus();
                                fila = page.getRow(y);
                                celda = fila.createCell(x);
                                celda.setCellValue(cellValue);
                                celda.setCellType(CellType.STRING);
                                break;
                        }
                    }
                    y++;
                    Log.info(LOG_HEADER + "Registro con Fecha: " + pivot.getCreatedAt() + " y tickets atendidos: " + ref.size() + " fue agregado al reporte.", SERVLET);
                }
            }

            libro.write(response.getOutputStream());
            libro.close();
        } catch (Exception ee) {
            Log.info(LOG_HEADER + ee, SERVLET);
        }

        Log.info(LOG_HEADER + "***************** " + SERVLET + " -- Fin de Servicio *****************", SERVLET);
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
        processRequest(request, response);
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
