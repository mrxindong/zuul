package com.gk.zuul.util;

import org.springframework.stereotype.Component;

import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;

@Component
public class FileHelper {

    public void readFromFile() throws FileNotFoundException, IOException {
        File file = new File("E:/workDoc/gateway/zuul/target/classes/com/gk/zuul/filter/pre/AccessFilter.class");// 指定要读取的文件
        FileReader reader = new FileReader(file);// 获取该文件的输入流
        char[] bb = new char[1024];// 用来保存每次读取到的字符
        String str = "";// 用来将每次读取到的字符拼接，当然使用StringBuffer类更好
        int n;// 每次读取到的字符长度
        while ((n = reader.read(bb)) != -1) {
            str += new String(bb, 0, n);
        }
        reader.close();// 关闭输入流，释放连接
        System.out.println(str);
    }


    public void write(String path, String content) throws IOException {

        File file = new File("");// 指定要写入的文件
        if (!file.exists()) {// 如果文件不存在则创建
            file.createNewFile();
        }
        // 获取该文件的缓冲输出流
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        // 写入信息
        bufferedWriter.write(content);
        bufferedWriter.flush();// 清空缓冲区
        bufferedWriter.close();// 关闭输出流
    }

    /**
     * 将InputStream转换成byte数组
     * @param in InputStream
     * @return byte[]
     * @throws IOException
     */
    private static byte[] InputStreamTOByte(InputStream in) throws IOException{

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int count = -1;
        while((count = in.read(data,0,data.length)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return outStream.toByteArray();
    }


    public  Class findClassByByte(Blob b) throws Exception {

        InputStream in = b.getBinaryStream();

        byte[] buffer =InputStreamTOByte(in);
        return new ClassLoaderSub().defineClassByName(null,buffer,0,buffer.length-1);
    }


    public  Class findClassByByte(byte[] b){
        return new ClassLoaderSub().defineClassByName(null,b,0,b.length-1);
    }

    public Class findClassByStr(String content){
        byte[] contentByte=content.getBytes();
        return new ClassLoaderSub().defineClassByName(null,contentByte,0,contentByte.length-1);
    }

    class ClassLoaderSub extends ClassLoader {
        public  Class defineClassByName(String name,byte[] b,int off,int len){
            //由于defineClass是protected，所以需要继承后来调用
            Class clazz = super.defineClass(name, b, off, len);
            return clazz;
        }
    }
}