/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qualtricssole.dao;

import desktopframework.ConexionBD;
import desktopframework.Fecha;
import desktopframework.Log;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import qualtricssole.beans.QualtricsRequest;
import qualtricssole.beans.QualtricsRequestReport;

/**
 *
 * @author Luis D
 */
public class QualtricsRequestDAO {

    //private static final String pool_name = Configuracion.getValueString("BD_RESOURCE");
    private static final String table_name = "qualtricsmasivos";

    public static boolean insert(QualtricsRequest refQualtricsRequest, String SERVLET, String uuid) {
        String LOG_HEADER = uuid + "QualtricsRequestDAO.insert() - ";
        int i = 1;
        Connection conn;
        CallableStatement cstmt = null;
        boolean exito = false;

        try {
            ConexionBD.conectarPorNombre("Principal");
            conn = ConexionBD.refConnection;
            if (conn != null) {

                cstmt = conn.prepareCall("INSERT INTO " + table_name + "(idRequest,createdAt, updatedAt, fileName, idSurvey, idMailingList, numClients, numQualtricsLinks, numWSSent, status)VALUES(?,?,?,?,?,?,?,?,?,?);");

                cstmt.setString(i++, refQualtricsRequest.getIdRequest());
                cstmt.setString(i++, Fecha.obtenerActualString("yyyy-MM-dd HH:mm:ss.SSS"));
                cstmt.setString(i++, Fecha.obtenerActualString("yyyy-MM-dd HH:mm:ss.SSS"));
                cstmt.setString(i++, refQualtricsRequest.getFileName());
                cstmt.setString(i++, refQualtricsRequest.getIdSurvey());
                cstmt.setString(i++, refQualtricsRequest.getIdMailingList());
                cstmt.setInt(i++, refQualtricsRequest.getNumClients());
                cstmt.setInt(i++, refQualtricsRequest.getNumQualtricsLinks());
                cstmt.setInt(i++, refQualtricsRequest.getNumWSSent());
                cstmt.setString(i++, refQualtricsRequest.getStatus());

                Log.debug(LOG_HEADER + "Query : " + cstmt.toString(), SERVLET);
                Log.debug(LOG_HEADER + "IN : idRequest = " + refQualtricsRequest.getIdRequest(), SERVLET);
                Log.debug(LOG_HEADER + "IN : fileName = " + refQualtricsRequest.getFileName(), SERVLET);
                Log.debug(LOG_HEADER + "IN : idSurvey = " + refQualtricsRequest.getIdSurvey(), SERVLET);
                Log.debug(LOG_HEADER + "IN : idMailingList = " + refQualtricsRequest.getIdMailingList(), SERVLET);
                Log.debug(LOG_HEADER + "IN : numClients = " + refQualtricsRequest.getNumClients(), SERVLET);
                Log.debug(LOG_HEADER + "IN : numQualtricsLinks = " + refQualtricsRequest.getNumQualtricsLinks(), SERVLET);
                Log.debug(LOG_HEADER + "IN : numWSSent = " + refQualtricsRequest.getNumWSSent(), SERVLET);
                Log.debug(LOG_HEADER + "IN : status = " + refQualtricsRequest.getStatus(), SERVLET);

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

    public static boolean update(QualtricsRequest refQualtricsRequest, String SERVLET, String uuid) {
        String LOG_HEADER = uuid + "QualtricsRequestDAO.update() - ";
        int i = 1;
        Connection conn;
        CallableStatement cstmt = null;
        boolean exito = false;

        try {
            ConexionBD.conectarPorNombre("Principal");
            conn = ConexionBD.refConnection;
            if (conn != null) {

                cstmt = conn.prepareCall("UPDATE " + table_name + " SET updatedAt=?, fileName=?, idSurvey=?, idMailingList=?, numClients=?, numQualtricsLinks=?, numWSSent=?,status=? WHERE idRequest=?;");

                cstmt.setString(i++, Fecha.obtenerActualString("yyyy-MM-dd HH:mm:ss.SSS"));
                cstmt.setString(i++, refQualtricsRequest.getFileName());
                cstmt.setString(i++, refQualtricsRequest.getIdSurvey());
                cstmt.setString(i++, refQualtricsRequest.getIdMailingList());
                cstmt.setInt(i++, refQualtricsRequest.getNumClients());
                cstmt.setInt(i++, refQualtricsRequest.getNumQualtricsLinks());
                cstmt.setInt(i++, refQualtricsRequest.getNumWSSent());
                cstmt.setString(i++, refQualtricsRequest.getStatus());
                cstmt.setString(i++, refQualtricsRequest.getIdRequest());

                Log.debug(LOG_HEADER + "Query : " + cstmt.toString());
                Log.debug(LOG_HEADER + "IN : fileName = " + refQualtricsRequest.getFileName(), SERVLET);
                Log.debug(LOG_HEADER + "IN : idSurvey = " + refQualtricsRequest.getIdSurvey(), SERVLET);
                Log.debug(LOG_HEADER + "IN : idMailingList = " + refQualtricsRequest.getIdMailingList(), SERVLET);
                Log.debug(LOG_HEADER + "IN : numClients = " + refQualtricsRequest.getNumClients(), SERVLET);
                Log.debug(LOG_HEADER + "IN : numQualtricsLinks = " + refQualtricsRequest.getNumQualtricsLinks(), SERVLET);
                Log.debug(LOG_HEADER + "IN : numWSSent = " + refQualtricsRequest.getNumWSSent(), SERVLET);
                Log.debug(LOG_HEADER + "IN : status = " + refQualtricsRequest.getStatus(), SERVLET);

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

    public static ArrayList<QualtricsRequestReport> getRegistriesByDateRange(String dateStart, String dateEnd, String SERVLET, String uuid) {
        String LOG_HEADER = uuid + "QualtricsRequestDAO.getRegistriesByDateRange() - ";
        int i = 1;
        Connection conn;
        CallableStatement cstmt;
        ResultSet rs;
        ArrayList<QualtricsRequestReport> ref = new ArrayList<QualtricsRequestReport>();
        QualtricsRequestReport pivot = null;

        try {
            ConexionBD.conectarPorNombre("Principal");
            conn = ConexionBD.refConnection;
            if (conn != null) {
                cstmt = conn.prepareCall("SELECT idRequest, createdAt, updatedAt, fileName, idSurvey, idMailingList, numClients, numQualtricsLinks, numWSSent, status FROM " + table_name + " WHERE createdAt BETWEEN ? AND ?;");

                cstmt.setString(i++, dateStart);
                cstmt.setString(i++, dateEnd);
                Log.debug(LOG_HEADER + "Query : " + cstmt.toString(), SERVLET);
                Log.debug(LOG_HEADER + "IN: dateStart=" + dateStart, SERVLET);
                Log.debug(LOG_HEADER + "IN: dateEnd=" + dateEnd, SERVLET);
                rs = ConexionBD.getResultSet(cstmt);
                if (rs != null) {
                    while (rs.next()) {
                        pivot = new QualtricsRequestReport();
                        pivot.setIdRequest(rs.getString("idRequest"));
                        pivot.setCreatedAt(Fecha.obtenerDateDeUnStringConHora(rs.getString("createdAt")));
                        pivot.setUpdatedAt(Fecha.obtenerDateDeUnStringConHora(rs.getString("updatedAt")));
                        pivot.setFileName(rs.getString("fileName"));
                        pivot.setIdSurvey(rs.getString("idSurvey"));
                        pivot.setIdMailingList(rs.getString("idMailingList"));
                        pivot.setNumClients(rs.getInt("numClients"));
                        pivot.setNumQualtricsLinks(rs.getInt("numQualtricsLinks"));
                        pivot.setNumWSSent(rs.getInt("numWSSent"));
                        pivot.setStatus(rs.getString("status"));

                        Log.debug(LOG_HEADER + "OUT: pivot.getIdRequest()= " + pivot.getIdRequest(), SERVLET);
                        Log.debug(LOG_HEADER + "OUT: pivot.getFileName()= " + pivot.getFileName(), SERVLET);
                        Log.debug(LOG_HEADER + "OUT: pivot.getIdSurvey()= " + pivot.getIdSurvey(), SERVLET);
                        Log.debug(LOG_HEADER + "OUT: pivot.getIdMailingList()= " + pivot.getIdMailingList(), SERVLET);
                        Log.debug(LOG_HEADER + "OUT: pivot.getNumClients()= " + pivot.getNumClients(), SERVLET);
                        Log.debug(LOG_HEADER + "OUT: pivot.getNumQualtricsLinks()= " + pivot.getNumQualtricsLinks(), SERVLET);
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
