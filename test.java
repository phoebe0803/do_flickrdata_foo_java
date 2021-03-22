package com.experiment;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class test {
    public static void main(String[] args) throws IOException {

        HashMap<String, ArrayList> map= new HashMap<>();
        //读取只有存储集合1的图像
        ///Users/lidan/PycharmProjects/pythonProject8/flickr_logos_27_dataset/flickr_logos_27_dataset_training_set_annotation.txt
        BufferedReader br=new BufferedReader(new FileReader("/Users/lidan/PycharmProjects/pythonProject8/flickr_logos_27_dataset/flickr_logos_27_dataset_training_set_annotation_unique.txt"));
        //BufferedReader br=new BufferedReader(new FileReader("/Users/lidan/PycharmProjects/pythonProject8/flickr_logos_27_dataset/flickr_logos_27_dataset_training_set_annotation_only1.txt"));
        String line=null;
        //依次读取annotation
        while((line=br.readLine())!=null){
            System.out.println(line);
            String[] s=line.split(" ");
            //文件名字作为key 如果没有则创建一个arraylist对象用来保存line
            //如果有key（） 那么直接追加就可以了
            if( map.get(s[0])==null){
                ArrayList<String> list1= new ArrayList<>();
                //list1.add(line);
                list1.add(convert(line));
                map.put(s[0],list1);
            }else{
                //map.get(s[0]).add(line);
               map.get(s[0]).add(convert(line));
            }
        }
        br.close();
        //set集合放的是所有的jpg照片
        Set<String> set =map.keySet();

        //把只有在set集合里的jpg的图像留下
        for(String s:set){
            ArrayList<String> array=map.get(s);
            BufferedWriter bw=new BufferedWriter(new FileWriter(s.replace(".jpg",".txt")));
            for (int i=0;i<array.size();i++){
                System.out.println(s+":"+array.get(i));
                bw.write(array.get(i).replace(s+" ",""));
                bw.write("\n");

            }

            bw.close();
            System.out.println("---");

        }
//        move(set);
    }
    public static void move(Set<String> set_jpg) {
        //将文件移动到指定目录
    //    File file_des = new File("/Users/lidan/untitled8/flickr_logos_27_dataset/flickr_logos_27_dataset_images/all_train_jpg/");
        String path = "/Users/lidan/Downloads/111/flickr_logos_27_dataset_images/";		//要遍历的路径
        File file = new File(path);		//获取其file对象
        File[] fs = file.listFiles();
        for(File f:fs){
            String jpg_name=f.getName();
            for(String set_jpg_name : set_jpg ){
                //jpg图像在所有文件里
                if (set_jpg_name.equals(jpg_name)){
                    String pathname="/Users/lidan/untitled8/flickr_logos_27_dataset/flickr_logos_27_dataset_images/all_train_jpg/"+f.getName();
                    if(f.renameTo(new File(pathname))){
                        System.out.println("sucess");
                    }
                }
            }
            System.out.println(jpg_name);
        }
    }

    public static String convert(String line) throws IOException {
        //'Sprite','Starbucks','Intel','Texaco','Unicef','Vodafone','Yahoo','Adidas','BMW','Citroen','Cocacola','Apple','DHL','Fedex','Ferrari','Google','Ford','Heineken','HP','McDonalds','Mini','Nbc','Nike','Pepsi','Porsche','Puma','RedBull'
        HashMap<String,String> brand_index=new HashMap<>();
        brand_index.put("Sprite","0");
        brand_index.put("Starbucks","1");
        brand_index.put("Intel","2");
        brand_index.put("Texaco","3");
        brand_index.put("Unicef","4");
        brand_index.put("Vodafone","5");
        brand_index.put("Yahoo","6");
        brand_index.put("Adidas","7");
        brand_index.put("BMW","8");
        brand_index.put("Citroen","9");
        brand_index.put("Cocacola","10");
        brand_index.put("Apple","11");
        brand_index.put("DHL","12");
        brand_index.put("Fedex","13");
        brand_index.put("Ferrari","14");
        brand_index.put("Google","15");
        brand_index.put("Ford","16");
        brand_index.put("Heineken","17");
        brand_index.put("HP","18");
        brand_index.put("McDonalds","19");
        brand_index.put("Mini","20");
        brand_index.put("Nbc","21");
        brand_index.put("Nike","22");
        brand_index.put("Pepsi","23");
        brand_index.put("Porsche","24");
        brand_index.put("Puma","25");
        brand_index.put("RedBull","26");
        String r=null;
        String[] string_array=line.split(" ");
        String name=string_array[0];
        //文件大小
        try {
                //为了读取原图的大小
                File picture = new File("/Users/lidan/Downloads/111/flickr_logos_27_dataset_images/" + name);
                BufferedImage sourceImg = ImageIO.read(new FileInputStream(picture));
                double size_width = sourceImg.getWidth();
                double size_height = sourceImg.getHeight();
                //------

                int class_index = Integer.parseInt(brand_index.get(string_array[1]));
                double x1 = Integer.parseInt(string_array[3]);
                double y1 = Integer.parseInt(string_array[4]);
                double x2 = Integer.parseInt(string_array[5]);
                double y2 = Integer.parseInt(string_array[6]);
                double w = 0.0;
                double h = 0.0;
                double dw = 1.0 / size_width;
                double dh = 1.0 / size_height;
                if (x2 - x1 != 0.0) {
                    w = x2 - x1;
                }
                if (y2 - y1 != 0.0) {
                    h = y2 - y1;
                }
                double center_x = ((x1 + x2) / 2.0) * dw;
                double center_y = ((y1 + y2) / 2.0) * dh;
                w = w * dw;
                h = h * dh;
                r = name + " " + class_index + " " + center_x + " " + center_y + " "  + w + " " + h;
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return r;
    }
}
