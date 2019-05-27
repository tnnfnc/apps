package it.tnnfnc.encoders;

import java.util.HashMap;

public class EncodingAlgorithmFactory {
	final private HashMap<String, I_BytesEncoder> encoders = new HashMap<String, I_BytesEncoder>();

	/**
	 * Create a
	 * 
	 * @param encoders
	 */
	public EncodingAlgorithmFactory(String... encoders) {
		for (String encoder : encoders) {
			try {
				addAlgorithm((I_BytesEncoder) Class.forName(encoder).getDeclaredConstructor().newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Add an available algorithm.
	 * 
	 * @param the
	 *            algorithm.
	 */
	private void addAlgorithm(I_BytesEncoder a) {
		encoders.put(a.getAlgorithm(), a);
	}

	/**
	 * Get the list of available algorithms.
	 * 
	 * @return the list of available algorithms.
	 */
	public String[] getList() {
		return encoders.keySet().toArray(new String[0]);
	}

	/**
	 * Get an algorithm instance.
	 * 
	 * @param name
	 *            the algorithm name.
	 * @return the algorithm.
	 */
	public I_BytesEncoder getAlgorithmInstance(String name) {
		if (name != null && name.length() != 0 && encoders.containsKey(name)) {
			return encoders.get(name);
		}
		return null;
	}

	// @Override
	// public char[] getPassword(Access access, SecurityObject security) throws
	// IllegalArgumentException {
	// char[] password = null;
	//
	// // System.out.println(
	// // this.getClass().getName() + "password type = " +
	// // access.getValue(AccessFactory.PASSWORD_TYPE) + "");
	//
	// String algorithm = (access.getValue(AccessFactory.PASSWORD_TYPE) +
	// "").split("\\.")[0];
	// try {
	// if ((algorithm).equalsIgnoreCase(IdentityEncoder.algorithm)) {
	// // System.out.println(this.getClass().getName() + "algorithm = "
	// // + algorithm);
	// // Init the decipher
	// byte input[];
	// input = Base64.decode(access.getUserPassword().getBytes());
	// password = new String(security.getCipher(access.getSalt(),
	// false).doFinal(input, 0, input.length))
	// .toCharArray();
	// } else {
	// // System.out.println(this.getClass().getName() + "algorithm = "
	// // + algorithm);
	// byte[] in = security.generateKey(access.getSalt(),
	// access.getPasswordLength());
	// password = getAlgorithmInstance(algorithm).encode(in,
	// PasswordDictionary.getDictionary(access),
	// access.getPasswordLength());
	// }
	// } catch (InvalidKeyException e) {
	// throw new IllegalArgumentException(e.getCause());
	// } catch (IndexOutOfBoundsException e) {
	// throw new IllegalArgumentException(e.getCause());
	// } catch (IllegalStateException e) {
	// throw new IllegalArgumentException(e.getCause());
	// } catch (BadPaddingException e) {
	// throw new IllegalArgumentException(e.getCause());
	// } catch (IllegalBlockSizeException e) {
	// throw new IllegalArgumentException(e.getCause());
	// } catch (DigestException e) {
	// throw new IllegalArgumentException(e.getCause());
	// }
	// return password;
	// }

}
