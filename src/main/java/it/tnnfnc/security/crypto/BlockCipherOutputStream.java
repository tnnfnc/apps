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
package it.tnnfnc.security.crypto;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

/**
 * The <code>BlockCipherOutputStream</code> filters the output data with a block
 * cipher. The cipher provided at creation is a block cipher with a padding
 * scheme. The output stream buffers the processes data before writing to the
 * underlying output stream. With calling of the <code>close</code> method the
 * cipher processes the data available in the buffer.
 * 
 * @author Franco Toninato
 * @version %I%, %G%
 * @since 1.2
 */
public class BlockCipherOutputStream extends FilterOutputStream {

	/* Cipher Block Operator with padding */
	private I_BlockCipherStream cipher;
	/* Output Buffer - contains bytes processed by the cipher */
	private byte[] buffer;
	/* Output Buffer offset at a time */
	private int offs = 0;
	/* The buffer length at a time */
	private int buffLen = 0;
	/* The processing state */
	private int internalState;
	private static int READY = 1;
	private static int CLOSED = 2;

	/**
	 * Creates a new filter output stream with the specified cipher.
	 * 
	 * @param out
	 *            the output stream.
	 * @param cipher
	 *            the cipher to perform encription/decription operation.
	 */
	public BlockCipherOutputStream(OutputStream out, I_BlockCipherStream cipher) {
		super(out);
		this.cipher = cipher;
		buffer = new byte[2 * cipher.getBlockSize()];
		offs = 0;
		internalState = READY;
	}

	/**
	 * Writes a byte to the output stream. As the cipher can process one block
	 * at a time, the writing process waits until the buffer match the block
	 * length, and the processed block is buffered. The writing to the
	 * underlying stream is performed a byte at a time as soon as there are
	 * bytes in the buffer.
	 * 
	 * @param in
	 *            the <code>byte</code>.
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	@Override
	public void write(int in) throws IOException {
		try {
			// Buffer fully processed
			if (offs == buffLen) {
				buffLen = 0;
				offs = 0;
			}
			int processedBytes = cipher.update((byte) (in & 0xff), buffer,
					buffLen);
			buffLen += processedBytes > 0 ? processedBytes : 0;
			if (offs < buffLen) {
				out.write(buffer[offs++]);
			}
		} catch (ShortBufferException e) {
			cipher.reset();
			initBuffer(buffer);
			IOException ioe = new IOException(" Input buffer too short.");
			ioe.initCause(e);
			throw ioe;
		} catch (IllegalStateException e) {
			cipher.reset();
			initBuffer(buffer);
			IOException ioe = new IOException(" Internal cipher error.");
			ioe.initCause(e);
			throw ioe;
		} catch (IllegalBlockSizeException e) {
			cipher.reset();
			initBuffer(buffer);
			IOException ioe = new IOException(" Cipher block mismatch.");
			ioe.initCause(e);
			throw ioe;
		}
	}

	/**
	 * Writes input bytes to the output stream. A multiple of the block length
	 * is processed and written to the stream, the bytes left are buffered.
	 * <p>
	 * The method calls <code> write(byte b[], int off, int len)</code>.
	 * 
	 * @param in
	 *            the data to be written.
	 * @exception IOException
	 *                if an I/O error occurs.
	 * @see java.io.FilterOutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte in[]) throws IOException {
		write(in, 0, in.length);
	}

	/**
	 * Writes the input buffer to the output stream. A multiple of the block
	 * length is processed and written to the stream, the bytes left are
	 * buffered.
	 * 
	 * @param in
	 *            the input buffer.
	 * @param off
	 *            the input offset the input starts from.
	 * @param len
	 *            the number of bytes to process.
	 * @exception IOException
	 *                if an I/O error occurs.
	 * @see java.io.FilterOutputStream#write(int)
	 */
	@Override
	public void write(byte in[], int off, int len) throws IOException {
		try {
			// At t = 0 all is 0
			int leftOver = buffLen - offs;

			// Write the accumulated byte starting from the offset
			if (leftOver > 0) {
				out.write(buffer, offs, leftOver);
			}

			// Starting processing input and accumulate in buffer
			// byte[] tmpBuffer = new byte[cipher.getOutputSize(len)];
			byte[] tmpBuffer = new byte[cipher.getOutputSize(len) * 2];
			buffLen = cipher.update(in, off, len, tmpBuffer, 0);
			out.write(tmpBuffer, 0, buffLen);
			buffLen = 0;
			offs = 0;
		} catch (ShortBufferException e) {
			cipher.reset();
			initBuffer(buffer);
			throw new IOException(e.getMessage()
					+ " Input buffer too short during writing.");
		} catch (IllegalStateException e) {
			cipher.reset();
			initBuffer(buffer);
			throw new IOException(e.getMessage()
					+ " Cipher not ready to process during writing.");
		} catch (IllegalBlockSizeException e) {
			cipher.reset();
			initBuffer(buffer);
			throw new IOException(e.getMessage()
					+ " Cipher block mismatch during writing.");
		} catch (IndexOutOfBoundsException e) {
			cipher.reset();
			initBuffer(buffer);
			throw new IOException(e.getMessage()
					+ " Input buffer offset or lenght error.");
		}
	}

	/**
	 * Forces the writing process to the output stream. The still buffered bytes
	 * are not processed by the underlying cipher. The method performs a call to
	 * the cipher <code>doFinal</code> method to accomplish the padding
	 * operations before closing the output stream.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs.
	 * @see java.io.FilterOutputStream#out
	 */
	// public void flush() throws IOException {
	// System.out.println(this.getClass().getName() + " intenal status = "
	// + (internalState == READY ? "READY" : "FLUSHED"));
	// super.flush();
	// }

	/**
	 * Processes the buffered input and closes the output stream.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs.
	 * @see java.io.FilterOutputStream#flush()
	 * @see java.io.FilterOutputStream#out
	 */
	@Override
	public void close() throws IOException {
		try {
			if (internalState == READY) {
				// Forces the writing process to the output stream
				super.flush();
				// Write buffered bytes
				out.write(buffer, offs, buffLen - offs);
				offs = 0;
				buffLen = 0;

				// Finalize the cipher
				buffLen = cipher.doFinal(buffer, 0);
				// Write last bytes
				if (buffLen > 0)
					out.write(buffer, 0, buffLen);
				internalState = CLOSED;
				super.close();
				cipher.reset();
				initBuffer(buffer);
				internalState = CLOSED;
			}
		} catch (ShortBufferException e) {
			IOException ioe = new IOException(
					" Input buffer too short during final processing.");
			ioe.initCause(e);
			throw ioe;
		} catch (IllegalStateException e) {
			IOException ioe = new IOException(
					"Internal cipher error during final processing.");
			ioe.initCause(e);
			throw ioe;
		} catch (IllegalBlockSizeException e) {
			IOException ioe = new IOException(
					" Cipher block mismatch during final processing.");
			ioe.initCause(e);
			throw ioe;
		} catch (BadPaddingException e) {
			IOException ioe = new IOException(" Bad padding.");
			ioe.initCause(e);
			throw ioe;
		}
	}

	/*
	 * Initializes an array.
	 */
	private void initBuffer(byte[] buff) {
		Arrays.fill(buff, (byte) 0);
		buff = new byte[buff.length];
		buffLen = 0;
		offs = 0;
	}
}