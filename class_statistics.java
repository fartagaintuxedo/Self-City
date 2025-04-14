
class Statistics {

  String bcName;
  int maxPrice;
  int minPrice;
  int avrgPrice;
  PVector loc;
  boolean isON;

  Statistics(String _bcName, int _maxPrice, int _minPrice, int _avrgPrice, PVector _loc, boolean _isON) {

    bcName=_bcName;
    maxPrice=_maxPrice;
    minPrice=_minPrice;
    avrgPrice=_avrgPrice;
    loc=_loc;
    isON=_isON;
  }

  void drawStatistics() {
    String[] data;
    String aux;
    int avrgCounter=0;
    int mycounter=1;
    int maxBC=5;
    
    if (mouseclick()) {// add && if mousButtOnPad==true
      int[] prices=new int[0];
      bcName="";
      aux="";
      for (int i=0;i<statsList.size();i++) {
        //println(statsList.size());
        data=statsList.get(i);
        //println(data.length);
        
        if( bcName.indexOf(data[0])<0){
          if(!bcName.equals("")){
          aux=aux + ", ";
            if(aux.indexOf(data[0])<0){
            mycounter +=1;
            }
          if(mycounter<=maxBC){
            bcName=bcName + ", ";
          }
          }  
          aux += data[0];
            if(mycounter<=maxBC){
              bcName += data[0];
              if(mycounter==maxBC){
                bcName=bcName + "...";

              }
            }
        }

        
        if (data.length>1) {
          prices=append(prices,int(data[2]));
          avrgCounter=avrgCounter+int(data[2]);
          
        }
      }  
      if (prices.length>0) {
        maxPrice=max(prices); minPrice=min(prices); avrgPrice=avrgCounter/prices.length;
      }
      statsList.clear();
      if(mycounter==bcList.length-1){// there's sth wrong with MN1, otherwise it should be just bcList.length
       bcName="Todos los barrios";
      }
    }
    

    String mystring=bcName + "\n\n" + "max=" + maxPrice + " euro/m2\n" + "min=" + minPrice + " euro/m2\n" + "media=" + avrgPrice + " euro/m2\n";
    noStroke();
    fill(255, 220);
    rect(20, height-160, 180, 140);
    fill(80);
    text(mystring, 40, height-120);
  }
}

