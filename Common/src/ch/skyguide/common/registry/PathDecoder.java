/*
 * Copyright (C) 2011 Yann Caron
 * License modality not yet defined.
 */
package ch.skyguide.common.registry;

/**
 * The PathDecoder class.<br> creation date : Jul 27, 2012.
 *
 * @author Yann Caron
 * @version v0.1
 */
public class PathDecoder {

	public static class KeyPathValue {

		private int key;
		private String path;
		private String valueKey;

		public int getKey() {
			return key;
		}

		public String getPath() {
			return path;
		}

		public String getValueKey() {
			return valueKey;
		}

		public KeyPathValue(int key, String path, String valueKey) {
			this.key = key;
			this.path = path;
			this.valueKey = valueKey;
		}
	}

	public static class KeyPath {

		private int key;
		private String path;

		public int getKey() {
			return key;
		}

		public String getPath() {
			return path;
		}

		public KeyPath(int key, String path) {
			this.key = key;
			this.path = path;
		}
	}

	/**
	 * Default constructor.
	 */
	private PathDecoder() {
	}

	public static int decodeKey(String keyString) throws IllegalAccessException {
		int key = 0;
		if ("HKEY_CURRENT_USER".equals(keyString) || "HKCU".equals(keyString)) {
			key = WinRegistry.HKEY_CURRENT_USER;
		} else if ("HKEY_LOCAL_MACHINE".equals(keyString) || "HKLM".equals(keyString)) {
			key = WinRegistry.HKEY_LOCAL_MACHINE;
		} else {
			throw new IllegalAccessException(String.format("HKEY [%s] cannot be reached !", keyString));
		}

		return key;
	}

	public static KeyPathValue decodeKeyPathValue(String completePath) throws IllegalAccessException {

		int first = completePath.indexOf("\\");
		int last = completePath.lastIndexOf("\\");
		String keyString = completePath.substring(0, first);
		String path = completePath.substring(first + 1, last);
		String valueKey = completePath.substring(last + 1);


		return new KeyPathValue(decodeKey(keyString), path, valueKey);
	}

	public static KeyPath decodeKeyPath(String completePath) throws IllegalAccessException {

		int first = completePath.indexOf("\\");
		String keyString = completePath.substring(0, first);
		String path = completePath.substring(first + 1);

		return new KeyPath(decodeKey(keyString), path);
	}
}
