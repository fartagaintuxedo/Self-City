class Vertice {
  PVector coord;
  PVector pan;

  Vertice(PVector _coord, PVector _pan) {
    coord=_coord;
    pan=_pan;
  }
  
  void update(float zoom) {
    pan=pan();
    float a=0;
    if(panstarter){
      a=((toPan-panned)/2);
    }
    coord.x=width/2-(width/2-coord.x)*zoom+pan.x+a*dirPan.x;
    coord.y=height/2-(height/2-coord.y)*zoom+pan.y+a*dirPan.y;
  }
  
}


