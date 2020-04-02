package com.aminebag.larjson.stringdecoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.aminebag.larjson.stream.ByteStream;

/**
 * @author ahmed
 *
 */
public class Utf8CharacterDecoderTest {
	Utf8CharacterDecoder decoder = new Utf8CharacterDecoder();
@Test
void testSimpleString() throws CharacterDecodingException, IOException{
	testFullString("denize");
	testFullString("13246");
	testFullString("the brown fox jumped over the lazy dog");
}
@Test
void testComplexString1() throws CharacterDecodingException, IOException{
	testFullString("©");
}
@Test
void testComplexString2() throws CharacterDecodingException, IOException{
	ByteStream byteStreamMock = (ByteStream) new ByteStreamMock("𠜎");
		int actual = decoder.decodeCharacter(byteStreamMock);
		assertEquals(132878, actual);
	}
@Test
void testComplexString3() throws CharacterDecodingException, IOException{
	testFullString("☃");
}


private void testFullString(String expected) throws CharacterDecodingException, IOException {
	ByteStream byteStreamMock = (ByteStream) new ByteStreamMock(expected);
	 int actual;
	for (int i = 0; i < expected.length(); i++) {
		actual = decoder.decodeCharacter(byteStreamMock);
		int expect = expected.charAt(i);
		assertEquals(expect, actual);
	}
}
}
