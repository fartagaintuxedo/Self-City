
void mouseMode() {
  if (mouseOnCanvas()==true && ! mousePressed) {
    cursor(ARROW);
  }else if(! mousePressed){
    cursor(HAND);
  }else{
    noCursor();
  }
}

//////////////////////////////////

void printClock(){
  Calendar mycal=Calendar.getInstance();
  Date mydate=mycal.getTime();
  Date fakedate=new Date();
  Date clockdate;
  String faketime="10:30:00";
  try{
    fakedate=timeformatter.parse(faketime);
  }catch(ParseException e){
    println(e);
  }
  long timedif=mydate.getTime()-fakedate.getTime();
  clockdate=new Date(timedif);
  fill(255);
  rect(4,0,140,46);
  fill(40,120);
  String clock=timeformatter.format(clockdate);
  textFont(fontClock);
  text(clock,6,32);
  textFont(font16);
  text("desde la ultima actualizacion",6,46);
  fill(255,200);
  rect(92,0,56,34);
}

////////////////////////////////

void panCounter(){
  if(panned>=toPan-1){
    panstarter=false;
  }
  if(panstarter){
    panned +=(toPan-panned)/2;
  }else{
    panned=0;
    dirPan=new PVector(0,0);
  }
}

void zoomCounter(){
  if(zoomstarter){
    if(toZoom>=1.1){
      Z=1.1;
      zoomed *=1.1;
      if(zoomed>=toZoom){
        zoomstarter=false;
      }
    }else if(toZoom<=0.9){
      Z=0.9;
      zoomed *=0.9;
      if(zoomed<=toZoom){
        zoomstarter=false;
      }
    }
  }else{
    zoomed=1;
  }
}

////////////////////////////////
  
PVector pan() {
  PVector pan;
  if (mousePressed && mouseOnCanvas()) {
    pan = new PVector(mouseX-pmouseX, mouseY-pmouseY);
    //pan = new PVector(-mousePos.x+pmouseX, -mousePos.y+pmouseY);//how to get pmouse with the listener?
  }
  else {
    pan=new PVector(0, 0);
  }
  return pan;
}

//////////////////////////////////////////////////

void mouseWheel(int delta) {
  Z=1-delta*0.1;
}


void mouseMoveIn(int x, int y) {
  mousePos.x=x;
  mousePos.y=y;
}
/////////////////////////////////////////////////

float transfCoord(float n, String xORy) {
  float nn=0;
  if (xORy=="x") {  
    nn=((n+5.95)*gScale+width/2+150);//scale and center coord x
  }
  if (xORy=="y") {
    nn=((-n+37.40)*gScale+height/2-100);//scale and center coord y
  }
  return nn;
}

//////////////////////////////////////////////////

void prepareVG() {
  String dirkey;color stcolor=color(color_not_street);color buffercolor=color(0); float strokeW=0.5; String type="defaultType";
  String[] streetArrayX; String[] streetArrayY; color orgcolor = stcolor; color sourcecolor=stcolor;color targetcolor=stcolor;int counterColor=0;//color(100,116,120);
  int[] prices=new int[0]; int[] sup=new int[0]; String[] url=new String[0];
  boolean isON=true;boolean filtered=false;boolean updated=false;int counter=0; int[] values=new int[4];
  for(int i=0;i<values.length;i++){
    values[i]=0;
  }
  XMLElement[] cartchildren = xmlcart.getChildren();

  for (int i = 0; i < cartchildren.length; i ++ ) {
    String[] data=new String[10];
    counter +=1;
    dirkey=cartchildren[i].getName();
    
    String bc="defaultBC";
    XMLElement[] bcname = cartchildren[i].getChildren("barriociudad");
    for(int j=0;j<bcname.length;j++){
      bc = bcname[j].getContent();
    }
    data[0]=bc;
    data[1]=dirkey;
    
    String name="defaultName";
    XMLElement[] stname = cartchildren[i].getChildren("name");
    for(int j=0;j<stname.length;j++){
      name = stname[j].getContent();
    }
    data[2]=name;
    
    XMLElement[] poltype = cartchildren[i].getChildren("type");
    for(int j=0;j<poltype.length;j++){
      type = poltype[j].getContent();
    }
    
    if(type.equals("street")){
      data[3]="defaultPrice";//price cero at the begining
      data[4]="defaultSup";
      data[5]="defaultNumhouses";
      data[6]="defaultTrend7d";
      data[7]="defaultTrend1m";
      data[8]="defaultTrend6m";
      data[9]="defaultEvents6m";
    }
    
    XMLElement[] xcoords = cartchildren[i].getChildren("coords/x");
    streetArrayX=new String[xcoords.length];
    for(int j=0;j<xcoords.length;j++){
      streetArrayX[j] = xcoords[j].getContent();
    }
    
    XMLElement[] ycoords = cartchildren[i].getChildren("coords/y");
    streetArrayY=new String[ycoords.length];
    for(int j=0;j<ycoords.length;j++){
      streetArrayY[j] = ycoords[j].getContent();
    }
    if (dirkey != null){
      if(dirkey.equals("pol")){
        dirkey=dirkey + str(counter);
    }}
    counterColor += 1;
    int redval=counterColor%256;
    int greenval=(int(counterColor/256))%256;
    int blueval=(int(counterColor/(256*256)))%256;
    buffercolor=color(redval,greenval,blueval);
    
    if(type.equals("street")){stcolor=color(color_street);strokeW=0.8;}else{strokeW=0.5;}
    
    ArrayList[] streetarrays=new ArrayList[0];
    if((Vertexgroup)vgHash.get(dirkey)==null){
      streetarrays=(ArrayList[])append(streetarrays,create_polyArr(streetArrayX, streetArrayY));
      Vertexgroup vgroup=new Vertexgroup(streetarrays, data, prices, sup, url, type, stcolor, orgcolor, sourcecolor, targetcolor, buffercolor, strokeW, isON, filtered, updated, values);
      vgHash.put(dirkey,vgroup);
    }else{
      Vertexgroup vg=(Vertexgroup)vgHash.get(dirkey);
      vg.listcoord=(ArrayList[])append(vg.listcoord,create_polyArr(streetArrayX, streetArrayY));
    }
    streetArrayX=new String[0];
    streetArrayY=new String[0];
    data=new String[0];
  }

}

///////////////

ArrayList create_polyArr(String[] streetptsx, String[] streetptsy) {
  ArrayList<Vertice> arrpts=new ArrayList<Vertice>();
  
  for (int i=0;i<streetptsx.length;i++) { //iterates through coords strings of the polyline contained in streetpts
    
    float x=float(streetptsx[i]);//get the x coord
    float y=float(streetptsy[i]);//get the y coord
    x=transfCoord(x, "x");//scale and center coord x
    y=transfCoord(y, "y");//scale and center coord y
    PVector coord=new PVector(x, y);
    Vertice govertex=new Vertice(coord, new PVector(0, 0));
    arrpts.add(govertex);    
  }
  return(arrpts);
}

/////////////////////////////////////////////////////////////////

void prepareBC() {
  
  barriosciudad=sort(barriosciudad);
  for (int i=0;i<barriosciudad.length;i++) {
    Vertice[] bounds=new Vertice[2];
    String[] bcstr=split(barriosciudad[i], ";");
    String id=bcstr[0];
    String name=bcstr[1];
    float x1=transfCoord(float(bcstr[2]), "x");
    float x2=transfCoord(float(bcstr[3]), "x");
    float y1=transfCoord(float(bcstr[4]), "y");
    float y2=transfCoord(float(bcstr[5]), "y");
    bounds[0]=new Vertice(new PVector(x1,y1),new PVector(0,0));
    bounds[1]=new Vertice(new PVector(x2,y2),new PVector(0,0));
    boolean isON=true;
    float r=random(bc1,bc2);
    color bccolor=color(r,r,r-10);
    BC bciudad=new BC(createPolyBC(barriosciudad[i]), id, bccolor, bccolor, isON, bounds);
    bcList=(BC[]) append(bcList, bciudad);
    Button butt=new Button(new PVector(4,74+24*i), new PVector(132,20), id, name, true);//should be true
    bcButtList=(Button[]) append(bcButtList, butt);
  }
  Button allButt=new Button(new PVector(4,50), new PVector(132,20), "ALL", "TODOS", true);
  bcButtList=(Button[]) append(bcButtList, allButt);
}
//////////////////////////////////////////////////////////////////////////////////////////////////////

Vertice[] createPolyBC(String bcString) {
  Vertice[] arrpts=new Vertice[0];
  String[] pts = split(bcString, ";");
  for (int j=7;j<pts.length-1;j++) {//0==>id,1==>name,2==>xmax,3==>xmin,4==>ymax,5==>ymin,6==>"next"
    String[] coords = split(pts[j], ",");
    float x=float(coords[0]);
    float y=float(coords[1]);
    x=transfCoord(x, "x")-10;//-25 is there to fit barriosciudad CAD file to OSM file (unaccuracy)
    y=transfCoord(y, "y")+3;//+10 is there to fit barriosciudad CAD file to OSM file (unaccuracy)
    PVector coord=new PVector(x, y);
    Vertice govertex=new Vertice(coord, new PVector(0, 0));
    arrpts=(Vertice[]) append(arrpts, govertex);
  }
  return arrpts;
}

//////////////////////////////////////////////////////////////////////////////////////////////////

void prepareSwitches(){
  map_switches=new boolean[8];
  map_switches[0]=true;//start the map showing the price map
  for(int i=1;i<map_switches.length;i++){
    map_switches[i]=false;
  }
}

/////////////////////////////////////////////////////////////////////////////////////////////////

void prepareMapButtons(){
  mapsButtList[0]=new Button(new PVector(150,14), new PVector(66,22), "precios", "Precios", true); 
  mapsButtList[1]=new Button(new PVector(220,14), new PVector(66,22), "stock", "Stock", false); 
  mapsButtList[2]=new Button(new PVector(290,14), new PVector(96,22), "sup", "Superficies", false); 
  mapsButtList[3]=new Button(new PVector(390,14), new PVector(116,22), "var7d", "Variación 7 días", false); 
  mapsButtList[4]=new Button(new PVector(510,14), new PVector(116,22), "var1m", "Variación 1 mes", false); 
  mapsButtList[5]=new Button(new PVector(630,14), new PVector(116,22), "var6m", "Variación 6 meses", false); 
  mapsButtList[6]=new Button(new PVector(750,14), new PVector(96,22), "transacciones", "Transacciones", false); 
  mapsButtList[7]=new Button(new PVector(850,14), new PVector(66,22), "graficas", "Gráficas", false);
}
///////////////////////////////////////////////////////////////////////////////////////////////////

void prepareTimeButtons(){
  timeButtList[0]=new Button(new PVector(lmargin+wcanvas/2.0-198,height-bmargin-22-27), new PVector(44,27), "-6m", "atrás 6 meses", false);
  timeButtList[1]=new Button(new PVector(lmargin+wcanvas/2.0-154,height-bmargin-22-27), new PVector(44,27), "-1m", "atrás 1 mes", false);
  timeButtList[2]=new Button(new PVector(lmargin+wcanvas/2.0-110,height-bmargin-22-27), new PVector(44,27), "-7d", "atrás 7 días", false);
  timeButtList[3]=new Button(new PVector(lmargin+wcanvas/2.0+66,height-bmargin-22-27), new PVector(44,27), "7d", "adelante 7 días", false);
  timeButtList[4]=new Button(new PVector(lmargin+wcanvas/2.0+110,height-bmargin-22-27), new PVector(44,27), "1m", "adelante 1 mes", false);
  timeButtList[5]=new Button(new PVector(lmargin+wcanvas/2.0+154,height-bmargin-22-27), new PVector(44,27), "6m", "adelante 6 meses", false);
}

void updateTimeButtLoc(){
  for(int i=-198, j=0;i<-100;i=i+44){
    timeButtList[j].loc.x=lmargin+wcanvas/2.0+i;
    timeButtList[j].loc.y=height-bmargin-22-27;
    j+=1;
  }
  for(int i=66, j=3;i<160;i=i+44){
    timeButtList[j].loc.x=lmargin+wcanvas/2.0+i;
    timeButtList[j].loc.y=height-bmargin-22-27;
    j+=1;
  }
}
  
/////////////////////////////////////////////////////////////////////////////////////////////

Statistics prepareStats(){
  Statistics s=new Statistics("",450,0,0,new PVector(0,0),true);
  return s;
}

//////////////////////////////////////////////////////////////////////////////////////////

void drawline(float x1, float y1, float x2, float y2, float strokeW, color mycolor) { //optimized for stroke weights ranging from 0.5 to 1.2, decimal values are welcome.
  int X1=int(x1+0.5); int Y1=int(y1+0.5);
  int X2=int(x2+0.5); int Y2=int(y2+0.5);
  if(((X1<lmargin)&&(X2<lmargin))||((X1>wcanvas+lmargin)&&(X2>wcanvas+lmargin))||((y1<tmargin)&&(y2<tmargin))||((y1>height)&&(y2>height))){return;} // exit if points are off bounds

  PVector myline=new PVector(x2-x1, y2-y1);
  PVector perp=new PVector(y2-y1, -(x2-x1));
  float ex=x2-x1; float ey=y2-y1; float mx=ey/ex; float my=ex/ey;
  perp.normalize(); PVector minusperp=PVector.mult(perp,-1);
  int a=0;int b=0;int c=1;
  
  if(abs(ey/ex)<=1){ //less than 45 degrees we iterate over x, else over y.
  if ((X2>X1)){a=1;}else if(X2<X1){b=1;c=-1;}
  
    for (int x=0; x<=int(c*(x2-x1)+0.5); x++){
      float yline=mx*x+y1*a+y2*b; 
      float xline=x;
      if (((x+X1*a+X2*b)>=lmargin)&&((x+X1*a+X2*b)<wcanvas+lmargin)&&(yline>=tmargin)&&(yline<height-bmargin)){
        //for (int k=0;k<steps.length;k++) {//10 for strokeW==2, seems 20 already gives gaps between lines. If strokeW==1 then 50 is enough  
          for (int k=-50;k<=50;k=k+100) {
          //PVector offsvec=PVector.mult(perp, steps[k]/100.0);  //these divisions take time, possible to get rid of them?
          PVector offsvec=PVector.mult(perp, k/100.0);
          float offsetx=xline+offsvec.x;  
          float offsety=yline+offsvec.y; 
          int gx=int(offsetx+0.5);//gx and gy are the real coords of the pixel offsettes from the line
          int gy=int(offsety+0.5);
          int gt=gy*width+gx+X1*a+X2*b;
          if((gt<width*height)&&(gt>=0)){ //reduce memory use: give him the product done already // could this move above the for loop? - danger of pixel array out of bounds
            PVector trueoffsvec=new PVector(gx-xline, gy-yline);
            float distance=abs(trueoffsvec.dot(perp));
            float blendval=distance-(strokeW/2-0.5);
            color origc=pixels[gy*width+gx+X1*a+X2*b];
            float rval=red(mycolor)-blendval*(red(mycolor)-red(origc));// reduce memory use: give him the division done already strokeW/2
            float gval=green(mycolor)-blendval*(green(mycolor)-green(origc));
            float bval=blue(mycolor)-blendval*(blue(mycolor)-blue(origc));
          //  rval=constrain(rval, 0, 255);gval=constrain(gval, 0, 255);bval=constrain(bval, 0, 255);//no need for this line
            color colorval=color(rval,gval,bval);
            pixels[gy*width+gx+X1*a+X2*b]=colorval; 
          }
        }
      }
    }
  }else{ //here we iterate over y.
    if ((Y2>Y1)){a=1;}else if(Y2<Y1){b=1;c=-1;}
  
    for (int y=0; y<=int(c*(y2-y1)+0.5); y++){
      float xline=my*y+x1*a+x2*b; 
      float yline=y;
      if (((y+Y1*a+Y2*b)>=tmargin)&&((y+Y1*a+Y2*b)<height-bmargin)&&((xline>=lmargin)&&(xline<lmargin+wcanvas))){
        //for (int k=0;k<steps.length;k++) {//10 for strokeW==2, seems 20 already gives gaps between lines. If strokeW==1 then 50 is enough  
          for (int k=-50;k<=50;k=k+100) {
          //PVector offsvec=PVector.mult(perp, steps[k]/100.0);  //these divisions take time, possible to get rid of them?
          PVector offsvec=PVector.mult(perp, k/100.0);
          float offsetx=xline+offsvec.x;  
          float offsety=yline+offsvec.y; 
          int gx=int(offsetx+0.5);//gx and gy are the real coords of the pixel offsettes from the line
          int gy=int(offsety+0.5);
          int gt=(gy+Y1*a+Y2*b)*width+gx;
          if((gt<width*height)&&(gt>=0)){ //could this move above the for loop? - danger of pixel array out of bounds
            PVector trueoffsvec=new PVector(gx-xline, gy-yline);
            float distance=abs(trueoffsvec.dot(perp));
            
            float blendval=distance-(strokeW/2-0.5);            
            color origc=pixels[(gy+Y1*a+Y2*b)*width+gx];      
            float rval=red(mycolor)-blendval*(red(mycolor)-red(origc));// reduce memory use: give him the division done already strokeW/2
            float gval=green(mycolor)-blendval*(green(mycolor)-green(origc));
            float bval=blue(mycolor)-blendval*(blue(mycolor)-blue(origc));
           // rval=constrain(rval, 0, 255);gval=constrain(gval, 0, 255);bval=constrain(bval, 0, 255);//no need for this line
            color colorval=color(rval,gval,bval);
            pixels[(gy+Y1*a+Y2*b)*width+gx]=colorval; 
          }
        }
      }
    } 
  }
}
/*
void fillpolygon(Vertice[] pts, color mycolor){
  for(int y=tmargin;y<height;y++){
    float[] xarray=new float[0];
    for(int i=0;i<pts.length-1;i++){ 
      PVector v1= pts[i].coord;
      PVector v2=pts[i+1].coord;
      if(((v2.y>=y && v1.y<y) || (v2.y<=y && v1.y>y))){
        int x=int(v2.x+(y-v2.y)*(v1.x-v2.x)/(v1.y-v2.y));
        xarray=append(xarray,x);
      }
      xarray=sort(xarray);
      for(int j=0;j<xarray.length-1;j=j+2){
        
        
          for(int k=int(xarray[j]);k<=xarray[j+1];k=k+1){
            if(k>lmargin && k<lmargin+wcanvas){
           pixels[width*y + k]=color(mycolor);
        }}}}}}
*/
