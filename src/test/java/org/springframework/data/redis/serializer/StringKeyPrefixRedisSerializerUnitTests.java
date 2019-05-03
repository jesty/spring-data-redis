/*
 * Copyright 2017-2019 the original author or authors.
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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

/**
 * Unit tests for {@link StringKeyPrefixRedisSerializer}.
 *
 * @author Davide Cerbo
 */
public class StringKeyPrefixRedisSerializerUnitTests {

	private static final String PREFIX = "test";

	@Test
	public void shouldSerializeToAscii() {
		StringKeyPrefixRedisSerializer serializer = new StringKeyPrefixRedisSerializer(PREFIX, StandardCharsets.US_ASCII);
		assertThat(serializer.serialize("foo-bar"), is(equalTo(prefixKey("foo-bar").getBytes())));
		assertThat(serializer.serialize("üßØ"), is(equalTo(prefixKey("???").getBytes())));
	}

	@Test
	public void shouldDeserializeFromAscii() {
		StringKeyPrefixRedisSerializer serializer = new StringKeyPrefixRedisSerializer(PREFIX, StandardCharsets.US_ASCII);
		assertThat(serializer.deserialize(prefixKey("foo-bar").getBytes()), is(equalTo("foo-bar")));
	}

	@Test
	public void shouldSerializeToIso88591() {
		StringKeyPrefixRedisSerializer serializer = new StringKeyPrefixRedisSerializer(PREFIX, StandardCharsets.ISO_8859_1);
		assertThat(serializer.serialize("üßØ"),
				is(equalTo(prefixKey("üßØ").getBytes(StandardCharsets.ISO_8859_1))));
	}

	@Test
	public void shouldDeserializeFromIso88591() {
		StringKeyPrefixRedisSerializer serializer = new StringKeyPrefixRedisSerializer(PREFIX, StandardCharsets.ISO_8859_1);
		assertThat(serializer.deserialize(prefixKey("üßØ").getBytes(StandardCharsets.ISO_8859_1)),
				is(equalTo("üßØ")));
	}

	@Test
	public void shouldSerializeToUtf8() {
		StringKeyPrefixRedisSerializer serializer = new StringKeyPrefixRedisSerializer(PREFIX, StandardCharsets.UTF_8);
		assertThat(serializer.serialize("foo-bar"), is(equalTo(prefixKey("foo-bar").getBytes())));
		assertThat(serializer.serialize("üßØ"), is(equalTo(prefixKey("üßØ").getBytes(StandardCharsets.UTF_8))));
	}

	@Test
	public void shouldDeserializeFromUtf8() {
		StringKeyPrefixRedisSerializer serializer = new StringKeyPrefixRedisSerializer(PREFIX, StandardCharsets.UTF_8);
		assertThat(serializer.deserialize(prefixKey("üßØ").getBytes(StandardCharsets.UTF_8)), is(equalTo("üßØ")));
	}
	
	private String prefixKey(String key) {
		return PREFIX + "::" + key;
	}
}
