 /*
  * Mobicents: The Open Source SLEE Platform      
  *
  * Copyright 2003-2005, CocoonHive, LLC., 
  * and individual contributors as indicated
  * by the @authors tag. See the copyright.txt 
  * in the distribution for a full listing of   
  * individual contributors.
  *
  * This is free software; you can redistribute it
  * and/or modify it under the terms of the 
  * GNU Lesser General Public License as
  * published by the Free Software Foundation; 
  * either version 2.1 of
  * the License, or (at your option) any later version.
  *
  * This software is distributed in the hope that 
  * it will be useful, but WITHOUT ANY WARRANTY; 
  * without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
  * PURPOSE. See the GNU Lesser General Public License
  * for more details.
  *
  * You should have received a copy of the 
  * GNU Lesser General Public
  * License along with this software; 
  * if not, write to the Free
  * Software Foundation, Inc., 51 Franklin St, 
  * Fifth Floor, Boston, MA
  * 02110-1301 USA, or see the FSF site:
  * http://www.fsf.org.
  */

package org.mobicents.slee.util;

import org.apache.log4j.Logger;

public class DefaultStateCallback implements StateCallback {
	private String state;
	private transient Logger log = Logger.getLogger(DefaultStateCallback.class);
	public void setSessionState(String state) {
		this.state = state;
		if ( log!=null && log.isDebugEnabled() ) {
			log.debug("setSessionState : " + state);
		}
	}

	public String getSessionState() {
		if ( log!=null && log.isDebugEnabled() ) {
			log.debug("getSessionState : " + state);
		}
		return state;
	}
}
