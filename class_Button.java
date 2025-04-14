class Button {

  PVector loc;
  PVector dim;
  String id;
  String name;
  boolean isON;

  Button(PVector _loc, PVector _dim, String _id, String _name, boolean _isON) {
    loc=_loc;
    dim=_dim;
    id=_id;
    name=_name;
    isON=_isON;
  }

  void drawBCButt() {

    visibilityBC();
    noStroke();
    if (mouseOnButton()) {
      fill(80);
    }else if(isON){
      fill(140);
    }else {
      fill(200);
    }
    rect(loc.x, loc.y, dim.x, dim.y);
    if (mouseOnButton()) {
      fill(140);
    }else if(isON){
      fill(50);
    }else {
      fill(230);
    }
    noStroke();
    rect(loc.x+dim.x+2,loc.y, 10,20);
    textFont(font18);
    text(name, loc.x+4, loc.y+dim.y-6);
  }
  
  void drawMapsButt() {
    color onmouse=0;
    color onselection=0;
    
    visibilityMaps();
    
    noStroke();
    if(mouseOnButton() && ! isON){
      onmouse=color(120);
      onselection=color(bkg);
    }else if(isON || (isON && mouseOnButton())){
      onmouse=color(255,0,0);
      onselection=color(255,0,0);
    }else{
      onmouse=color(180);
      onselection=color(bkg);
    }
    noStroke();
    fill(140);
    rect(loc.x,loc.y+2,dim.x,dim.y-4);
    fill(onmouse);
    rect(loc.x,loc.y + dim.y, dim.x,10);
    fill(onselection);
    rect(loc.x,loc.y, dim.x,-10);
    if(isON){
      fill(30);
    }else{
      fill(180);
    }
    textFont(font18);
    text(name, loc.x+4, loc.y+dim.y-6);
  }
  
  void drawTimeButt(){
    //visibilityTime();//we call this function separately, before drawing map
    noFill();
    noStroke();
    if(mousePressed && mouseOnButton()){
      fill(220,50);
    }
    rect(loc.x,loc.y,dim.x,dim.y);
    textFont(font22);
    textAlign(CENTER);  
    fill(100,40);
    //text(dateDisplay,lmargin+wcanvas/2+1,height-bmargin-22-8+1);
    fill(200);
    text(dateDisplay,lmargin+wcanvas/2,height-bmargin-22-8);
    textAlign(LEFT);
  }
  
  boolean mouseOnButton() {
    boolean msonbutt=false;
    if ((mousePos.x<loc.x+dim.x)&&(mousePos.x>loc.x)&&(mousePos.y<loc.y+dim.y)&&(mousePos.y>loc.y)) {
      //if ((mouseX<loc.x+dim.x)&&(mouseX>loc.x)&&(mouseY<loc.y+dim.y)&&(mouseY>loc.y)) {
      msonbutt=true;
    }
    return msonbutt;
  }
  
  void fitBCtoScreen(){
    
    if(mouseOnBCArea() && mouseclick() && ! leftbutton && mouseOnButton()){ 
      for(int i=0;i<bcList.length;i++){
        BC bc=bcList[i];
        if(id.equals(bc.id)){
          float widthdim=abs(bc.bounds[0].coord.x-bc.bounds[1].coord.x)+200;
          float heightdim=abs(bc.bounds[0].coord.y-bc.bounds[1].coord.y)+200;
          PVector bcPos=new PVector((bc.bounds[0].coord.x+bc.bounds[1].coord.x)/2,(bc.bounds[0].coord.y+bc.bounds[1].coord.y)/2);
          PVector cen=new PVector(lmargin+wcanvas/2,tmargin+hcanvas/2);
          PVector exc=PVector.sub(cen,bcPos);
          panstarter=true;
          zoomstarter=true;
          dirPan=exc.get();
          dirPan.normalize();
          panned=0;
          zoomed=1;
          toPan=exc.mag();
          if(widthdim/heightdim>=wcanvas/hcanvas){
            toZoom=wcanvas/widthdim;
          }else{
            toZoom=hcanvas/heightdim;
          }  
        }
      }
    }
  }
  
  void visibilityBC() {
    
    if(mouseclick()==true){
      if(mouseOnButton() || allPressed()){
        global_fade_timer=0;
      }
      if(allPressed()==true && leftbutton==true){
        isON=true;
      }
      if(leftbutton){
        if(mouseOnButton()==true && ! id.equals("ALL")){
          isON=toogle(isON);
        }
      }else{
        if(mouseOnButton()==true ){
          isON=true;
        }else if(mouseOnBCArea()==true && ! id.equals("ALL")){
          isON=false;
        }
      }
    }
  }
  
  void visibilityMaps() {
    if(mouseclick()==true){
      if(leftbutton){
        if(mouseOnButton()==true && mouseOnMapsArea()==true){
          isON=true;
          global_fade_timer=0;//reset the timer if a new map is selected
        }else if(mouseOnMapsArea()==true){
          isON=false;
        }
      }
    }
  }
  
  void visibilityTime(){
    isON=false;
    if(mouseclick()==true && mouseOnButton()==true){
      isON=true;
      global_fade_timer=0;
    }
  }
  
 
}

