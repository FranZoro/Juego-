package juego;

import java.awt.Color;
import java.awt.color.*; 

import entorno.Entorno; 

public class Spawn { 
    int x; 
    int y; 
    boolean hayMonstruo = false; 

    public Spawn(int x, int y){
        this.x = x; 
        this.y = y; 
    } 

    public int getX(){
        return x; 
    } 

    public int getY(){
        return y; 
    } 

    public boolean getHayMonstruo(){
        return hayMonstruo; 
    } 

    public void setHayMonstruo(boolean b){
        hayMonstruo = b; 
    }

    public void dibujar(Entorno entorno){
        entorno.dibujarRectangulo(x, y, 5, 5, 0, Color.white); 
    }
}
