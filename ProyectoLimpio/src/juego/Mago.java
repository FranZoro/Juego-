package juego; 

import java.awt.Color;
import java.awt.color.*;
import java.util.ArrayList;

import entorno.Entorno; 

public class Mago {
    private double x; 
    private double y; 
    private double ancho; 
    private double alto; 
    private double desplazamiento; 
    private int vidasPerso; 
    private int tiempoInmune = 0; 
    boolean estaSaltando; 
    private double caida; 
    private String mirando = "der"; 
    int vidas = 3; 
    private final double gravedad = 0.25; 
    private final double caidaMax = 5; 
    
    public Mago(double x, double y, double ancho, double alto, double desplazamiento){
        this.x = x; 
        this.y = y; 
        this.ancho = ancho; 
        this.alto = alto; 
        this.estaSaltando = false; 
        this.desplazamiento = desplazamiento; 
        this.caida = 0; 
        this.vidasPerso = 3;  
    } 

    public void dibujar(Entorno e){
        e.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.blue); 
    } 

    public void moverDerecha(ArrayList<Viga> vigas, Entorno e){//Entorno e, Viga[] vigas){
        if(this.x + this.ancho / 2 < e.ancho() && !hayColisionDerecha(vigas)){  
            this.x += this.desplazamiento;  
        }
    } 

    public void moverIzquierda(ArrayList<Viga> vigas, Entorno e){//Entorno e, Viga[] vigas){
        if(this.x - this.ancho / 2 > 0 && !hayColisionIzquierda(vigas)){
            this.x -= this.desplazamiento; 
        }
    } 

    public void moverAbajo(Entorno e) {
		if (this.y + this.alto / 2 < e.alto())
			this.y += this.desplazamiento;
	}

	public void setDesplazamiento(double nuevoDesplazamiento) {
		this.desplazamiento = nuevoDesplazamiento;
	}

    public boolean hayColisionAbajo(ArrayList<Viga> vigas){//Viga[] plataformas){
        for(Viga viga : vigas){//plataforma: plataformas){ 
            if(viga == null){//plataforma == null){
                continue; 
            } 
            double bordeInfMago = this.y + this.alto / 2; 
            double bordeSupViga = viga.getY() - (viga.getAlto() / 2); //plataforma.getY() - (plataforma.getAlto() / 2); 
            boolean abajo = bordeInfMago >= bordeSupViga && bordeInfMago <= bordeSupViga + desplazamiento; 
            boolean der = this.x + this.ancho / 2 > viga.getX() - viga.getAncho() / 2;//plataforma.getX() - plataforma.getAncho() / 2; 
            boolean izq = viga.getX() + viga.getAncho() / 2 > this.x - this.ancho / 2;//plataforma.getX() + plataforma.getAncho() / 2 > this.x - this.ancho / 2; 
            if(abajo){
                if(der && izq){
                    this.y = (int) bordeSupViga - (this.alto / 2); 
                    return true; 
                }
            } 
        } 
        return false; 
    }  

    public boolean hayColisionArriba(ArrayList<Viga> vigas){//Viga[] plataformas){
        for(Viga viga : vigas){//plataforma: plataformas){
            if(viga == null){//plataforma == null){
                continue; 
            } 
            double bordeSupMago = this.y - this.alto / 2; 
            double bordeInfViga = viga.getY() + (viga.getAlto() / 2); //plataforma.getY() + (plataforma.getAlto() / 2); 
            boolean arriba = bordeSupMago <= bordeInfViga && bordeSupMago >= bordeInfViga + caida; 
            boolean der = this.x + this.ancho / 2 > viga.getX() - viga.getAncho() / 2; //plataforma.getX() - plataforma.getAncho() / 2; 
            boolean izq = viga.getX() + viga.getAncho() / 2 > this.x - this.ancho / 2;//plataforma.getX() + plataforma.getAncho() / 2 > this.x - this.ancho / 2; 
            if(arriba){
                if(der && izq){
                    this.y = (int) bordeInfViga + (this.alto / 2); 
                    return true; 
                }
            }
        }
        return false; 
    }

    public boolean hayColisionDerecha(ArrayList<Viga> vigas){//Viga[] plataformas){
        for(Viga viga : vigas){//plataforma: plataformas){
            if(viga == null){//plataforma == null){
                continue; 
            } 
            double bordeDerMago = this.x + this.ancho / 2; 
            double bordeIzqViga = viga.getX() - (viga.getAncho() / 2); //plataforma.getX() - (plataforma.getAncho() / 2); 
            boolean der = bordeDerMago >= bordeIzqViga && bordeDerMago <= bordeIzqViga + desplazamiento; 
            boolean arriba = this.y + this.alto / 2 > viga.getY() - (viga.getAlto() / 2); //plataforma.getY() - (plataforma.getAlto() / 2); 
            boolean abajo = this.y - this.alto / 2 < viga.getY() + (viga.getAlto() / 2); //plataforma.getY() + (plataforma.getAlto() / 2); 
            if(der){
                if(arriba && abajo){
                    this.x = (int) bordeIzqViga - (this.ancho / 2); 
                    return true; 
                }
            }
        } 
        return false; 
    } 

    public boolean hayColisionIzquierda(ArrayList<Viga> vigas){//Viga[] plataformas){
        for(Viga  viga : vigas){//plataforma: plataformas){
            if(viga == null){//plataforma == null){
                continue; 
            }
            double bordeIzqMago = this.x - this.ancho / 2; 
            double bordeDerViga = viga.getX() + (viga.getAncho() / 2); //plataforma.getX() + (plataforma.getAncho() / 2); 
            boolean izq = bordeIzqMago <= bordeDerViga && bordeIzqMago >= bordeDerViga - desplazamiento; 
            boolean arriba = this.y + this.alto / 2 > viga.getY() - (viga.getAlto() / 2); //plataforma.getY() - (plataforma.getAlto() / 2); 
            boolean abajo = this.y - this.alto / 2 < viga.getY() + (viga.getAlto() / 2);//plataforma.getY() + (plataforma.getAlto() / 2); 
            if(izq){
                if(arriba && abajo){
                    this.x = (int) bordeDerViga + (this.ancho / 2); 
                    return true; 
                }
            }
        }
        return false; 
    } 

    public boolean tocaMonstruo(Monstruo[] monstruos){ 
        boolean aba; 
        boolean arr; 
        boolean der; 
        boolean izq; 
        for(Monstruo monstruo: monstruos){
            if(monstruo == null){
                continue; 
            } 
            aba = monstruo.getY() <= this.getY() && monstruo.getY() - this.getY() <= monstruo.getAlto() / 2 + this.getAlto() / 2; 
            arr = monstruo.getY() <= this.getY() && this.getY() - monstruo.getY() <= monstruo.getAlto() / 2 + this.getAlto() / 2; 
            der = monstruo.getX() >= this.getX() && monstruo.getX() - this.getX() <= monstruo.getAncho() / 2 + this.getAncho() / 2;
            izq = monstruo.getX() <= this.getX() && this.getX() - monstruo.getX() <= monstruo.getAncho() / 2 + this.getAncho() / 2; 
            if((der && (arr || aba)) || (izq && (arr || aba))){
                return true; 
            } 
        } 
        return false; 
    } 

    public boolean seCayo(Entorno e){ 
        return this.y - this.alto / 2 > e.alto(); //Realmente el mago salió de la pantalla 
        //double bordeSupMago = this.y - this.alto / 2; 
        //if(bordeSupMago > e.alto()){
            //return true; 
        //}
        //return false; 
    } 

    public boolean murioMago(Monstruo[] monstruos, Entorno entorno){
        if(tiempoInmune > 0){
            return false; 
        }
        return(tocaMonstruo(monstruos) || seCayo(entorno)); 
    } 

    public void muerteMago(Entorno entorno){
        x = entorno.ancho() / 2; 
        y = entorno.alto() / 2 - 100; 
        perderVida();  
        tiempoInmune = 100; // Ciclos de inmunidad 
    } 

    public void actualizar(){
        if(tiempoInmune > 0) tiempoInmune--; 
    } 

    public void caida(ArrayList<Viga> vigas){//Viga[] vigas){ 
        if(!hayColisionAbajo(vigas)){
            if(caida < caidaMax){
                caida += gravedad; 
            }
            this.y += this.caida; 
        } else {
            caida = 0; // Reinicia las vidas al tocar una viga 
        }
    }  

    public void salto(ArrayList<Viga> vigas){//Viga[] vigas){
        if(!estaSaltando){
            caida = -8; 
            estaSaltando = true; 
        }
        if(hayColisionAbajo(vigas)){ 
            estaSaltando = false; 
        }
    } 

    public void reiniciarCaida(){
        caida = 1; 
    } 

    public void actualizarCaida(){
        caida = 0; 
    } 

    public void setY(double i){
        this.y = i; 
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

    public void aumentarVida(){
        vidas ++; 
    } 

    public void perderVida(){
        vidas--; 
    } 

    public void setMirando(String direccion) {
		mirando = direccion;
	}

	public String getMirando() {
		return mirando;
	}
	
	public int getVidas() {
		return vidas;
	}
	public void reiniciarVidas() {
		vidas = 3;
	}
}
