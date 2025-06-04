<%@ page errorPage="../../ErrorPage.jsp" %>
<%@page import="fr.paris.lutece.plugins.unittree.web.UnitJspBean"%>

${ unitJspBean.init( pageContext.request, UnitJspBean.RIGHT_MANAGE_UNITS ) }
${ pageContext.response.sendRedirect( unitJspBean.getConfirmRemoveUnit( pageContext.request ) ) }
