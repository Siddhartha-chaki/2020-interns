/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viusalizer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONObject; 
import org.json.simple.parser.*;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
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
    
    public static void main(String[] args) throws IOException, ParseException, java.text.ParseException{
        Viusalizer vz=new Viusalizer();
        FileOperations fop=new FileOperations();
        JSONObject jo=fop.getJSONObjFromFile("./data/data.json");
        NavigableMap<String,HashMap> data=fop.processData(jo);
        new UserInterface(data).setVisible(true);
    }

 }  

class UserInterface extends JFrame{
    private JButton eurbtton = new JButton("INR vs EUR from 1 Jan 2019 to 31 Jan 2019");
    private JButton gbpbtton = new JButton("GBP,INR vs EUR from 1 Jan 2019 to 31 Jan 2019");
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
            List<List> data_points=new ArrayList<>();
            SortedMap<String,HashMap> subdata=data.subMap("2019-01-01","2019-01-31");
            data_points.add(fop.getValues(subdata, "INR"));
            ArrayList<String> titles=new ArrayList<>();titles.add("INR");
            ArrayList<Color> colors=new ArrayList<>();colors.add(Color.BLUE);
            MultiLine mainPanel=new MultiLine(data_points,new ArrayList(subdata.keySet()),1,titles,colors);
            mainPanel.setPreferredSize(new Dimension(800, 600));
            JDialog mydialog = new JDialog();
            mydialog.setSize(new Dimension(1000,600));
            mydialog.setTitle("INR exchange rate against EUR from 1 Jan 2019 to 31 Jan 2019");
            mydialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL); // prevent user from doing something else
            mydialog.add(mainPanel);
            mydialog.setVisible(true);
            SwingUtilities.updateComponentTreeUI(this);
        });
        gbpbtton.addActionListener((e) -> {
            List<List> data_points=new ArrayList<>();
            SortedMap<String,HashMap> subdata=data.subMap("2019-01-01","2019-01-31");
            data_points.add(fop.getValues(subdata, "INR"));
            data_points.add(fop.getValues(subdata, "GBP"));
            ArrayList<String> titles=new ArrayList<>();titles.add("INR");titles.add("GBP");
            ArrayList<Color> colors=new ArrayList<>();colors.add(Color.RED);colors.add(Color.GREEN);
            MultiLine mainPanel=new MultiLine(data_points,new ArrayList(subdata.keySet()),2,titles,colors);
            mainPanel.setPreferredSize(new Dimension(800, 600));
            JDialog mydialog = new JDialog();
            mydialog.setSize(new Dimension(1000,600));
            mydialog.setTitle("INR and GBP exchange rate against EUR from 1 Jan 2019 to 31 Jan 2019");
            mydialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL); // prevent user from doing something else
            mydialog.add(mainPanel);
            mydialog.setVisible(true);
            SwingUtilities.updateComponentTreeUI(this);
        });
        latest_gbpbtton.addActionListener((e) -> {

        });
        add(centerPanel, BorderLayout.CENTER);
        setSize(1000, 500);

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