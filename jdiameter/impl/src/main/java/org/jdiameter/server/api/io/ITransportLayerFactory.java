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

package org.jdiameter.server.api.io;

import org.jdiameter.client.api.io.TransportException;

import java.net.InetAddress;

/**
 * Factory of Network Layer elements. This interface append to parent interface
 * additional method for creating INetWorkGuard guard instances.
 * Additional parameters (Configuration, Parsers and etc) injection to instance over constructor
 */
public interface ITransportLayerFactory extends org.jdiameter.client.api.io.ITransportLayerFactory {

    /**
     * Create INetworkGuard instance with predefined parameters
     * 
     * @param inetAddress address of server socket
     * @param port  port of server socket
     * @return INetWorkGuard instance
     * @throws TransportException
     */
    INetworkGuard createNetworkGuard(InetAddress inetAddress, int port) throws TransportException;

    /**
     * Create INetworkGuard instance with predefined parameters
     * 
     * @param inetAddress address of server socket
     * @param port  port of server socket
     * @param listener event listener
     * @return INetWorkGuard instance
     * @throws TransportException
     */    
    INetworkGuard createNetworkGuard(InetAddress inetAddress, int port, INetworkConnectionListener listener) throws TransportException;

}
