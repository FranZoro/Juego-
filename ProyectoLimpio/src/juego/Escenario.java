package juego;

import entorno.Entorno; 
import java.util.ArrayList; 

public class Escenario {
    private ArrayList<Viga> vigas; 
    private double anchoPantalla; 
    private double altoPantalla; 

    public Escenario(double anchoPantalla, double altoPantalla){
        this.anchoPantalla = anchoPantalla; 
        this.altoPantalla = altoPantalla; 
        this.vigas = new ArrayList<>(); 
        inicializarVigas(); 
    } 

    private void inicializarVigas(){
        double alturaBase = altoPantalla * 0.2; 
        double anchoViga = anchoPantalla * 0.4; 
        double altoViga = 15; 
        
        // Nivel 1 (superior) - Una viga centrada 
        vigas.add(new Viga(anchoPantalla / 2, alturaBase, anchoViga, altoViga)); 

        // Nivel 2 - Dos vigas en los extremos 
        vigas.add(new Viga(anchoViga / 2, alturaBase * 2, anchoViga, altoViga)); //Izquierda 
        vigas.add(new Viga(anchoPantalla - (anchoViga / 2), alturaBase * 2, anchoViga, altoViga)); //Derecha 
        
        // Nivel 3 - Dos vigas centradas 
        vigas.add(new Viga(anchoPantalla * 0.3, alturaBase * 3, anchoViga, altoViga)); 
        vigas.add(new Viga(anchoPantalla * 0.7, alturaBase * 3, anchoViga, altoViga)); 

        // Nivel 4 (Inferior) - Dos vigas dejando un hueco en el centro 
        vigas.add(new Viga(altoViga / 2, alturaBase * 4, anchoViga, altoViga)); //Izquierda 
        vigas.add(new Viga(anchoPantalla - (anchoViga / 2), alturaBase * 4, anchoViga, altoViga)); //Derecha
    } 

    public ArrayList<Viga> getVigas(){
        return this.vigas; 
    }

    public void dibujar(Entorno e){
        for(Viga viga : vigas){
            viga.dibujar(e); 
        }
    }
} 
