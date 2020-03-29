package com.aminebag.larjson.stream;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author Ahmed Kassi
 *
 */
public class ArrayByteStream implements ByteStream{
	private final byte[] array;
	private int cursor;
	
	public ArrayByteStream(String str, Charset charset) {
		this.array = str.getBytes(charset);
	}

	@Override
	public byte nextByte() {
		return array[cursor++];
	}

	@Override
	public short nextShort() {
		return (short) ((array[cursor++] << 8 )| array[cursor++]);
	}
	@Override
	public int nextInt() {
		return (array[cursor++]<<24 )|(array[cursor++] & 0xff <<16)|(array[cursor++] & 0xff <<8)|(array[cursor++]);
	}

	@Override
	public long nextLong() {
		return (array[cursor++]<<56 )|(array[cursor++] & 0xff <<48 )|(array[cursor++]  & 0xff <<40 )|(array[cursor++]  & 0xff <<32 )|(array[cursor++]  & 0xff <<24 )|(array[cursor++]  & 0xff <<16)|(array[cursor++]  & 0xff <<8)|(array[cursor++]);
	}
	@Override
	public boolean hasAtLeastRemainingBytes(int bytes) throws IOException {
		return array.length-cursor >= bytes;
	}

	@Override
	public long currentPosition() {
		return cursor;
	}
}
