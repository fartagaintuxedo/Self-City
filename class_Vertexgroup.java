class Vertexgroup {
  //data[0]=barriociudad, data[1]=dirkey, data[2]=street name, data[3]=street price, data[4]=sup, data[5]=numhouses,
 // data[6]=trend7d, data[7]=trend1m, data[8]=trend6m, data[9]=events6m
  ArrayList[] listcoord;
  String[] data;
  int[] prices;
  int[] sup;
  String[] url;
  String type;
  color stcolor;
  color orgcolor;
  color sourcecolor;
  color targetcolor;
  color buffercolor;
  float strokeW;
  boolean isON;
  boolean filtered;
  boolean updated;
  int[] values;

  Vertexgroup(ArrayList[] _listcoord, String[] _data, int[] _prices, int[] _sup, String[] _url, String  _type, 
  color _stcolor, color _orgcolor, color _sourcecolor, color _targetcolor, color _buffercolor, 
  float _strokeW, boolean _isON, boolean _updated, boolean _filtered, int[] _values) {
  
    listcoord=_listcoord;
    data=_data;
    prices=_prices;
    sup=_sup;
    url=_url;
    type=_type;
    stcolor=_stcolor;
    orgcolor=_orgcolor;
    sourcecolor=_sourcecolor;
    buffercolor=_buffercolor;
    strokeW=_strokeW;
    isON=_isON;
    filtered=_filtered;
    updated=_updated;
    values=_values;
  }

  void loadStatsArray() {
    if (mouseclick()==true) {
      if (isON) {
        updateStatsList();
      }
    }
  }
 
  void filtered(){
    int val=values[3];
    if((val<dataf.bottval && dataf.bottval>dataf.minval) || (val>dataf.topval && dataf.topval<dataf.maxval)){//if the limit is 5000 houses above 5000 will not be filtered if arrows are at top limit
        filtered=true;
    }else{
      filtered=false;
    }
  }
  
  void visibility() {
    if (mouseclick()==true && data.length>1) {      
      
      if (allPressed()==true && leftbutton==true) {
        isON=true;
      }
      for (int i=0;i<bcButtList.length-1;i++) {
        if (leftbutton) {

          if ((bcButtList[i].mouseOnButton()==true)&&(data[0].equals(bcButtList[i].id)==true)) {
            isON=toogle(isON);
          }
        }
        else {
          if (bcButtList[i].mouseOnButton()==true) {
            if (data[0].equals(bcButtList[i].id)==true) {
              isON=true;
            }
            else {
              isON=false;
            }
          }
        }
      }
    }
  }

  void updateStatsList() {
    statsList.add(data);
  }

  void drawVG(float zoom) {
    visibility();
    filtered();
    //loadStatsArray();
    if(frameCount>startframe && strokeW<startstroke && type.equals("pricestreet")){//this is for the sake of a smooth start
      strokeW+=0.1;
    }
    if(updated==false && (type.equals("pricestreet") || type.equals("street"))){//change type to street if street is not onsale given a new date
      type="street";
    }
    if(mouseclick() && mouseOnMapsArea()==true && type.equals("pricestreet")){//get new orgcolor from mapSwitch
      values=getcolorfromdata(values, data, orgcolor);
      orgcolor=color(values[0],values[1],values[2]);
    }
    
    eventmanager();
    
    if(! type.equals("not_street")){
      //if(global_fade_timer<fadingTime){
        stcolor=fade_from_to(sourcecolor,targetcolor,global_fade_timer);
      //}else{
       // stcolor=targetcolor;
      //}
    }else{
      stcolor=orgcolor;
    }
    //////////////////////////////////////////////////////////////
    for (int j=0;j<listcoord.length;j++) {
      for (int i=0;i<listcoord[j].size();i++) {
        Vertice v=(Vertice)listcoord[j].get((i));
        v.update(zoom);
      }
    }
    buffer.strokeWeight(3);
    buffer.stroke(buffercolor);
    
    for (int j=0;j<listcoord.length;j++) {
      for (int i=0;i<listcoord[j].size()-1;i++) {

        Vertice v2=(Vertice)listcoord[j].get((i+1));
        Vertice v1=(Vertice)listcoord[j].get(i);

        drawline(v1.coord.x, v1.coord.y, v2.coord.x, v2.coord.y, strokeW, stcolor);
        
        if (type.equals("pricestreet")&& mousePressed==false) {
          buffer.beginDraw();
          buffer.line(v1.coord.x, v1.coord.y, v2.coord.x, v2.coord.y);
          buffer.endDraw();
        }
      }
    }
  }
  
  void eventmanager(){
    if(type.equals("pricestreet")){
      if((onoffevent)){//includes filterevent, see visibility above
        sourcecolor=stcolor;
        if(isON && !filtered){
          targetcolor=orgcolor;
        }else{
          targetcolor=color_street;
        }
      }
      if(! filtered && isON){//filter does not require an event to happen, it should be always active as the filters can be effectively applied while other buttons are in action
        sourcecolor=stcolor;
        targetcolor=orgcolor;
      }else{
        sourcecolor=stcolor;
        targetcolor=color_street;
      }
    }
    if(isON && ! filtered && switchmapevent && type.equals("pricestreet")){
      sourcecolor=stcolor;
      targetcolor=orgcolor;
    }else if(type.equals("street")){
      sourcecolor=stcolor;
      targetcolor=color_street;
    }
    if(isON && ! filtered && dateevent ){
      sourcecolor=stcolor;
      if(! updated){ 
        targetcolor=color_street;
      }
      if(updated){
        targetcolor=orgcolor;
      }
    }
  }
  
  void drawStdata() {
    if (! filtered && mouseOnCanvas() && isON 
    && buffercolor != color(0, 0, 0) && type.equals("pricestreet") 
    && buffer.pixels[int(mousePos.y)*buffer.width + int(mousePos.x)]==buffercolor) {
      
      String strtext="";String[] txtlines=new String[0];
      int n=2; // number of lines of the text displayed
      String streetname=data[2];
      String otherlines1="";String otherlines2="";
      txtlines=append(txtlines,streetname);
 
      if (map_switches[0]) { //price map
        strtext=",\nPrecio medio: " + data[3] + " €/m2" + "\n";
        otherlines1="Sup media:" + data[4] + " m2" + "\n";
        otherlines2="Stock:" + data[5] + " viviendas";
        txtlines=append(txtlines,strtext);
        txtlines=append(txtlines,otherlines1);
        txtlines=append(txtlines,otherlines2);
        n=4;
      }
      if (map_switches[1]) { //stock map
        strtext=",\nStock: " + data[5] + " viviendas" + "\n";
        txtlines=append(txtlines,strtext);
      }
      if (map_switches[2]) { //sup map
        strtext=",\nSup media: " + data[4] + " m2";
        txtlines=append(txtlines,strtext);
      }
      if (map_switches[3]) { //7d map
        strtext=",\nTendencia última semana: " + data[6] + " €";
        txtlines=append(txtlines,strtext);
      }
      if (map_switches[4]) { //1m map
        strtext=",\nTendencia último mes: " + data[7] + " €";
        txtlines=append(txtlines,strtext);
      }
      if (map_switches[5]) { //6m map
        strtext=",\nTendencia último semestre: " + data[8] + " €";
        txtlines=append(txtlines,strtext);
      }
      if (map_switches[6]) { //events map
        strtext=",\nAltas / bajas último semestre: " + data[9];
        txtlines=append(txtlines,strtext);
      }
      if (map_switches[7]) { //stats by neighbourhood
        println("twist and shout!");
      }
      int[] lenlines=new int[0];
      for(int k=0;k<txtlines.length;k++){
        int strlen=txtlines[k].length();
        lenlines=append(lenlines,strlen);
      }
      lenlines=sort(lenlines);
      int strlength=lenlines[lenlines.length-1];
      lenlines=new int[0];
      float rectWidth=strlength*7.5;float rectHeight=-18*n-4;
      float posx=mousePos.x;float posy=mousePos.y;float offsetTextx=15;float offsetTexty=rectHeight+22;float offsetRectx=10;float offsetRecty=5;
      float leftTextx=0;
      if(posx+offsetTextx+rectWidth>lmargin+wcanvas){
        rectWidth=-rectWidth;
        offsetRectx=-offsetRectx;
        leftTextx=rectWidth+2*offsetRectx;
      }
      if(posy+rectHeight<tmargin){
        posy=posy-rectHeight;
      }
      textFont(font22);
      noStroke();
      fill(255, 255, 255, 230);//was alpha=230
      rect(posx+offsetRectx, posy+offsetRecty, rectWidth, rectHeight);
      fill(0,40);//test //shadow
      text(streetname + strtext + otherlines1+otherlines2, posx+offsetTextx+leftTextx+1, posy+offsetTexty+1);//test //shadow
      fill(textcolor);
      text(streetname + strtext + otherlines1+otherlines2, posx+offsetTextx+leftTextx, posy+offsetTexty);
    }
  }
}

