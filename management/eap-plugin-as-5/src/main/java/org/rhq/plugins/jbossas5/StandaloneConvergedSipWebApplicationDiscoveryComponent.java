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

package org.rhq.plugins.jbossas5;

import java.util.Map.Entry;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.deployers.spi.management.KnownDeploymentTypes;
import org.jboss.deployers.spi.management.ManagementView;
import org.jboss.managed.api.ManagedComponent;
import org.jboss.managed.api.ManagedDeployment;
import org.jboss.managed.api.ManagedObject;
import org.jboss.profileservice.spi.NoSuchDeploymentException;
import org.jetbrains.annotations.Nullable;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.resource.ResourceType;
import org.rhq.core.pluginapi.inventory.DiscoveredResourceDetails;
import org.rhq.core.pluginapi.inventory.ResourceDiscoveryContext;
import org.rhq.plugins.jbossas5.util.ConversionUtils;
import org.rhq.plugins.jbossas5.util.ManagedComponentUtils;

/**
 * @author Ian Springer
 */
public class StandaloneConvergedSipWebApplicationDiscoveryComponent extends StandaloneManagedDeploymentDiscoveryComponent {
	
	private final Log log = LogFactory.getLog(this.getClass());
	private static final String CONTEXT_COMPONENT_NAME = "ContextMO";
	
	@Override
	public Set<DiscoveredResourceDetails> discoverResources(
			ResourceDiscoveryContext<ProfileServiceComponent> discoveryContext) throws Exception {
		
		Set<DiscoveredResourceDetails> discoveredResources = new HashSet<DiscoveredResourceDetails>();
        ResourceType resourceType = discoveryContext.getResourceType();
        log.trace("Discovering " + resourceType.getName() + " Resources...");
        KnownDeploymentTypes deploymentType = ConversionUtils.getDeploymentType(resourceType);
        String deploymentTypeString = deploymentType.getType();

        ManagementView managementView = discoveryContext.getParentResourceComponent().getConnection().getManagementView();
        // TODO (ips): Only refresh the ManagementView *once* per runtime discovery scan, rather than every time this
        //             method is called. Do this by providing a runtime scan id in the ResourceDiscoveryContext.        
        managementView.load();

        Set<String> deploymentNames = null;
        try
        {
            deploymentNames = managementView.getDeploymentNamesForType(deploymentTypeString);
        }
        catch (Exception e)
        {
            log.error("Unable to get deployment for type " + deploymentTypeString, e);
        }

        discoveredResources = new HashSet<DiscoveredResourceDetails>(deploymentNames.size());
        Configuration pluginConfig = discoveryContext.getDefaultPluginConfiguration();
        
        /* Create a resource for each managed component found. We know all managed components will be of a
           type we're interested in, so we can just add them all. There may be need for multiple iterations
           over lists retrieved from different component types, but that is possible through the current API.
        */
        for (String deploymentName : deploymentNames)
        {
            try
            {
                ManagedDeployment managedDeployment = managementView.getDeployment(deploymentName);
                if (!accept(managedDeployment))
                    continue;
                String resourceName = managedDeployment.getSimpleName();
                // @TODO remove this when AS5 actually implements this for sars, and some other DeploymentTypes that haven't implemented getSimpleName()
                if (resourceName.equals("%Generated%"))
                {
                    resourceName = getResourceName(deploymentName);
                }
                String version = null; // TODO
                DiscoveredResourceDetails resource =
                        new DiscoveredResourceDetails(resourceType,
                                deploymentName,
                                resourceName,
                                version,
                                resourceType.getDescription(),
                                discoveryContext.getDefaultPluginConfiguration(),
                                null);
                // example of a deployment name: vfszip:/C:/opt/jboss-5.1.0.CR1/server/default/deploy/foo.war
                resource.getPluginConfiguration().put(
                        new PropertySimple(AbstractManagedDeploymentComponent.DEPLOYMENT_NAME_PROPERTY, deploymentName));
                String contextPath = getContextPath(managedDeployment);
                log.debug("StandaloneConvergedSipWebApplicationDiscoveryComponent contextPath " + contextPath);
                Set<ManagedComponent> webApplicationComponents = ConvergedSipWebApplicationContextDiscoveryComponent.getWebApplicationComponents(contextPath, managementView);
	              log.debug("StandaloneConvergedSipWebApplicationDiscoveryComponent webAppComponents " + webApplicationComponents.size());
	              for (ManagedComponent webApplicationComponent : webApplicationComponents)
	              {
	                  String virtualHost = ConvergedSipWebApplicationContextDiscoveryComponent.getWebApplicationComponentVirtualHost(webApplicationComponent);
	      
	                  // Make sure to set the "virtualHost" and "contextPath" props before setting the "componentName" props,
	                  // since those two props are referenced in the template for the "componentName" property.
	                  pluginConfig.put(new PropertySimple(WebApplicationContextComponent.VIRTUAL_HOST_PROPERTY, virtualHost));
	                  pluginConfig.put(new PropertySimple(WebApplicationContextComponent.CONTEXT_PATH_PROPERTY, contextPath));
	                  resource.getPluginConfiguration().put(
	                		  new PropertySimple(WebApplicationContextComponent.VIRTUAL_HOST_PROPERTY, virtualHost));
	                  resource.getPluginConfiguration().put(
	                		  new PropertySimple(WebApplicationContextComponent.CONTEXT_PATH_PROPERTY, contextPath));
	                  
	                  log.debug("StandaloneConvergedSipWebApplicationDiscoveryComponent vhost " + virtualHost + " contextPath " + contextPath);
	                  
	              }
                discoveredResources.add(resource);
            }
            catch (NoSuchDeploymentException e)
            {
                // This is a bug in the profile service that occurs often, so don't log the stack trace.
                log.error("ManagementView.getDeploymentNamesForType() returned [" + deploymentName
                        + "] as a deployment name, but calling getDeployment() with that name failed.");
            }
            catch (Exception e)
            {
                log.error("An error occurred while discovering " + resourceType + " Resources.", e);
            }
        }

        log.trace("Discovered " + discoveredResources.size() + " " + resourceType.getName() + " Resources.");
        return discoveredResources;
	}
	
	 /**
     * Returns the parent WAR's context path (e.g. "/jmx-console"), or <code>null</code> if the WAR is currently
     * stopped, since stopped WARs are not associated with any contexts.
     *
     * @return this WAR's context path (e.g. "/jmx-console"), or <code>null</code> if the WAR is currently stopped,
     *         since stopped WARs are not associated with any contexts
     * @throws NoSuchDeploymentException if the WAR is no longer deployed
     */
    @Nullable
    private String getContextPath(ManagedDeployment deployment)
            throws NoSuchDeploymentException
    {
        ManagedComponent contextComponent = deployment.getComponent(CONTEXT_COMPONENT_NAME);
        // e.g. "/jmx-console"
        if (contextComponent != null)
        {
            return (String)ManagedComponentUtils.getSimplePropertyValue(contextComponent, "contextRoot");
        }
        else
        {
            return null;
        }
    }
	
	@Override
    protected boolean accept(ManagedDeployment managedDeployment) {
    	boolean accept = super.accept(managedDeployment);
    	if(!accept) {
    		return accept;
    	}
    	if(log.isDebugEnabled()) {
    		getInfo(managedDeployment);
    	}
    	if(managedDeployment.getManagedObjectNames().contains("SipApplicationNameMO")) {
    		accept = true;
    	} else {
    		accept = false;
    	}
    	
        return accept;
    }

	private void getInfo(ManagedDeployment managedDeployment) {
		log.debug("managed deployment "  + managedDeployment.getName());
    	for(Entry<String, ManagedComponent> entry : managedDeployment.getComponents().entrySet()) {
    		log.debug("component key " + entry.getKey() + " name " + entry.getValue().toString());
    	}
    	for(String objectName : managedDeployment.getManagedObjectNames()) {
    		log.debug("managed object Name " + objectName);
    	}
    	for(Entry<String, ManagedObject> entry : managedDeployment.getManagedObjects().entrySet()) {
    		log.debug("managed Object key " + entry.getKey() + " name " + entry.getValue().toString());
    	}    	
	}
}
