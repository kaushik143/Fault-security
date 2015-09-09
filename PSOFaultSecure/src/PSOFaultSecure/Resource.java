/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PSOFaultSecure;

import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author SAPVI
 */
public class Resource {
 String name;
 //int delay;
 int curOpr;
 boolean status;
 String operator;
 //false busy true free
 //int currDelay;
 Vector<Integer> oprComplete; //add operation with "," whenever completed 
 Vector<Integer> oprStartTime;
 Vector<Integer> oprComTime;  //add time , when operation will be completed
public Resource(String name){
    this.name=name;
    String tname=name.substring(0, 3);
    if(tname.equalsIgnoreCase("mul"))
        this.operator="*";
    if(tname.equalsIgnoreCase("add"))
        this.operator="+";
    if(tname.equalsIgnoreCase("sub"))
        this.operator="-";
    if(tname.equalsIgnoreCase("com"))
        this.operator="<";
    //this.delay=delay;
    curOpr=0;
    status=true;
    //currDelay=0;
    oprComplete=new <Integer>Vector();
    oprStartTime=new <Integer>Vector();
    oprComTime=new <Integer>Vector();
}
}

