package de.hff.ChatServer.server;

import java.util.HashMap;
import java.util.Map;

public class ServiceRegistry {

	public static final String LOGIN_MESSAGE_HANDLER = "lmh";
	public static final String CHAT_MESSAGE_HANDLER = "cmh";
	public static final String DISCONNECT_MESSAGE_HANDLER = "dmh";
	public static final String UPLOAD_REQUEST_MESSAGE_HANDLER = "urmh";
	public static final String UPLOAD_PACKAGE_MESSAGE_HANDLER = "upmh";
	public static final String DOWNLOAD_REQUEST_MESSAGE_HANDLER = "drmh";
	public static final String COMMAND_MESSAGE_HANDLER = "cmdmh";

	private static final Map<String, Object> registry = new HashMap<>();

	public static <T> void register(T service, String key) {
		registry.put(key, service);
	}

	public static <T> T getService(String key, Class<T> type) {
		T t = null;
		try {
			t = (T) registry.get(key);
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		return t;
	}
}
