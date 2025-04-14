
boolean mouseclick() {
  boolean event=false;
  if (frameCount-counter==1) {
    event=true;
  }
  return event;
}

/////////////////////////////////

void mouseReleased() {
  click=true;
  if(mouseButton==LEFT){
  leftbutton=true;
  }else{
    leftbutton=false;
  }
  counter=frameCount;
  cursor(ARROW);//test
}

///////////////////////////////

boolean toogle(boolean isON) {
  if (isON==false) {
    isON=true;
  }else {
    isON=false;
  }
  return isON;
}

////////////////////////////////

boolean allPressed(){ //shouldnt this go under buttons class?
  
  boolean allP=false;
  if( bcButtList[bcButtList.length-1].id=="ALL" && bcButtList[bcButtList.length-1].mouseOnButton()==true){
    allP=true;
  }
  return allP;
}

//////////////////////////////////////////////////////////////

void switch_map(){
  if(mouseclick() && mouseOnMapsArea()==true){
    for(int i=0;i<map_switches.length;i++){
      map_switches[i]=false;//reset all
    }
    for(int k=0;k<mapsButtList.length;k++){
      if(mapsButtList[k].isON==true){
        map_switches[k]=true;//then turn map switch on if button isON
      }
    }
  }
}

////////////////////////////////////////////////////////////////

boolean mouseOnBCArea() {
    boolean msOnBCArea=false;
    if ((mousePos.x<lmargin)&&(mousePos.y<height-bmargin)&&(mousePos.y>tmargin)) {
      msOnBCArea=true;
    }
    return msOnBCArea;
  }
  
  boolean mouseOnMapsArea() {
    boolean msOnMapsArea=false;
    if ((mousePos.x<width-rmargin)&&(mousePos.x>lmargin)&&(mousePos.y<tmargin)) {
      msOnMapsArea=true;
    }
    return msOnMapsArea;
  }
  
  boolean mouseOnFilterArea() {
    boolean msOnFilterArea=false;
    if ((mousePos.x>width-rmargin)&&(mousePos.y<height-bmargin)&&(mousePos.y>tmargin)) {
      msOnFilterArea=true;
    }
    return msOnFilterArea;
  }
  
  boolean mouseOnTimeArea() {
    boolean msOnTimeArea=false;
    if ((mousePos.x>lmargin+wcanvas/2.0-211)&&(mousePos.x<lmargin+wcanvas/2.0+211)&&(mousePos.y>height-bmargin-22-27)&&(mousePos.y<height-bmargin-22)) {
      msOnTimeArea=true;
    }
    return msOnTimeArea;
  }
  
  boolean mouseOnCanvas() {
    boolean msOnCanvas=false;
    if ((! mouseOnTimeArea()) && (mousePos.x>lmargin)&&(mousePos.x<width-rmargin)&&(mousePos.y>tmargin)&&(mousePos.y<height-bmargin)) { //incomplete *needs to subtract time area//
        msOnCanvas=true;
    }
    return msOnCanvas;
  }

