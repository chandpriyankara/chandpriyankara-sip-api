package org.mobicents.media.server.impl.resource.video;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * 
 * @author amit bhayani
 * 
 */
public class ESDBox extends FullBox {

	// File Type = mp4a
	static byte[] TYPE = new byte[] { AsciiTable.ALPHA_e, AsciiTable.ALPHA_s, AsciiTable.ALPHA_d, AsciiTable.ALPHA_s };
	static String TYPE_S = "mp4a";
	static {
		bytetoTypeMap.put(TYPE, TYPE_S);
	}

	public ESDBox(long size) {
		super(size, TYPE_S);
	}

	@Override
	protected int load(DataInputStream fin) throws IOException {
		int count = 8;
		count += super.load(fin);
		
		//TODO : How to parse this?
		fin.skip((this.getSize() - count));
		
		return (int) this.getSize();
	}
}
