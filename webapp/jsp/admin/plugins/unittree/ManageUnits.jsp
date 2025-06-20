<%@page import="fr.paris.lutece.portal.web.pluginaction.IPluginActionResult"%>
<%@page import="fr.paris.lutece.plugins.unittree.web.UnitJspBean"%>
 
	${ unitJspBean.init( pageContext.request, UnitJspBean.RIGHT_MANAGE_UNITS ) }
	
	${ pageContext.setAttribute( 'pluginActionResult', unitJspBean.getManageUnits( pageContext.request, pageContext.response ) ) }
	${ not empty pageContext.getAttribute( 'pluginActionResult' ).redirect ? pageContext.response.sendRedirect( pageContext.getAttribute( 'pluginActionResult' ).redirect ) : '' }
	
	<%@ page errorPage="../../ErrorPage.jsp" %>
	<jsp:include page="../../AdminHeader.jsp" />

	${ not empty pageContext.getAttribute( 'pluginActionResult' ).htmlContent ? pageContext.getAttribute( 'pluginActionResult' ).htmlContent : '' }

	<%@ include file="../../AdminFooter.jsp" %>