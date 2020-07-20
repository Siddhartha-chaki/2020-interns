/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viusalizer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Phoenix
 */
public class FileOperations {
    JSONObject getJSONObjFromFile(String filename) throws FileNotFoundException, IOException, ParseException{
        return (JSONObject) new JSONParser().parse(new FileReader(filename)); 
    }
    public ArrayList<Double> getValues(SortedMap oobj,String currency){
        ArrayList<Double> v = new ArrayList<Double>();
        Iterator<Map.Entry> itr1 = oobj.entrySet().iterator(); 
         while (itr1.hasNext()) { 
             Map.Entry pair = itr1.next(); 
             JSONObject prices=(JSONObject)pair.getValue();
             v.add((double)prices.get(currency)); 
         } 
        return v;
    }
    public ArrayList<Double> getValues(JSONObject oobj,String currency){
        ArrayList<Double> v = new ArrayList<Double>();
        Iterator<Map.Entry> itr1 = oobj.entrySet().iterator(); 
         while (itr1.hasNext()) { 
             Map.Entry pair = itr1.next(); 
             JSONObject prices=(JSONObject)pair.getValue();
             v.add((double)prices.get(currency)); 
         } 
        return v;
    }
    public NavigableMap processData(JSONObject jo){
        NavigableMap<String,HashMap> data=new TreeMap<String,HashMap>();
        Map mp=((Map)(jo.get("rates")));
        Iterator<Map.Entry> itr1 = mp.entrySet().iterator(); 
        
         while (itr1.hasNext()) { 
             Map.Entry pair = itr1.next(); 
             String s=pair.getKey().toString();
             HashMap dt=(HashMap)pair.getValue();
             data.put(s, dt);
         }
         System.out.println(data.keySet());
         return data;
    }
}
