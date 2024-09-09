package com.dissonance.itit.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class Keys {
	@JsonProperty(value = "keys")
	private List<Key> keyList;

	@Getter
	public static class Key {
		private String kty;
		private String kid;
		private String use;
		private String alg;
		private String n;
		private String e;
	}
}