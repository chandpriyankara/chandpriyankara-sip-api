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

package org.jdiameter.api;

import java.util.List;

/**
 * Stack properties for working.
 * This interface equals IMemento interface from Eclispe (pattern Memento).
 * It interface hideWay of a storage of stack properties (XML file ant etc)
 * @version 1.5.1 Final
 */

public interface Configuration {

    /**
     * Returns the Byte point value of the given key.
     *
     * @param key the key
     * @param defaultValue the Default Value
     * @return the value, or <code>defValue</code> if the key was not found or was found
     *   but was not a Byte point number
     */
    byte getByteValue(int key, byte defaultValue);

    /**
     * Returns the Ineteger point value of the given key.
     *
     * @param key the key
     * @param defaultValue the Default Value
     * @return the value, or <code>defaultValue</code> if the key was not found or was found
     *   but was not a Ineteger point number
     */
    int getIntValue(int key, int defaultValue);

    /**
     * Returns the long point value of the given key.
     *
     * @param key the key
     * @param defaultValue the Default Value
     * @return the value, or <code>defaultValue</code> if the key was not found or was found
     *   but was not a long point number
     */
    long getLongValue(int key, long defaultValue);

    /**
     * Returns the double point value of the given key.
     *
     * @param key the key
     * @param defaultValue the Default Value
     * @return the value, or <code>defaultValue</code> if the key was not found or was found
     *   but was not a double point number
     */
    double getDoubleValue(int key, double defaultValue);

    /**
     * Returns the byte[] point value of the given key.
     *
     * @param key the key
     * @param defaultValue the Default Value
     * @return the value, or <code>defaultValue</code> if the key was not found or was found
     *   but was not a byte[] point number
     */
    byte[] getByteArrayValue(int key, byte[] defaultValue);

    /**
     * Returns the boolean point value of the given key.
     *
     * @param key the key
     * @param defaultValue the Default Value
     * @return the value, or <code>defaultValue</code> if the key was not found or was found
     *   but was not a boolean point number
     */
    boolean getBooleanValue(int key, boolean defaultValue);

    /**
     * Returns the String point value of the given key.
     *
     * @param key the key
     * @param defaultValue the Default Value
     * @return the value, or <code>defaultValue</code> if the key was not found or was found
     *   but was not a String point number
     */
    String getStringValue(int key, String defaultValue);

    /**
     * @param key key of attribute
     * @return true if value of parameter is not null
     */
    boolean isAttributeExist(int key);

    /**
     * Returns all children with the given type id.
     *
     * @param key the type id
     * @return an array of children with the given type
     */
    Configuration[] getChildren(int key);
}
