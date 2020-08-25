package com.github.zbase.tools;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by wzz on 2017/03/01.
 * wuzhenzhen@tiamaes.com
 * 串口相关工具类
 *
 *  String intToHexString(int algorism)  十进制转换为十六进制字符串
 *  String encode(String str)  将字符串编码成16进制数字,适用于所有字符（包括中文）
 *  String decode(String bytes) 将16进制数字解码成字符串,适用于所有字符（包括中文）
 *  intToHexString(int algorism, int length)    十进制转换为十六进制字符串(不足length,前面补0)
 *
 *
 *  getEscape(String content)       发送数据转义： 发送时将7D、7E转义
 *  getUnEscape(String content)     接收数据 转义 7D02->7E,   7D01->7D
 */

public class AscIITools {

    /**
     * 十进制转换为十六进制字符串
     *
     * @param algorism
     *            int 十进制的数字
     * @return String 对应的十六进制字符串
     */
    public static String intToHexString(int algorism) {
        String result = "";
        result = Integer.toHexString(algorism);

        if (result.length() % 2 == 1) {
            result = "0" + result;
        }
        return result.toUpperCase();
    }

    /**
     *  long 转换为十六进制字符串
     * @param lng
     * @return
     */
    public static String longToHexString(long lng){
        String result = "";
        result = Long.toHexString(lng);

        if (result.length() % 2 == 1) {
            result = "0" + result;
        }
        return result.toUpperCase();
    }

    /**
     *  long 转换为指定长度十六进制字符串，不足前面补0
     * @param lng
     * @param length
     * @return
     */
    public static String longToHexString(long lng, int length){
        String result = "";
        result = Long.toHexString(lng);

        int resultLength = result.length();
        if(resultLength < length){
            for(int i=0; i<length-resultLength; i++){
                result="0"+result;
            }
        }else{
            result = result.substring(result.length()-length,result.length());
        }
        return result.toUpperCase();
    }

    /**
     * 十进制转换为十六进制字符串(不足length,前面补0)
     * 如 AscIITools.intToHexString(1,4)  = 0001
     * @param algorism
     * @param length    转换为十六进制的长度
     * @return
     */
    public static String intToHexString(int algorism, int length){
        String result = "";
        result = Integer.toHexString(algorism);

        int resultLength = result.length();
        if(resultLength < length){
            for(int i=0; i<length-resultLength; i++){
                result="0"+result;
            }
        }else{
            result = result.substring(result.length()-length,result.length());
        }
        return result.toUpperCase();
    }

    /*
     * 16进制数字字符集
     */
    private static String hexString = "0123456789ABCDEF";

    /*
     * 将字符串编码成16进制数字,适用于所有字符（包括中文）
     */
    public static String encode(String str) {
        // 根据默认编码获取字节数组
        byte[] bytes = str.getBytes();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        // 将字节数组中每个字节拆解成2位16进制整数
        for (int i = 0; i < bytes.length; i++) {
            sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
            sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
        }
        return sb.toString();
    }

    /*
     * 将16进制数字解码成字符串,适用于所有字符（包括中文）
     */
    public static String decode(String bytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
        // 将每2位16进制整数组装成一个字节
        for (int i = 0; i < bytes.length(); i += 2)
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
        return new String(baos.toByteArray());
    }

    // 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
    static public int isOdd(int num)
    {
        return num & 0x1;
    }

    //----------Hex字符串转int------------------
    static public int HexToInt(String inHex)
    {
        return Integer.parseInt(inHex, 16);
    }

    //----------Hex字符串转long-----------------
    static public long HexToLong(String hexStr){
        return Long.parseLong(hexStr, 16);
    }

    //----------Hex字符串转byte-----------------
    static public byte HexToByte(String inHex)
    {
        return (byte) Integer.parseInt(inHex,16);
    }

    //----------1字节转2个Hex字符----------------
    static public String Byte2Hex(Byte inByte){
        return String.format("%02x", inByte).toUpperCase();
    }
    //----------字节数组转转hex字符串--------------------
    static public String ByteArrToHex(byte[] inBytArr){
        StringBuilder strBuilder=new StringBuilder();
        int j=inBytArr.length;
        for (int i = 0; i < j; i++)
        {
            strBuilder.append(Byte2Hex(inBytArr[i]));
//            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }
    //-------------字节数组转转hex字符串，可选长度----------------------
    static public String ByteArrToHex(byte[] inBytArr, int offset, int byteCount){
        StringBuilder strBuilder=new StringBuilder();
        int j=byteCount;
        for (int i = offset; i < j; i++)
        {
            strBuilder.append(Byte2Hex(inBytArr[i]));
        }
        return strBuilder.toString();
    }
    //--------------hex字符串转字节数组------------------------------
    static public byte[] HexToByteArr(String inHex){
        int hexlen = inHex.length();
        byte[] result;
        if (isOdd(hexlen)==1)
        {//奇数
            hexlen++;
            result = new byte[(hexlen/2)];
            inHex="0"+inHex;
        }else {//偶数
            result = new byte[(hexlen/2)];
        }
        int j=0;
        for (int i = 0; i < hexlen; i+=2)
        {
            result[j]=HexToByte(inHex.substring(i,i+2));
            j++;
        }
        return result;
    }
    //-------------字节数组转hex字符串， 不足length 长度，前面补0
    public static String ByteArrToHex(byte[] inBytArr, int length){
        String result = "";
        result = ByteArrToHex(inBytArr);

        int resultLength = result.length();
        if(resultLength < length){
            for(int i=0; i<length-resultLength; i++){
                result="0"+result;
            }
        }else{
            result = result.substring(result.length()-length,result.length());
        }
        return result.toUpperCase();
    }

    /**
     * 二进制字符串转十六进制字符串  如"00000011"->"03"
     * @param bString
     * @return
     */
    public static String binaryStr2hexStr(String bString){
        if (bString == null || bString.equals("") || bString.length() % 8 != 0)
            return null;
        StringBuffer tmp = new StringBuffer();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4)
        {
            iTmp = 0;
            for (int j = 0; j < 4; j++)
            {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }

    /**
     *  十六进制字符串转二进制字符串  如03--》00000011
     * @param hexString
     * @return
     */
    public static String hexStr2binaryStr(String hexString){
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++)
        {
            tmp = "0000"
                    + Integer.toBinaryString(Integer.parseInt(hexString
                    .substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }
    /**
     * 十进制转换为二进制字符串(不足length,前面补0)
     * 如 AscIITools.intToBinaryStr(1,4)  = 0001
     * @param algorism
     * @param length    转换为二进制保留的长度
     * @return
     */
    public static String intToBinaryStr(int algorism, int length){
        String result = Integer.toBinaryString(algorism);

        int resultLength = result.length();
        if(resultLength < length){
            for(int i=0; i<length-resultLength; i++){
                result="0"+result;
            }
        }else{
            result = result.substring(result.length()-length,result.length());
        }
        return result;
    }

    /**
     * bytes转换成十六进制字数组
     *
     * @param src
     * @return
     */
    public static String[] bytes2HexStrings(byte[] src) {
        if (src == null || src.length <= 0) {
            return null;
        }
        String[] str = new String[src.length];

        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0XFF;
            String hv = Integer.toHexString(v);
            if (hv.length() == 1) {
                str[i] = "0" + hv;
            } else {
                str[i] = hv;
            }
        }
        return str;
    }


    /**
     * 将byte数组转换成16进制字符串
     * @param buf
     * @return
     * */
    public static String Bytes2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**将16进制字符串转换为byte数组          同HexToByteArr
     * @param hexStr
     * @return
     * */
    public static byte[] HexStr2Bytes(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * @功能: BCD码转为10进制串(阿拉伯数据)
     * @参数: BCD码
     * @结果: 10进制串
     */
    public static String bcd2Str(byte[] bytes) {
        StringBuffer temp = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
            temp.append((byte) (bytes[i] & 0x0f));
        }
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp
                .toString().substring(1) : temp.toString();
    }

    /**
     * @功能: 10进制串转为BCD码
     * @参数: 10进制串
     * @结果: BCD码
     */
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }
        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    /**
     * @功能: 检查16进制串是否合法
     * @参数: 16进制串
     * @结果: true/false
     */
    public static boolean isHex(String hexStr){
        for(int i = 0; i < hexStr.length(); i++) {// 判断是否满足16进制数
            if (!((hexStr.charAt(i) <= '9' && hexStr.charAt(i) >= '0')
                    ||(hexStr.charAt(i) <= 'F' && hexStr.charAt(i) >= 'A')
                    || (hexStr.charAt(i) <= 'f' && hexStr.charAt(i) >= 'a'))) {
                System.out.println("hex error");
                return false;
            }
        }
        return true;
    }

    /**
     * @功能: format longitude and latitude
     * 	将39936472/116267048 转换为double类型39.936472/116.267048
     * @参数: int
     * @结果:
     */
    public static double formatLAL(int lal){
        int remainder = lal%1000000;
        int result = lal/1000000;
        StringBuffer sb = new StringBuffer();
        sb.append(result);
        sb.append(".");
        sb.append(remainder);
        return Double.parseDouble(sb.toString());
    }

    /**
     * @功能: 16进制字符串经纬度转double 类型
     * 	将39936472/116267048 转换为double类型39.936472/116.267048
     * @参数: String
     * @结果:
     * LAL = Latitude and Longtitude
     */
    public static double HexToLAL(String hexStr){
        return formatLAL(HexToInt(hexStr));
    }


    //------------byte数组---异或和校验---异或和检验---------------------------
    /**
     * @功能: byte数组---异或和校验
     * 	 如：byte datas[] = {0x00,0x01,0x11,0x00}; byte checkCode = 0x10;
     *   0x00^0x01^0x11^0x00 = 0x10
     * @参数: byte 数组， byte检验码
     * @结果:
     */
    public static boolean xorVerify(byte datas[], byte checkCode){
        byte result = datas[0];
        for (int i = 1; i < datas.length; i++){
            result ^= datas[i];
        }
        if(result == checkCode)
            return true;
        return false;
    }

    //------------十六进制字符串---异或和检验---------------------------
    /**
     * @category  十六进制字符串---异或和检验
     *  如：String str = "0001100102010110A3B8A3B8A1FAB8DFC1D6BED3D7A1C7F8"; byte checkCode = 0x0D;
     * @param str  十六进制字符串
     * @param checkCode  校验码
     * @return 校验结果
     */
    public static boolean xorVerify(String str, byte checkCode){
        byte result = HexToByte(str.substring(0,2));
        for (int i=2; i<str.length(); i+=2){
            String b = str.substring(i, i+2);
            result ^= HexToByte(b);
        }
        if(result == checkCode)
            return true;
        return false;
    }

    /**
     *  异或检验码
     * @param str
     * @return
     */
    public static byte getXor(String str){
        if(str == null || str.equals("")) return 0;
        byte result = HexToByte(str.substring(0,2));
        for (int i=2; i<str.length(); i+=2){
            String b = str.substring(i, i+2);
            result ^= HexToByte(b);
        }
        return result;
    }

    /**
     * 异或检验码
     * @param b
     * @return
     */
    public static byte getXor(byte[] b){
        byte result = b[0];
        for (int i=1; i<b.length; i+=1){
            result ^= b[i];
        }
        return result;
    }

    /**
     *  得到和校验码
     * @param hexString     十六进制字符串
     * @param length    校验码长度  length 字节
     * @return
     */
    public static String getSumCheck(String hexString, int length){
        if(hexString == null || hexString.equals("")) return "";
        long sum = HexToInt(hexString.substring(0,2));
        for (int i=2; i<hexString.length(); i+=2){
            String b = hexString.substring(i, i+2);
            sum += HexToInt(b);
        }
        String result = longToHexString(sum);
        if(result.length() > length*2){
            return result.substring(result.length()-length*2);
        }else{
            String temp = result;
            for(int i=0; i<length*2 - result.length(); i++){
                temp = "0"+temp;
            }
            return temp;
        }
    }

    /**
     *  和校验
     * @param hexString 十六进制字符串
     * @param checkCode 十六进制校验码
     * @return
     */
    public static boolean SumCheck(String hexString, String checkCode){
        String nCheckCode = getSumCheck(hexString, checkCode.length()/2);
        if(nCheckCode.equals(checkCode)){
            return true;
        }
        return false;
    }

    /**
     * 校验和
     *
     * @param msg 需要计算校验和的byte数组
     * @param length 校验和位数
     * @return 计算出的校验和数组
     */
    public static byte[] SumCheck(byte[] msg, int length) {
        long mSum = 0;
        byte[] mByte = new byte[length];

        /** 逐Byte添加位数和 */
        for (byte byteMsg : msg) {
            long mNum = ((long)byteMsg >= 0) ? (long)byteMsg : ((long)byteMsg + 256);
            mSum += mNum;
        } /** end of for (byte byteMsg : msg) */

        /** 位数和转化为Byte数组 */
        for (int liv_Count = 0; liv_Count < length; liv_Count++) {
            mByte[length - liv_Count - 1] = (byte)(mSum >> (liv_Count * 8) & 0xff);
        } /** end of for (int liv_Count = 0; liv_Count < length; liv_Count++) */

        return mByte;
    }



    /**
     *  发送数据转义： 发送时将7D、7E转义
     *  0x7e <————> 0x7d 后紧跟一个 0x02；
     *  0x7d <————> 0x7d 后紧跟一个 0x01。
     * @param content
     * @return
     */
    public static String getEscape(String content){
        if(content.length()%2 != 0){
            System.out.println("getEscape content is has error"+content);
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i<content.length(); ){
            String str = content.substring(i,i+2);
            if(str.equals("7D")){
                sb.append("7D01");
            }else if (str.equals("7E")){
                sb.append("7D02");
            }else{
                sb.append(str);
            }
            i+=2;
        }
//        System.out.println(content+"\n"+sb.toString());
        return sb.toString();
    }

    /**
     *  接收数据 转义
     * @param content
     * @return
     */
    public static String getUnEscape(String content){
        if(content.length()%2 != 0){
            System.out.println("getEscape content is has error"+content);
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i<content.length()-2; ){
            String str = content.substring(i,i+4);
            if(str.equals("7D02")){
                sb.append("7E");
                i+=4;
            }else if (str.equals("7D01")){
                sb.append("7D");
                i+=4;
            }else{
                sb.append(content.substring(i,i+2));
                i+=2;
            }
            //处理尾
            if(i==content.length()-2){
                    sb.append(content.substring(i,i+2));//添加尾
            }
        }
//        System.out.println(content+"\n"+sb.toString());
        return sb.toString();
    }

    /**
     *  得到指定长度的byte 数组（用于后补0）
     *  例：如果length=10 ,"TM-01".getBytes()=bytes= [84,77,45,48,49] 则结果为[84,77,45,48,49,0,0,0,0,0]
     *  十六进制为：54 4D 2D 30 31 00 00 00 00 00
     *  等于是在bytes 后加length-bytes.length 个0
     * @param bytes
     * @param length
     * @return   返回指定的bytes 数据长度
     */
    public static byte[] getLengthBytes(byte [] bytes, int length){
        if(bytes.length>length){
            Log.e("AscIITools","getLengthBytes is error = bytes.length>length");
        }
        byte [] result = new byte [length];
        for(int i=0; i<length; i++){
            if(i<bytes.length){
                result[i]=bytes[i];
            }else{
                result[i]=0;
            }
        }
        return result;
    }

    /**
     *  得到指定长度的BCD, 不足时前补0
     * @param bcd   原始bcd
     * @param length  长度
     * @return   如 bcd= "123456", length = 10  则result = "0000123456"
     */
    public static String getLengthBCD(String bcd, int length){
        if(bcd.length()>length){
            Log.e("AscIITools","getLengthBCD is error = bcd.length>length");
        }
        String result = bcd;
        for(int i =0; i<length-bcd.length(); i++){
            result = "0"+result;
        }
        return result;
    }


    // 反转 ，用来处理，大端，小端
    public static String toSmallEnd(String hex){
        //0001C200
        String smallEnd = "";
        if(hex.length()%2 == 0){
            for(int i=0; i<hex.length()/2; i++){
                smallEnd = hex.substring(i*2,i*2+2) + smallEnd;
            }
        }
        return smallEnd;
    }

    /**
     * UTF-8编码 转换为对应的 汉字
     *
     * URLEncoder.encode("上海", "UTF-8") ---> %E4%B8%8A%E6%B5%B7
     * URLDecoder.decode("%E4%B8%8A%E6%B5%B7", "UTF-8") --> 上 海
     *
     * convertUTF8ToString("E4B88AE6B5B7")
     * E4B88AE6B5B7 --> 上海
     *
     * @param hexStr
     * @return
     */
    public static String convertUTF8ToString(String hexStr) {
        if (hexStr == null || hexStr.equals("")) {
            return null;
        }

        try {
            hexStr = hexStr.toUpperCase();

            int total = hexStr.length() / 2;
            int pos = 0;

            byte[] buffer = new byte[total];
            for (int i = 0; i < total; i++) {

                int start = i * 2;

                buffer[i] = (byte) Integer.parseInt(
                        hexStr.substring(start, start + 2), 16);
                pos++;
            }

            return new String(buffer, 0, pos, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hexStr;
    }

    /**
     *  将汉字转为UTF8编码的串
     * 将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名.
     * convertStringToUTF8("上海")
     * 上海 --> E4B88AE6B5B7
     * @param s	原串
     * @return
     */
    public static String convertStringToUTF8(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        try {
            char c;
            for (int i = 0; i < s.length(); i++) {
                c = s.charAt(i);
                if (c >= 0 && c <= 255) {
                    sb.append(c);
                } else {
                    byte[] b;

                    b = Character.toString(c).getBytes("utf-8");

                    for (int j = 0; j < b.length; j++) {
                        int k = b[j];
                        if (k < 0)
                            k += 256;
                        sb.append(Integer.toHexString(k).toUpperCase());
                        // sb.append("%" +Integer.toHexString(k).toUpperCase());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return sb.toString();
    }
}
