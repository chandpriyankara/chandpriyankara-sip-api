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

package org.mobicents.media;

import org.mobicents.media.server.spi.clock.Task;

/**
 * Abstracts a read interface that pushes data in the form of Buffer objects.
 *  
 * This interface allows a source stream to transfer data in the form of an 
 * entire media chunk to the user of this source stream.
 * 
 * @author Oleg Kulikov
 * @author baranowb
 */
public interface MediaSource extends Component, Task {
    
    /**
     * Starts media processing.
     */
    public void start();
    
    /**
     * Terminates media processing.
     */
    public void stop();
    
    /**
     * Gets the current media time position.
     * 
     * @return the current media time position in milliseconds.
     */
    public long getMediaTime();
    
    /**
     * Assigns media time from which this component should start media data.
     * 
     * @param time the value measured in millesconds.
     */
    public void setMediaTime(long time);
    
    /**
     * Returns duration of the signal generated by this component if 
     * it is known.
     * 
     * @return the duration of the signal in milliseconds or -1 if not known.
     */
    public long getDuration();
    
    /**
     * Sets maximum duration for the signal generated by media source
     * 
     * @param duration the positive value in milliseconds.
     */
    public void setDuration(long duration);
    
    /**
     * Joins this source with media sink.
     * 
     * @param sink the media sink to join with.
     */
    public void connect(MediaSink sink);
    
    /**
     * Drops connection between this source and media sink.
     * 
     * @param sink the sink to disconnect.
     */
    public void disconnect(MediaSink sink);
    
    /**
     * Flags which indicates that multiple connections to this sink are allowed.
     * 
     * @return true if multiple connections are allowed
     */
    public boolean isMultipleConnectionsAllowed();

    /**
     * Connects this source with specified inlet.
     * 
     * @param inlet the inlet to connect with.
     */
    public void connect(Inlet inlet);
    
    /**
     * Disconnects this source with specified inlet.
     * 
     * @param inlet the inlet to disconnect from.
     */
    public void disconnect(Inlet inlet);
    
    /**
     * Get possible formats in which this source can stream media.
     * 
     * @return an array of Format objects.
     */
    public Format[] getFormats();    
    
    /**
     * Gets the state of the component.
     * 
     * @return  true if component is connected to other component.
     */
    public boolean isConnected();
    
    /**
     * Gets true if component is transmitting media.
     * 
     * @return true if component is transmitting media.
     */
    public boolean isStarted();
    
    /**
     * Shows the number of packets received by this medis sink since last start.
     * 
     * @return the number of packets.
     */
    public long getPacketsTransmitted();
    
    /**
     * Shows the number of bytes received by this sink since last start;
     * 
     * @return the number of bytes.
     */
    public long getBytesTransmitted();

    
}
