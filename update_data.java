

void updatedata() {
  String dirkey="";
  int counter=0;
  updateCurrentDate(counter);
  //run only if mouseonbutton is mousereleased

  if (frameCount==startframe || (mouseOnTimeArea() && mouseclick())){
    global_fade_timer=0;//set the timer to cero if a new date is given
    
    //load the file from history file
    while(true){
      try{
        xmldata = new XMLElement(this, "D:/Jaimosa/TAAU.eu/Project/prj_001_Self-City/_Programming/_Data/_history/xml/" + strcurrentdate + "_marketDataBase_history.xml" ); //they are in data/  - its the default folder
        break;
      }catch(NullPointerException e){
        counter +=1;
        updateCurrentDate(counter);
      }}
    XMLElement[] datachildren = xmldata.getChildren();
    //parse the xml and get the values
    for (int i = 0; i < datachildren.length; i ++ ) {
      String[] data=new String[10];
      data[0]=" - ";
      //counter +=1;//what is this?
      dirkey=datachildren[i].getName();
      data[1]=dirkey;
      
      String name=" - ";
      XMLElement[] dataname = datachildren[i].getChildren("name");
      for (int j=0;j<dataname.length;j++) {
        name = dataname[j].getContent();
      }
      data[2]= name;
      
      String avrgprice=" - ";
      XMLElement[] dataavrgprice = datachildren[i].getChildren("avrgprice");
      for (int j=0;j<dataavrgprice.length;j++) {
        avrgprice = dataavrgprice[j].getContent();
      }
      data[3]=avrgprice;
      
      String avrgsup=" - ";
      XMLElement[] dataavrgsup = datachildren[i].getChildren("avrgsup");
      for (int j=0;j<dataavrgsup.length;j++) {
        avrgsup = dataavrgsup[j].getContent();
      }
        data[4]=avrgsup;
      
      String numhouses=" - ";
      XMLElement[] datanumhouses = datachildren[i].getChildren("numhouses");
      for (int j=0;j<datanumhouses.length;j++) {
        numhouses = datanumhouses[j].getContent();
      }
        data[5]=numhouses;
      
      String trend7d=" - ";
      XMLElement[] datatrend7d = datachildren[i].getChildren("trend7d");
      for (int j=0;j<datatrend7d.length;j++) {
        trend7d = datatrend7d[j].getContent();
      }
        data[6]=trend7d;
      
      String trend1m=" - ";
      XMLElement[] datatrend1m = datachildren[i].getChildren("trend1m");
      for (int j=0;j<datatrend1m.length;j++) {
        trend1m = datatrend1m[j].getContent();
      }
        data[7]=trend1m;
      
      String trend6m=" - ";
      XMLElement[] datatrend6m = datachildren[i].getChildren("trend6m");
      for (int j=0;j<datatrend6m.length;j++) {
        trend6m = datatrend6m[j].getContent();
      }
      data[8]=trend6m;
      
      String events6m=" - ";
      XMLElement[] dataevents6m = datachildren[i].getChildren("events6m");
      for (int j=0;j<dataevents6m.length;j++) {
        events6m = dataevents6m[j].getContent();
      }
      data[9]=events6m;
      
      int[] prices;
      XMLElement[] dataprices = datachildren[i].getChildren("prices/ul");
      prices=new int[dataprices.length];
      for (int j=0;j<dataprices.length;j++) {
        prices[j] = int(dataprices[j].getContent());
      }
      int[] sup;
      XMLElement[] datasup = datachildren[i].getChildren("area/ul");
      sup=new int[datasup.length];
      for (int j=0;j<datasup.length;j++) {
        sup[j] = int(datasup[j].getContent());
      }
      String[] url;
      XMLElement[] dataurl = datachildren[i].getChildren("url/ul");
      url=new String[dataurl.length];
      for (int j=0;j<dataurl.length;j++) {
        url[j] = dataurl[j].getContent();
      }

      //foreach dirkey change an element of vgHash
      Vertexgroup vg=(Vertexgroup)vgHash.get(dirkey);
      //println(data);
      if (vg != null) {
        String bc=vg.data[0];
        name=vg.data[2];
        vg.data=data;
        vg.data[0]=bc;//the bc data is defined in the cartographty xml
        vg.data[2]=name;//we keep the name from the osm map
        vg.type="pricestreet"; 
        vg.prices=prices;
        vg.sup=sup;
        vg.url=url;
        int[] values=getcolorfromdata(vg.values, data, vg.orgcolor);
        vg.orgcolor=color(values[0],values[1],values[2]);
        vg.updated=true;
        vg.values=values;
        vg.strokeW=0.8;
        values=new int[4];
      }
    }
  }
}

///////////////////////////////////////////////////////////////////

int[] getcolorfromdata(int[] values, String[] data, color inheritedcolor) {
  int value=values[3];int minval=dataf.minval;int maxval=dataf.maxval;
  //values => [0]red,[1]green,[2]blue,[3]value
  color stcolor=inheritedcolor;
  int[] val=new int[4];
    stcolor=color(0);
    
    if (map_switches[0]) { //price map
      minval=1000;maxval=3500;
      int price=int(data[3]);
      value=price;
      price=int(map(price, minval, maxval, 0, 255)); 
      float r, g, b;
      if(price>=0 && price<=122){r=0;b=map(price,0,122,60,255);g=200-map(price,0,122,0,80);}else{r=map(price,122,255,0,255);b=255-map(price,122,255,0,40);g=120-map(price,122,255,0,120);}
      
      stcolor=color (r, g, b);
      if(price>255){stcolor=color(255,0,140);}
      if(price<0){stcolor=color(0,200,60);}
    }
    if (map_switches[1]) { //stock map
      minval=1;maxval=12;
      int numhouses=int(data[5]);
      value=numhouses;
      numhouses=int(map(numhouses,minval,maxval,0,255));
      numhouses=constrain(numhouses,0,255);
      float r,g,b;
      r=numhouses;g=180-map(numhouses,0,255,0,180);b=0;
      stcolor=color(r,g,b);
    }
    if (map_switches[2]) { //sup map
      minval=30;maxval=200;
      int sup=int(data[4]);
      value=sup;
      sup=int(map(sup, minval,maxval,-110,0));
      sup=constrain(sup,-110,0);
      colorMode(HSB, 360, 100, 100);
      stcolor=color(abs(sup), 100-abs(sup)/6, 100-abs(sup)/6);
      colorMode(RGB, 255, 255, 255);
    }
    if (map_switches[3]) { //7d map
      minval=-1000;maxval=1000;
      int trend7d=int(data[6]);
      value=trend7d;
      trend7d=int(map(trend7d, minval,maxval,230,360));
      trend7d=constrain(trend7d,230,360);
      colorMode(HSB, 360, 100, 100);
      stcolor=color(trend7d, 20+abs(map(trend7d,230,360,-80,80)), 80);
      colorMode(RGB, 255, 255, 255);
    }
    if (map_switches[4]) { //1m map
      minval=-1000;maxval=1000;
      int trend1m=int(data[7]);
      value=trend1m;
      trend1m=int(map(trend1m, minval,maxval,230,360));
      trend1m=constrain(trend1m,230,360);
      colorMode(HSB, 360, 100, 100);
      stcolor=color(trend1m, 20+abs(map(trend1m,230,360,-80,80)), 80);
      colorMode(RGB, 255, 255, 255);
    }
    if (map_switches[5]) { //6m map
      minval=-1000;maxval=1000;
      int trend6m=int(data[8]);
      value=trend6m;
      trend6m=int(map(trend6m, minval,maxval,230,360));
      trend6m=constrain(trend6m,230,360);
      colorMode(HSB, 360, 100, 100);
      stcolor=color(trend6m, 20+abs(map(trend6m,230,360,-80,80)), 80);
      colorMode(RGB, 255, 255, 255);
    }
    if (map_switches[6]) { //events map
      minval=7;maxval=30;
      int events6m=int(data[9]);
      value=events6m;
      events6m=int(map(events6m,minval,maxval,0,255));
      events6m=constrain(events6m,0,255);
      float r,g,b;
      r=events6m;g=180-map(events6m,0,255,0,180);b=60;
      stcolor=color(r,g,b);
    }
    if (map_switches[7]) { //stats by neighbourhood
      println("twist and shout!");
    }
  
  val[0]=int(red(stcolor));val[1]=int(green(stcolor));val[2]=int(blue(stcolor));
  val[3]=value;
  dataf.minval=minval;
  dataf.maxval=maxval;
  return val;
}

/////////////////////////////////////////////////////////////////////

void updateCurrentDate(int counter) {
Date firstdate=new Date();
int daystosum=0;

if(mouseclick()&&mouseOnTimeArea()){
  for(int i=0;i<timeButtList.length;i++){
    boolean on=timeButtList[i].isON;
    String id=timeButtList[i].id;
    if(on==true && id.equals("-7d")){
      daystosum=-7;
    }
    if(on==true && id.equals("-1m")){
      daystosum=-30;
    }  
    if(on==true && id.equals("-6m")){
      daystosum=int(-30.5*6);
    }
    if(on==true && id.equals("7d")){
      daystosum=7;
    }
    if(on==true && id.equals("1m")){
      daystosum=30;
    }  
    if(on==true && id.equals("6m")){
      daystosum=int(30.5*6);
    }
  }
}
if(counter != 0){
  daystosum=-1;
}
  
  
Calendar c=Calendar.getInstance();
//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

try{
firstdate=formatter.parse("2012-04-13");//first day of the database
}catch(ParseException e){
  println(e);
}
c.setTime(currentdate);
c.add(Calendar.DATE,daystosum);
//println("daystosum: " + str(daystosum));
currentdate=c.getTime();
if(currentdate.before(firstdate)){
  currentdate=firstdate;
}
strcurrentdate=formatter.format(currentdate);
dateDisplay=formatterDisplay.format(currentdate);//.toUpperCase()
}

color fade_from_to(color source, color target, int fade_timer) {
  float redcomp=0;
  float greencomp=0;
  float bluecomp=0;
  color fadecolor=target;
  
  if (source != target && fade_timer<fadingTime) {
    
    if (red(source) < red(target)) {
      redcomp=red(source)+(red(target)-red(source))*fade_timer/fadingTime;
    }else if (red(source) > red(target)) {
      redcomp=red(source)-(red(source)-red(target))*fade_timer/fadingTime;
    }else {
      redcomp=red(target);
    }

    if (green(source) < green(target)) {
      greencomp=green(source)+(green(target)-green(source))*fade_timer/fadingTime;
    }else if (green(source) > green(target)) {
      greencomp=green(source)-(green(source)-green(target))*fade_timer/fadingTime;
    }else {
      greencomp=green(target);
    }
    
    if (blue(source) < blue(target)) {
      bluecomp=blue(source)+(blue(target)-blue(source))*fade_timer/fadingTime;
    }else if (blue(source) > blue(target)) {
      bluecomp=blue(source)-(blue(source)-blue(target))*fade_timer/fadingTime;
    }else {
      bluecomp=blue(target);
    }
    fadecolor=color(redcomp, greencomp, bluecomp);
  }
  return(fadecolor);
}

