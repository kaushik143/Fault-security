/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package PSOFaultSecure;

import java.util.StringTokenizer;
import java.util.Vector;


/**
 *
 * @author VIPUL
 */
public class Checkpointing {
    static  int IOMatrix[][];
    static String schMatrix[][]; //operation   CS   operator   FU   delay
    static  String scheduleS="";
     static  String schedule="",schedulereverse="";
    static int kc=2;
     static String [][] schcopy;
     static Vector<Resource> resourc=new <Resource>Vector();
      Vector <String> availList=new <String>Vector();
     GlobalConstants gc;
    public Checkpointing(){
       gc = GlobalConstants.getInstance();
   }
    public double checkpointion(String sch, Vector<Resource> resource){
        double delay=0.0;
        resourc=resource;
        
        schMatrix=new String[gc.operation.size()/2][5];
        schedule=sch;
        kc=gc.kc;
        availList.clear();
        for(int a=0;a<resource.size();a++){
              Resource temp=resource.get(a);
               availList.add(temp.name);
             }
       // String schedule="",schedulereverse="";
         String dilim="\n";
         String dilim1=" ";
        String token="";
        String token1="";
       // String scheduleS="";
       createIOMatrix(gc.operation);
        
       int a=0; 
  String strLine="";
 
  StringTokenizer tok = new StringTokenizer(sch,dilim,false);
  //Read File Line By Line
  while (tok.hasMoreTokens()){
  // Print the content on the console
      strLine=tok.nextToken();
  System.out.println (strLine);
  schedule=schedule+strLine+"\n";
  StringTokenizer tok1 = new StringTokenizer(strLine,dilim1,false);
      token1 = tok1.nextToken();
       int CS=Integer.parseInt(token1);
       
       while (tok1.hasMoreTokens()) {
          schMatrix[a][1]=CS+""; 
         token1 = tok1.nextToken();  
        //int op=Integer.parseInt(token1.substring(0, token1.length()-1));
        schMatrix[a][0]=token1;
        token1 = tok1.nextToken();
        schMatrix[a][2]=token1.substring(1, token1.length()-1);
        if(schMatrix[a][2].equals("*")){
         schMatrix[a][4]=11000+"";   
        }
        else{
          schMatrix[a][4]=270+"";  
        }
        token1 = tok1.nextToken();
        schMatrix[a][3]=token1;
        //System.out.println("CS  "+CS+"operation  "+op);
        //String input=printionodes(op);
        //System.out.println(input);
        
        a++;
       
       }
    
  
  }
 
   //schcopy=coppy(schMatrix);
   coppy(schMatrix);
        
        int Odelay=0,Ndelay=0;
        String chpntnode="No single check Pointing";
        coppy(schMatrix);
      /*  Odelay=delayCalc(genSchedule(schMatrix));
        String s=chkpntR1R4("8'",schMatrix);
        
         if(!s.equals("N")) {        
        checkPointing(s,schcopy);
        //genSchedule(schMatrix);
       
        Ndelay=delayCalc(genSchedule(schcopy));
         }
         else{
             System.out.println("No check pointing");
         }
*/
        int size=gc.operation.size()/4;
        for( int x= size;x>1;x--){
            coppy(schMatrix);
          Odelay=delayCalc(genSchedule(schMatrix));
            String s=chkpntR1R4(x+"'",schMatrix);
         if(!s.equals("N") && !s.equals("")) {        
            checkPointing(s,schcopy);
        //genSchedule(schMatrix);
            Ndelay=delayCalc(genSchedule(schcopy));
            if(Odelay>Ndelay){
                chpntnode=" Check pointing between  "+s;
                break;
            }
         }
         else {
             coppy(schMatrix);
             String s1=chkpntR2R3(x+"'",schMatrix);
             if(!s1.equals("N") && !s1.equals("")){
              checkPointing(s1,schcopy);
        //genSchedule(schMatrix);
                Ndelay=delayCalc(genSchedule(schcopy));
            if(Odelay>Ndelay){
                chpntnode=" Check pointing between  "+s1;
                break;
            }   
           }
         }  
        }
        //System.out.println(genSchedule(schcopy));
        if(Odelay==Ndelay){
           coppy(schMatrix); 
          schcopy=multiCheckpointing(schcopy) ; 
        }
        Ndelay=delayCalc(genSchedule(schcopy));
        System.out.println("old delay -"+Odelay );
        System.out.println("New delay -"+Ndelay );
        System.out.println("Check pointing node- "+chpntnode);
        
       /* for (int i=0;i<schMatrix.length;i++){
            for(int j=0;j<schMatrix[i].length;j++){
                System.out.print(schMatrix[i][j]+"   ");
            }
        System.out.println();
        }*/
        
        //main flow of checkpointing
      return Ndelay;  
        
    }
    public void createIOMatrix(Vector operation){
    int dim=gc.operation.size()/4;
    IOMatrix=new int[dim][dim]; 
    for(int i=0;i<operation.size();i+=4){
        int input1,input2,oper;
        input1=Integer.parseInt(operation.get(i+1)+"");
        input2=Integer.parseInt(operation.get(i+2)+"");
        oper=Integer.parseInt(operation.get(i+3)+"");
        if(input1!=0){
            IOMatrix[oper-1][input1-1]=1;
        }
        if(input2!=0){
            IOMatrix[oper-1][input2-1]=1;
        }
    }
    for(int i=0;i<IOMatrix.length;i++){
        System.out.print("{");
        for (int j=0;j<IOMatrix[0].length;j++){
            System.out.print(IOMatrix[i][j]+"  ");
        }
    System.out.println("}");
    }
}    
    public String[][] coppy(String schmain[][]){
        schcopy=new String[schmain.length][schmain[0].length];
        for (int i=0;i<schmain.length;i++){
            for(int j=0;j<schmain[i].length;j++){
                schcopy[i][j]=schmain[i][j];
            }
        }
        return schcopy;
    }
    public  String chkpntR1R4(String node, String [][]schcopy){
        String result="";
        Vector inode=inputnodes(node);
         //System.out.println(inode.size());
        if(inode.size()==0){
            return "N";
        }
        else if(inode.size()==1){
            result= node+" "+inode.get(0);
        }
        else{
           if(getCS(inode.get(0)+"'",schcopy)==getCS(inode.get(1)+"'",schcopy)){
               
                   result="N";
               
           }
           else {
               if(getCS(inode.get(0)+"'",schcopy)>getCS(inode.get(1)+"'",schcopy)){
                result= node+" "+inode.get(0)+"'";
                }
               else
                   result= node+" "+inode.get(1)+"'";
           } 
        }
        return result;
    }
        public  String chkpntR2R3(String node, String [][] schcopy){
        String result="";
        Vector onode=outputnodes(node);
        if(onode.size()==0){
            result= "N";
        }
        else if(onode.size()==1){
            result= node+" "+onode.get(0)+"'";
        }
        else{
           if(getCS(onode.get(0)+"'",schcopy)==getCS(onode.get(1)+"'",schcopy)){
               if(getOperator(onode.get(0)+"'",schcopy).equals(getOperator(onode.get(1)+"'",schcopy))){
                   result="N";
               }
               else {
                   if(getDelay(onode.get(0)+"'",schcopy)>getDelay(onode.get(1)+"'",schcopy)){
                   result= node+" "+onode.get(0)+"'";
               }
               else
                   result= node+" "+onode.get(1)+"'";
           } }
        }
        return result;
    }
        public  String checkPointing(String chpnt, String [][]schcopy){
            
            boolean maincheck=false,checkFU=false,checkKC=false;
            String schduleF="";
            String dilim=" ";
            StringTokenizer tok = new StringTokenizer(chpnt,dilim,false);
            String opr1=tok.nextToken();
            //String opr2=tok.nextToken();
            int CSdup,CSmain;
            String FUdup,FUmain;
            CSdup=getCS(opr1,schcopy);
            CSmain=getCS(opr1.substring(0, opr1.length()-1),schcopy);
            FUdup=getFU(opr1,schcopy);
            FUmain=getFU(opr1.substring(0, opr1.length()-1),schcopy);
            for(int j=0;j<schcopy.length;j++){
                if(schcopy[j][1].equals((CSdup-1)+"")){
                    if(schcopy[j][3].equals(FUdup)){
                        checkFU=true;
                        System.out.println("check1 FU");
                        break;
                    }
                }
            }
            if((!checkFU) && FUdup.equals(FUmain)){
            if((CSdup-1)-CSmain>=kc){
                           checkKC=true; 
                        }
            }
           if((!checkFU) || (checkKC)){
                maincheck=true;
            }
		if(!maincheck){
               boolean tempcheck=false;
               tempcheck=reassignFU(opr1, schcopy);
               if(tempcheck){
                   maincheck=true;
               }
           }
           
          if(maincheck){
              setnewCS(opr1,schcopy);
              //check1=checkshift(opr1);
           // generate list to check output siblings
           Vector <String>listopT=new <String>Vector();
           Vector <String>listopF=new <String>Vector();
           listopT.add(opr1);
           for (int i=0;i<listopT.size();i++){
               String node=listopT.get(i);
               Vector temp=outputnodes(node);
               for(int j=0;j<temp.size();j++){
                   listopT.add(temp.get(j)+"'");
               }
           }
           
            boolean ch=true;
            listopF.add(listopT.get(0));
            for(int i=1;i<listopT.size();i++){
            ch=true;
            String temp=listopT.get(i);
            for(int j=0;j<listopF.size();j++){
                if(temp.equals(listopF.get(j))){
                    ch=false;
                }
            }
            if(ch){
                listopF.add(temp);
            }
        }
            System.out.println("operation-"+opr1);
         System.out.println(listopF.toString());
         for(int i=1;i<listopF.size();i++){
             //System.out.println(checkshift(listopF.get(i),schcopy));
           if(checkshift(listopF.get(i),schcopy)){
               setnewCS(listopF.get(i),schcopy);
           }  
           else{
               break;
           }
         }
            
          }  
                   return schduleF;
        }
       public  String[][] multiCheckpointing(String [][]schcopy){
            int maxCS=1;
            int CS=1;
       for (int i=0;i<schcopy.length;i++){
            //for(int j=0;j<schMatrix[i].length;j++){
                CS=Integer.parseInt(schcopy[i][1]);
                if(maxCS<CS){
                    maxCS=CS;
                } 
               
             }
       Vector<String> mchpntList=new <String>Vector();
     for(int i=maxCS;i>=1;i--){
         int counter=0;
         
         for (int j=0;j<schcopy.length;j++){
             if(counter==2){
                 break;
             }
            CS=Integer.parseInt(schcopy[j][1]);
                if(CS==i){
                    String opr=schcopy[j][0];
                    if(opr.substring(opr.length()-1, opr.length()).equals("'")){
                        String chnode=chkpntR1R4(opr,schcopy);
                        if(!chnode.equals("N")){
                            mchpntList.add(opr);
                            mchpntList.add(chnode);
                            counter++;
                        }
                    }
                } 
               
             }
         if(mchpntList.size()==4){
             break;
         }
         else{
             mchpntList.clear();
         }
         }
     if(mchpntList.size()==4){
             checkPointing(mchpntList.get(1),schcopy);
             checkPointing(mchpntList.get(3),schcopy);
         }
            return schcopy;
        }
public  boolean reassignFU(String opr, String [][]schcopy){
            boolean yes=false;
            int CSdup,CSmain;
            String FUdup,FUmain,operator;
            Vector <String> usedList=new <String>Vector();
           
            Vector <String> freeList=new <String>Vector();
            availList.add("mul1");
            availList.add("mul2");
            availList.add("mul3");
            availList.add("mul4");
            availList.add("add1");
            availList.add("add2");
            availList.add("add3");
            availList.add("add4");
            operator=getOperator(opr,schcopy);
            CSdup=getCS(opr,schcopy);
            CSmain=getCS(opr.substring(0, opr.length()-1),schcopy);
            FUdup=getFU(opr,schcopy);
            FUmain=getFU(opr.substring(0, opr.length()-1),schcopy);
            for(int j=0;j<schcopy.length;j++){
                if(schcopy[j][1].equals((CSdup-1)+"")){
                    //if(schcopy[j][2].equals(operator)){
                        usedList.add(schcopy[j][3]);
                    //}
                }
            }
            System.out.println(usedList.toString());
            for(int i=0;i<availList.size();i++){
                boolean flag=false;
               for (int j=0;j<usedList.size();j++){
                   
                   if(availList.get(i).equals(usedList.get(j))){
                       flag=true;
                   }
                   
               } 
               if(!flag){
                       freeList.add(availList.get(i));
                   }
            }
            System.out.println(freeList.toString());
            for (int i=0;i<freeList.size();i++){
                boolean exitflag=false;
                String FUfree=freeList.get(i);
                if(FUfree.substring(0, opr.length()-1).equals(FUmain.substring(0, opr.length()-1))){
                if(FUfree.equals(FUmain)){
                    if((CSdup-1)-CSmain>=kc){
                       for(int j=0;j<schcopy.length;j++){
                           if(schcopy[j][0].equals(opr)){
                               schcopy[j][3]=FUfree;
                               exitflag=true;
                           } 
                        }
                        
                    }
                    else{
                        //determine list of same type operation
                        String sameFUopr="";
                        for(int j=0;j<schcopy.length;j++){
                            if(schcopy[j][3].equals(FUdup) && getCS(schcopy[j][0],schcopy)==(CSdup-1)){
                                
                                sameFUopr=schcopy[j][0];
                                System.out.println(sameFUopr);
                                int CSmainS=getCS(sameFUopr.substring(0, opr.length()-1),schcopy);
                                int CSdupS=getCS(sameFUopr,schcopy);
                                String FUmainS=getFU(sameFUopr.substring(0, opr.length()-1),schcopy);
                                System.out.println(CSmainS+"  "+CSdupS+"   "+FUmainS);
                                    if(FUmainS.equals(FUfree)){
                                        if((CSdupS)-CSmainS>=kc){
                                           schcopy[j][3]=FUfree;
                                           exitflag=true;
                                           break;
                                        } 
                                    }
                                    else{
                                        schcopy[j][3]=FUfree;
                                           exitflag=true;
                                           break;
                                    }
                            }
                        }
                        //check for  main FU for kc violation
                        
                    }
                }
                else{
                   for(int j=0;j<schcopy.length;j++){
                           if(schcopy[j][0].equals(opr)){
                               schcopy[j][3]=FUfree;
                               exitflag=true;
                               break;
                           } 
                        } 
                }
                }
                if(exitflag){
                    yes=true;
                    break;
                }
            }
            return yes;
        }
        public  boolean checkshift(String opr, String [][]schcopy){
            boolean maincheck=false,checkFU=false,checkKC=false,checkIN=false;
            int CSdup,CSmain;
            String FUdup,FUmain;
            CSdup=getCS(opr, schcopy);
            System.out.println("CSdup- "+CSdup);
            CSmain=getCS(opr.substring(0, opr.length()-1),schcopy);
            System.out.println("CSmain- "+CSmain);
            FUdup=getFU(opr,schcopy);
            System.out.println("FUdup- "+FUdup);
            FUmain=getFU(opr.substring(0, opr.length()-1),schcopy);
            System.out.println("FUmain- "+FUmain);
            Vector inode=inputnodes(opr);
            System.out.println("inode list"+inode.toString());
            if(inode.size()==1){
                System.out.println("CS inode1"+getCS(inode.get(0)+"'",schcopy));
            if(getCS(inode.get(0)+"'",schcopy)<(CSdup-1)){
                checkIN=true;
                }
            }
            else if(inode.size()==2){
                System.out.println("CS inode1"+getCS(inode.get(0)+"'",schcopy));
                System.out.println("CS inode2"+getCS(inode.get(1)+"'",schcopy));
            if(getCS(inode.get(0)+"'",schcopy)<(CSdup-1) && getCS(inode.get(1)+"'",schcopy)<(CSdup-1)){
                checkIN=true;
                }
            }
            if(checkIN){
            for(int j=0;j<schcopy.length;j++){
                String temp=(CSdup-1)+"";
                if(schcopy[j][1].equals(temp)){
                    if(schcopy[j][3].equals(FUdup)){
                       checkFU=true;
                       break;
                    }
                }
            }
        }
            /*if((!checkFU) && FUdup.equals(FUmain)){
            if((CSdup-1)-CSmain<kc){
                           checkKC=true; 
                        }
            }*/
            if(((!checkFU) && checkIN)) {//|| (checkKC)){
                maincheck=true;
            }
            return maincheck;
        }
    public static Vector inputnodes(String opt){
        int op=0;
        if(opt.length()>=2){
            op=Integer.parseInt(opt.substring(0, opt.length()-1));
        }
        else {
            op=Integer.parseInt(opt);
        }
            
      Vector <Integer> input=new <Integer>Vector();
        for(int i=0;i<IOMatrix[0].length;i++){
          if(IOMatrix[op-1][i]==1){
              input.add(i+1);
          }
          
      }  
     return input;
    }
    public  Vector outputnodes(String opt){
        int op=0;
        if(opt.length()>=2){
            op=Integer.parseInt(opt.substring(0, opt.length()-1));
        }
        else {
            op=Integer.parseInt(opt);
        }
            
      Vector <Integer> output=new <Integer>Vector();
        for(int i=0;i<IOMatrix[0].length;i++){
          if(IOMatrix[i][op-1]==1){
              output.add(i+1);
          }
          
      }  
     return output;
    }
    public  int getCS(String op, String [][]schcopy){
        int CS=0;
       for (int i=0;i<schcopy.length;i++){
            //for(int j=0;j<schMatrix[i].length;j++){
                if(schcopy[i][0].equals(op)){
                    CS=Integer.parseInt(schcopy[i][1]);
                    return CS;
                }
                    //}
       // System.out.println();
        }
   
        return CS;
    }
    public  void setnewCS(String op, String schcopy[][]){
        int CS=0;
       for (int i=0;i<schcopy.length;i++){
            //for(int j=0;j<schMatrix[i].length;j++){
                if(schcopy[i][0].equals(op)){
                    CS=Integer.parseInt(schcopy[i][1]);
                   schcopy[i][1]=(CS-1)+"";
                   break;
                }
                    //}
       // System.out.println();
        }
   
      }
     public  int getDelay(String op, String schcopy[][]){
        int delay=0;
       for (int i=0;i<schcopy.length;i++){
            //for(int j=0;j<schMatrix[i].length;j++){
                if(schcopy[i][0].equals(op)){
                    delay=Integer.parseInt(schcopy[i][4]);
                    return delay;
                }
                    //}
       // System.out.println();
        }
   
        return delay;
    }
   
    public String getOperator(String op, String schcopy[][]){
       String opr="";
       for (int i=0;i<schcopy.length;i++){
            //for(int j=0;j<schMatrix[i].length;j++){
                if(schcopy[i][0].equals(op)){
                    opr=schcopy[i][2];
                    return opr;
                }
                    //}
       // System.out.println();
        }
       return opr;
    } 
     public  String getFU(String op,String [][]schcopy){
       String FU="";
       for (int i=0;i<schcopy.length;i++){
            //for(int j=0;j<schMatrix[i].length;j++){
                if(schcopy[i][0].equals(op)){
                    FU=schcopy[i][3];
                    return FU;
                }
                    //}
       // System.out.println();
        }
       return FU;
    } 
  
    public  String genSchedule(String [][]schMatrix){
       String schedule="";
       int CS=1;
       int maxCS=1;
       for (int i=0;i<schMatrix.length;i++){
            //for(int j=0;j<schMatrix[i].length;j++){
                CS=Integer.parseInt(schMatrix[i][1]);
                if(maxCS<CS){
                    maxCS=CS;
                } 
               
             }
     for(int i=1;i<=maxCS;i++){
         schedule += i;
         for (int j=0;j<schMatrix.length;j++){
            //for(int j=0;j<schMatrix[i].length;j++){
                CS=Integer.parseInt(schMatrix[j][1]);
                if(CS==i){
                    schedule += "  "+schMatrix[j][0]+" ("+schMatrix[j][2]+") "+schMatrix[j][3];
                } 
               
             }
         schedule +="\n";
     }
       System.out.println(schedule);
       return schedule;
        
    }
    public int delayCalc(String sch){
        int delay=0;
        String token="",token1="";
         String dilim="\n";
         String dilim1=" ";
         boolean mul=false;
        StringTokenizer tok = new StringTokenizer(sch,dilim,false);
         while (tok.hasMoreTokens()) {
      token = tok.nextToken();
      StringTokenizer tok1 = new StringTokenizer(token,dilim1,false);
      token1 = tok1.nextToken();
      while (tok1.hasMoreTokens()) {
        
        token1 = tok1.nextToken();
           
        token1 = tok1.nextToken();
        String operator=token1.substring(1, token1.length()-1);
        //System.out.println(operator+"  ");
        token1 = tok1.nextToken();
        if(operator.equals("*")){
            mul=true;
            //break;
        }
       }
      if(mul){
          delay +=11000;
      }
      else{
          delay +=270;
      }
      mul=false;
       }
         System.out.println("Delay= "+delay);
        return delay;
    }
}