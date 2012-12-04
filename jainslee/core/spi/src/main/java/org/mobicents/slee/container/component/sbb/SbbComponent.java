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

/**
 * 
 */
package org.mobicents.slee.container.component.sbb;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.facilities.AlarmFacility;

import org.mobicents.slee.container.component.SleeComponentWithUsageParametersInterface;

/**
 * 
 * @author martins
 *
 */
public interface SbbComponent extends SleeComponentWithUsageParametersInterface {

	/**
	 * Retrieves the component's descriptor.
	 * @return
	 */
	public SbbDescriptor getDescriptor();
	
	/**
	 * Retrieves the sbb id
	 * 
	 * @return
	 */
	public SbbID getSbbID();

	/**
	 * Retrieves the sbb abstract class
	 * 
	 * @return
	 */
	public Class<?> getAbstractSbbClass();

	/**
	 * Retrieves the concrete sbb class, generated by SLEE
	 * 
	 * @return
	 */
	public Class<?> getConcreteSbbClass();

	/**
	 * This must never return null, if no custom interface is defined, this has
	 * to return generic javax.slee.SbbLocalObject FIXME emmartins: this should
	 * return null, since in runtime it will avoid some instanceof for sure
	 * 
	 * @return
	 */
	public Class<?> getSbbLocalInterfaceClass();

	/**
	 * Retrieves the concrete sbb local interface class, generated by SLEE
	 * 
	 * @return
	 */
	public Class<?> getSbbLocalInterfaceConcreteClass();

	/**
	 * Retrieves the sbb own activity context interface
	 * 
	 * @return
	 */
	public Class<?> getActivityContextInterface();

	/**
	 * Retrieves the concrete sbb own activity context interface class,
	 * generated by SLEE
	 * 
	 * @return
	 */
	public Class<?> getActivityContextInterfaceConcreteClass();

	/**
	 * Sets the sbb abstract class
	 * 
	 * @param abstractSbbClass
	 */
	public void setAbstractSbbClass(Class<?> abstractSbbClass);

	/**
	 * Sets the concrete sbb class, generated by SLEE
	 * 
	 * @param concreteSbbClass
	 */
	public void setConcreteSbbClass(Class<?> concreteSbbClass);

	/**
	 * Sets the sbb local interface
	 * 
	 * @param sbbLocalInterfaceClass
	 */
	public void setSbbLocalInterfaceClass(Class<?> sbbLocalInterfaceClass);

	/**
	 * Sets the concrete sbb local interface class, generated by SLEE
	 * 
	 * @param sbbLocalInterfaceConcreteClass
	 */
	public void setSbbLocalInterfaceConcreteClass(Class<?> sbbLocalInterfaceConcreteClass);

	/**
	 * Sets the sbb own activity context interface
	 * 
	 * @param activityContextInterface
	 */
	public void setActivityContextInterface(Class<?> activityContextInterface) ;

	/**
	 * Sets the concrete sbb own activity context interface class, generated by
	 * SLEE
	 * 
	 * @param activityContextInterfaceConcreteClass
	 */
	public void setActivityContextInterfaceConcreteClass(Class<?> activityContextInterfaceConcreteClass);

	/**
	 * Retrieves the JAIN SLEE specs descriptor
	 * 
	 * @return
	 */
	public javax.slee.management.SbbDescriptor getSpecsDescriptor();

	/**
	 * Retrieves the evetn handler methods for this sbb component, mapped by
	 * event type id
	 * 
	 * @return
	 */
	public Map<EventTypeID, EventHandlerMethod> getEventHandlerMethods();
	
	/**
	 * Retrieves the evetn handler methods for this sbb component, mapped by
	 * event type id
	 * 
	 * @return
	 */
	public Map<String, Method> getInitialEventSelectorMethods();
	
	/**
	 *  
	 * @return the abstractSbbClassInfo
	 */
	public AbstractSbbClassInfo getAbstractSbbClassInfo();
	
	/**
	 * Sbb event handler method wrapper to deliver an event to the sbb
	 * component.
	 * 
	 * @author martins
	 * 
	 */
	public static class EventHandlerMethod {

		private final Method eventHandlerMethod;
		private boolean hasCustomACIParam;
		private boolean hasEventContextParam;

		public EventHandlerMethod(Method eventHandlerMethod) {
			this.eventHandlerMethod = eventHandlerMethod;
		}

		public Method getEventHandlerMethod() {
			return eventHandlerMethod;
		}

		public boolean getHasCustomACIParam() {
			return hasCustomACIParam;
		}

		public void setHasCustomACIParam(boolean hasCustomACIParam) {
			this.hasCustomACIParam = hasCustomACIParam;
		}

		public boolean getHasEventContextParam() {
			return hasEventContextParam;
		}

		public void setHasEventContextParam(boolean hasEventContextParam) {
			this.hasEventContextParam = hasEventContextParam;
		}
	}

	/**
	 * Provides a shortcut to the value of the isolate security permissions
	 * property of the sbb local interface in the sbb descriptor.
	 * 
	 * @return 
	 */
	public boolean isolateSecurityPermissionsInLocalInterface();
	
	/**
	 * Retrieves the constructor for the SLEE generated implementation class of the custom sbb local object interface.
	 * @return 
	 */
	public Constructor<?> getSbbLocalObjectClassConstructor();
	
	/**
	 * Sets the constructor for the SLEE generated implementation class of the custom sbb local object interface.
	 * @param c
	 */
	public void setSbbLocalObjectClassConstructor(
			Constructor<?> c);
	
	/**
	 * 
	 * @return true if the sbb component descriptor defines this sbb as reentrant
	 */
	public boolean isReentrant();
	
	/**
	 * Retrieves the alarm facility bound to the sbb component
	 * @return
	 */
	public AlarmFacility getAlarmFacility();
	
	/**
	 * 
	 * @param alarmFacility
	 */
	public void setAlarmFacility(AlarmFacility alarmFacility);
}
