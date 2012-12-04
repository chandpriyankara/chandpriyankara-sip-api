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

package org.mobicents.media.server.spi.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import org.mobicents.media.MediaSink;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface Recorder extends MediaSink {

	/**
	 * Set the Record path. This will be the parent path and file path passed in
	 * start(String file) will be appended to this base record path. For example
	 * if recordDir = "/home/user/recordedfiles" (for Win OS c:/recordedfiles),
	 * then calling start("myapp/recordedFile.wav") will create recorded file
	 * /home/user/recordedfiles/myapp/recordedFile.wav (for win OS
	 * c:/recordedfiles/myapp/recordedFile.wav)
	 * 
	 * @param recordDir
	 */
	public void setRecordDir(String recordDir);

	/**
	 * Assign file for recording.
	 * 
	 * @param uri
	 *            the URI which points to a file.
	 * @throws java.io.IOException
	 * @throws java.io.FileNotFoundException
	 */
	public void setRecordFile(String uri) throws IOException;
}
