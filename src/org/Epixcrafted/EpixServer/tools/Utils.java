package org.Epixcrafted.EpixServer.tools;

import org.jboss.netty.buffer.ChannelBuffer;

public class Utils {
	
	public static ChannelBuffer writeStringToBuffer(ChannelBuffer buf, String str) {
		buf.writeShort(str.length());
		for (int i=0; i < str.length(); ++i) {
			buf.writeChar(str.charAt(i));
		}
		return buf;
	}
	
	public static String readStringFromBuffer(ChannelBuffer buf) {
		int length = buf.readShort();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) sb.append(buf.readChar());
		return sb.toString();
	}

}
