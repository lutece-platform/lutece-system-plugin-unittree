<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:param name="id-current-unit" select="id-current-unit" />
    <xsl:variable name="current-unit">
        <xsl:value-of select="id-current-unit" />
    </xsl:variable>

    <xsl:template match="units">
        <div id="tree" class="jstree-default">
            <ul>
                <xsl:apply-templates select="unit" />
            </ul>
        </div>
    </xsl:template>

    <xsl:template match="unit">
        <xsl:variable name="index">
            <xsl:value-of select="id-unit" />
        </xsl:variable>
        <li id="node-{$index}">
            <a
                href="jsp/admin/plugins/unittree/ManageUnits.jsp?idUnit={id-unit}"
                title="{description}">
                <i class="fa fa-folder">&#160;</i>
                <xsl:choose>
                    <xsl:when test="id-unit=$id-current-unit">
                        <strong>
                            <xsl:value-of select="label" />
                        </strong>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="label" />
                    </xsl:otherwise>
                </xsl:choose>
            </a>
            <xsl:apply-templates
                select="unit-children" />
        </li>
    </xsl:template>

    <xsl:template match="unit-children">
        <xsl:if test="unit">
            <ul>
                <xsl:apply-templates select="unit" />
            </ul>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
