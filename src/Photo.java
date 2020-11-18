import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.*;
import java.util.ArrayList;

public class Photo
{
    static ArrayList<String> points= new ArrayList<String>();
    static int len;
    static Point[] coord;
    static Point[] coordinat;
    public static void main(String[] args) throws IOException {
        String new_path = "DS9.txt";
        File file = new File(new_path);
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        points.add(line);
        while (line != null) {
            line = reader.readLine();
            if(line!=null)
            points.add(line);

        }
        len=points.size();
        coord=new Point[len];
        coordinat=new Point[len];
        for(int i = 0; i < len-1; i++)
        {
            String[] XY = points.get(i).split(" ");

            Point point =new Point(Integer.parseInt(XY[1]),Integer.parseInt(XY[0]));
            coord[i]=point;

        }
        FindShape();
        CreateImahe();


    }
static  void FindShape() throws IOException {
int j=0;
        Point point =new Point(0,0);
    for(int i = 0; i < len-1; i++)
    {
        if(coord[i].x>point.x)
            point.x=coord[i].x;
    }
    for(int i = 0; i < len-1; i++)
    {
        if(coord[i].y>point.y && coord[i].x==point.x) {
            point.y = coord[i].y;
            j=i;
        }

    }
    coordinat[0]=point;
    coord[j]=coord[0];
    coord[0]=coordinat[0];
    j=0;
    int min=0;
    do
    {
        for (int i = 1; i < len-1; i++)
        {
            if (FindVectMult(coordinat[j], coord[min],  coord[i]) < 0 || FindVectMult(coordinat[j], coord[min],  coord[i]) == 0 && FindDist(coordinat[j], coord[min]) < FindDist(coordinat[j], coord[i]))
            {
                min = i;
            }
        }
        j++;
        coordinat[j] = coord[min];

        min = 0;
    }
    while (!(coordinat[j].x == coordinat[0].x && coordinat[j].y == coordinat[0].y));


}
static int FindDist(Point dot1,Point dot2)
{
    return (dot2.x - dot1.x) * (dot2.x - dot1.x) + (dot2.y - dot1.y) * (dot2.y - dot1.y);
}
static long FindVectMult(Point dot1,Point dot2,Point dot3)
    {
        return (long)(dot2.x - dot1.x) * (dot3.y - dot1.y) - (long)(dot3.x- dot1.x) * (dot2.y - dot1.y);
    }
   static void CreateImahe() throws IOException {
    int w = 960;
    int h = 560;
    BufferedImage image = new BufferedImage(w, h, ColorSpace.TYPE_RGB);
    Graphics2D graphics = image.createGraphics();
       Graphics graphic = image.createGraphics();

   graphics.setPaint ( new Color ( 200, 200, 200 ) );
   graphics.fillRect (1 ,1, w, h);
       graphics.setPaint ( new Color ( 0, 0, 0 ) );
      for(int i = 0; i < len-1; i++)
   {
       graphics.fillRect (coord[i].x ,h-coord[i].y, 1, 1);

         }
       graphics.setPaint ( new Color ( 0, 0, 250 ) );
       for(int i = 0; i < len-1; i++)
       {
           if(coordinat[i+1]!=null) {
               graphics.drawLine(coordinat[i].x,h- coordinat[i].y, coordinat[i + 1].x,h- coordinat[i + 1].y);

           }
   }

       File outFile = new File("lab3_image.png");
       ImageIO.write(image, "png", outFile);
}
}
