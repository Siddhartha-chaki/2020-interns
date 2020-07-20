/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viusalizer;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject; 
import org.json.simple.parser.*;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.NavigableMap;
import java.util.SortedMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
/**
 *
 * @author Phoenix
 */
public class Viusalizer {

    /**
     * @param args the command line arguments
     */
    Graph mainPanel;
    public static void main(String[] args) throws IOException, ParseException, java.text.ParseException{
        Viusalizer vz=new Viusalizer();
        FileOperations fop=new FileOperations();
        JSONObject jo=fop.getJSONObjFromFile("./data/data.json");
        
        NavigableMap<String,HashMap> data=fop.processData(jo);
        ArrayList<Double> v=fop.getValues(data, "INR");
        
        SortedMap<String,HashMap> subdata=data.subMap("2019-01-01","2019-01-31");
        
//        vz.plot(fop.getValues(subdata, "INR"), new ArrayList(subdata.keySet()));
        
        ArrayList<Double> v2=fop.getValues(subdata, "GBP");
        
//        vz.mainPanel.addValues(v2);
        new UserInterface(data).setVisible(true);
        
    }
    
    void plot(ArrayList<Double> scores,ArrayList<String> labels){
        mainPanel = new Graph(scores,labels);
        mainPanel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("DrawGraph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

 }

//   
//    private SortedMap processData(JSONObject jo){
//        SortedMap<Date,HashMap> data=new TreeMap<Date,HashMap>();
//        Map mp=((Map)(jo.get("rates")));
//        Iterator<Map.Entry> itr1 = mp.entrySet().iterator(); 
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//         while (itr1.hasNext()) { 
//             Map.Entry pair = itr1.next(); 
//             String s=pair.getKey().toString();
//            try {
//                Date ds=sdf.parse(s);
//                HashMap dt=(HashMap)pair.getValue();
//                data.put(ds, dt);
//            } catch (java.text.ParseException ex) {
//                System.out.println(ex);
//            }   
//         }
//         return data;
//    }

//        ArrayList<String> lables=new ArrayList<>(rowdata.keySet());
//        System.out.println(lables);
        
//        ArrayList<Double> d1=vz.getValues(jo1, "INR");
//
//        ArrayList<Double> d3=vz.getValues(jo1, "GBP");
//        
//
//        SimpleDateFormat sdfo=new SimpleDateFormat("yyyy-MM-dd");
//        
//        ArrayList<Date> lbs=new ArrayList<>();
//        for(Object dt:lables){
//            try {
//                lbs.add(sdfo.parse(dt.toString()));
//            } catch (java.text.ParseException ex) {
//                System.out.println(ex);
//            }
//        }
        
   
    

class UserInterface extends JFrame{
    private JButton eurbtton = new JButton("INR vs EUR");
    private JButton gbpbtton = new JButton("GBP,INR vs EUR");
    private JButton latest_gbpbtton = new JButton("GBP,INR vs EUR (Latest)");
    private JButton closeButton = new JButton("Close");
    JPanel centerPanel;
    NavigableMap<String, HashMap> data;
    Viusalizer vz;
    FileOperations fop;
    public UserInterface(NavigableMap<String, HashMap> data) {
        this.data=data;
        vz=new Viusalizer();
        fop=new FileOperations();
        mainInterface();

    }

    private void mainInterface() {
        setTitle("Currency prices with respect to EUR");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel centerPanel = new JPanel(new GridLayout(5, 3));
        centerPanel.add(eurbtton);
        centerPanel.add(gbpbtton);
        centerPanel.add(latest_gbpbtton);
        centerPanel.add(closeButton);
        eurbtton.addActionListener((e) -> {
            SortedMap<String,HashMap> subdata=data.subMap("2019-01-01","2019-01-31");
            Graph mainPanel = new Graph(fop.getValues(subdata, "INR"), new ArrayList(subdata.keySet()));
            mainPanel.setPreferredSize(new Dimension(800, 600));
            JDialog mydialog = new JDialog();
            mydialog.setSize(new Dimension(1000,600));
            mydialog.setTitle("INR exchange rate against EUR from 1 Jan 2019 to 31 Jan 2019");
            mydialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL); // prevent user from doing something else
            mydialog.add(mainPanel);
            mydialog.setVisible(true);
            
            SwingUtilities.updateComponentTreeUI(this);
        });
        add(centerPanel, BorderLayout.CENTER);
        setSize(1000, 500);
//        setVisible(true);
    }

    private void addReportPanel() {
        JPanel reportPanel = createNewPanel();
        getContentPane().add(reportPanel, BorderLayout.CENTER);

    }

    private JPanel createNewPanel() {
        JPanel localJPanel = new JPanel();
        localJPanel.setLayout(new FlowLayout());
        return localJPanel;
    }

}