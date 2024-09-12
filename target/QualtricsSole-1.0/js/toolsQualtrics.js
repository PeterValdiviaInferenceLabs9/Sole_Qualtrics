/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
function getParamByName(param_name) {
    var queryString = window.location.search;
    var urlParams = new URLSearchParams(queryString);
    var rs = urlParams.get(param_name);
    return rs;
}

