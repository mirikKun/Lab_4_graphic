import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

public class Photo extends JFrame {
    static ArrayList<String> points = new ArrayList<String>();
    static int len;
    static int masss;
    static int w = 960;
    static int h = 540;
    static Point[] coord;
    static Point[] coordinat;
    static Point[] centralMass;
    static int[] groups;
    static int[] px;
    static int[] py;
    static int[] color;
    static BufferedImage I;
    static BufferedImage image;

    public static void main(String[] args) throws IOException {
        String new_path = "DS9.txt";
        File file = new File(new_path);
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        points.add(line);
        while (line != null) {
            line = reader.readLine();
            if (line != null)
                points.add(line);

        }
        len = points.size();
        coord = new Point[len];
        coordinat = new Point[len];
        groups = new int[len - 1];
        for (int i = 0; i < len - 1; i++) {
            String[] XY = points.get(i).split(" ");

            Point point = new Point(Integer.parseInt(XY[1]), Integer.parseInt(XY[0]));
            coord[i] = point;

        }
        FindMass();
        CreateImage();


    }

    static double distance(int x1, int x2, int y1, int y2) {
        double d;
        d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));

        return d;
    }

    static void FindMass() {
        coordinat[0] = coord[0];
        groups[0] = 1;
        for (int i = 1; i < len - 1; i++) {
            near(coord[i], coordinat, i);
        }

        List<Integer> distinctGroups = Arrays.stream(groups).distinct().boxed().collect(toList());
        masss = distinctGroups.size();

        centralMass = new Point[masss];
        for (int groupIndex = 0; groupIndex < distinctGroups.size(); groupIndex++) {
            int x = 0;
            int y = 0;
            int pointCounter = 0;
            for (int pointIndex = 0; pointIndex < coord.length - 1; pointIndex++) {
                if (distinctGroups.get(groupIndex) == groups[pointIndex]) {
                    x += coord[pointIndex].getX();
                    y += coord[pointIndex].getY();
                    pointCounter++;
                }
            }
            if (pointCounter != 0) {
                centralMass[groupIndex] = new Point(x / pointCounter, y / pointCounter);
            }
        }
    }

    static void near(Point a1, Point[] a2, int g) {
        int k = 0;
        int f = 0;
        int t = 0;
        coordinat[g] = coord[g];
        groups[0] = 1;
        do {

            if ((a1.x + 1 == a2[k].x && a1.y == a2[k].y) || (a1.x == a2[k].x && a1.y - 1 == a2[k].y) || (a1.x - 1 == a2[k].x && a1.y == a2[k].y) || (a1.x == a2[k].x && a1.y + 1 == a2[k].y) || (a1.x - 1 == a2[k].x && a1.y + 1 == a2[k].y) || (a1.x + 1 == a2[k].x && a1.y + 1 == a2[k].y) || (a1.x + 1 == a2[k].x && a1.y - 1 == a2[k].y) || (a1.x - 1 == a2[k].x && a1.y - 1 == a2[k].y)) {
                if (groups[g] == 0) {
                    t = 1;
                    groups[g] = groups[k];
                    f = groups[k];
                } else if (!(f == groups[k])) {

                    int y = groups[k];
                    for (int i = 0; i < groups.length; i++)
                        if (groups[i] == y)
                            groups[i] = f;
                }
            }

            k++;

        } while (a2[k] != null);
        if (t == 0) {
            for (int i = 1; i < len - 1; i++) {
                if (groups[g] <= groups[i]) {
                    groups[g] = groups[i];
                }
            }
            groups[g]++;
        }
    }

    public void paint(Graphics g) {
        g.drawImage(I, 0, 0, this);
    }

    static void CreateImage() throws IOException {

        Random rand = new Random();
        color = new int[masss];
        for (int i = 0; i < masss; i++)
            color[i] = rand.nextInt(16777215);
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int n = 0;
                for (byte i = 0; i < masss; i++) {
                    if (distance(centralMass[i].x, x, h - centralMass[i].y, y) < distance(centralMass[n].x, x, h - centralMass[n].y, y)) {
                        n = i;
                    }
                }
                image.setRGB(x, y, color[n]);


            }
        }

        graphics.setPaint(new Color(0, 0, 0, 20));

        for (int i = 0; i < len - 1; i++) {
            if (groups[i] > 0)
                graphics.fillRect(coord[i].x, h - coord[i].y, 1, 1);

        }
        graphics.setPaint(new Color(200, 0, 0));
        for (Point mass : centralMass) {
            if (mass != null) {
                graphics.fillOval(mass.x - 5, h - mass.y - 5, 10, 10);

            }

        }
        File outFile = new File("lab4_image.png");
        ImageIO.write(image, "png", outFile);

    }
}
