/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qualtricssole.controllers;

import java.util.ArrayList;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import qualtricssole.beans.QualtricsIndividualRequest;
import qualtricssole.beans.QualtricsRequest;
import qualtricssole.beans.QualtricsRequestReport;
import qualtricssole.dao.QualtricsIndividualRequestDAO;
import qualtricssole.beans.QualtricsIndividualRequestReport;
import qualtricssole.dao.QualtricsRequestDAO;

/**
 *
 * @author Luis D
 */
public class QualtricsRequestController {

    public static boolean createQualtricsRequestRegistry(QualtricsRequest refQualtricsRequest, String uuid, String SERVLET) {
        boolean rs = true;
        rs = QualtricsRequestDAO.insert(refQualtricsRequest, SERVLET, uuid);
        return rs;
    }

    public static boolean updateQualtricsRequestRegistry(QualtricsRequest refQualtricsRequest, String uuid, String SERVLET) {
        boolean rs = true;
        rs = QualtricsRequestDAO.update(refQualtricsRequest, SERVLET, uuid);
        return rs;
    }

    public static boolean createQualtricsIndividualRequestRegistry(QualtricsIndividualRequest refQualtricsIndividualRequest, String uuid, String SERVLET) {
        boolean rs = true;
        rs = QualtricsIndividualRequestDAO.insert(refQualtricsIndividualRequest, SERVLET, uuid);
        return rs;
    }

    public static boolean updateQualtricsIndividualRequestRegistry(QualtricsIndividualRequest refQualtricsIndividualRequest, String uuid, String SERVLET) {
        boolean rs = true;
        rs = QualtricsIndividualRequestDAO.update(refQualtricsIndividualRequest, SERVLET, uuid);
        return rs;
    }

    public static ArrayList<QualtricsRequestReport> getRegistriesByDateRange(String dateStart, String dateEnd, String uuid, String SERVLET) {
        ArrayList<QualtricsRequestReport> rs = QualtricsRequestDAO.getRegistriesByDateRange(dateStart, dateEnd, SERVLET, uuid);
        return rs;
    }

    public static ArrayList<QualtricsIndividualRequestReport> getIndividualRegistriesByDateRange(String dateStart, String dateEnd, String uuid, String SERVLET) {
        ArrayList<QualtricsIndividualRequestReport> rs = QualtricsIndividualRequestDAO.getRegistriesByDateRange(dateStart, dateEnd, SERVLET, uuid);
        return rs;
    }
}
