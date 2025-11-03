<%@ page import="web.tools.http.CalcResultsArray" %>
<%@ page import="java.io.IOException" %>
<%@ page import="web.tools.http.StandartCalcResult" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>lab1</title>

    <link rel="stylesheet" href="css/colors.css">
    <link rel="stylesheet" href="css/main.css">
</head>
<body>
<table class="main-table">
    <tr>
        <td colspan="3">
            <header class="primary-text">
                <h1>Лабораторная работа №2</h1>
                <h3>Храбров Артём Алексеевич Р3215</h3>
                <h3>Вариант 73109</h3>
            </header>
        </td>
    </tr>
    <tr>
        <td>
            <button class="button-secondary" onclick="goBack()">Назад</button>
        </td>
    </tr>
    <tr>

        <td class="table-column">
            <div class="table-wrapper">
                <table class="table" id="results">
                    <thead class="primary-text">
                    <tr>
                        <th>X</th>
                        <th>Y</th>
                        <th>R</th>
                        <th>Результат</th>
                        <th>Время выполнения</th>
                        <th>Время</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        CalcResultsArray resultsArray = (CalcResultsArray) session.getAttribute("results");
                        if (resultsArray != null) {
                            for (StandartCalcResult res : resultsArray.results()) {
                    %>
                    <tr>
                        <td><%= res.x() %></td>
                        <td><%= res.y() %></td>
                        <td><%= res.r() %></td>
                        <td data-result=<%=res.result()%>><%= res.result() %></td>
                        <td><%= res.time() %></td>
                        <td><%= res.currentTime() %></td>
                    </tr>
                    <%
                            }
                        }
                    %>
                    </tbody>
                </table>
            </div>
        </td>
    </tr>
</table>
<script src="js/tools/ajax/ajax-manager.js"></script>
<script>function goBack(){
    window.location.href = api + '/start';
}</script>
</body>
</html>