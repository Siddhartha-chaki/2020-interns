/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viusalizer;
import java.io.FileReader; 
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator; 
import java.util.Map; 
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject; 
import org.json.simple.parser.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComboBox;
import javax.swing.JFrame;
/**
 *
 * @author Phoenix
 */
public class Viusalizer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ParseException{
        Viusalizer vz=new Viusalizer();
        // parsing file "JSONExample.json" 
        
        Object obj = new JSONParser().parse(new FileReader("./data/data.json")); 
          
        // typecasting obj to JSONObject 
        JSONObject jo = (JSONObject) obj; 
        JSONObject jo1 = (JSONObject) jo.get("rates");
        Map data = ((Map)(jo.get("rates")));
        
        Set dates=data.keySet();
        System.out.println(dates);
        ArrayList<Double> d=vz.getValues(jo1, "INR");
        System.out.println(d);
        vz.plot(d);
        
    }
    private ArrayList<Double> getValues(JSONObject oobj,String currency){
        ArrayList<Double> v = new ArrayList<Double>();
        Iterator<Map.Entry> itr1 = oobj.entrySet().iterator(); 
         while (itr1.hasNext()) { 
             Map.Entry pair = itr1.next(); 
             JSONObject prices=(JSONObject)pair.getValue();
             v.add((double)prices.get(currency)); 
         } 
        return v;
    }
    void plot(ArrayList<Double> scores){
        Graph mainPanel = new Graph(scores);
        mainPanel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("DrawGraph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
}
//class Graph extends JFrame{
//    ArrayList<Double> data;
//    public Graph(String t,ArrayList<Double> data){
//        this.data=data;
//        setTitle(t);
//        setSize(900,700);
//        setLayout(null);
//        setVisible(true);
//        setResizable(false);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
//    public void paint(Graphics g){
//        g.setColor(Color.BLACK);
//        int n=data.size();
//        int xp=0,yp=data.get(0).intValue();
//        
//        for(int i=1;i<data.size();i++){
//            int yi=data.get(i).intValue();
//            g.drawLine(xp*10, yp, i*10,yi); 
//            xp=i;yp=yi;
//        }
//        
//    }
//}
