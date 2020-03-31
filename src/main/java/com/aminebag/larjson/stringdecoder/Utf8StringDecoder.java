package com.aminebag.larjson.stringdecoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Formatter;

import com.aminebag.larjson.ByteStream;


public class Utf8StringDecoder implements StringDecoder {


    @Override
    public int decodeCharacter(ByteStream byteStream) throws StringDecodingException {
    if(byteStream.hasRemainingBytes(1)) {
    	int b = byteStream.nextByte();
    	if(true) {
    		
    	}
    }
    	
    	return 0;
    }
    
    public static byte[] utf8encode(int codepoint) {
        return new String(new int[]{codepoint}, 0, 1).getBytes(StandardCharsets.UTF_8);
    }
 
    public static int utf8decode(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8).codePointAt(0);
    }
 
    public static void main(String[] args) {
        System.out.printf("%-7s %-43s %7s\t%s\t%7s%n",
                "Char", "Name", "Unicode", "UTF-8 encoded", "Decoded");
        String test = "Atest";
        byte[] btest = test.getBytes(StandardCharsets.UTF_8);
//        for (int codepoint : new int[]{0x0041, 0x00F6, 0x0416, 0x20AC, 0x1D11E}) {
//            byte[] encoded = utf8encode(codepoint);
//            Formatter formatter = new Formatter();
//            for (byte b : encoded) {
//                formatter.format("%02X ", b);
//            }
//            String encodedHex = formatter.toString();
//          
//        }
//        Char    Name                                        Unicode	UTF-8 encoded	Decoded
//        A       LATIN CAPITAL LETTER A                      U+0041	41          	U+0041
  System.out.println(utf8decode(btest));
    }
}
