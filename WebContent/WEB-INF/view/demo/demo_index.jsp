<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.tp.util.ConfigManager" %>
<%@ page import="com.tp.constant.SystemConstant" %><%--
  Created by IntelliJ IDEA.
  User: wenhm
  Date: 2018/3/22
  Time: 15:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    //String version = "v1.0";
    Date versionDate=new Date();
    SimpleDateFormat versionDateSdf=new SimpleDateFormat("yyyyMM");
    String versionStr=versionDateSdf.format(versionDate);
    String version="v1_"+versionStr;

    String context_path = request.getContextPath();
    String css_path = ConfigManager.getItemValue(SystemConstant.CSS_PATH) ;
    String image_path = ConfigManager.getItemValue(SystemConstant.IMAGE_PATH) ;
    String javascript_path = ConfigManager.getItemValue(SystemConstant.JAVASCRIPT_PATH) ;

%>

<html>
<head>
    <title>demo</title>
    <script type="text/javascript" src="<%=javascript_path %>/resource/js/common/plugins/jquery/jquery.min.js?v=<%=version %>"></script>
    <script type="text/javascript" src="<%=javascript_path %>/resource/plugin/common.js?v=<%=version %>"></script>
    <script type="text/javascript">
        var list = "${list}";
        var contextpath = '<%=context_path%>';
        var css_path = '<%=css_path%>';
        var image_path = '<%=image_path%>';
        var javascript_path = '<%=javascript_path%>';
    </script>

</head>
<body>
<div>
    <span>
        demo啊啊啊223 ${list}
    </span>
</div>

</body>

<script type="text/javascript" src="<%=javascript_path %>/resource/js/demo/demo.js?v=<%=version %>"></script>
</html>
