
class DataFilter{
  PVector toploc;
  PVector bottloc;
  int minval;
  int maxval;
  boolean dragtop;
  boolean dragbott;
  int counterpressed;
  int topval;
  int bottval;
  
  DataFilter (PVector _bottloc, PVector _toploc, int _minval, int _maxval, boolean _dragtop, boolean _dragbott, int _counterpressed, int _topval, int _bottval){
    
    toploc=_toploc;
    bottloc=_bottloc;
    minval=_minval;
    maxval=_maxval;
    dragtop=_dragtop;
    dragbott=_dragbott;
    counterpressed=_counterpressed;
    topval=_topval;
    bottval=_bottval;
  }
  
  void drawFilter(){
    updateValues();
    dragFilterArrows();
    constrainArrows();
    fill(bkg);
    rect(width-rmargin,0,rmargin,height);
    drawgradient();
    fill(255,200);
    rect(toploc.x+5,tmargin,20,toploc.y-tmargin);
    rect(bottloc.x+5,height-bmargin,20,bmargin+bottloc.y-height);
    //draw top triangle - arrow
    noStroke();
    fill(150);
    if(toploc.y !=tmargin){
      fill(200);
    }
    beginShape();
    vertex(toploc.x,toploc.y-15);
    vertex(toploc.x+30,toploc.y-15);
    vertex(toploc.x+30,toploc.y-5);
    vertex(toploc.x+15,toploc.y+10);
    vertex(toploc.x,toploc.y-5);
    endShape();
    //draw bottom triangle - arrow
    fill(150);
    if(bottloc.y !=height-bmargin){
      fill(200);
    }
    beginShape();
    vertex(bottloc.x,bottloc.y+15);
    vertex(bottloc.x+30,bottloc.y+15);
    vertex(bottloc.x+30,bottloc.y+5);
    vertex(bottloc.x+15,bottloc.y-10);
    vertex(bottloc.x,bottloc.y+5);
    endShape();
    
    fill(50);
    textFont(font14);
    text(bottval,bottloc.x+5,bottloc.y+12);
    text(topval,toploc.x+5,toploc.y-5);
    
    
  }
  
  void updateValues(){
    float bottvalfilter=minval+(height-bmargin-bottloc.y)*(maxval-minval)/(hcanvas);//map(height-bmargin-bottloc.y,tmargin,height-bmargin,minval,maxval);
    float topvalfilter=maxval-(toploc.y-tmargin)*(maxval-minval)/(hcanvas);
    bottval=int(bottvalfilter);
    topval=int(topvalfilter);
  }

  void drawgradient(){
    int[] values=new int[4];
    String[] data=new String[10];
    color defaultcolor=color(0);
    int step=12;
    int gap=2;
    noStroke();
    for(int i=gap/2;i<hcanvas;i=i+step){
      float val=map(i,hcanvas,0,minval,maxval);
      for(int j=0;j<data.length;j++){
        data[j]=str(val);
      }
      values=getcolorfromdata(values, data, defaultcolor);
      fill(values[0],values[1],values[2]);
      rect(toploc.x+5,tmargin+i,20,step-gap);
      fill(bkg);
      rect(toploc.x+5,tmargin+i+(step-gap),20,gap);
    }
  }
  
  void constrainArrows(){
    int toplimit=int(bottloc.y-40);
    int bottlimit=int(toploc.y+40);
    
    if(toploc.y>=toplimit){
      toploc.y=toplimit;
    }
    if(bottloc.y<bottlimit){
      bottloc.y=bottlimit;
    }
    if(bottloc.y>height-bmargin){
      bottloc.y=height-bmargin;
    }
    if(toploc.y<tmargin){
      toploc.y=tmargin;
    }
  }
  
  void dragFilterArrows(){
    toploc.x=width-rmargin+2;
    bottloc.x=toploc.x;
    
    if(dragtop || dragbott){
      if(counterpressed%4==0){
        global_fade_timer=0;
      }
      counterpressed+=1;
    }

    if(mousePressed && mouseOnTopArrow()){
      dragtop=true;
    }
    if(mousePressed && mouseOnBottArrow()){
      dragbott=true;
    }
    if(mouseclick()){
      dragtop=false;
      dragbott=false;
      counterpressed=0;
    }
    if(dragtop){
      toploc.y=toploc.y+(mouseY-pmouseY);
    }
    if(dragbott){
      bottloc.y=bottloc.y+(mouseY-pmouseY);
    }
  }
  
  boolean mouseOnTopArrow(){
    boolean onTop=false;
    if(mouseX>=toploc.x && mouseX<=toploc.x+20 && mouseY<=toploc.y+10 && mouseY>=toploc.y){
      onTop=true;
    }
    return onTop;
  }
  
   boolean mouseOnBottArrow(){
    boolean onBott=false;
    if(mouseX>=bottloc.x && mouseX<=bottloc.x+20 && mouseY<=bottloc.y && mouseY>=bottloc.y-10){
      onBott=true;
    }
    return onBott;
  }

}



