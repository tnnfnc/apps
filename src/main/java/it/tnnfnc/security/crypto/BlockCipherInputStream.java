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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

/**
 * The <code>BlockCipherInputStream</code> filters the data with a block cipher.
 * The cipher provided at creation is a block cipher with a padding scheme. The
 * cipher buffers and processes the data from the input stream before returning
 * any output. When the end-of-stream is encountered during the reading of the
 * underlying stream, the cipher <code>doFinal</code> method is called for
 * providing the last bytes with the padding.
 * 
 * @author Franco Toninato
 * @version %I%, %G%
 * @since 1.2
 */
public class BlockCipherInputStream extends FilterInputStream {

	/* Cipher Block Operator */
	private I_BlockCipherStream cipher;
	/* Buffer containing the processed data */
	private byte[] buffer;
	/* In buffer containing data read from the underlying stream */
	private byte[] inBuffer;
	/* The buffer length of valid processed data*/
	private int buffLen;
	/* The buffer offset - accumulated data are from offs to buffer length  */
	private int offs;
	/* EOF - End of stream */
	private boolean EOF;
	/* The cipher block size */
	private int blockSize = 0;

	/**
	 * Creates a new input stream with the specified cipher and the underlying
	 * input stream.
	 * 
	 * @param in
	 *            the input stream.
	 * @param cipher
	 *            the cipher to perform encrypting/decrypting operation.
	 */
	public BlockCipherInputStream(InputStream in, I_BlockCipherStream cipher) {
		super(in);
		this.cipher = cipher;
		EOF = false;
		blockSize = cipher.getBlockSize();
		inBuffer = new byte[blockSize];
		buffer = new byte[2 * blockSize];
	}

	/**
	 * Reads a byte from the input stream ciphered buffer. When the
	 * end-of-stream is encountered reading the underlying stream the cipher
	 * <code>doFinal</code> method is called for providing padding. The returned
	 * value is the next processed byte in the buffer or <em>-1</em> if the
	 * end-of-stream was reached.
	 * 
	 * @return the next byte of data, or <code>-1</code> if the end of the
	 *         stream is reached.
	 * @exception IOException
	 *                if an I/O error occurs.
	 * @see java.io.FilterInputStream#in
	 */
	@Override
	public int read() throws IOException {
		// There are no bytes to read
		if (EOF && offs == buffLen)
			return -1;
		// If the buffer is empty read a new buffer
		if (offs >= buffLen && !EOF) {
			readBlock();
		}
		return buffer[offs++] & 0Xff; // Get a byte from buffer and shift offs
	}

	/**
	 * Reads up to <code>b.length</code> bytes from the input stream processed
	 * bytes. When the end-of-stream is encountered reading the underlying
	 * stream the cipher <code>doFinal</code> method is called for providing
	 * padding. The returned value is the bytes number read in the buffer or
	 * <em>-1</em> if the end-of-stream was reached.
	 * 
	 * @param b
	 *            the output buffer.
	 * @return the number of processed bytes into the output buffer.
	 * @exception IOException
	 *                if an I/O error occurs.
	 * @see java.io.FilterInputStream#read(byte[], int, int)
	 */
	@Override
	public int read(byte b[]) throws IOException {
		return read(b, 0, b.length);
	}

	/**
	 * Reads up to <code>len</code> bytes from the input stream processed bytes.
	 * The returned value is the bytes number actually read in the buffer or
	 * <em>-1</em> if the end-of-stream was reached. If <code>len</code> is zero
	 * it returns <em>0</em>.
	 * 
	 * @param out
	 *            the output buffer.
	 * @param outOff
	 *            the output offset where processed bytes starts.
	 * @param len
	 *            the number of bytes processed from the underlying input
	 *            stream.
	 * @return the number of processed bytes into the output buffer.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs.
	 * @see java.io.FilterInputStream#in
	 */
	@Override
	public int read(byte[] out, int outOff, int len) throws IOException {
		int processedBytes = 0;
		// There are no bytes to read
		if (EOF && offs == buffLen)
			return -1;
		// If the buffer is empty read a new buffer
		if (offs == buffLen && !EOF) {
			readBlock();
		}

		int leftOver = buffLen - offs;

		if (len < leftOver) // Copy the buffer
		{
			System.arraycopy(buffer, offs, out, outOff, len);
			offs += len;
			processedBytes = len;
		} else {
			// Copy the remaining
			System.arraycopy(buffer, offs, out, outOff, leftOver);
			outOff += leftOver;
			offs = buffLen;
			len -= leftOver;
			processedBytes = leftOver;
			// Process the bytes up to len
			while (len > 0 && !EOF) {
				readBlock();
				leftOver = len > buffLen ? buffLen : len;
				System.arraycopy(buffer, 0, out, outOff, leftOver);
				outOff += leftOver;
				offs = leftOver;
				len -= leftOver;
				processedBytes += leftOver;
			}
		}
		return processedBytes;
	}

	/**
	 * Skip bytes from the input stream without blocking. The bytes skipped are
	 * up to the requested number, it is returned the actual amount. The
	 * returned value depends on the number of bytes already processed by the
	 * cipher and currently buffered by the class.
	 * 
	 * @param n
	 *            the number of bytes to be skipped.
	 * @return the actual number of bytes skipped.
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	// public long skip(long n) throws IOException {
	// return in.skip(n);
	// }

	/**
	 * Returns the bytes already processed by the cipher and currently buffered.
	 * 
	 * @return the buffered data processed by the cipher.
	 * @exception IOException
	 *                if an I/O error occurs.
	 * @see java.io.FilterInputStream#in
	 */
	@Override
	public int available() throws IOException {

		return (buffLen - offs);
	}

	/**
	 * Closes the underlying input stream. No cipher processing is involved. The
	 * method simply calls the {@link java.io.InputStream#close()} method of the
	 * underlying input stream.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs.
	 * @see java.io.FilterInputStream#in
	 */
	@Override
	public void close() throws IOException {
		cipher.reset();
		initBuffer(buffer);
		super.close();
	}

	/**
	 * Not supported.
	 * 
	 * @see java.io.FilterInputStream#in
	 * @see java.io.FilterInputStream#reset()
	 */
	// public void mark(int readlimit) {
	// in.mark(readlimit);
	// }

	/**
	 * Not supported.
	 * 
	 * @exception IOException
	 *                if the stream has not been marked or if the mark has been
	 *                invalidated.
	 * @see java.io.FilterInputStream#in
	 * @see java.io.FilterInputStream#mark(int)
	 */
	// public void reset() throws IOException {
	// in.reset();
	// }

	/**
	 * Mark is not supported.
	 * 
	 * @see java.io.FilterInputStream#in
	 * @see java.io.InputStream#mark(int)
	 * @see java.io.InputStream#reset()
	 */
	@Override
	public boolean markSupported() {
		return false;
	}

	/*
	 * Reads and ciphers a new block of bytes from the input stream into the
	 * buffer. The buffer is the double of the cipher block length for fitting
	 * the last block padding.
	 * 
	 * @return
	 */
	private void readBlock() throws IOException {

		try {
			buffLen = 0;
			offs = 0;
			/* Reads bytes from in stream until buffer is filled */
			while (!EOF && buffLen == 0) {
				int inputBytes = in.read(inBuffer);
				if (inputBytes > 0) {
					buffLen = cipher.update(inBuffer, 0, inputBytes, buffer, 0);
				}
				// EOF occurred
				else {
					// if (!EOF) {
					// System.out.println(this.getClass().getName()
					// + " buffer to process =" + inputBytes
					// + " output buffer =" + buffer.length
					// + " output length =" + buffLen);
					EOF = true;
					buffLen += cipher.doFinal(buffer, buffLen);
					
					// System.out.println(this.getClass().getName()
					// + "last processed output length =" + buffLen);
					// }
				}
			}
		} catch (BadPaddingException e) {
			IOException ioe = new IOException(e.getMessage()
					+ " Bad padding schema of cipher bytes.");
			ioe.initCause(e);
			throw ioe;
		} catch (ShortBufferException e) {
			IOException ioe = new IOException(e.getMessage()
					+ " Input buffer too short.");
			ioe.initCause(e);
			throw ioe;
		} catch (IllegalStateException e) {
			IOException ioe = new IOException(e.getMessage()
					+ " Cipher internal error.");
			ioe.initCause(e);
			throw ioe;
		} catch (IllegalBlockSizeException e) {
			IOException ioe = new IOException(e.getMessage()
					+ " Input not aligned with block length.");
			ioe.initCause(e);
			throw ioe;
		}

	}

	/*
	 * Initializes an array.
	 */
	private static void initBuffer(byte[] b) {
		Arrays.fill(b, (byte) 0x00);
		b = new byte[b.length];
	}
}
