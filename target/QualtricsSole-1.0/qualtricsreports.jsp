<%-- 
    Document   : qualtricsreports
    Created on : 26 abr. 2023, 5:56:33 p. m.
    Author     : Luis D
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat" %>  
<%@page import="java.util.Date" %>  
<%! final static String DATE_FORMAT_NOW = "yyyy-MM-dd";%>
<%! final static String DATE_FORMAT_NOW_2 = "yyyy-MM-01";%>
<!DOCTYPE html>
<html>
    <head>
        <title>Qualtrics SOLE Reports</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/style3.css">
        <link rel='stylesheet' href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css'>
    </head>
    <body>
        <section>
            <div class="form-box">
                <div class="form-value">
                    <%SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_NOW);%>
                    <%SimpleDateFormat df2 = new SimpleDateFormat(DATE_FORMAT_NOW_2);%>
                    <% String fechaEnd = "2022-06-21";%>

                    <form action="QualtricsReports" method="GET">
                        <h2>Qualtrics SOLE Reportes</h2>
                        <div class="reportSection">
                            <a href="qualtricssoleservice.html" class='bx bx-upload'></a>
                        </div>
                        <div class="inputbox">
                            <i class='bx bx-calendar-edit'></i>
                            <input type="date" id="start" name=FechaStart value=<%= df2.format(new Date())%>>
                            <p>Fecha Inicio</p>
                        </div>
                        <div class="inputbox">
                            <i class='bx bx-calendar-edit'></i>
                            <input type="date" id="end" name=FechaEnd value=<%= df.format(new Date())%>>
                            <p>Fecha Fin</p>
                        </div>
                        <div class="inputSelectbox">
                            <select id="opcionReporte" name="opcionReporte">
                                <option value="MASIVO">Masivo</option>
                                <option value="1A1">1 a 1</option>
                            </select>
                        </div>
                        <div class="cont-button">
                            <input type="submit" onclick="this.form.submit(); this.disabled = true; this.value = 'Sending…';" value="Descargar" class="do_it">
                        </div>
                    </form>
                </div>
            </div>
        </section>
        <script type="module" src="https://unpkg.com/ionicons@5.5.2/dist/ionicons/ionicons.esm.js"></script>
        <script nomodule src="https://unpkg.com/ionicons@5.5.2/dist/ionicons/ionicons.js"></script>
    </body>
</html>
