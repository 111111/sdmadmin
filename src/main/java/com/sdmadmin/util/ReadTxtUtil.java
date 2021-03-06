package com.sdmadmin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ReadTxtUtil {
	/**
     * 功能：Java读取txt文件的内容
     * 步骤：1：先获得文件句柄
     * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
     * 3：读取到输入流后，需要读取生成字节流
     * 4：一行一行的输出。readline()。
     * 备注：需要考虑的是异常情况
     * @param filePath
     */

    public static List<String>  readTxtFile(String filePath, String encoding){
        List<String> list = new ArrayList<String>();
        try {
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
//                    System.out.println(lineTxt);
                    list.add(lineTxt);
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return list;

    }
    public static List<String>  readTxtFile(String filePath){

        return readTxtFile(filePath, "utf-8");

    }
	public static void main(String[] args) {
		 //String filePath = "D:\\20151012.txt";
	     //readTxtFile(filePath);
		//
	    //System.out.println(Math.round(Math.random()*(999999-100000)));

        String path = "I:\\电子竞技嘉年华\\COLLEGE_20180411_01.txt";
        List<String> list = readTxtFile(path);
        System.out.println("list = " + list);

	}

}
