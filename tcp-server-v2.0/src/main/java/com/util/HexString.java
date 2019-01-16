package com.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 
 * @author Administrator
 * @time  2018.10.28
 *
 */
public class HexString {

  private static final char[] digits = {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
    'A', 'B', 'C', 'D', 'E', 'F'
  };
  
  /** Convert input HexStrings to Byte[]. */
  public static byte[] hexStringToBytes(String hexString){
   /* if(StringUtils.isEmpty(hexString))
      return null;*/
    int stringLen = hexString.length();
    byte[] temp = new byte[stringLen];
    int resultLength = 0;
    int nibble = 0;
    byte nextByte = 0;
    for(int i = 0; i < stringLen; i++){
      char c = hexString.charAt(i);
      byte b = (byte)Character.digit(c, 16);
      if(b == -1){
        if(!Character.isWhitespace(c))
          throw new IllegalArgumentException("Not HexString character: " + c);
        continue;
      }
      if(nibble == 0){
        nextByte = (byte)(b << 4);
        nibble = 1;
      }else{
        nextByte |= b;
        temp[resultLength++] = nextByte;
        nibble = 0;
      }
    }//for
    if(nibble != 0){
      throw new IllegalArgumentException("odd number of characters.");      
    }else{
      byte[] result = new byte[resultLength];
      System.arraycopy(temp, 0, result, 0, resultLength);
      return result;
    }
  }
  
  /** Convert input HexString to Byte. */
  public static byte hexStringToByte(String s)
  throws Exception{
    if(s == null)
      throw new RuntimeException("input String is null");    
    
    int strLength = s.length();   
    if(strLength == 1)
      s = "0" + s;
    else if (strLength > 2)
      s = s.substring(0, 2);
    
    byte[] result = new byte[1];
    result = hexStringToBytes(s);
    return result[0];
  }
  
  /** Convert input Byte to HexString. */ 
  public static String byteToHexString(byte b){
    StringBuilder result = new StringBuilder();
    result
      .append(digits[b >> 4 & 0xf])
      .append(digits[b & 0xf]);
    return result.toString();
  }


  /** Convert input Byte[] to HexString from offset as length. */
  public static String bytesToHexString(byte d[], int offset, int len, boolean spaced){
    StringBuilder result = new StringBuilder();
    int to = offset + len;
    for(int i = offset; i < to; i++){
      result.append(byteToHexString(d[i]));
      if(spaced)
        result.append(" ");
    }
    return result.toString();
  }

  /** Convert input Byte[] to HexString. */ 
  public static String bytesToHexString(byte[] d){
    if(d == null) return null;
    return bytesToHexString(d, 0, d.length, false);
  }

  /** Convert input Byte[] to HexString. */ 
  public static String bytesToHexString(byte[] d, boolean spaced){
    if(d == null) return null;
    return bytesToHexString(d, 0, d.length, spaced);
  }
  
  
  /**
   * hex表示的s值转为时间
   * 基础时间2000-01-01-00：00：00
   * @param Hextime
   * @return
   */
  public static String HextoTime(String Hextime){
		 String time=Integer.parseInt(Hextime,16)+"";
		 long millisTime = Long.parseLong(time)*1000;
		//基础时间
		 Calendar c = Calendar.getInstance();
		 c.set(2000, 0, 1, 00, 0, 0);
		 long GMTmillisTime = c.getTime().getTime();
		 
		 DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Calendar instance = Calendar.getInstance();
		 instance.setTimeInMillis(GMTmillisTime+millisTime);
		 return format.format(instance.getTime());
	}
 
  /**
   * 16进制转字符串10进制字符串
   * @param hex
   * @return
   */
  public static String HexStringToDecimalString(String hex){
	  return String.valueOf(Long.parseLong(hex, 16));
	  
  }
  
  /**
   * 获取当前时间s值的的16进制字符串
   * 
   */
  public static String getCurrentHexTime(){
	  Long currentTimeMillis = System.currentTimeMillis()/1000;
		return Integer.toHexString(currentTimeMillis.intValue());
    }
}
