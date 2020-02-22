<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>OES | 试卷列表</title>
    <%@include file="../include/css.jsp" %>
    <style>
        .table > tbody > tr:hover {
            cursor: pointer;
        }

        .table > tbody > tr > td {
            vertical-align: middle;
        }

        th {
            font-size: 15px;
            text-align: center;
        }

        td {
            font-size: 16px;
            text-align: center;
        }

    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">
    <%@include file="../teacher/include/header.jsp" %>
    <!-- 左侧菜单栏 -->
    <jsp:include page="../teacher/include/sider.jsp">
        <jsp:param name="menu" value="paper"/>
    </jsp:include>
    <!-- 右侧内容部分 -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">
            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">试卷管理</h3>
                    <div class="box-tools pull-right">
                        <a href="<c:url value="/teacher/paper/importNewPaper"/>" class="btn btn-primary btn-sm"><i
                                class="fa fa-plus-circle"></i> （导入试题） 新增考试</a>
                        <a id="newBtn" class="btn btn-success btn-sm"><i class="fa fa-plus-circle"></i>（随机抽题） 新增试卷</a>
                        <a href="<c:url value="/teacher/paper/showPaperForm"/>" class="btn btn-warning btn-sm"><i
                                class="fa fa-search"></i> 查看模版</a>
                    </div>
                </div>
                <div class="box-body no-padding">
                    <table class="table table-hover">
                        <tbody>
                        <tr>
                            <th>序号</th>
                            <th>试卷编号</th>
                            <th>试卷名称</th>
                            <th>试卷状态</th>
                            <th>试卷类型</th>
                            <th>操作</th>
                        </tr>
                        <c:if test="${empty page.list}">
                            <tr>
                                <td colspan="6">试卷库中还未添加试卷</td>
                            </tr>
                        </c:if>
                        <c:forEach items="${page.list}" var="paper" varStatus="vs">
                            <tr class="rowDetail" rel="${paper.id}">
                                <td>${vs.count}</td>
                                <td>${paper.id}</td>
                                <td>${paper.paperName}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${paper.end}">已结束</c:when>
                                        <c:when test="${paper.start}">考试中</c:when>
                                        <c:otherwise>未开始</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${paper.paperType}</td>
                                <td>
                                    <button class="btn btn-vk btn-sm viewBtn" rel="${paper.id}">查看试卷</button>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->
            <ul id="pagination-demo" class="pagination-sm pull-right"></ul>
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
    <%@include file="../include/footer.jsp" %>
</div>
<!-- ./wrapper -->
<%@include file="../include/js.jsp" %>
<script src="<c:url value="/static/plugins/layer/layer.js"/>"></script>
<script src="<c:url value="/static/plugins/jquery-pagination/jquery.twbsPagination.min.js"/>"></script>
<script>

    $(function () {
        $(".viewBtn").click(function () {
            let id = $(this).attr("rel");
            window.location.href = "/teacher/paper/show/" + id;
        });

        $("#newBtn").click(function () {
            layer.confirm('是否使用默认试卷模版？', {
                btn: ['是', '否'] //按钮
            }, function () {
                window.location.href = "/teacher/paper/newPaper/1";
            }, function () {
                window.location.href = "/teacher/paper/newPaperForm";
            });
        });

        //分页
        $('#pagination-demo').twbsPagination({
            totalPages: "${page.pages}",
            visiblePages: 3,
            first: '首页',
            last: '末页',
            prev: '上一页',
            next: '下一页',
            href: "?p={{number}}"
        });
    });
</script>
</body>
</html>


