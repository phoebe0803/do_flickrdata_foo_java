package com.experiment;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class do_dataset_like_voc {
    public static void main(String[] args) throws IOException {
        HashMap<String, ArrayList> map = new HashMap<>();
        //读取只有存储集合1的图像
        BufferedReader br = new BufferedReader(new FileReader("/Users/lidan/PycharmProjects/pythonProject8/flickr_logos_27_dataset/flickr_logos_27_dataset_training_set_annotation_unique.txt"));
        String line = null;
        //依次读取annotation
        while ((line = br.readLine()) != null) {
            String[] s = line.split(" ");
            //文件名字作为key 如果没有则创建一个arraylist对象用来保存line
            //如果有key（） 那么直接追加就可以了
            if (map.get(s[0]) == null) {
                ArrayList<String> list1 = new ArrayList<>();
                list1.add(get_format_variable(line));
                map.put(s[0], list1);
            } else {
                map.get(s[0]).add(get_format_variable(line));
            }
        }
        br.close();

        //set集合放的是所有的jpg照片
        //set集合放的是所有的jpg照片
        Set<String> set_picture = map.keySet();
        System.out.println("----");
        for (String s_jpg_name:set_picture){
            System.out.println(s_jpg_name);
        }
        System.out.println("------");
//        //把只有在set集合里的jpg的图像留下
        for (String s : set_picture) {
            ArrayList<String> array = map.get(s);
            BufferedWriter bw = new BufferedWriter(new FileWriter(s.replace(".jpg", ".xml")));
            String xml=multi_object_part_of_xml(array);
            bw.write(xml);
            bw.close();

        }


    }
    //返回所要格式的一些变量
    public static String get_format_variable(String line) throws IOException {
        String r=null;
        String[] string_array=line.split(" ");
        String picture_name=string_array[0];
        File picture = new File("/Users/lidan/Downloads/111/flickr_logos_27_dataset_images/" + picture_name);
        BufferedImage sourceImg = ImageIO.read(new FileInputStream(picture));
        String size_width = String.valueOf(sourceImg.getWidth());
        String size_height = String.valueOf(sourceImg.getHeight());
        String class_name=string_array[1];
        String x1 = string_array[3];
        String y1 = string_array[4];
        String x2 = string_array[5];
        String y2 = string_array[6];
        String res = picture_name + " " + class_name + " "+size_width+" "+size_height+" "+ x1 + " " + y1 + " "  + x2 + " " + y2;
        return res;
    }
    //做成格式
    public static String multi_object_part_of_xml(ArrayList<String> array){
        String file_name=array.get(0).split(" ")[0];
        String width=array.get(0).split(" ")[2];
        String height=array.get(0).split(" ")[3];
        String begin = String.format("<annotation>"+"\n"+
                        "    <folder>VOC2012</folder>"+"\n"+
                        "    <filename>%s</filename>"+"\n"+
                        "    <source>"+"\n"+
                        "    <database>The VOC2007 Database</database>"+"\n"+
                        "    <annotation>PASCAL VOC2007</annotation>"+"\n"+
                        "    <image>flickr</image>"+"\n"+
                        "    </source>"+"\n"+
                        "    <size>"+"\n"+
                        "        <width>%s</width>"+"\n" +
                        "        <height>%s</height>" +"\n"+
                        "        <depth>3</depth>" +"\n"+
                        "    </size>"+"\n" +
                        "    <segmented>0</segmented>"+"\n"
                ,file_name,width,height);

        String s=begin;
        for(int i=0;i<array.size();i++){
            String info=array.get(i);
            String[] temp=info.split(" ");
            s=s+String.format(
                    "    <object>"+"\n" +
                    "        <name>%s</name>"+"\n" +
                    "        <pose>Unspecified</pose>"+"\n" +
                    "        <truncated>0</truncated>"+"\n" +
                    "        <difficult>0</difficult>"+"\n" +
                    "        <bndbox>"+"\n" +
                    "            <xmin>%s</xmin>"+"\n" +
                    "            <ymin>%s</ymin>"+"\n" +
                    "            <xmax>%s</xmax>"+"\n" +
                    "            <ymax>%s</ymax>"+"\n" +
                    "        </bndbox>"+"\n" +
                    "    </object>"+"\n",temp[1],temp[4],temp[5],temp[6],temp[7]) ;
        }
        String end="</annotation>";
        String res_xml=s+end;
        return res_xml;

    }


}
