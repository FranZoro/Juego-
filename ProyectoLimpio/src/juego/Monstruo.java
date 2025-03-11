package juego; 

import java.awt.Color;
import java.awt.color.*;
import java.util.ArrayList;

import entorno.Entorno;

public class Monstruo { 
    private double x; 
    private double y; 
    private int ancho; 
    private int alto; 
    private int caida; 
    private double velocidad; 
    private int viga; 
    private int indice; 
    private int spawn; 
    private Hechizo hechizo; 
    private String mirando; 

    public Monstruo(int x, int y, int ancho, int alto, int caida, double velocidad, int indice, int spawn){
        this.x = x; 
        this.y = y; 
        this.ancho = ancho; 
        this.alto = alto; 
        this.caida = caida; 
        this.velocidad = velocidad / 3; 
        this.indice = indice; 
        this.spawn = spawn; 
    } 

    public void dibujar(Entorno entorno){
        entorno.dibujarRectangulo(x, y, ancho, alto, 0, Color.red); 
        if(velocidad < 0){
            mirando = "izq"; 
        } else {
            mirando = "der"; 
        }
    } 

    public boolean hayColisionAbajo(ArrayList<Viga> vigas){//Viga[] plataformas){
        int contadorViga = -1; 
        for(Viga viga : vigas){//plataforma : plataformas){
            contadorViga++; 
            if(viga == null){//plataforma == null){
                continue; 
            } 
            double bordeInfMonstruo = this.y + this.alto / 2; 
            double bordeSupViga = viga.getY() -(viga.getAlto() / 2); //plataforma.getY() - (plataforma.getAlto() / 2); 
            double bordeInfViga = viga.getY() + (viga.getAlto() / 2); //plataforma.getY() + (plataforma.getAlto() / 2); 
            boolean abajo = bordeInfMonstruo >= bordeSupViga && (bordeInfMonstruo >= bordeSupViga && bordeInfMonstruo <= bordeInfViga); 
            boolean der = this.x + this.ancho / 2 > viga.getX() - viga.getAncho() / 2;//plataforma.getX() - plataforma.getAncho() / 2; 
            boolean izq = viga.getX() + viga.getAncho() / 2 > this.x - this.ancho / 2; //plataforma.getX() + plataforma.getAncho() / 2 > this.x - this.ancho / 2; 
            if(abajo){
                if(der && izq){
                    this.y = (int) bordeSupViga - (this.alto / 2) - 1; 
                    this.viga = contadorViga; 
                    return true;   
                }
            }
        } 
        return false; 
    } 

    public boolean hayColisionDerecha(Viga  viga){//plataforma){
        //boolean aux = false; 
            double bordeDerMago = this.x + this.ancho / 2; 
            double bordeDerViga = viga.getX() + (viga.getAncho() / 2);//plataforma.getX() + (plataforma.getAncho() / 2); 
            boolean der = bordeDerMago >= bordeDerViga - 1; 
            //if(der){
                //aux = true; 
            //}
            return der;  
    } 

    public boolean hayColisionIzquierda(Viga viga){//plataforma){
        //boolean aux = false; 
            double bordeIzqMago = this.x - this.ancho / 2; 
            double bordeIzqViga = viga.getX() - (viga.getAncho() / 2); //plataforma.getX() - (plataforma.getAncho() / 2); 
            boolean izq = bordeIzqMago <= bordeIzqViga + 1; 
            //if(izq){
                //aux = true; 
            //} 
            return izq;  
    } 

    public boolean tocaHechizo(Hechizo hechizo){
        if(hechizo == null){
            return false; 
        } 
        boolean aba = hechizo.getY() >= this.getY() && hechizo.getY() - this.getY() <= hechizo.getAlto()/2 + this.getAlto()/2;
        boolean arr = hechizo.getY() <= this.getY() && this.getY() - hechizo.getY() <= hechizo.getAlto()/2 + this.getAlto()/2;
        boolean der = hechizo.getX() >= this.getX() && hechizo.getX() - this.getX() <= hechizo.getAncho()/2 + this.getAncho()/2;
        boolean izq = hechizo.getX() <= this.getX() && this.getX() - hechizo.getX() <= hechizo.getAncho()/2 + this.getAncho()/2;
    return (der && (arr || aba)) || (izq && (arr || aba)); 
    } 

    public boolean congeladoMonstruo(Hechizo hechizo){
        return tocaHechizo(hechizo); 
    }  

    public boolean empujarMonstruo(Mago mago, ArrayList<Viga> vigas, String direccion){//Viga[] plataformas, String direccion){
        if(!congeladoMonstruo(this.hechizo)){
            return false; // No se puede empujar si el monstruo no está congelado 
        } 
        double distancia = Math.abs(mago.getX() - this.x);
        if (distancia > mago.getAncho()) {
        return false; // El personaje debe estar cerca del monstruo para empujarlo
        }

        boolean puedeCaer = true;
        for (Viga viga : vigas){//plataforma : plataformas) {
            if (viga == null){//plataforma == null) continue;
                if (direccion.equals("der") && hayColisionDerecha(viga)){//plataforma)) {
                    puedeCaer = false;
                    break;
                } else if (direccion.equals("izq") && hayColisionIzquierda(viga)){//plataforma)) {
                    puedeCaer = false;
                    break;
                }
            }
        }

        if (puedeCaer) {
            this.x += (direccion.equals("der") ? this.ancho : -this.ancho); // Mueve el monstruo
            this.y += this.caida; // Simula la caída
            return true;
        } 
        return false; 
    }

    public void caida(ArrayList<Viga> vigas){//Viga[] plataformas){ 
        if(!hayColisionAbajo(vigas)){//plataformas)){ // Sí no hay una viga debajo 
            this.y += caida; //Aumenta la posicion en Y para similar la caída 
        } 
    } 

    public void mover(ArrayList<Viga> vigas){//Viga[] plataformas){
        if(hayColisionAbajo(vigas)){//plataformas)){
            //Viga[] plataformaActual = vigas.toArray(new Viga[0]);//plataformas[this.viga]; 
            boolean colisionDerecha = false; 
            boolean colisionIzquierda = false; 
            
            for(Viga viga : vigas){
                if(viga == null){
                    continue; 
                } 
                if(hayColisionDerecha(viga)){
                    colisionDerecha = true; 
                } 
                if(hayColisionIzquierda(viga)){
                    colisionIzquierda = true; 
                }
            } 
            System.out.println("Monstruo en x: " + this.x + " Y:" + this.y); 
            System.out.println("Colision derecha: " + colisionDerecha + " Colision izquierda: " + colisionIzquierda);
            if(velocidad > 0 && !colisionDerecha){
                this.x += velocidad; 
            } else if(velocidad < 0 && !colisionIzquierda){
                this.x += velocidad; 
            } else {
                System.out.println("Cambiando dirección del monstruo"); 
                cambiarMov(); 
            }
        } else {
            System.out.println("El monstruo está cayendo"); 
            this.y += caida; 
        }
    }
    
    public void cambiarMov(){
        this.velocidad *= -1; 
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

    public int getIndice(){
        return indice; 
    } 

    public int getSpawn(){
        return spawn; 
    } 

    public Hechizo getHechizo(){
        return hechizo; 
    }

    public void chauHechizo(){
        hechizo = null; 
    }
}
