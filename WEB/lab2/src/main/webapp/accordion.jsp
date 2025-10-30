<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ui" uri="/WEB-INF/tags.tld" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="accordion/accordion.css">
</head>
<body>
<script src="accordion/accordion.js"></script>

<ui:accordion mode="single" expanded="1, 2">
    [TITLE]Заголовок 1[/TITLE]
    [CONTENT]Содержимое тега аккордион 1[/CONTENT]
    [TITLE]Заголовок 2[/TITLE]
    [CONTENT]Содержимое тега аккордион 2[/CONTENT]
    [TITLE]Заголовок 3[/TITLE]
    [CONTENT]Содержимое тега аккордион 3[/CONTENT]
</ui:accordion>

<ui:accordion id="12" mode="multiply">
    [TITLE]Заголовок 1[/TITLE]
    [CONTENT]Содержимое тега аккордион 1[/CONTENT]
    [TITLE]Заголовок 2[/TITLE]
    [CONTENT]Содержимое тега аккордион 2[/CONTENT]
    [TITLE]Заголовок 3[/TITLE]
    [CONTENT]Содержимое тега аккордион 3[/CONTENT]
</ui:accordion>
</body>
</html>
