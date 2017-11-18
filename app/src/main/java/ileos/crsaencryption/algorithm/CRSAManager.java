package ileos.crsaencryption.algorithm;

import java.math.BigInteger;
import java.io.*;
import java.util.*;

public class CRSAManager extends RSAManager {
	public CRSAManager() throws UnsupportedEncodingException {
	}

	// kompress blok-blok plainteks menjadi 2-blok plainteks
	public BigInteger[] compressionProcedure(BigInteger plaintext[]) {
		BigInteger n = new BigInteger("1");
		BigInteger d = plaintext[plaintext.length-1];

		for(int i = plaintext.length - 2; i >= 0; i--)
		{
			BigInteger nextd = d.multiply(plaintext[i]).add(n);
			n = d;
			d = nextd;
		}
		return new BigInteger[]{(d), (n)};
	}
	// dekompress 2-blok plainteks menjadi blok-blok plainteks(original plainteks)
	public BigInteger[] decompressionProcedure(BigInteger a, BigInteger b) {
		BigInteger q, r;
		BigInteger s = new BigInteger("0");
		LinkedList<BigInteger> quo = new LinkedList<>();
		do {
			q = a.divide(b);
			r = a.subtract(q.multiply(b));
			quo.addLast(q);
			if(r.equals(s)){
				break;
			}
			a = b;
			b = r;
		} while (true);

		return quo.toArray(new BigInteger[quo.size()]);
	}
}