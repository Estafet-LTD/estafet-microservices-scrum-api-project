package com.estafet.microservices.api.project.model;

import java.util.StringTokenizer;

public final class API {

	public static String getVersion(String version) {
		if (version == null) return "0.0.0";
		StringTokenizer tokenizer = new StringTokenizer(version.replaceAll("\\-SNAPSHOT", ""), ".");
		String p1 = tokenizer.nextToken();
		String p2 = tokenizer.nextToken();
		String p3 = tokenizer.nextToken();
		return p1 + "." + p2 + "." + Integer.toString(Integer.parseInt(p3) - 1);
	}

}
