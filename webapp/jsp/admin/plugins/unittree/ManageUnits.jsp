<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="unit" scope="session" class="fr.paris.lutece.plugins.unittree.web.UnitJspBean" />

<% unit.init( request, unit.RIGHT_MANAGE_UNITS ); %>
<%= unit.getManageUnits( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
