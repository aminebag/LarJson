package com.aminebag.larjson.stringdecoder;

import com.aminebag.larjson.stream.ByteStream;

import java.io.IOException;

public class Utf8CharacterDecoder implements CharacterDecoder {
	public final static int MASK_1 = 0b1_0000000;
	public final static int MASK_2 = 0b111_00000;
	public final static int MASK_3 = 0b1111_0000;
	public final static int MASK_4 = 0b11111_000;
	public final static String DECODING_ERROR = "An error while decoding charachter :";
	
	@Override
    public int decodeCharacter(ByteStream byteStream) throws CharacterDecodingException, IOException {
       if(!byteStream.hasAtLeastRemainingBytes(1))
    	return -1;
       int firstByte = byteStream.nextByte() & 255;
      //First case : 0xxxxxxx
       if((firstByte & MASK_1 ) == 0) {
    	 return firstByte;
       } else {
		
		if((firstByte & MASK_2)==0b110_00000) {
			   if(!byteStream.hasAtLeastRemainingBytes(1))
				   throw new CharacterDecodingException(DECODING_ERROR + firstByte);
			int secondByte = byteStream.nextByte();
			return (firstByte & 0b000_11111)<<6 |(secondByte & 0b00_111111);
		   }
		 //Third case : 1110xxxx	10xxxxxx	10xxxxxx
		   else if((firstByte & MASK_3)==0b1110_0000) {
			   if(!byteStream.hasAtLeastRemainingBytes(2))
				   throw new CharacterDecodingException(DECODING_ERROR + firstByte);
			   int secondByte = byteStream.nextByte();
			   int thirdByte = byteStream.nextByte();
			   return (firstByte & 0b0000_1111)<<12 | (secondByte & 0b00111111)<< 6  |(thirdByte & 0b00111111) ;
		   }
		 //Fourth case : 11110xxx	10xxxxxx	10xxxxxx	10xxxxxx
		   else if((firstByte & MASK_4)==0b11110_000) {
			   if(!byteStream.hasAtLeastRemainingBytes(3))
				   throw new CharacterDecodingException(DECODING_ERROR + firstByte);
			   int secondByte = byteStream.nextByte();
			   int thirdByte = byteStream.nextByte();
			   int fourthByte = byteStream.nextByte();
			   return (firstByte & 0b00000_111)<<18 | (secondByte & 0b00111111)<< 12  | (thirdByte & 0b00111111)<< 6 | (fourthByte & 0b00111111) ;	
		   }
		   else {
			  throw new CharacterDecodingException(DECODING_ERROR + firstByte); 
		   }
	}
    }
}
