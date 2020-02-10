<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- 顶部导航栏部分 -->
<header class="main-header">
    <!-- Logo -->
    <a href="#" class="logo">
        <span class="logo-mini"><b>OES</b></span>
        <span class="logo-lg"><b>OES - </b>教师端</span>
    </a>
    <nav class="navbar navbar-static-top">
        <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </a>
        <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">
                <!-- User Account: style can be found in dropdown.less -->
                <li class="dropdown user user-menu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <img src="<c:url value="/static/dist/img/tea1.jpg"/>" class="user-image" alt="User Image">
                        <span class="hidden-xs">老师：${sessionScope.get("teacher").name}</span>
                    </a>
                    <ul class="dropdown-menu">
                        <!-- User image -->
                        <li class="user-header">
                            <img src="<c:url value="/static/dist/img/tea1.jpg"/>" class="img-circle" alt="User Image">
                            <p>
                                <span>${sessionScope.get("teacher").name} 老师你好！</span> <br/>
                                欢迎使用OES考试系统教师端！
                            </p>
                        </li>
                        <!-- Menu Footer-->
                        <li class="user-footer">
                            <div class="pull-left">
                                <a href="/teacher/${sessionScope.get("teacher").id}/changePass"
                                   class="btn btn-default btn-flat">个人设置</a>
                            </div>
                            <div class="pull-right">
                                <a href="<c:url value="/teacher/logout"/>" class="btn btn-default btn-flat">安全退出</a>
                            </div>
                        </li>
                    </ul>
                </li>
                <!-- Control Sidebar Toggle Button -->
                <li>
                    <a href="#" data-toggle="control-sidebar"><i class="fa fa-gears"></i></a>
                </li>
            </ul>
        </div>
    </nav>
</header>