package juego;

import java.awt.Color;
import entorno.Entorno; 

public class Viga {  
    private double x; 
    private double y; 
    private double ancho; 
    private double alto; 
    private Color color = new Color(83, 94, 92); 

    public Viga(double x, double y, double ancho, double alto){
        this.x = x; 
        this.y = y; 
        this.ancho = ancho; 
        this.alto = alto; 
    } 

    public boolean tocaHechizo(Hechizo hechizo){
        if(hechizo == null){
            return false; 
        } 
        double hx = hechizo.getX(); 
        double hy = hechizo.getY(); 
        // Verifica si la posisión del hechizo está dentro de la viga  
        boolean colisiona = (hx >= x - ancho / 2 && hx <= x + ancho / 2) && (hy >= y - alto / 2 && hy <= y + alto / 2); 

        return colisiona; 
    }

    public void dibujar(Entorno e){
        e.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, color); 
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
