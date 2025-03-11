package juego;

import java.awt.Color;
import java.awt.color.*; 

import entorno.Entorno; 

public class Hechizo {
    private int x; 
    private int y; 
    private int ancho; 
    private int alto; 
    private int velocidad; 
    private String direccion; 
    private Color color = new Color(55, 255, 251); 

    public Hechizo(int x, int y, int ancho, int alto, int velocidad, String direccion){
        this.x = x; 
        this.y = y; 
        this.ancho = ancho; 
        this.alto = alto; 
        this.velocidad = velocidad; 
        this.direccion = direccion; 
    } 

    public void dibujar(Entorno entorno){
        entorno.dibujarRectangulo(x, y, ancho, alto, 0, color); 
        if(direccion.equals("izq") && velocidad > 0){
            velocidad *= -1; 
        }
    } 

    public boolean seFue(Entorno entorno){
        boolean izq = this.x - this.ancho / 2 > entorno.ancho(); 
        boolean der = this.x + this.ancho / 2 < 0; 
        if(izq || der){
            return true; 
        }
        return false; 
    } 

    public void mover(){
        x += velocidad; 
    } 

    public double getX(){
        return x; 
    } 

    public double getY(){
        return y; 
    } 

    public double getAncho(){
        return ancho; 
    } 

    public double getAlto(){
        return alto; 
    }
} 
