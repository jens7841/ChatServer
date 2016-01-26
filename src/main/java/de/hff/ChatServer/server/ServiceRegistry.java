package de.hff.ChatServer.server;

import java.util.HashMap;

public class ServiceRegistry {

	public static final String loginMessageHandler = "lmh";
	public static final String chatMessageHandler = "cmh";
	public static final String disconnectMessageHandler = "dmh";
	public static final String uploadRequestMessageHandler = "urmh";
	public static final String uploadPackageMessageHandler = "upmh";
	public static final String commandMessageHandler = "cmdmh";

	private static final HashMap<String, Service> hashMap = new HashMap<>();

	public static void register(Service service, String key) {
		hashMap.put(key, service);
	}

	public static Service getService(String key) {
		return hashMap.get(key);
	}
}