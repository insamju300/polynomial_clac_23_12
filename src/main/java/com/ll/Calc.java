package com.ll;

public class Calc {
  public static int run(String exp) {

    String[] bits = exp.split(" ");
    String oper = null;
    int curretnInt = 0;

    for(String bit: bits){
        if(isInt(bit)){
          if(oper == null ){
              curretnInt = Integer.parseInt(bit);
          }else{

            if(oper.equals("+")){
              curretnInt += Integer.parseInt(bit);
            }else if(oper.equals("-")){
              curretnInt -= Integer.parseInt(bit);
            }

          }

        }else{
          oper = bit;
        }
    }

//
//    int a = Integer.parseInt(bits[0]);
//    int b = Integer.parseInt(bits[1]);

    return curretnInt;
  }

  public static boolean isInt(String input){
    try{
      Integer.parseInt(input);
      return true;
    }catch(Exception e){

    }
    return false;


  }
}