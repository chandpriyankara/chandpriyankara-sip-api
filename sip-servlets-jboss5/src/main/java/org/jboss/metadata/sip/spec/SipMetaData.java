/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.metadata.sip.spec;

import java.lang.reflect.Method;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.jboss.metadata.javaee.spec.AnnotatedEJBReferencesMetaData;
import org.jboss.metadata.javaee.spec.EJBLocalReferenceMetaData;
import org.jboss.metadata.javaee.spec.EJBLocalReferencesMetaData;
import org.jboss.metadata.javaee.spec.EJBReferenceMetaData;
import org.jboss.metadata.javaee.spec.EJBReferencesMetaData;
import org.jboss.metadata.javaee.spec.EmptyMetaData;
import org.jboss.metadata.javaee.spec.Environment;
import org.jboss.metadata.javaee.spec.EnvironmentEntriesMetaData;
import org.jboss.metadata.javaee.spec.EnvironmentEntryMetaData;
import org.jboss.metadata.javaee.spec.EnvironmentRefsGroupMetaData;
import org.jboss.metadata.javaee.spec.LifecycleCallbacksMetaData;
import org.jboss.metadata.javaee.spec.MessageDestinationReferenceMetaData;
import org.jboss.metadata.javaee.spec.MessageDestinationReferencesMetaData;
import org.jboss.metadata.sip.spec.MessageDestinationsMetaData;
import org.jboss.metadata.javaee.spec.PersistenceContextReferenceMetaData;
import org.jboss.metadata.javaee.spec.PersistenceContextReferencesMetaData;
import org.jboss.metadata.javaee.spec.PersistenceUnitReferenceMetaData;
import org.jboss.metadata.javaee.spec.PersistenceUnitReferencesMetaData;
import org.jboss.metadata.javaee.spec.ResourceEnvironmentReferenceMetaData;
import org.jboss.metadata.javaee.spec.ResourceEnvironmentReferencesMetaData;
import org.jboss.metadata.javaee.spec.ResourceReferenceMetaData;
import org.jboss.metadata.javaee.spec.ResourceReferencesMetaData;
import org.jboss.metadata.sip.spec.SecurityRolesMetaData;
import org.jboss.metadata.javaee.spec.ServiceReferenceMetaData;
import org.jboss.metadata.javaee.spec.ServiceReferencesMetaData;
import org.jboss.metadata.javaee.support.AbstractMappedMetaData;
import org.jboss.metadata.javaee.support.IdMetaDataImplWithDescriptionGroup;
import org.jboss.metadata.sip.spec.LocaleEncodingsMetaData;
import org.jboss.metadata.sip.spec.SessionConfigMetaData;

/**
 * The sip-app spec metadata. This class is based on the CR12 Tag of the WebMetaData class
 * @author jean.deruelle@gmail.com
 */
public abstract class SipMetaData  extends IdMetaDataImplWithDescriptionGroup
   implements Environment
{
   private static final long serialVersionUID = 1;

   private String dtdPublicId;
   private String dtdSystemId;
   private String version;
   private String applicationName;
   private EmptyMetaData distributable;   
   private List<ListenerMetaData> listeners;
   private List<ParamValueMetaData> contextParams;
   private ServletSelectionMetaData servletSelection;   
   private ProxyConfigMetaData proxyConfig;
   private ServletsMetaData servlets;
   private SessionConfigMetaData sessionConfig;   
   private List<SipSecurityConstraintMetaData> sipSecurityContraints;
   private SipLoginConfigMetaData sipLoginConfig;           
   private SecurityRolesMetaData securityRoles;   
   private LocaleEncodingsMetaData localEncodings;      

   /** The environment */
   private EnvironmentRefsGroupMetaData jndiEnvironmentRefsGroup;

   /** The message destinations */
   private MessageDestinationsMetaData messageDestinations;

   private Method sipApplicationKeyMethod;
   /**
    * Callback for the DTD information
    * @param root
    * @param publicId
    * @param systemId
    */
   @XmlTransient
   public void setDTD(String root, String publicId, String systemId)
   {
      this.dtdPublicId = publicId;
      this.dtdSystemId = systemId;
   }
   /**
    * Get the DTD public id if one was seen
    * @return the value of the web.xml dtd public id
    */
   @XmlTransient
   public String getDtdPublicId()
   {
      return dtdPublicId;
   }
   /**
    * Get the DTD system id if one was seen
    * @return the value of the web.xml dtd system id
    */
   @XmlTransient
   public String getDtdSystemId()
   {
      return dtdSystemId;
   }

   public String getVersion()
   {
      return version;
   }
   @XmlAttribute
   public void setVersion(String version)
   {
      this.version = version;
   }

   /**
    * Is this a servlet 2.3 version application
    * @return true if this is a javaee 2.3 version application
    */
   @XmlTransient
   public boolean is10()
   {
      return dtdPublicId != null && dtdPublicId.equals("-//Java Community Process//DTD SIP Application 1.0//EN"); 
   }
   
   @XmlTransient
   public boolean is11()
   {
      return version != null && version.equals("1.1");
   }

   public EmptyMetaData getDistributable()
   {
      return distributable;
   }
   public void setDistributable(EmptyMetaData distributable)
   {
      this.distributable = distributable;
   }
   public SessionConfigMetaData getSessionConfig()
   {
      return sessionConfig;
   }
   public void setSessionConfig(SessionConfigMetaData sessionConfig)
   {
      this.sessionConfig = sessionConfig;
   }
   
   public List<ParamValueMetaData> getContextParams() {
	   return contextParams;
   }
   
   @XmlElement(name = "context-param")
   public void setContextParams(List<ParamValueMetaData> params) {
	   this.contextParams = params;
   }
   
   public List<ListenerMetaData> getListeners()
   {
      return listeners;
   }
   @XmlElement(name="listener")
   public void setListeners(List<ListenerMetaData> listeners)
   {
      this.listeners = listeners;
   }

   public LocaleEncodingsMetaData getLocalEncodings()
   {
      return localEncodings;
   }
   @XmlElement(name="locale-encoding-mapping-list")
   public void setLocalEncodings(LocaleEncodingsMetaData localEncodings)
   {
      this.localEncodings = localEncodings;
   }

   public SipLoginConfigMetaData getSipLoginConfig()
   {
      return sipLoginConfig;
   }
   @XmlElement(name="login-config")
   public void setSipLoginConfig(SipLoginConfigMetaData sipLoginConfig)
   {
      this.sipLoginConfig = sipLoginConfig;
   }         
   
   public List<SipSecurityConstraintMetaData> getSipSecurityContraints()
   {
      return sipSecurityContraints;
   }
   @XmlElement(name="security-constraint")
   public void setSipSecurityContraints(List<SipSecurityConstraintMetaData> sipSecurityContraints)
   {
      this.sipSecurityContraints = sipSecurityContraints;
   }

   public SecurityRolesMetaData getSecurityRoles()
   {
      return securityRoles;
   }
   @XmlElement(name="security-role")
   public void setSecurityRoles(SecurityRolesMetaData securityRoles)
   {
      this.securityRoles = securityRoles;
   }  

   public ServletsMetaData getServlets() {
	   return servlets;
   }
   @XmlElement(name="servlet")
   public void setServlets(ServletsMetaData sipServlets) {
	   this.servlets = sipServlets;
   }
   /**
    * Get the jndiEnvironmentRefsGroup.
    * 
    * @return the jndiEnvironmentRefsGroup.
    */
   public EnvironmentRefsGroupMetaData getJndiEnvironmentRefsGroup()
   {
      return jndiEnvironmentRefsGroup;
   }

   /**
    * Set the jndiEnvironmentRefsGroup.
    * 
    * @param jndiEnvironmentRefsGroup the jndiEnvironmentRefsGroup.
    * @throws IllegalArgumentException for a null jndiEnvironmentRefsGroup
    */
   public void setJndiEnvironmentRefsGroup(EnvironmentRefsGroupMetaData env)
   {
      if (env == null)
         throw new IllegalArgumentException("Null jndiEnvironmentRefsGroup");
      if(jndiEnvironmentRefsGroup != null)
         jndiEnvironmentRefsGroup.merge(env, null, "jboss-web.xml", "sip.xml", false);
      else
         this.jndiEnvironmentRefsGroup = env;
   }

   public EJBLocalReferenceMetaData getEjbLocalReferenceByName(String name)
   {
      return AbstractMappedMetaData.getByName(name, getEjbLocalReferences());
   }

   public EJBLocalReferencesMetaData getEjbLocalReferences()
   {
      if (jndiEnvironmentRefsGroup != null)
         return jndiEnvironmentRefsGroup.getEjbLocalReferences();
      return null;
   }

   public EJBReferenceMetaData getEjbReferenceByName(String name)
   {
      return AbstractMappedMetaData.getByName(name, getEjbReferences());
   }

   public EJBReferencesMetaData getEjbReferences()
   {
      if (jndiEnvironmentRefsGroup != null)
         return jndiEnvironmentRefsGroup.getEjbReferences();
      return null;
   }
   // TODO?
   @XmlTransient
   public AnnotatedEJBReferencesMetaData getAnnotatedEjbReferences()
   {
      AnnotatedEJBReferencesMetaData refs = null;
      if(jndiEnvironmentRefsGroup != null)
         refs = jndiEnvironmentRefsGroup.getAnnotatedEjbReferences();
      return refs;
   }

   public EnvironmentEntriesMetaData getEnvironmentEntries()
   {
      if (jndiEnvironmentRefsGroup != null)
         return jndiEnvironmentRefsGroup.getEnvironmentEntries();
      return null;
   }

   public EnvironmentEntryMetaData getEnvironmentEntryByName(String name)
   {
      return AbstractMappedMetaData.getByName(name, getEnvironmentEntries());
   }

   public MessageDestinationReferenceMetaData getMessageDestinationReferenceByName(String name)
   {
      return AbstractMappedMetaData.getByName(name, getMessageDestinationReferences());
   }

   public MessageDestinationReferencesMetaData getMessageDestinationReferences()
   {
      if (jndiEnvironmentRefsGroup != null)
         return jndiEnvironmentRefsGroup.getMessageDestinationReferences();
      return null;
   }

   public PersistenceContextReferenceMetaData getPersistenceContextReferenceByName(String name)
   {
      return AbstractMappedMetaData.getByName(name, getPersistenceContextRefs());
   }

   public PersistenceContextReferencesMetaData getPersistenceContextRefs()
   {
      if (jndiEnvironmentRefsGroup != null)
         return jndiEnvironmentRefsGroup.getPersistenceContextRefs();
      return null;
   }

   public PersistenceUnitReferenceMetaData getPersistenceUnitReferenceByName(String name)
   {
      return AbstractMappedMetaData.getByName(name, getPersistenceUnitRefs());
   }

   public PersistenceUnitReferencesMetaData getPersistenceUnitRefs()
   {
      if (jndiEnvironmentRefsGroup != null)
         return jndiEnvironmentRefsGroup.getPersistenceUnitRefs();
      return null;
   }

   public LifecycleCallbacksMetaData getPostConstructs()
   {
      if (jndiEnvironmentRefsGroup != null)
         return jndiEnvironmentRefsGroup.getPostConstructs();
      return null;
   }

   public LifecycleCallbacksMetaData getPreDestroys()
   {
      if (jndiEnvironmentRefsGroup != null)
         return jndiEnvironmentRefsGroup.getPreDestroys();
      return null;
   }

   public ResourceEnvironmentReferenceMetaData getResourceEnvironmentReferenceByName(String name)
   {
      return AbstractMappedMetaData.getByName(name, getResourceEnvironmentReferences());
   }

   public ResourceEnvironmentReferencesMetaData getResourceEnvironmentReferences()
   {
      if (jndiEnvironmentRefsGroup != null)
         return jndiEnvironmentRefsGroup.getResourceEnvironmentReferences();
      return null;
   }

   public ResourceReferenceMetaData getResourceReferenceByName(String name)
   {
      return AbstractMappedMetaData.getByName(name, getResourceReferences());
   }

   public ResourceReferencesMetaData getResourceReferences()
   {
      if (jndiEnvironmentRefsGroup != null)
         return jndiEnvironmentRefsGroup.getResourceReferences();
      return null;
   }

   public ServiceReferenceMetaData getServiceReferenceByName(String name)
   {
      return AbstractMappedMetaData.getByName(name, getServiceReferences());
   }

   public ServiceReferencesMetaData getServiceReferences()
   {
      if (jndiEnvironmentRefsGroup != null)
         return jndiEnvironmentRefsGroup.getServiceReferences();
      return null;
   }

   public MessageDestinationsMetaData getMessageDestinations()
   {
      return messageDestinations;
   }
   @XmlElement(name="message-destination")
   public void setMessageDestinations(MessageDestinationsMetaData messageDestinations)
   {
      this.messageDestinations = messageDestinations;
   }
/**
 * @param servletSelection the servletSelection to set
 */
public void setServletSelection(ServletSelectionMetaData servletSelection) {
	this.servletSelection = servletSelection;
}
/**
 * @return the servletSelection
 */
public ServletSelectionMetaData getServletSelection() {
	return servletSelection;
}
/**
 * @param proxyConfig the proxyConfig to set
 */
public void setProxyConfig(ProxyConfigMetaData proxyConfig) {
	this.proxyConfig = proxyConfig;
}
/**
 * @return the proxyConfig
 */
public ProxyConfigMetaData getProxyConfig() {
	return proxyConfig;
}
/**
 * @param applicationName the applicationName to set
 */
public void setApplicationName(String applicationName) {
	this.applicationName = applicationName;
}
/**
 * @return the applicationName
 */
@XmlElement(name="app-name")
public String getApplicationName() {
	return applicationName;
}
/**
 * @param sipApplicationKeyMethod the sipApplicationKeyMethod to set
 */
@XmlTransient
public void setSipApplicationKeyMethod(Method sipApplicationKeyMethod) {
	this.sipApplicationKeyMethod = sipApplicationKeyMethod;
}
/**
 * @return the sipApplicationKeyMethod
 */
@XmlTransient
public Method getSipApplicationKeyMethod() {
	return sipApplicationKeyMethod;
}
}
