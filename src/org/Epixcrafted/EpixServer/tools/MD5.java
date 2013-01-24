package org.Epixcrafted.EpixServer.tools;

import java.nio.charset.Charset;
import java.security.MessageDigest;

public class MD5 {
	public static String hash(String str) {
		try {
			final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes(Charset.forName("UTF8")));
			final byte[] array = messageDigest.digest();
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < array.length; ++i) {
	          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	       }
	        return sb.toString();
	    } catch (java.security.NoSuchAlgorithmException e) {
	    }
	    return null;
	}
}
