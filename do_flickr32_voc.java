package com.experiment;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class do_flickr32_voc {
    public static void main(String[] args) throws IOException {
        HashMap<String, ArrayList> map = new HashMap<>();
        //读取图像 索引
        BufferedReader br_path = new BufferedReader(new FileReader("/Users/lidan/Public/FlickrLogos-v2/testset.relpaths.txt"));
        String line=null;
        //根据这个路径去读每一个要训练的图像路径
        while ((line = br_path.readLine()) != null) {
            if(line.contains("no-logo")){
                continue;
            }else {
                System.out.println(line);
                String class_info=line.split("/")[2];
                String img=line.split("/")[3];
                System.out.println(class_info);
                String image_size=get_image_size(line);
                System.out.println(image_size);
                //去读取所有的annatation
                ArrayList<String> array_annotations=get_every_image_annotations(class_info,img);
                ArrayList<String> array_annotations_complete=new ArrayList<>();
                for(String annoation_line:array_annotations){
                    System.out.println(annoation_line);
                    String combine_info=null;
                    //picture_name + " " + class_name + " "+size_width+" "+size_height+" "+ x1 + " " + y1 + " "  + x2 + " " + y2;
                    combine_info=img+" "+class_info+" "+image_size+" "+annoation_line;
                    System.out.println(combine_info);
                    array_annotations_complete.add(combine_info);
                }
                String xml_path="/Users/lidan/Public/FlickrLogos-v2/classes/annotations_test/"+img.replace(".jpg",".xml");
                BufferedWriter bw = new BufferedWriter(new FileWriter(xml_path));
                String xml=multi_object_part_of_xml(array_annotations_complete);
                bw.write(xml);
                bw.close();
            }

        }
        // /Users/lidan/Public/FlickrLogos-v2/classes/masks/adidas/3068575660.jpg.bboxes.txt
    }
    public static  ArrayList get_every_image_annotations(String class_info,String img) throws IOException {
        String image_annotation_path="/Users/lidan/Public/FlickrLogos-v2/classes/masks/"+class_info+"/"+img+".bboxes.txt";
        BufferedReader br_image_annotation = new BufferedReader(new FileReader(image_annotation_path));
        String line=null;
        ArrayList<String> annotations=new ArrayList();
        while ((line=br_image_annotation.readLine())!=null){
            if(line.contains("x")){
                continue;
            }else{
                String[] temp_annoation_will_change=null;
                temp_annoation_will_change=line.split(" ");
                int x1=Integer.parseInt(temp_annoation_will_change[0]);
                int y1=Integer.parseInt(temp_annoation_will_change[1]);
                int w=Integer.parseInt(temp_annoation_will_change[2]);
                int h=Integer.parseInt(temp_annoation_will_change[3]);
                int x2=x1+w;
                int y2=y1+h;
                line=x1 + " " + y1 + " "  + x2 + " " + y2;
                annotations.add(line);
            }

        }
        return annotations;



    }

    public static String get_image_size(String img_path) throws IOException{
        String image_size=null;
        BufferedImage sourceImg = ImageIO.read(new FileInputStream("/Users/lidan/Public/FlickrLogos-v2/"+img_path));
        String size_width = String.valueOf(sourceImg.getWidth());
        String size_height = String.valueOf(sourceImg.getHeight());
        image_size=size_width+" "+size_height;
        return image_size;

    }
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
