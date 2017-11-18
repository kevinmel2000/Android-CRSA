package ileos.crsaencryption.algorithm;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class RSAManager {
	protected BigInteger E;
	protected BigInteger D;
	protected BigInteger N;

	public void setE(BigInteger e) {
		E = e;
	}

	public void setD(BigInteger d) {
		D = d;
	}

	public void setN(BigInteger n) {
		N = n;
	}

	//enkripsi
	public BigInteger[] encrypt(BigInteger[] plaintext) {
		BigInteger[] encrypted = new BigInteger[plaintext.length];
		for (int i = 0; i < plaintext.length; i++) {
			encrypted[i] = plaintext[i].modPow(E, N);
		}
		return encrypted;
	}
	//dekripsi
	public BigInteger[] decrypt(BigInteger[] ciphertext) {
		BigInteger[] decrypted = new BigInteger[ciphertext.length];
		for (int i = 0; i < ciphertext.length; i++) {
			decrypted[i] = ciphertext[i].modPow(D, N);
		}
		return decrypted;
	}
}
