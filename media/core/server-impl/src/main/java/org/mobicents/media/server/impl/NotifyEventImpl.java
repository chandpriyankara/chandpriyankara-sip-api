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

package org.mobicents.media.server.impl;

import org.mobicents.media.Component;
import org.mobicents.media.server.spi.events.NotifyEvent;

/**
 * Implementation for standard events.
 * 
 * @author kulikov
 */
public class NotifyEventImpl implements NotifyEvent {
    
    private BaseComponent component;
    private int eventID;
    private String desc;
    
    public NotifyEventImpl(BaseComponent component, int eventID) {
        this.component = component;
        this.eventID = eventID;
    }
    
    public NotifyEventImpl(BaseComponent component, int eventID, String desc) {
		super();
		this.component = component;
		this.eventID = eventID;
		this.desc = desc;
	}

	public int getEventID() {
        return eventID;
    }

    public Component getSource() {
        return component;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NotifyEventImpl [component=" + component + ", desc=" + desc
				+ ", eventID=" + eventID + "]";
	}

}
