package com.aminebag.larjson.stringdecoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.aminebag.larjson.stream.ByteStream;

/**
 * @author ahmed
 *
 */
public class ByteStreamMock implements ByteStream{
	private byte[] test ;
	int cursor;
	
	public ByteStreamMock(String test) {
		this.test = test.getBytes(StandardCharsets.UTF_8);
		cursor = 0;
	
	}
	@Override
	public byte nextByte() {
		return test[cursor++];
	}

	@Override
	public short nextShort() {
		return (short) (((byte)(test[cursor++]) << 8 )|((byte)test[cursor++]));
	}
	@Override
	public int nextInt() {
		return (test[cursor++]<<24 )|(test[cursor++] & 0xff <<16)|(test[cursor++] & 0xff <<8)|(test[cursor++]);
	}

	@Override
	public long nextLong() {
		return (test[cursor++]<<56 )|(test[cursor++] & 0xff <<48 )|(test[cursor++]  & 0xff <<40 )|(test[cursor++]  & 0xff <<32 )|(test[cursor++]  & 0xff <<24 )|(test[cursor++]  & 0xff <<16)|(test[cursor++]  & 0xff <<8)|(test[cursor++]);
	}
	@Override
	public boolean hasAtLeastRemainingBytes(int bytes) throws IOException {
		return test.length-cursor >= bytes;
	}
}
