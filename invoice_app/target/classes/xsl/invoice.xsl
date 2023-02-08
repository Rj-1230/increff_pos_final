<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:msxsl="urn:schemas-microsoft-com:xslt" version="1.0"
                exclude-result-prefixes="msxsl">
    <xsl:output method="xml" indent="yes"/>
    <xsl:template match="/">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4" page-height="29.7cm" page-width="21.0cm" margin-top="1cm"
                                       margin-left="2cm" margin-right="2cm" margin-bottom="1cm">
                    <!-- Page template goes here -->
                    <fo:region-body margin-bottom="1.5cm"/>
                    <!--                    <fo:region-before region-name="xsl-region-before"/>-->
                    <fo:region-after region-name="xsl-region-after" extent="1cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="A4">
                <!-- Page content goes here -->
                <fo:static-content flow-name="xsl-region-after">
                    <fo:block line-height="20pt">
                        <fo:block font-weight="bold" text-align="center">
                            INCREFF - Building Excellence :
                            <fo:inline font-style="italic">Have a nice Day!!</fo:inline>
                        </fo:block>
                    </fo:block>
                </fo:static-content>
                <fo:flow flow-name="xsl-region-body" line-height="20pt">
                    <fo:block>
                        <xsl:apply-templates/>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <xsl:template match="invoice">
        <fo:block>
            <fo:table table-layout="fixed" width="100%">
                <fo:table-column column-width="8.5cm"/>
                <fo:table-column column-width="8.5cm"/>
                <fo:table-body>
                    <fo:table-row font-size="18pt" line-height="30px" background-color="#00285E" color="white">
                        <fo:table-cell padding-left="20pt">
                            <fo:block>
                                <fo:external-graphic
                                        src="url(https://cdn.skillenza.com/files/45aa07e5-5b0b-4fee-9cea-2009d437cea4/increff_logo.png)"
                                        content-height="scale-to-fit" height="50px" content-width="0.70in"
                                        scaling="non-uniform"/>
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding-top="20pt" padding-right="10pt" font-weight="bold">
                            <fo:block text-align="right">
                                INVOICE
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:block>
        <fo:block space-before="10pt" width="17cm">
            <fo:table>
                <fo:table-column column-width="8.5cm"/>
                <fo:table-column column-width="4.5cm"/>
                <fo:table-column column-width="4cm"/>
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block>
                                <fo:inline font-weight="bold">From ,</fo:inline>&#x2028;
                                <xsl:value-of select="./companyName"></xsl:value-of>&#x2028;
                                <xsl:value-of select="./building"></xsl:value-of>&#x2028;
                                <xsl:value-of select="./street"></xsl:value-of>&#x2028;
                                <xsl:value-of select="./locality"></xsl:value-of>&#x2028;
                                <xsl:value-of select="./city"></xsl:value-of>&#x2028;
                                <xsl:value-of select="./state"></xsl:value-of>&#x2028;
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell>
                            <fo:block text-align="left">
                                <fo:inline font-weight="bold">Customer Name :</fo:inline>&#x2028;
                                <fo:inline font-weight="bold">Customer Phone :</fo:inline>&#x2028;
                                <fo:inline font-weight="bold">Invoice ID :</fo:inline>&#x2028;
                                <fo:inline font-weight="bold">Invoiced Date :</fo:inline>&#x2028;
                                <fo:inline font-weight="bold">Invoiced Time :</fo:inline>&#x2028;
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell>
                            <fo:block text-align="left">
                                <xsl:value-of select="./customerName"></xsl:value-of>&#x2028;
                                <xsl:value-of select="./customerPhone"></xsl:value-of>&#x2028;
                                <fo:inline font-weight="bold">#</fo:inline>
                                <xsl:value-of select="./orderId"></xsl:value-of>&#x2028;
                                <xsl:value-of select="./invoiceDate"></xsl:value-of>&#x2028;
                                <xsl:value-of select="./invoiceTime"></xsl:value-of>&#x2028;
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:block>
        <fo:block space-before="35pt">
            <fo:table line-height="30px">
                <fo:table-column column-width="1.5cm"/>
                <fo:table-column column-width="3cm"/>
                <fo:table-column column-width="4cm"/>
                <fo:table-column column-width="2cm"/>
                <fo:table-column column-width="3cm"/>
                <fo:table-column column-width="3cm"/>
                <fo:table-header>
                    <fo:table-row background-color="#f5f5f5" text-align="center" font-weight="bold">
                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block>S.No.</fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block>ItemId</fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block>Product</fo:block>
                        </fo:table-cell>

                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block>Quantity</fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block text-align="right">Unit Price</fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block text-align="right">Total</fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-header>
                <fo:table-body>
                    <xsl:apply-templates select="orderItems/orderItem"></xsl:apply-templates>
                    <fo:table-row font-weight="bold">
                        <fo:table-cell text-align="center" number-columns-spanned="5" padding-right="3pt"
                                       border="1px solid #b8b6b6">
                            <fo:block>Total Bill Amount :-</fo:block>
                        </fo:table-cell>
                        <fo:table-cell text-align="right" padding-right="3pt" background-color="#f5f5f5"
                                       border="1px solid #b8b6b6">
                            <fo:block>Rs.
                                <xsl:value-of select="totalAmount"/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:block>
    </xsl:template>

    <xsl:template match="orderItem">
        <fo:table-row>
            <fo:table-cell border="1px solid #b8b6b6" text-align="center">
                <fo:block>
                    <xsl:value-of select="sn"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1px solid #b8b6b6" text-align="center">
                <fo:block>
                    <xsl:value-of select="id"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1px solid #b8b6b6" text-align="center" padding-left="3pt">
                <fo:block>
                    <xsl:value-of select="productName"/>
                </fo:block>
            </fo:table-cell>

            <fo:table-cell border="1px solid #b8b6b6" text-align="center" padding-right="3pt">
                <fo:block>
                    <xsl:value-of select="quantity"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1px solid #b8b6b6" text-align="right" padding-right="3pt">
                <fo:block>
                    <xsl:value-of select="sellingPrice"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1px solid #b8b6b6" text-align="right" padding-right="3pt">
                <fo:block>
                    <xsl:value-of select="total"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>

    </xsl:template>
</xsl:stylesheet>