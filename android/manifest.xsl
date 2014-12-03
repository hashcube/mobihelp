<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:android="http://schemas.android.com/apk/res/android">

	<xsl:param name="mobihelpDomain" />
	<xsl:param name="mobihelpAppKey" />
	<xsl:param name="mobihelpAppSecret" />
	<xsl:param name="mobihelpAutoReplyEnabled" />
	<xsl:param name="mobiHelpReviewPromptCount" />

	<xsl:output indent="yes" />
	<xsl:template match="comment()" />

	<xsl:template match="meta-data[@android:name='MOBIHELP_DOMAIN']">
		<meta-data android:name="MOBIHELP_DOMAIN" android:value="{$mobihelpDomain}"/>
	</xsl:template>

	<xsl:template match="meta-data[@android:name='MOBIHELP_APP_ID']">
		<meta-data android:name="MOBIHELP_APP_ID" android:value="{$mobihelpAppKey}"/>
	</xsl:template>
	
	<xsl:template match="meta-data[@android:name='MOBIHELP_APP_SECRET']">
		<meta-data android:name="MOBIHELP_APP_SECRET" android:value="{$mobihelpAppSecret}"/>
	</xsl:template>

        <xsl:template match="meta-data[@android:name='MOBIHELP_AUTO_REPLY_ENABLE']">
                <meta-data android:name="MOBIHELP_AUTO_REPLY_ENABLE" android:value="{$mobihelpAutoReplyEnabled}"/>
        </xsl:template>

        <xsl:template match="meta-data[@android:name='MOBIHELP_RATE_PROMPT_COUNT']">
                <meta-data android:name="MOBIHELP_RATE_PROMPT_COUNT" android:value="{$mobiHelpReviewPromptCount}"/>
        </xsl:template>

	<xsl:output indent="yes" />
	<xsl:template match="comment()" />

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
