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

package org.mobicents.slee.resource.diameter;

import java.util.concurrent.ConcurrentHashMap;

import net.java.slee.resource.diameter.base.DiameterActivity;

import org.mobicents.slee.resource.diameter.base.DiameterActivityHandle;

/**
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class LocalDiameterActivityManagement implements DiameterActivityManagement{

  private ConcurrentHashMap<DiameterActivityHandle,DiameterActivity> activities = new ConcurrentHashMap<DiameterActivityHandle, DiameterActivity>();

  public DiameterActivity get(DiameterActivityHandle handle) {
    return activities.get(handle);
  }

  public void put(DiameterActivityHandle handle, DiameterActivity activity) {
    DiameterActivity existingActivity = this.activities.putIfAbsent(handle, activity);
    if(existingActivity != null) {
      throw new IllegalArgumentException("There is already activity for '" + handle + "': " + existingActivity);
    }
  }

  public DiameterActivity remove(DiameterActivityHandle handle) {
    return this.activities.remove(handle);
  }

  public boolean containsKey(DiameterActivityHandle activityHandle) {
    return this.activities.containsKey(activityHandle);
  }

  public void update(DiameterActivityHandle handle, DiameterActivity activity) {
    //here we don't do a thing
  }

}
