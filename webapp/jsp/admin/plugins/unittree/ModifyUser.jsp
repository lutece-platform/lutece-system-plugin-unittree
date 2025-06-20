<%@page import="fr.paris.lutece.portal.service.admin.AccessDeniedException"%>
<%@page import="fr.paris.lutece.portal.service.message.AdminMessageService"%>
<%@page import="fr.paris.lutece.portal.web.constants.Messages"%>
<%@page import="fr.paris.lutece.portal.service.message.AdminMessage"%>
<%@page import="fr.paris.lutece.plugins.unittree.web.UnitJspBean"%>
<%@page import="jakarta.el.ELException" %>


${ unitJspBean.init( pageContext.request, UnitJspBean.RIGHT_MANAGE_UNITS ) }
<% 
	try
	{
%>
		${ pageContext.setAttribute( 'strHtml', unitJspBean.getModifyUser( pageContext.request ) ) }
<%
	}
	catch( ELException el )
	{		
		if ( AccessDeniedException.class.getCanonicalName(  ).equals( el.getCause( ).getClass( ).getCanonicalName( ) ) )
        {
			response.sendRedirect( AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP ) );
        }
        else
        {
    		throw el;
        }
	}
%>

<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

${ pageContext.getAttribute( 'strHtml' ) }

<%@ include file="../../AdminFooter.jsp" %>
