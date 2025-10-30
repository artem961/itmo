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
        <td class="canvas-column">
            <div id="canvas-container">
                <canvas id="canvas" width="400" height="400"></canvas>
            </div>
            <div id="r-container">
                <div id="r-label" class="label primary-text">R</div>
                <div id="r-group" class="r-group">
                    <select id="r">
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                    </select>
                </div>
            </div>

        </td>
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
        <td class="menu-column">
            <div class="menu">
                <div class="label primary-text">X</div>
                <div id="x-group" class="x-group primary-text">
                    <input type="button" name="x" id="x-4" value="-4">

                    <input type="button" name="x" id="x-3" value="-3">

                    <input type="button" name="x" id="x-2" value="-2">

                    <input type="button" name="x" id="x-1" value="-1">

                    <input type="button" name="x" id="x0" value="0" class="selected">

                    <input type="button" name="x" id="x1" value="1">

                    <input type="button" name="x" id="x2" value="2">

                    <input type="button" name="x" id="x3" value="3">

                    <input type="button" name="x" id="x4" value="4">
                </div>
                <div class="label primary-text">Y</div>
                <div id="y-group" class="y-group">
                    <input type="text" id="y" name="y" value="0" maxlength="7">
                    <label for="y"></label>
                </div>

                <div id="b-group" class="b-group">
                    <button type="submit" id="submit">Отправить</button>
                </div>
            </div>
            <div class="settings">
                <button class="button-secondary" type="submit" id="clear">Очистить</button>
            </div>
        </td>
    </tr>
</table>

<script src="js/tools/ajax/ajax-manager.js"></script>
<script src="js/tools/table/table-manager.js"></script>
<script src="js/tools/validation/validator.js"></script>
<script src="js/tools/graphics.js"></script>
<script src="js/tools/tooltip.js"></script>
<script src="js/tools/canvas-controller.js"></script>

<script src="js/interactiveObjects/interactive-object.js"></script>
<script src="js/interactiveObjects/grid.js"></script>
<script src="js/interactiveObjects/axis.js"></script>
<script src="js/interactiveObjects/point-object.js"></script>
<script src="js/interactiveObjects/zones/rectangle-zone.js"></script>
<script src="js/interactiveObjects/zones/sector-zone.js"></script>
<script src="js/interactiveObjects/zones/triangle-zone.js"></script>
<script src="js/interactiveObjects/label.js"></script>

<script src="js/tools/plane.js"></script>
<script src="js/main.js"></script>
<script src="js/menu.js"></script>
</body>
</html>