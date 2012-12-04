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

package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayInputStream;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginalCalledINNumber;

/**
 * Start time:18:36:26 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class OriginalCalledINNumberImpl extends CalledNumberImpl implements OriginalCalledINNumber {

	public OriginalCalledINNumberImpl(byte[] representation) throws ParameterRangeInvalidException {
		super(representation);
		
	}

	public OriginalCalledINNumberImpl(ByteArrayInputStream bis) throws ParameterRangeInvalidException {
		super(bis);
		
	}

	public OriginalCalledINNumberImpl(int natureOfAddresIndicator, String address, int numberingPlanIndicator, int addressRepresentationREstrictedIndicator) {
		super(natureOfAddresIndicator, address, numberingPlanIndicator, addressRepresentationREstrictedIndicator);
		
	}

	public OriginalCalledINNumberImpl() {
		super();
		
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
