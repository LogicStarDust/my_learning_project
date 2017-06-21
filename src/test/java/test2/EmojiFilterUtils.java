package test2;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class EmojiFilterUtils {
    /**
     * emoji表情替换
     *
     * @param source 原字符串
     * @param slipStr emoji表情替换成的字符串                
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source,String slipStr) {
        if(StringUtils.isNotBlank(source)){
            return source.replaceAll("[ud800udc00-udbffudfffud800-udfff]", slipStr);
        }else{
            return source;
        }
    }
    
    
    /**
     * 替换四个字节的字符 '\xF0\x9F\x98\x84\xF0\x9F）
     * @param content
     * @return
     */
    public static String removeFourChar(String content) {
        byte[] conbyte = content.getBytes();
        for (int i = 0; i < conbyte.length; i++) {
            if ((conbyte[i] & 0xF8) == 0xF0) {
                for (int j = 0; j < 4; j++) {                          
                    conbyte[i+j]=0x30;                     
                }  
                i += 3;
            }
        }
        content = new String(conbyte);
        return content.replaceAll("0000", "###");
    }
    
    /**
     * 判断是否存在四个字节的字符 '\xF0\x9F\x98\x84\xF0\x9F）
     * @param content
     * @return
     */
    public static boolean isHaveFourChar(String content) {
        boolean flag = false;
        if(StringUtils.isNotBlank(content)){
            byte[] conbyte = content.getBytes();
            for (int i = 0; i < conbyte.length; i++) {
                if ((conbyte[i] & 0xF8) == 0xF0) {
                    flag = true;
                }
            }
        }
        return flag;
    }
    static public void main(String[] args) throws UnsupportedEncodingException {
        String e="\\xF0\\x9F\\x90\\xBE";
        boolean b=EmojiFilterUtils.isHaveFourChar(e);
        System.out.println(e+","+b);
        String de=URLDecoder.decode("%F0%9F%90%BE","utf-8");
        boolean b1=EmojiFilterUtils.isHaveFourChar(de);
        System.out.println(de+","+b1);
    }
}
