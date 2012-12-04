/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.5-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.05.01 at 05:38:05 PM WEST 
//


package org.openxdm.xcap.client.appusage.presrules.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * <p>Java class for provideServicePermission complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="provideServicePermission">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="all-services">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;choice>
 *             &lt;element ref="{urn:ietf:params:xml:ns:pres-rules}service-uri"/>
 *             &lt;element ref="{urn:ietf:params:xml:ns:pres-rules}service-uri-scheme"/>
 *             &lt;element ref="{urn:ietf:params:xml:ns:pres-rules}occurrence-id"/>
 *             &lt;element ref="{urn:ietf:params:xml:ns:pres-rules}class"/>
 *             &lt;any/>
 *           &lt;/choice>
 *         &lt;/sequence>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "provideServicePermission", namespace = "urn:ietf:params:xml:ns:pres-rules", propOrder = {
    "allServices",
    "serviceUriOrServiceUriSchemeOrOccurrenceId"
})
@XmlRootElement(name = "provide-services", namespace = "urn:ietf:params:xml:ns:pres-rules")
public class ProvideServicePermission {

    @XmlElement(name = "all-services")
    protected ProvideServicePermission.AllServices allServices;
    @XmlElementRefs({
        @XmlElementRef(name = "class", namespace = "urn:ietf:params:xml:ns:pres-rules", type = JAXBElement.class),
        @XmlElementRef(name = "service-uri-scheme", namespace = "urn:ietf:params:xml:ns:pres-rules", type = JAXBElement.class),
        @XmlElementRef(name = "service-uri", namespace = "urn:ietf:params:xml:ns:pres-rules", type = JAXBElement.class),
        @XmlElementRef(name = "occurrence-id", namespace = "urn:ietf:params:xml:ns:pres-rules", type = JAXBElement.class)
    })
    @XmlAnyElement(lax = true)
    protected List<Object> serviceUriOrServiceUriSchemeOrOccurrenceId;

    /**
     * Gets the value of the allServices property.
     * 
     * @return
     *     possible object is
     *     {@link ProvideServicePermission.AllServices }
     *     
     */
    public ProvideServicePermission.AllServices getAllServices() {
        return allServices;
    }

    /**
     * Sets the value of the allServices property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProvideServicePermission.AllServices }
     *     
     */
    public void setAllServices(ProvideServicePermission.AllServices value) {
        this.allServices = value;
    }

    /**
     * Gets the value of the serviceUriOrServiceUriSchemeOrOccurrenceId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the serviceUriOrServiceUriSchemeOrOccurrenceId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServiceUriOrServiceUriSchemeOrOccurrenceId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link Element }
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * 
     */
    public List<Object> getServiceUriOrServiceUriSchemeOrOccurrenceId() {
        if (serviceUriOrServiceUriSchemeOrOccurrenceId == null) {
            serviceUriOrServiceUriSchemeOrOccurrenceId = new ArrayList<Object>();
        }
        return this.serviceUriOrServiceUriSchemeOrOccurrenceId;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class AllServices {


    }

}
