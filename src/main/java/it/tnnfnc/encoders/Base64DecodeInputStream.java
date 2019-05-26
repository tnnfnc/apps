/*
 * Copyright (c) 2015, Franco Toninato. All rights reserved.
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * THERE IS NO WARRANTY FOR THE PROGRAM, TO THE EXTENT PERMITTED BY APPLICABLE LAW. 
 * EXCEPT WHEN OTHERWISE STATED IN WRITING THE COPYRIGHT HOLDERS AND/OR OTHER 
 * PARTIES PROVIDE THE PROGRAM �AS IS� WITHOUT WARRANTY OF ANY KIND, EITHER 
 * EXPRESSED OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE ENTIRE RISK AS TO THE 
 * QUALITY AND PERFORMANCE OF THE PROGRAM IS WITH YOU. SHOULD THE PROGRAM PROVE 
 * DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING, REPAIR OR CORRECTION.
 */
package it.tnnfnc.encoders;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * This class reads an encoded input to a decoded stream. Internal buffer of 16
 * bytes.
 * 
 * @author franco toninato
 * 
 */
public class Base64DecodeInputStream extends FilterInputStream {
	private byte buffer[] = new byte[16];
	private byte outBuffer[] = new byte[16];
	private int outOff = outBuffer.length;

	public Base64DecodeInputStream(InputStream in) {
		super(in);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterInputStream#read()
	 */
	@Override
	public int read() throws IOException {
		byte b[] = new byte[1];
		return read(b, 0, b.length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterInputStream#read(byte[], int, int)
	 */
	@Override
	public int read(byte[] b, int off, int len) throws IOException {

		int left = len;
		// Write processed bytes from the buffer until empty.
		int i = 0;
		for (; outOff < outBuffer.length && (left > 0); i++) {
			b[off + i] = outBuffer[outOff++];
			left--;
		}

		// Read from the stream and process into the buffer
		while (left > 0) {
			// fill a buffer
			int j = in.read(buffer);
			if (j > 0) {
				outOff = 0;
				outBuffer = Base64.decodeStream(buffer);
				for (; i < len && outOff < outBuffer.length; i++) {
					b[off + i] = outBuffer[outOff++];
					left--;
				}
			}
			// EOF when processing bytes
			else if (j < 0 && i > 0) {
				break;
			}
			// EOF
			else {
				i = j;
				break;
			}
		}
		return i;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterInputStream#read(byte[])
	 */
	@Override
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}

	/* (non-Javadoc)
	 * @see java.io.FilterInputStream#close()
	 */
	@Override
	public void close() throws IOException {
		super.close();
	}
}
