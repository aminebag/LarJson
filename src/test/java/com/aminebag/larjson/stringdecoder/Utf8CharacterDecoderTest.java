package com.aminebag.larjson.stringdecoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.aminebag.larjson.stream.ByteStream;

/**
 * @author ahmed
 *
 */
public class Utf8CharacterDecoderTest {
	Utf8CharacterDecoder decoder = new Utf8CharacterDecoder();

	@Test
	void testSimpleString() throws CharacterDecodingException, IOException {
		testFullString("denize");
		testFullString("13246");
		testFullString("the brown fox jumped over the lazy dog");
	}

	@Test
	void testComplexString() throws CharacterDecodingException, IOException {
		testFullString("© ☃ 122 & é \\\" \\' (-è_çà)= &###{[[|\\^@]$¨¨^^$$£¤¤%µ!§/.;,<>");
	}

	@Test
	void testFourBytesCharacter() throws CharacterDecodingException, IOException {
		String test = new String("𠜎");
		ByteStream byteStreamMock = (ByteStream) new ByteStreamMock(test);
		int actual = decoder.decodeCharacter(byteStreamMock);
		assertEquals(132878, actual);
	}
	
	@Test
	void testJSONSample() throws CharacterDecodingException, IOException {
		testFullString("{\"menu\": {\r\n" + 
				"  \"id\": \"file\",\r\n" + 
				"  \"value\": \"File\",\r\n" + 
				"  \"popup\": {\r\n" + 
				"    \"menuitem\": [\r\n" + 
				"      {\"value\": \"New\", \"onclick\": \"CreateNewDoc()\"},\r\n" + 
				"      {\"value\": \"Open\", \"onclick\": \"OpenDoc()\"},\r\n" + 
				"      {\"value\": \"Close\", \"onclick\": \"CloseDoc()\"}\r\n" + 
				"    ]\r\n" + 
				"  }\r\n" + 
				"}} ");
	}

	private void testFullString(String expected) throws CharacterDecodingException, IOException {
		ByteStream byteStreamMock = (ByteStream) new ByteStreamMock(expected);
		StringBuilder builder = new StringBuilder();
		boolean done = false;
		int c = 0;
		while (!done) {
			c = decoder.decodeCharacter(byteStreamMock);
			if (c == -1) {
				done = true;
				break;
			}
			builder.append((char) c);
		}
		assertTrue(expected.equals(builder.toString()));
	}

}
