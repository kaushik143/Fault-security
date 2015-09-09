/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PSOFaultSecure;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author SAPVI
 */
public class ReadFile {
    
    public static Vector <Object> operation;
   
    
    GlobalConstants gc;
    public ReadFile(){
       gc = GlobalConstants.getInstance();
   }    
  public synchronized  void  readDFG(String path){
           operation=new <Object>Vector();
        //String path="C:/Users/SAPVI/Documents/DAP Files new/New folder/JPEG_IDCT.txt";
        System.out.println("Start Read_DFG");
        try{
  // Open the file that is the first 
    FileInputStream fstream = new FileInputStream(path);
  // Get the object of DataInputStream
  DataInputStream in = new DataInputStream(fstream);
  BufferedReader br = new BufferedReader(new InputStreamReader(in));
  String strLine;
  String dilim=",";
  String token="";
  
  //Read File Line By Line
  while ((strLine = br.readLine()) != null){
  // Print the content on the console
  System.out.println (strLine);
  StringTokenizer tok = new StringTokenizer(strLine,dilim,false);
  while (tok.hasMoreTokens()) {
    token = tok.nextToken();
    operation.add(token);
    token = tok.nextToken();
    operation.add(Integer.parseInt(token));
    token = tok.nextToken();
    operation.add(Integer.parseInt(token));
    token = tok.nextToken();
    operation.add(Integer.parseInt(token));
  
         
  }
  }
  gc.no_operation=operation.size()/4;
  gc.operation=operation;
  //Close the input stream
  
  in.close();
    }catch (Exception e){//Catch exception if any
  System.err.println("Error: " + e.getMessage());
  }
          System.out.println("End Read_DFG");
  }      
  
 
     public synchronized  void read_detail(String path){
     System.out.println("Start Read_Detail");
    // String path="C:/Users/SAPVI/Documents/DAP Files new/module_library_JPEGIDCT.txt";
     try{
  // Open the file that is the first 
    FileInputStream fstream = new FileInputStream(path);
  // Get the object of DataInputStream
  DataInputStream in = new DataInputStream(fstream);
  BufferedReader br = new BufferedReader(new InputStreamReader(in));
  String strLine;
  String c;
  int j=0;
  String dilim=",";
  String token[]=new String[5];
  Vector<Object> max_component_detail=new <Object>Vector();
  //double component_area[];
  //double component_spower[];
  //double component_dpower[];
  //double component_clockcycle[];
 // String component_name[];
 // int max[];
  int i=0;
  //Read File Line By Line
  while ((strLine = br.readLine()) != null){
  // Print the content on the console
     // System.out.println(strLine);
   StringTokenizer tok = new StringTokenizer(strLine,dilim,false);
   // System.out.println("testing1");
   while (tok.hasMoreTokens()) {
      
    token[0] = tok.nextToken();
     
    //operation.add(token);
    token[1] = tok.nextToken();
    //operation.add(Integer.parseInt(token));
    token[2] = tok.nextToken();
    
    //operation.add(Integer.parseInt(token));
    token[3] = tok.nextToken();
    token[4] = tok.nextToken();
   // System.out.println(token[0]+"  "+token[1]+"  "+token[2]+"  "+token[3]);
    if(Integer.parseInt(token[4])!=0){
        gc.size++;
     max_component_detail.add(token[0].subSequence(0, 3));
   
    max_component_detail.add(Integer.parseInt(token[4]));
     
    gc.component_name[i]=(token[0].subSequence(0, 3)+"");
    if(gc.component_name[i].equalsIgnoreCase("mul"))
        gc.operator[i]="*";
    if(gc.component_name[i].equalsIgnoreCase("add"))
        gc.operator[i]="+";
    if(gc.component_name[i].equalsIgnoreCase("sub"))
        gc.operator[i]="-";
     if(gc.component_name[i].equalsIgnoreCase("com"))
        gc.operator[i]="<";
    gc.max[i]=Integer.parseInt(token[4]);
    gc.vel_range[i]=((1+gc.max[i])/2);
   // System.out.println(token[4]+"   ");
   // System.out.println("test"+ gc.max[i]);
    gc.component_dpower[i]=Double.parseDouble(token[1].substring(0,token[1].length()-2));
    gc.component_area[i]=Double.parseDouble(token[2].substring(0,token[2].length()-2));
   // System.out.println("testin...3  "+token[2].substring(0,token[2].length()-2));
    gc.component_clockcycle[i]=Integer.parseInt(token[3].substring(0,token[3].length()-2));
   // System.out.println(gc.component_name[i]+"   "+gc.max[i]+"  "+gc.component_area[i]);
    i++;
    }
    else{
         String mux=(token[0].subSequence(0, 3)+"");
         if(mux.equalsIgnoreCase("mux")){
          gc.mux_dpower=Double.parseDouble(token[1].substring(0,token[1].length()-2));
          gc.mux_area=Double.parseDouble(token[2].substring(0,token[2].length()-2));
          gc.mux_delay=Integer.parseInt(token[3].substring(0,token[3].length()-2));   
         }
    }
          
  }
   
 
  }
  //add unrolling factor in component name , max, maxcomponent
    
    //gc.vel_range[i]=((1-gc.max[i])/2);
  for(int k=0;k<i;k++){
 System.out.println("Dynamic power= "+gc.component_dpower[k]);
 System.out.println("Area= "+gc.component_area[k]);
 System.out.println("Delay= "+gc.component_clockcycle[k]);
  }
  System.out.println("muxpower="+gc.mux_dpower+"   muxarea"+gc.mux_area+"   muxdelay"+gc.mux_delay );
  
 
  gc.max_component_detail=max_component_detail;
  
   Enumeration e=gc.max_component_detail.elements();
  //System.out.println("The elements of vector: " + vector);
  while(e.hasMoreElements()){
  System.out.println("The elements are: " + e.nextElement());
  }
  in.close();
    }catch (Exception e){//Catch exception if any
  System.err.println("Error: " + e.getMessage());
  }
        System.out.println("End Read_Detail");
    }
   
}
