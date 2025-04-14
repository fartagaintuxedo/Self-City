
import java.awt.event.*;

XMLElement xmlcart;
XMLElement xmldata;

Calendar cal; 
Date currentdate;
String strcurrentdate="";
String dateDisplay="";
SimpleDateFormat formatter;
SimpleDateFormat timeformatter;
SimpleDateFormat formatterDisplay;

int global_fade_timer;
boolean click=false;
boolean leftbutton;
boolean resized=false;
boolean panstarter=false;
boolean zoomstarter=false;
float panned=0;
float toPan=0;
float toZoom=1;
float zoomed=1;
PVector dirPan=new PVector(0,0);
int counter;//this is for mouseclick event

int wcanvas;//=width-rmargin-lmargin;//824;//width in pixels
int hcanvas;//=height-tmargin-bmargin;
int pwidth;
int pheight;

DataFilter dataf;
boolean[] map_switches;
String[] barriosciudad;
ArrayList<String[]>statsList=new ArrayList<String[]>();
BC[] bcList=new BC[0];
Button[] bcButtList=new Button[0];
Button[] mapsButtList=new Button[8];
Button[] timeButtList=new Button[6];

HashMap vgHash = new HashMap(1500);

Statistics stats;

PVector mousePos=new PVector(0,0);
float Z;//this is for zoom
PFont font22,font18,font14,font16,fontClock;
PImage plantilla;
PImage timecontrol;
PGraphics buffer;

void setup() {
  size(1024, 768, P2D);
  frame.setResizable(true);
  wcanvas=width-rmargin-lmargin;
  hcanvas=height-tmargin-bmargin;

  pwidth=width;pheight=height;
  
  fill(0);//fill(0,9,11);
  noStroke();
  rect(0,0,width,height);
  //plantilla = loadImage("plantilla.png");//its in data directory
  timecontrol=loadImage("timecontrol.png");
  //image(plantilla,0,0);
  
  cal=Calendar.getInstance();
  currentdate=cal.getTime();
  
  buffer = createGraphics(width, height, P2D);
  
  timeformatter = new SimpleDateFormat("HH:mm:ss",new Locale("es", "ES")); 
  formatter = new SimpleDateFormat("yyyy-MM-dd");
  formatterDisplay = new SimpleDateFormat("dd MMMMM yyyy", new Locale("es", "ES"));
  //font = loadFont("OCRAExtended-16.vlw");
  //font14=createFont("Arial Unicode MS", 14, false);
  //font11=createFont("Arial Unicode MS", 11, false);
  font14=loadFont("BrowalliaNew-14.vlw");
  font16=loadFont("BrowalliaNew-16.vlw");
  font18=loadFont("Arial-BoldMT-12.vlw");
  font22=loadFont("BrowalliaNew-22.vlw");
  fontClock=loadFont("BrowalliaNew-52.vlw");
  //textFont(font14);
  textMode(SCREEN);
  barriosciudad=loadStrings("data/121123_barrios_ciudad_WGS84.txt");

  xmlcart = new XMLElement(this, "2012-10-18_base_cartography.xml" ); //they are in data/  - its the default folder

  Z=1;
  global_fade_timer=0;
  
  dataf=new DataFilter(new PVector(width-rmargin+2,height-bmargin),new PVector(width-rmargin+2,tmargin),0,0,false,false,0,0,0);
  prepareSwitches();
  prepareVG();
  prepareBC();
  prepareMapButtons();
  prepareTimeButtons();
  //stats=prepareStats();

  addMouseWheelListener(new MouseWheelListener() { 
    public void mouseWheelMoved(MouseWheelEvent mwe) { 
      mouseWheel(mwe.getWheelRotation());
    }});
  
  addMouseMotionListener(new MouseMotionListener() { 
    public void mouseMoved(MouseEvent e) { 
      mouseMoveIn(e.getX(), e.getY());
    }
    public void mouseDragged(MouseEvent e) { 
      ;}});
    }

void draw() {
  //background(255);
  mouseMode();
  fill(bkg);//fill(0,9,11);
  noStroke();
  rect(lmargin,tmargin,wcanvas+1,hcanvas+1);
  ////////////////////////////////////////////////////////
  wcanvas=width-rmargin-lmargin;
  hcanvas=height-tmargin-bmargin;
  resized=false;
  if(pwidth != width || pheight != height){
    resized=true;
  }
  if(resized){
    background(bkg);
    //image(plantilla,0,0);
    buffer = createGraphics(width, height, P2D);
    updateTimeButtLoc();
    if(dataf.bottloc.y>height-bmargin){
      dataf.bottloc.y=height-bmargin;
    }
  }
  pwidth=width; pheight=height;
  //////////////////////////////////////////////////////////
  buffer.beginDraw();
  buffer.fill(0);
  buffer.rect(lmargin,tmargin,wcanvas,hcanvas);
  buffer.endDraw();
  /////////////////////////////////////////////////////////
  float zoom=Z; 
  Z=1;
  global_fade_timer+=1;
  panCounter();
  zoomCounter();
  /////////////////////////////////////
  onoffevent=false;
  for (int i=0;i<bcButtList.length;i++) {
    bcButtList[i].drawBCButt();
    bcButtList[i].fitBCtoScreen();
    if(mouseclick() && bcButtList[i].mouseOnButton()){
      onoffevent=true;
    }
  }
  dateevent=false;
  for (int i=0;i<timeButtList.length;i++) {
    timeButtList[i].visibilityTime();//we have to call this one here because we need to call the drawtimebutt later after drawing the map.
    if(click && timeButtList[i].mouseOnButton()){
      dateevent=true;
    }
  }
  switchmapevent=false;
  for (int i=0;i<mapsButtList.length;i++) {
    mapsButtList[i].drawMapsButt();
    if(mouseclick() && mapsButtList[i].mouseOnButton()){
      switchmapevent=true;
    }
  }
  //////////////////////////////////////////////////
  switch_map();
  
  /////////////////////////////////////
  filterevent=false;
  dataf.drawFilter();
  if((dataf.dragtop || dataf.dragbott)){
    filterevent=true;
  }
  ///////////////////////////////////////
  for (int i=0;i<bcList.length;i++) {
    bcList[i].drawBC(zoom);
  }
  /////////////////////////////////////
  Iterator vgiter0 = vgHash.entrySet().iterator(); //al this loop through the hash is to reset "updated", pretty annoying isnt it?
  while(vgiter0.hasNext()){
    Map.Entry val = (Map.Entry)vgiter0.next();
     Vertexgroup vg=(Vertexgroup)val.getValue();
     if(mouseOnTimeArea() && mouseclick() ){//reset the update switch, it turns true at the global update function
      for(int i=0;i<timeButtList.length;i++){
        if(timeButtList[i].isON==true){
          vg.updated=false;
        }
      }
    }
  }
  ////////////////////////////////////////////
  updatedata();
  ////////////////////////////////////////////
  loadPixels();
  Iterator vgiter1 = vgHash.entrySet().iterator(); // this loop through the hash is to draw the lines
  while(vgiter1.hasNext()){
    Map.Entry val = (Map.Entry)vgiter1.next();
     Vertexgroup vg=(Vertexgroup)val.getValue();
     vg.drawVG(zoom);
  }
  updatePixels();
  ////////////////////////////////////////////////
  Iterator vgiter2 = vgHash.entrySet().iterator();  //this loop through the hash is to display text when mouse is on top of a street
  while(vgiter2.hasNext()){
    Map.Entry val = (Map.Entry)vgiter2.next();
     Vertexgroup vg=(Vertexgroup)val.getValue();
     vg.drawStdata();
    // if(vg.data.length>0 && vg.data[1] != null){
     //if(vg.data[1].equals("calle_arroyo")){println(str(vg.values[3]) + " val");}}
  }
  ////////////////////////////
  image(timecontrol,lmargin+wcanvas/2-209,height-bmargin-22-27);
  for (int i=0;i<timeButtList.length;i++) {
    timeButtList[i].drawTimeButt();
  }
  printClock();
  //////////////////////////////      
  //stats.drawStatistics();
  //////////////////////////////
    println(frameRate);
    //println(str(Z) + ";" + panstarter + ";zoomed:" + str(zoomed) + ";to zoom:" + str(toZoom));
    //println(global_fade_timer);
    //println("onoffevent " + onoffevent + ", dateevent " + dateevent + ", switchmapevent " + switchmapevent + ", filterevent " + filterevent);
    //println(frameCount);
    //println(currentdate);
    //println(str(dataf.minval) + ", " + str(dataf.maxval));
    //println("bott " + str(dataf.values[2]) + ", top" + str(dataf.values[3]));
    //println(width);
  //println(statsList.get(0));
  ///////////////////////////////
  click=false;
  
}




