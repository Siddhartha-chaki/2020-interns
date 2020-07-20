/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viusalizer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Graph extends JPanel {
    private final static int padding = 25;
    private final static int labelPadding = 25;
    private final static int pointWidth = 4;
    private final static int numberYDivisions = 10;
    private final static Color lineColor = new Color(44, 102, 230, 180);
    private final static Color pointColor = new Color(100, 100, 100, 180);
    private final static Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke graphStroke = new BasicStroke(2f);
    
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private List<Double> data_points;
    private List<String> data_labels;

    public Graph(List<Double> data_points,List<String> labels) {
        this.data_points = data_points;
        data_labels=labels;
    }


    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (!(graphics instanceof Graphics2D)) {
            graphics.drawString("Graphics is not Graphics2D, unable to render", 0, 0);
            return;
        }
        final Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        final int width = getWidth();
        final int height = getHeight();
        final double maxScore = getMaxValue();
        final double minScore = getMinValue();
        final double scoreRange = maxScore - minScore;
        
        double xScale = ((double) width - (2 * padding) - labelPadding) / (data_points.size() - 1);
        double yScale = ((double) height - 2 * padding - labelPadding) / scoreRange;

        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < data_points.size(); i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxValue() - data_points.get(i)) * yScale + padding);
            graphPoints.add(new Point(x1, y1));
        }

        // draw white background
        g.setColor(Color.WHITE);
        g.fillRect(padding + labelPadding, padding, 
                width - (2 * padding) - labelPadding,
                height - 2 * padding - labelPadding);
        g.setColor(Color.BLACK);
        
        final FontMetrics fontMetrics = g.getFontMetrics();
        final int fontHeight = fontMetrics.getHeight();

        // create hatch marks and grid lines for y axis.
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = height - ((i * (height - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            int y1 = y0;
            if (data_points.size() > 0) {
                g.setColor(gridColor);
                g.drawLine(padding + labelPadding + 1 + pointWidth, y0, width - padding, y1);
                g.setColor(Color.BLACK);
                String yLabel = ((int) ((getMinValue() + (scoreRange) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
                int labelWidth = fontMetrics.stringWidth(yLabel);
                g.drawString(yLabel, x0 - labelWidth - 5, y0 + (fontHeight / 2) - 3);
            }
            g.drawLine(x0, y0, x1, y1);
        }

        // and for x axis
        int ipre=-1;
        for (int i = 0; i < data_points.size(); i++) {
            if (data_points.size() > 1) {
                int x0 = i * (width - padding * 2 - labelPadding) / (data_points.size() - 1) + padding + labelPadding;
                int x1 = x0;
                int y0 = height - padding - labelPadding;
                int y1 = y0 - pointWidth;
                if ((i % ((int) ((data_points.size() / 20.0)) + 1)) == 0) {
                    g.setColor(gridColor);
                    g.drawLine(x0, height - padding - labelPadding - 1 - pointWidth, x1, padding);
                    g.setColor(Color.BLACK);
                    if(ipre<i){
                        String xLabel = data_labels.get(i);
                        int labelWidth = fontMetrics.stringWidth(xLabel);
                        g.drawString(xLabel, x0 - labelWidth / 2, y0 + fontHeight + 3);
                        ipre=i;
                    }
                }
                g.drawLine(x0, y0, x1, y1);
            }
        }

        // create x and y axes 
        g.drawLine(padding + labelPadding, height - padding - labelPadding, padding + labelPadding, padding);
        g.drawLine(padding + labelPadding, height - padding - labelPadding, width - padding, height - padding - labelPadding);

        Stroke oldStroke = g.getStroke();
        g.setColor(lineColor);
        g.setStroke(GRAPH_STROKE);
        
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g.drawLine(x1, y1, x2, y2);
        }

        boolean drawDots = width > (data_points.size() * pointWidth);
        if (drawDots) {
            g.setStroke(oldStroke);
            g.setColor(pointColor);
            for(Point graphPoint : graphPoints){
               int x = graphPoint.x - pointWidth / 2;
                int y = graphPoint.y - pointWidth / 2;
                g.fillOval(x, y, pointWidth, pointWidth); 
            }
        }
    }

//    @Override
//    public Dimension getPreferredSize() {
//        return new Dimension(width, height);
//    }
    
    public void addValues(List<Double> newValues) {
        data_points.clear();
        data_points.addAll(newValues);
        updateUI();
    }
    
    private double getMinValue() {
        double minScore = Double.MAX_VALUE;
        for (Double score : data_points) {
            minScore = Math.min(minScore, score);
        }
        return minScore;
    }

    private double getMaxValue() {
        double maxScore = Double.MIN_VALUE;
        for (Double score : data_points) {
            maxScore = Math.max(maxScore, score);
        }
        return maxScore;
    }

    public void setPoints(List<Double> data_points) {
        this.data_points = data_points;
        invalidate();
        this.repaint();
    }

    public List<Double> getPoints() {
        return data_points;
    }
     void plot(ArrayList<Double> scores,ArrayList<String> labels){
        Graph mainPanel = new Graph(scores,labels);
        mainPanel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("DrawGraph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
//    private static void createAndShowGui() {
//        List<Double> data_points = new ArrayList<>();
//        Random random = new Random();
//        int maxDataPoints = 40;
//        int maxScore = 10;
//        for (int i = 0; i < maxDataPoints; i++) {
//            data_points.add((double) random.nextDouble() * maxScore);
////            data_points.add((double) i);
//        }
//        Graph mainPanel = new Graph(data_points);
//        mainPanel.setPreferredSize(new Dimension(800, 600));
//        JFrame frame = new JFrame("DrawGraph");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().add(mainPanel);
//        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//    }
    
//    public static void main(String[] args) {
//      SwingUtilities.invokeLater(new Runnable() {
//         public void run() {
//            createAndShowGui();
//         }
//      });
//   }
    

