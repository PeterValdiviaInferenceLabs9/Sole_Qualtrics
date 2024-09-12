/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qualtricssole.dao;

import qualtricssole.beans.QualtricsIndividualRequestReport;
import desktopframework.ConexionBD;
import desktopframework.Fecha;
import desktopframework.Log;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import qualtricssole.beans.QualtricsIndividualRequest;
import qualtricssole.beans.QualtricsRequestReport;

/**
 *
 * @author Luis D
 */
public class QualtricsIndividualRequestDAO {

    private static final String table_name = "qualtricsindividual";

    public static boolean insert(QualtricsIndividualRequest refQualtricsIndividualRequest, String SERVLET, String uuid) {
        String LOG_HEADER = uuid + "QualtricsIndividualRequestDAO.insert() - ";
        int i = 1;
        Connection conn;
        CallableStatement cstmt = null;
        boolean exito = false;

        try {
            ConexionBD.conectarPorNombre("Principal");
            conn = ConexionBD.refConnection;
            if (conn != null) {

                cstmt = conn.prepareCall("INSERT INTO " + table_name + "(idRequest, createdAt, updatedAt, phoneNumber, idMailingList, idSurvey, surveyLink, status)VALUES(?,?,?,?,?,?,?,?);");

                cstmt.setString(i++, refQualtricsIndividualRequest.getIdRequest());
                cstmt.setString(i++, Fecha.obtenerActualString("yyyy-MM-dd HH:mm:ss.SSS"));
                cstmt.setString(i++, Fecha.obtenerActualString("yyyy-MM-dd HH:mm:ss.SSS"));
                cstmt.setString(i++, refQualtricsIndividualRequest.getPhoneNumber());
                cstmt.setString(i++, refQualtricsIndividualRequest.getIdMailingList());
                cstmt.setString(i++, refQualtricsIndividualRequest.getIdSurvey());
                cstmt.setString(i++, refQualtricsIndividualRequest.getSurveyLink());
                cstmt.setString(i++, refQualtricsIndividualRequest.getStatus());

                Log.debug(LOG_HEADER + "Query : " + cstmt.toString(), SERVLET);
                Log.debug(LOG_HEADER + "IN : idRequest = " + refQualtricsIndividualRequest.getIdRequest(), SERVLET);
                Log.debug(LOG_HEADER + "IN : phoneNumber = " + refQualtricsIndividualRequest.getPhoneNumber(), SERVLET);
                Log.debug(LOG_HEADER + "IN : idMailingList = " + refQualtricsIndividualRequest.getIdMailingList(), SERVLET);
                Log.debug(LOG_HEADER + "IN : idSurvey = " + refQualtricsIndividualRequest.getIdSurvey(), SERVLET);
                Log.debug(LOG_HEADER + "IN : surveyLink = " + refQualtricsIndividualRequest.getSurveyLink(), SERVLET);
                Log.debug(LOG_HEADER + "IN : status = " + refQualtricsIndividualRequest.getStatus(), SERVLET);

                cstmt.execute();
                exito = true;
                Log.debug(LOG_HEADER + "Inserción correcta", SERVLET);
            }
        } catch (SQLException ex) {
            Log.error(LOG_HEADER, ex, SERVLET);
        } finally {
            try {
                cstmt.close();
            } catch (SQLException ee) {
                Log.error(LOG_HEADER, ee, SERVLET);
            }
            ConexionBD.desconectar();
        }
        return exito;
    }

    public static boolean update(QualtricsIndividualRequest refQualtricsIndividualRequest, String SERVLET, String uuid) {
        String LOG_HEADER = uuid + "QualtricsIndividualRequestDAO.update() - ";
        int i = 1;
        Connection conn;
        CallableStatement cstmt = null;
        boolean exito = false;

        try {
            ConexionBD.conectarPorNombre("Principal");
            conn = ConexionBD.refConnection;
            if (conn != null) {

                cstmt = conn.prepareCall("UPDATE " + table_name + " SET updatedAt=?, phoneNumber=?, idMailingList=?, idSurvey=?, surveyLink=?, status=? WHERE idRequest=?;");

                cstmt.setString(i++, Fecha.obtenerActualString("yyyy-MM-dd HH:mm:ss.SSS"));
                cstmt.setString(i++, refQualtricsIndividualRequest.getPhoneNumber());
                cstmt.setString(i++, refQualtricsIndividualRequest.getIdMailingList());
                cstmt.setString(i++, refQualtricsIndividualRequest.getIdSurvey());
                cstmt.setString(i++, refQualtricsIndividualRequest.getSurveyLink());
                cstmt.setString(i++, refQualtricsIndividualRequest.getStatus());
                cstmt.setString(i++, refQualtricsIndividualRequest.getIdRequest());

                Log.debug(LOG_HEADER + "Query : " + cstmt.toString());
                Log.debug(LOG_HEADER + "IN : phoneNumber = " + refQualtricsIndividualRequest.getPhoneNumber(), SERVLET);
                Log.debug(LOG_HEADER + "IN : idMailingList = " + refQualtricsIndividualRequest.getIdMailingList(), SERVLET);
                Log.debug(LOG_HEADER + "IN : idSurvey = " + refQualtricsIndividualRequest.getIdSurvey(), SERVLET);
                Log.debug(LOG_HEADER + "IN : surveyLink = " + refQualtricsIndividualRequest.getSurveyLink(), SERVLET);
                Log.debug(LOG_HEADER + "IN : status = " + refQualtricsIndividualRequest.getStatus(), SERVLET);
                Log.debug(LOG_HEADER + "IN : idRequest = " + refQualtricsIndividualRequest.getIdRequest(), SERVLET);

                cstmt.execute();

                exito = true;
                Log.debug(LOG_HEADER + "Actualización correcta");
            }
        } catch (SQLException ex) {
            Log.error(LOG_HEADER, ex, SERVLET);
        } finally {
            try {
                cstmt.close();
            } catch (SQLException ee) {
                Log.error(LOG_HEADER, ee, SERVLET);
            }
            ConexionBD.desconectar();
        }
        return exito;
    }

    public static ArrayList<QualtricsIndividualRequestReport> getRegistriesByDateRange(String dateStart, String dateEnd, String SERVLET, String uuid) {
        String LOG_HEADER = uuid + "QualtricsIndividualRequestDAO.getRegistriesByDateRange() - ";
        int i = 1;
        Connection conn;
        CallableStatement cstmt;
        ResultSet rs;
        ArrayList<QualtricsIndividualRequestReport> ref = new ArrayList<QualtricsIndividualRequestReport>();
        QualtricsIndividualRequestReport pivot = null;

        try {
            ConexionBD.conectarPorNombre("Principal");
            conn = ConexionBD.refConnection;
            if (conn != null) {
                cstmt = conn.prepareCall("SELECT idRequest, createdAt, updatedAt, phoneNumber, idSurvey, idMailingList, surveyLink, status FROM " + table_name + " WHERE createdAt BETWEEN ? AND ?;");

                cstmt.setString(i++, dateStart);
                cstmt.setString(i++, dateEnd);
                Log.debug(LOG_HEADER + "Query : " + cstmt.toString(), SERVLET);
                Log.debug(LOG_HEADER + "IN: dateStart=" + dateStart, SERVLET);
                Log.debug(LOG_HEADER + "IN: dateEnd=" + dateEnd, SERVLET);
                rs = ConexionBD.getResultSet(cstmt);
                
                if (rs != null) {
                    while (rs.next()) {
                        pivot = new QualtricsIndividualRequestReport();
                        pivot.setIdRequest(rs.getString("idRequest"));
                        pivot.setCreatedAt(Fecha.obtenerDateDeUnStringConHora(rs.getString("createdAt")));
                        pivot.setUpdatedAt(Fecha.obtenerDateDeUnStringConHora(rs.getString("updatedAt")));
                        pivot.setPhoneNumber(rs.getString("phoneNumber"));
                        pivot.setIdSurvey(rs.getString("idSurvey"));
                        pivot.setIdMailingList(rs.getString("idMailingList"));
                        pivot.setSurveyLink(rs.getString("surveyLink"));
                        pivot.setStatus(rs.getString("status"));

                        Log.debug(LOG_HEADER + "OUT: pivot.getIdRequest()= " + pivot.getIdRequest(), SERVLET);
                        Log.debug(LOG_HEADER + "OUT: pivot.getPhoneNumber()= " + pivot.getPhoneNumber(), SERVLET);
                        Log.debug(LOG_HEADER + "OUT: pivot.getIdSurvey()= " + pivot.getIdSurvey(), SERVLET);
                        Log.debug(LOG_HEADER + "OUT: pivot.getIdMailingList()= " + pivot.getIdMailingList(), SERVLET);
                        Log.debug(LOG_HEADER + "OUT: pivot.getSurveyLink()= " + pivot.getSurveyLink(), SERVLET);
                        Log.debug(LOG_HEADER + "OUT: pivot.getStatus()= " + pivot.getStatus(), SERVLET);
                        
                        ref.add(pivot);
                    }
                }
                Log.debug(LOG_HEADER + "Se retorno " + ref.size() + " registros de la consulta!", SERVLET);
            }
        } catch (NullPointerException | SQLException ex) {
            Log.error(LOG_HEADER, ex, SERVLET);
        } finally {
            ConexionBD.desconectar();
        }
        return ref;
    }
}
