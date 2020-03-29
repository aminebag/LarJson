package com.aminebag.larjson;

public interface ByteStream {
    public byte nextByte();
    public short nextShort();
    public int nextInt();
    public long nextLong();
    public int hasRemainingBytes(int bytes);
}
