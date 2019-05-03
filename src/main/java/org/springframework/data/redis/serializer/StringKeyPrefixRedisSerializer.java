/*
 * Copyright 2011-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.redis.serializer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Simple {@link java.lang.String} to {@literal byte[]} (and back) serializer 
 * inspired to {@link StringRedisSerializer Strings} but that prepend a 
 * prefix to the key. Converts {@link java.lang.String Strings} into bytes and vice-versa using the specified charset 
 * (by default {@literal UTF-8}) and prefix. Use it only as key serializer.
 * This serializer is useful when you need to share the same Redis instance between different clients.
 * <p>
 * Useful when the interaction with the Redis happens mainly through Strings and you are sharing
 * the same Redis instance between different clients.
 * <p>
 * Does not perform any {@literal null} conversion since empty strings are valid keys/values.
 *
 * @author Davide Cerbo
 */
public class StringKeyPrefixRedisSerializer implements RedisSerializer<String> {

	private final Charset charset;
	private final String prefix;
	private final int prefixLength;

	public StringKeyPrefixRedisSerializer(String prefix) {
		this(prefix, StandardCharsets.UTF_8);
	}

	/**
	 * Creates a new {@link StringRedisSerializer} using the given {@link Charset} to encode and decode strings.
	 *
	 * @param charset must not be {@literal null}.
	 */
	public StringKeyPrefixRedisSerializer(String prefix, Charset charset) {
		Assert.notNull(charset, "Charset must not be null!");
		Assert.notNull(charset, "Prefix must not be null!");
		this.charset = charset;
		this.prefix = prefix;
		this.prefixLength = prefix.length();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.serializer.RedisSerializer#deserialize(byte[])
	 */
	@Override
	public String deserialize(@Nullable byte[] bytes) {
		String deserialized = (bytes == null ? null : new String(bytes, charset));
		return deserialized.substring(prefixLength + 2);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.serializer.RedisSerializer#serialize(java.lang.Object)
	 */
	@Override
	public byte[] serialize(@Nullable String string) {
		String valueWithPrefix = prefix + "::" + string;
		return (valueWithPrefix == null ? null : valueWithPrefix.getBytes(charset));
	}

	@Override
	public Class<?> getTargetType() {
		return String.class;
	}
}
