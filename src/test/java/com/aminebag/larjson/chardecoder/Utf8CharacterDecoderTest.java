package com.aminebag.larjson.chardecoder;

import com.aminebag.larjson.stream.ArrayByteStream;
import com.aminebag.larjson.stream.ByteStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ahmed Kassi
 */
public class Utf8CharacterDecoderTest extends CharacterDecoderTest {

	private final Utf8CharacterDecoder decoder = new Utf8CharacterDecoder();

	@Test
	void testSingleByteCharactersString() throws CharacterDecodingException, IOException {
		testString(ASCII_STRING);
	}

	@Test
	void testMultiBytesCharactersString() throws CharacterDecodingException, IOException {
		testString("© ☃ 122 & é \\\" \\' (-è_çà)= &###{[[|\\^@]$¨¨^^$$£¤¤%µ!§/.;,<>");
	}

	@Test
	void testFourBytesCharacter() throws CharacterDecodingException, IOException {
		String test = "\ud841\udf0e";
		ByteStream byteStreamMock = new ArrayByteStream(test, StandardCharsets.UTF_8);
		int actual = decoder.decodeCharacter(byteStreamMock);
		assertEquals(132878, actual);
	}
	
	@Test
	void testJsonString() throws CharacterDecodingException, IOException {
		testString("{\"menu\": {\r\n" +
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

	@Override
	protected CharacterDecoder decoder() {
		return decoder;
	}

	@Override
	protected Charset charset() {
		return StandardCharsets.UTF_8;
	}
}
