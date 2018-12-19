/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.valueManager.types;

/**
 *
 * @author CyaNn
 */
public abstract class Binary {

    private byte[] value;

    public Binary(byte value) {
	this.value = new byte[1];
	this.value[0] = value;
    }

    public Binary(byte[] value) {
	this.value = value;
    }

    public static final byte[] valueOfHex(String s) {
	byte[] bs = new byte[s.length()];

	int i = 0;
	for (char c : s.toCharArray()) {
	    bs[i++] = Byte.valueOf(String.valueOf(c), 16);
	}

	return bs;
    }

    public static final byte[] valueOfBinary(String s) {
	byte[] bs = new byte[((s.length() - 1) / 4) + 1];

	int i = 0;
	int end = 0;
	while (end < s.length()) {
	    int beg = i * 4;
	    end = Math.min(beg + 4, s.length());
	    String digit = addZeroToEnd(s.substring(beg, end));
	    bs[i] = Byte.valueOf(digit, 2);
	    i++;
	}

	return bs;
    }

    public String toHexString() {
	StringBuilder sb = new StringBuilder();
	for (byte b : value) {
	    sb.append(Integer.toHexString(b));
	}
	return sb.toString();
    }

    private static final String addZeroToBeg(String s) {
	String ret = "";
	for (int i = s.length(); i < 4; i++) {
	    ret += '0';
	}
	return ret + s;
    }

    private static final String addZeroToEnd(String s) {
	for (int i = s.length(); i < 4; i++) {
	    s += '0';
	}
	return s;
    }

    public String toBinaryString() {
	StringBuilder sb = new StringBuilder();
	for (byte b : value) {
	    sb.append(addZeroToBeg(Integer.toBinaryString(b)));
	}
	return sb.toString();
    }
}
