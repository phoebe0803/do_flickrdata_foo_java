package com.experiment;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class do_flickr32_to_yolov5  {
    public static void main(String[] args) throws IOException {
            HashMap<String, ArrayList> map = new HashMap<>();
            BufferedReader br_path = new BufferedReader(new FileReader("/Users/lidan/Public/FlickrLogos-v2/testset.relpaths.txt"));
            String line=null;
            //根据这个路径去读每一个要训练的图像路径
            while ((line = br_path.readLine()) != null) {
                if(line.contains("no-logo")){
                    continue;
                }else{
                    System.out.println(line);
                    String class_info=line.split("/")[2];
                    String img=line.split("/")[3];
                    System.out.println(class_info);
                    String image_size=get_image_size(line);
                    //去读取annotations
                    String get_convert=null;
                    ArrayList<String> array_annotations=get_every_image_annotations(class_info,img);
                    String xml_path="/Users/lidan/Public/FlickrLogos-v2/classes/annotations_yolov5_test/"+img.replace(".jpg",".txt");
                    BufferedWriter bw = new BufferedWriter(new FileWriter(xml_path));
                    for (String annotations_line : array_annotations ){
                        get_convert=convert(class_info,annotations_line,image_size);
                        System.out.println(get_convert);
                        bw.write(get_convert);
                        bw.write("\n");
//

                    }
                    bw.close();




                }

            }

        }

    public static String get_image_size(String img_path) throws IOException{
        String image_size=null;
        BufferedImage sourceImg = ImageIO.read(new FileInputStream("/Users/lidan/Public/FlickrLogos-v2/"+img_path));
        String size_width = String.valueOf(sourceImg.getWidth());
        String size_height = String.valueOf(sourceImg.getHeight());
        image_size=size_width+" "+size_height;
        return image_size;

    }


    public static ArrayList get_every_image_annotations(String class_info,String img ) throws IOException {
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

    public static String convert(String class_info,String annotation_line,String image_size) throws FileNotFoundException {
        HashMap<String,String> brand_index=new HashMap<>();
        brand_index.put("HP","0");
        brand_index.put("chimay","1");
        brand_index.put("ferrari","2");
        brand_index.put("starbucks","3");
        brand_index.put("adidas","4");
        brand_index.put("cocacola","5");
        brand_index.put("ford","6");
        brand_index.put("nvidia","7");
        brand_index.put("stellaartois","8");
        brand_index.put("aldi","9");
        brand_index.put("corona","10");
        brand_index.put("fosters","11");
        brand_index.put("paulaner","12");
        brand_index.put("texaco","13");
        brand_index.put("apple","14");
        brand_index.put("dhl","15");
        brand_index.put("google","16");
        brand_index.put("pepsi","17");
        brand_index.put("tsingtao","18");
        brand_index.put("becks","19");
        brand_index.put("erdinger","20");
        brand_index.put("guiness","21");
        brand_index.put("rittersport","22");
        brand_index.put("ups","23");
        brand_index.put("bmw","24");
        brand_index.put("esso","25");
        brand_index.put("heineken","26");
        brand_index.put("shell","27");
        brand_index.put("carlsberg","28");
        brand_index.put("fedex","29");
        brand_index.put("milka","30");
        brand_index.put("singha","31");
        String class_index=brand_index.get(class_info);
        String r=null;
        String[] string_array=annotation_line.split(" ");
        double size_width = Double.parseDouble(image_size.split(" ")[0]);
        double size_height =  Double.parseDouble(image_size.split(" ")[1]);
        double x1 = Integer.parseInt(string_array[0]);
        double y1 = Integer.parseInt(string_array[1]);
        double w = Integer.parseInt(string_array[2]);
        double h = Integer.parseInt(string_array[3]);
        double x2=x1+w;
        double y2=y1+h;
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
        r = class_index + " " + center_x + " " + center_y + " "  + w + " " + h;

        return r;

    }

}
