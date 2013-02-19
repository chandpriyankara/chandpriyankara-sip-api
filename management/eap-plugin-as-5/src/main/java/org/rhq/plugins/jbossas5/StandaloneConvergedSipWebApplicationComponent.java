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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mc4j.ems.connection.EmsConnection;
import org.mc4j.ems.connection.bean.EmsBean;
import org.mc4j.ems.connection.bean.operation.EmsOperation;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.pluginapi.inventory.ResourceContext;
import org.rhq.core.pluginapi.operation.OperationResult;
import org.rhq.plugins.jbossas5.util.ResourceComponentUtils;
import org.rhq.plugins.jmx.ObjectNameQueryUtility;

/**
 * A resource component for managing a standalone/top-level Profile Service
 * managed deployment.
 * 
 * @author Ian Springer
 */
public class StandaloneConvergedSipWebApplicationComponent extends
		StandaloneManagedDeploymentComponent {
	private final Log log = LogFactory.getLog(this.getClass());

	private static final String CONTEXT_NAME_TEMPLATE = "jboss.web:J2EEApplication=none,J2EEServer=none,j2eeType=WebModule,name=//%"
			+ WebApplicationContextComponent.VIRTUAL_HOST_PROPERTY + "%%" + WebApplicationContextComponent.CONTEXT_PATH_PROPERTY + "%";

	private String contextComponentNamesRegex;

	@Override
	public OperationResult invokeOperation(String name, Configuration parameters)
			throws InterruptedException, Exception {

		Configuration pluginConfig = getResourceContext()
				.getPluginConfiguration();
		this.contextComponentNamesRegex = ResourceComponentUtils
				.replacePropertyExpressionsInTemplate(CONTEXT_NAME_TEMPLATE,
						pluginConfig); 
				
		log.debug("Operation '" + name + "' on " + contextComponentNamesRegex);
		if (name.equals("stopGracefully")) {
			long timeToWait = parameters.getSimple("timeToWait").getLongValue();
			log.debug("Operation '" + name + "' on "
					+ contextComponentNamesRegex + " with timeToWait "
					+ timeToWait);

			ProfileServiceComponent warComponent = getResourceContext()
					.getParentResourceComponent();
			EmsConnection jmxConnection = warComponent.getEmsConnection();

			ObjectNameQueryUtility queryUtility = new ObjectNameQueryUtility(
					contextComponentNamesRegex);
			List<EmsBean> mBeans = jmxConnection.queryBeans(queryUtility
					.getTranslatedQuery());

			log.debug("Operation '" + name + "' on "
					+ contextComponentNamesRegex + " with timeToWait "
					+ timeToWait + " found " + mBeans.size() + "mbeans");

			for (EmsBean mBean : mBeans) {
				EmsOperation operation = mBean.getOperation("stopGracefully",
						long.class);
				Object result = operation.invoke(timeToWait);
				log.debug("Operation '" + name + "' on "
						+ getResourceDescription() + " completed with status ["
						+ result + "].");
			}

			return new OperationResult();
		} else {
			return super.invokeOperation(name, parameters);
		}
	}
}
