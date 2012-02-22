<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="unit" scope="session" class="fr.paris.lutece.plugins.unittree.web.UnitJspBean" />

<% unit.init( request, unit.RIGHT_MANAGE_UNITS ); %>
<% response.sendRedirect( unit.doRemoveUser( request ) ); %>
