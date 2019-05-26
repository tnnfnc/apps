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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * This class writes an encoded output to a stream. Internal buffer of 24 bytes.
 * 
 * @author franco toninato
 * 
 */
public class Base64EncodeOutputStream extends FilterOutputStream {

	private byte buffer[] = new byte[24];
	// Length of the unprocessed byte accumulated in the buffer
	private int buffLen = 0;

	public Base64EncodeOutputStream(OutputStream out) {
		super(out);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterOutputStream#close()
	 */
	@Override
	public void close() throws IOException {
		if (buffLen > 0)
			out.write(Base64.encode(buffer, 0, buffLen));
		buffLen = 0;
		super.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterOutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		// write the buffer
		// Test OK

		int j = 0;
		while (j < len) {
			// Fill the buffer completely
			for (; (buffLen < buffer.length) && (j < len); buffLen++) {
				buffer[buffLen] = b[off + j++];
			}

			// When filled process it!
			if (buffLen == buffer.length) {
				out.write(Base64.encodeStream(buffer));
				buffLen = 0;
			}
			// Else accumulated in the buffer
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterOutputStream#write(byte[])
	 */
	@Override
	public void write(byte[] b) throws IOException {
		write(b, 0, b.length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterOutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException {
		// Test OK
		if (buffLen < buffer.length) {
			buffer[buffLen] = (byte) b;
			buffLen++;
		} else {
			buffLen = 0;
			out.write(Base64.encodeStream(buffer));
			buffer[buffLen++] = (byte) b;// ??
		}
	}

}
