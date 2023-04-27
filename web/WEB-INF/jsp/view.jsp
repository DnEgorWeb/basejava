<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/styles.css">
    <title>Resume ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <h3>Contacts:</h3>
            <c:forEach var="contactType" items="${resume.getContacts().keySet()}">
                <c:if test="${resume.getContact(contactType) != null}">
                    <dl>
                        <dt>${contactType.getTitle()}</dt>
                        <dd><span>${resume.getContact(contactType)}</span></dd>
                    </dl>
                </c:if>
            </c:forEach>
            <h3 id="sections">Sections:</h3>
            <c:forEach var="sectionType" items="${resume.getStringSections().keySet()}">
                <c:if test="${resume.getStringSection(sectionType) != null}">
                    <dl>
                        <dt>${sectionType.title}</dt>
                        <dd><span>${resume.getStringSection(sectionType)}</span></dd>
                    </dl>
                </c:if>
            </c:forEach>
            <c:forEach var="sectionType" items="${resume.getListSections().keySet()}">
                <c:if test="${resume.getListSection(sectionType) != null}">
                    <dl>
                        <dt>${sectionType.title}</dt>
                        <dd>
                            <ul style="margin: 0; padding: 0; list-style-type: none;">
                            <c:forEach var="listItem" items="${resume.getListSection(sectionType)}">
                                <li style="margin: 0;">${listItem}</li>
                            </c:forEach>
                            </ul>
                        </dd>
                    </dl>
                </c:if>
            </c:forEach>
            <c:forEach var="sectionType" items="${resume.companySections.keySet()}">
                <c:forEach var="company" items="${resume.companySections.get(sectionType)}" varStatus="companyIteration">
                    <dl name="${sectionType.name()}_0">
                        <dt>${sectionType.title}</dt>
                        <dd>
                            <label>название:</label>
                            <span>${company.getName()}</span>
                            <br />
                            <br />
                            <label>сайт:</label>
                            <span>${company.website}</span>
                            <c:forEach var="period" items="${company.getPeriods()}" varStatus="periodIteration">
                                <br />
                                <br />
                                <hr />
                                <label>дата начала:</label>
                                <span>${period.getStartDate()}</span>
                                <br />
                                <br />
                                <label>дата окончания:</label>
                                <span>${period.getEndDate()}</span>
                                <br />
                                <br />
                                <label>название периода:</label>
                                <span>${period.getTitle()}</span>
                                <br />
                                <br />
                                <label>описание периода:</label>
                                <span>${period.getDescription()}</span>
                            </c:forEach>
                        </dd>
                    </dl>
                </c:forEach>
            </c:forEach>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
