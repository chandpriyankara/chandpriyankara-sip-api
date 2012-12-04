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

package org.mobicents.slee.container.component.validator.sbb.abstracts.cmp;

import java.io.Serializable;

import javax.naming.Context;
import javax.slee.ActivityContextInterface;
import javax.slee.EventContext;
import javax.slee.NoSuchObjectLocalException;
import javax.slee.RolledBackContext;
import javax.slee.SLEEException;
import javax.slee.SbbContext;
import javax.slee.SbbLocalObject;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.TransactionRolledbackLocalException;
import javax.slee.profile.ProfileLocalObject;

import org.mobicents.slee.container.component.validator.sbb.abstracts.aci.ACIConstraintsOk;
import org.mobicents.slee.container.component.validator.sbb.abstracts.aci.SimpleSeralizable;
import org.mobicents.slee.container.component.validator.sbb.abstracts.localinterface.SbbLocalInterfaceSlaveOne;

public abstract class SbbConstraintsSbbCMPsScopeSbb implements javax.slee.Sbb {

	/**
	 * Called when an sbb object is instantied and enters the pooled state.
	 */
	public void setSbbContext(SbbContext context) {

	}

	/*
	 * Init the connection and retrieve data when the service is activated by
	 * SLEE
	 */
	public void onServiceStartedEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {

	}

	public void unsetSbbContext() {

	}

	public void sbbCreate() throws javax.slee.CreateException {

	}

	public void sbbPostCreate() throws javax.slee.CreateException {

	}

	public void sbbActivate() {

	}

	public void sbbPassivate() {

	}

	public void sbbRemove() {

	}

	public void sbbLoad() {

	}

	public void sbbStore() {

	}

	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface activity) {

	}

	public void sbbRolledBack(RolledBackContext sbbRolledBack) {

	}

	protected SbbContext getSbbContext() {
		return null;
	}

	public void makeSomeStupidThing(String paramOne, int makeCounterParamTwo)
			throws NoSuchMethodException {
		// TODO Auto-generated method stub

	}

	public int makeSomeThingDifferentGood(Serializable ser, SbbLocalObject local)
			throws IllegalArgumentException, IllegalStateException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int makeSomeThingDifferent(Serializable ser, SbbLocalObject local)
			throws IllegalStateException {
		// TODO Auto-generated method stub

		return 0;
	}

	// CMPS -----------------------^

	 abstract int getSimpleIntegerType();

	public abstract void setSimpleIntegerType(int t);

	public abstract String getSimpleStringType();

	public abstract void setSimpleStringType(String t);

	public abstract SimpleSeralizable getSimpleSerializableType();

	public abstract void setSimpleSerializableType(SimpleSeralizable t);

	public abstract ActivityContextInterface getSimpleActivityContextInterfaceType();

	public abstract void setSimpleActivityContextInterfaceType(
			ActivityContextInterface t);

	public abstract ACIConstraintsOk getCustomActivityContextInterfaceType();

	public abstract void setCustomActivityContextInterfaceType(
			ACIConstraintsOk t);

	public abstract SbbLocalObject getGenericSBBLONoRef();

	public abstract void setGenericSBBLONoRef(SbbLocalObject t);

	public abstract SbbLocalObject getGenericSBBLOWithRef();

	public abstract void setGenericSBBLOWithRef(SbbLocalObject t);

	public abstract SbbLocalInterfaceSlaveOne getCustomSBBLOWithRef();

	public abstract void setCustomSBBLOWithRef(SbbLocalInterfaceSlaveOne t);

	public abstract EventContext getSimpleEventContext();

	public abstract void setSimpleEventContext(EventContext t);

	public abstract ProfileLocalObject getSimpleProfileLocalObject();

	public abstract void setSimpleProfileLocalObject(ProfileLocalObject p);

}