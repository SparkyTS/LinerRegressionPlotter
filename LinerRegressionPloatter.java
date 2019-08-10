package linerRegression;

import javafx.scene.shape.Line;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


class Point {
    double x,y;

    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}

public class LinerRegressionPloatter {

    static class Canvas extends JPanel{
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.setColor(Color.WHITE);
            for(Point point : points){
                double x = map(point.getX(),0,1,0,getWidth());
                double y = map(point.getY(),0,1,getHeight(),0);
                g.fillOval((int)x-5,(int)y-5,10,10);
            }
            g.setColor(Color.PINK);
            if(points.size() > 1)
            g.drawLine((int)line.getStartX(),(int)line.getStartY(),(int)line.getEndX(),(int)line.getEndY());
        }
    }

    static MouseAdapter putPoint = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            points.add(new Point(map(e.getX(),0,canvas.getWidth(),0,1),
                                 map(e.getY(),0,canvas.getHeight(),1,0)));
            calculateSlope();
            addLine();
            canvas.repaint();
        }
    };

    static JFrame frame = new JFrame("LR display");
    static ArrayList<Point> points = new ArrayList<>();
    static Canvas canvas = new Canvas();
    static double m=1,b=0;
    static Line line = new Line(0,0,0,0);

    public static void main(String[] args){
        canvas.addMouseListener(putPoint);
        frame.setContentPane(canvas);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(600,600);
        frame.setVisible(true);
        frame.repaint();
        canvas.setBackground(Color.BLACK);
    }

    private static void calculateSlope() {
        double sum_x=0,sum_y=0;

        for(Point point:points){
            sum_x += point.x;
            sum_y += point.y;
        }

        double avg_x = sum_x / points.size();
        double avg_y = sum_y / points.size();

        double num=0,den=0;

        for(Point point:points){
            num += (point.x-avg_x) * (point.y-avg_y);
            den += (point.x - avg_x) * (point.x - avg_x);
        }

        if(den != 0)
        m = num / den;
        b = avg_y - m * avg_x;
    }

    static double map(double x, double in_min, double in_max, double out_min, double out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    private static void addLine() {
//        double x1,x2,y1,y2;
//        x1 = 0;
//        y1 = m * x1 + b;
//        x2 = 1;
//        y2 = m * x2 + b;

        line.setStartX(map(0,0,1,0,canvas.getWidth()));
        line.setStartY(map(b,0,1,canvas.getHeight(),0));
        line.setEndX(map(1,0,1,0,canvas.getWidth()));
        line.setEndY(map(m+b,0,1,canvas.getHeight(),0));
    }
}
