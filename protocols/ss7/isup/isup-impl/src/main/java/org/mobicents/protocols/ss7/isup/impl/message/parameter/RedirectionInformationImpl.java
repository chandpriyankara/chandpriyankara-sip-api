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

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionInformation;

/**
 * Start time:15:18:18 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class RedirectionInformationImpl extends AbstractParameter implements RedirectionInformation{

	
	private int redirectingIndicator;
	private int originalRedirectionReason;
	private int redirectionCounter;
	private int redirectionReason;

	public RedirectionInformationImpl(byte[] b) throws IllegalArgumentException, ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}

	public RedirectionInformationImpl(int redirectingIndicator, int originalRedirectionReason, int redirectionCounter, int redirectionReason) throws IllegalArgumentException {
		super();
		this.setRedirectingIndicator(redirectingIndicator);
		this.setOriginalRedirectionReason(originalRedirectionReason);
		this.setRedirectionCounter(redirectionCounter);
		this.setRedirectionReason(redirectionReason);
	}
	public RedirectionInformationImpl() {
		super();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		if (b == null || b.length != 2) {
			throw new ParameterRangeInvalidException("byte[] must  not be null and length must  be 2");
		}
		try {
			this.setRedirectingIndicator((b[0] & 0x07));
			this.setOriginalRedirectionReason(((b[0] >> 4) & 0x0F));
			this.setRedirectionCounter((b[1] & 0x07));
			this.setRedirectionReason(((b[1] >> 4) & 0x0F));
		} catch (Exception e) {
			throw new ParameterRangeInvalidException(e);
		}
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		int b0 = redirectingIndicator & 0x07;
		b0 |= (this.originalRedirectionReason & 0x0F) << 4;

		int b1 = redirectionCounter & 0x07;
		b1 |= (this.redirectionReason & 0x0F) << 4;
		return new byte[] { (byte) b0, (byte) b1 };
	}

	public int getRedirectingIndicator() {
		return redirectingIndicator;
	}

	public void setRedirectingIndicator(int redirectingIndicator) {
		this.redirectingIndicator = redirectingIndicator & 0x07;
	}

	public int getOriginalRedirectionReason() {
		return originalRedirectionReason;
	}

	public void setOriginalRedirectionReason(int originalRedirectionReason) {
		this.originalRedirectionReason = originalRedirectionReason & 0x0F;
	}

	public int getRedirectionCounter() {
		return redirectionCounter;
	}

	public void setRedirectionCounter(int redirectionCounter) throws IllegalArgumentException {
		if (redirectionCounter < 1 || redirectionCounter > 5) {
			throw new IllegalArgumentException("Out of range - must be between 1 and 5");
		}
		this.redirectionCounter = redirectionCounter & 0x07;
	}

	public int getRedirectionReason() {
		return redirectionReason;
	}

	public void setRedirectionReason(int redirectionReason) {
		this.redirectionReason = redirectionReason & 0x0F;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
