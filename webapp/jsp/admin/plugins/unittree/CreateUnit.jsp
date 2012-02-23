<%@page import="fr.paris.lutece.portal.service.admin.AccessDeniedException"%>
<%@page import="fr.paris.lutece.portal.service.message.AdminMessageService"%>
<%@page import="fr.paris.lutece.portal.web.constants.Messages"%>
<%@page import="fr.paris.lutece.portal.service.message.AdminMessage"%>
<%@page import="fr.paris.lutece.portal.service.util.AppException"%>

<jsp:useBean id="unit" scope="session" class="fr.paris.lutece.plugins.unittree.web.UnitJspBean" />

<% unit.init( request, unit.RIGHT_MANAGE_UNITS ); %>
<% 
	String strHtml = "";
	try
	{
		strHtml = unit.getCreateUnit( request );
	}
	catch( AccessDeniedException ex )
	{
		response.sendRedirect( AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP ) );
	}
%>

<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<%= strHtml %>

<%@ include file="../../AdminFooter.jsp" %>
