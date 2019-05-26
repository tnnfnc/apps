package it.tnnfnc.encoders;

public class IdentityEncoder implements I_BytesEncoder {
	public static String algorithm = "User definition";

	@Override
	public char[] encode(byte[] input, String[] alphabeth, int len) throws IndexOutOfBoundsException {
		return new String(input).toCharArray();
	}

	@Override
	public String getAlgorithm() {
		return algorithm;
	}

}
