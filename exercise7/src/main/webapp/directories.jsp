<%@ page import="java.util.List" %>
<%@ page import="util.entity.ListItem" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Directories</title>
</head>
<body>
<h1>Current directory: <i><%= request.getAttribute("currDir") %></i></h1>
<h1>Files:</h1>
<%
    List<ListItem> fileList = (List<ListItem>) request.getAttribute("fileList");
    if (fileList != null) {
        for (ListItem item: fileList) {
%>
            <p style="margin-left: <%=item.getNestingLvl() * 20%>px; font-style: italic; font-size: x-large;">
                <%=item.getItemName()%>
            </p>
<%
       }
    }
%>
</body>
</html>
