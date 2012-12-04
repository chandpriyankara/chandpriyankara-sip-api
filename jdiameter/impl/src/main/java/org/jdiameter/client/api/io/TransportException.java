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

package org.jdiameter.client.api.io;

/**
 * Signals that an transport exception has occurred in a during initialize
 * transport element.
 */
public class TransportException extends Exception{

    /**
     * Error code
     */
    TransportError code;

    /**
     * Create instance of class with predefined parameters
     * @param message error message
     * @param code error code
     */
    public TransportException(String message, TransportError code) {
        super(message);
        this.code = code;
    }

    /**
     * Create instance of class with predefined parameters
     * @param message error message
     * @param code error code
     * @param cause error cause
     */
    public TransportException(String message, TransportError code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * Create instance of class with predefined parameters
     * @param code error code
     * @param cause error cause
     */
    public TransportException(TransportError code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    /**
     * Return code of error
     * @return  code of error
     */
    public TransportError getCode() {
        return code;
    }
}
