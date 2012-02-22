<%@page import="fr.paris.lutece.portal.web.pluginaction.IPluginActionResult"%>

<jsp:useBean id="unit" scope="session" class="fr.paris.lutece.plugins.unittree.web.UnitJspBean" />

<% 
	unit.init( request, unit.RIGHT_MANAGE_UNITS );
	IPluginActionResult result = unit.getManageUnits( request, response );
	if ( result.getRedirect(  ) != null )
	{
		response.sendRedirect( result.getRedirect(  ) );
	}
	else if ( result.getHtmlContent(  ) != null )
	{
%>
		<%@ page errorPage="../../ErrorPage.jsp" %>
		<jsp:include page="../../AdminHeader.jsp" />

		<%= result.getHtmlContent(  ) %>

		<%@ include file="../../AdminFooter.jsp" %>
<%
	}
%>
