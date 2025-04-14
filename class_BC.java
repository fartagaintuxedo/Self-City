class BC {

  Vertice[] arrpts;
  String id;
  color mycolor;
  color mycolororiginal;
  boolean isON;
  Vertice[] bounds;

  BC(Vertice[] _arrpts, String _id, color _mycolor, color _mycolororiginal, boolean _isON, Vertice[] _bounds) {
    arrpts=_arrpts;
    id=_id;
    mycolor=_mycolor;
    mycolororiginal=_mycolororiginal;
    isON=_isON;
    bounds=_bounds;
  }

  void drawBC(float zoom) {
    visibility();
    noStroke();
    fill(mycolor);
    
    if (isON==false) {
      noFill();
    }
    for(int i=0;i<bounds.length;i++){
      bounds[i].update(zoom);
    }
    //rectMode(CORNERS);
    //stroke(1);noFill();
    //rect(bounds[0].coord.x,bounds[0].coord.y,bounds[1].coord.x,bounds[1].coord.y);
    //rectMode(CORNER);
    beginShape();
    int counter1=0;int counter2=0;int counter3=0;int counter4=0;
    for (int i=0;i<arrpts.length;i++) {
      arrpts[i].update(zoom);
      float x=arrpts[i].coord.x; float y=arrpts[i].coord.y;
      //if((lmargin-x)<5 && (lmargin-x)>0){x=lmargin;}if((x-lmargin+wcanvas)<5 && (x-lmargin+wcanvas)>0){x=lmargin+wcanvas;}
      //if((tmargin-y)<5 && (tmargin-y)>0){y=tmargin;}if((y-height)<5 && (y-height)>0){y=height;}
      if(x>=lmargin && x<=lmargin+wcanvas && y>=tmargin && y<=height-bmargin){
      vertex(x, y);
    }
    if(x<lmargin && y<tmargin && counter1==0){//upper left corner
      vertex(lmargin,tmargin);
      counter1+=1;
    }
    if(x>lmargin+wcanvas && y<tmargin && counter2==0){//upper right corner
      vertex(lmargin+wcanvas,tmargin);
      counter2+=1;
    }
    if(x>lmargin+wcanvas && y>height-bmargin && counter3==0){//bottom right
      vertex(lmargin+wcanvas,height-bmargin);
      counter3+=1;
    }
    if(x<lmargin && y>height-bmargin && counter4==0){//bottom left
      vertex(lmargin,height-bmargin);
      counter4+=1;
    }
  
}
    endShape();
  //fillpolygon(arrpts, mycolor);
  }
  

  void visibility() {
    mycolor=mycolororiginal;
   
      for (int i=0;i<bcButtList.length-1;i++) {
        
        if (mouseclick()==false && bcButtList[i].mouseOnButton()==true && id.equals(bcButtList[i].id)==true){
          mycolor=color (255,255,100,150);//selected BC
          
        }
      }
  }

}

