package juego;


import java.awt.Color;
import java.awt.image.*; 

import entorno.Entorno;
import entorno.Herramientas; 
import entorno.InterfaceJuego;
import java.util.ArrayList; 

public class Juego extends InterfaceJuego {
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno; 

	private Mago mago; 
	private Viga[] vigas; 
	private Monstruo[] monstruos; 
	private Hechizo hechizo; 
	private Escenario escenario; 
	public int kills = 0; 
	private Spawn[] spawns; 
	public int tiempos; 
	public int tiempo = 0; 
	public int tiempoMenu; 
	public int estadoDelJuego = 0; 
	public int puntos; 
	public int causaGameOver = 0; 
	public int getTiempo(){
		return(int) tiempos; 
	}  
	
	// Variables y métodos propios de cada grupo
	// ...
	public Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "TP Libre - Herrera Fernández", 800, 600); 
		//Uso en ancho y el alto para crear el escenario  
		escenario = new Escenario(entorno.ancho(), entorno.alto()); 
		//vigas = crearVigas(entorno); 
		ArrayList<Viga> vigas = escenario.getVigas();    
		this.mago = new Mago(entorno.ancho() / 2, entorno.alto() / 2 + 150, 15, 30, 4); 
		this.entorno.iniciar(); 
		spawns = generarSpawns(entorno); 
		monstruos = monstruosIniciales(2, 25, 20, 5, 1); 
		
		// Inicializar lo que haga falta para el juego
		// ...

		// Inicia el juego!
		this.entorno.iniciar();
	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y 
	 * por lo tanto es el método más importante de esta clase. Aquí se debe 
	 * actualizar el estado interno del juego para simular el paso del tiempo 
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick()
	{
		// Procesamiento de un instante de tiempo
		// ...

		escenario.dibujar(entorno); 
		if(estadoDelJuego == 0){
			dibujarMenu(); 
			if(entorno.sePresiono(entorno.TECLA_ENTER)){
				estadoDelJuego = 4; 
			} 
			if(entorno.sePresiono(entorno.TECLA_ESPACIO)){
				tiempoMenu = entorno.tiempo(); 
				estadoDelJuego = 1; // Cambia el estado para inciar el juego 
				iniciarJuego(); // Inicializa todos los objetos del juego 
			}
		} else if(estadoDelJuego == 1){
			correrJuego(); 
		} else if(estadoDelJuego == 2){
			dibujarGameOver(); 
			if(entorno.sePresiono(entorno.TECLA_ENTER)){
				estadoDelJuego = 0; 
				resetearJuego(); 
			} 
		} else if(estadoDelJuego == 3){
			dibujarGanaste(); 
			if(entorno.sePresiono(entorno.TECLA_ENTER)){
				estadoDelJuego = 0; 
				resetearJuego(); 
			}
		//} else if(estadoDelJuego == 4){
			//dibujarInstrucciones(); 
			if(entorno.sePresiono(entorno.TECLA_ESPACIO)){
				tiempoMenu = entorno.tiempo(); 
				estadoDelJuego = 1; 
				iniciarJuego(); 
			}
		}
	} 

	private void correrJuego(){ 
		ArrayList<Viga> vigas = escenario.getVigas(); 
		for(Spawn spawn : spawns){
			if(spawn != null){
				spawn.dibujar(entorno); 
			}
		} 
		tiempos = entorno.tiempo() - tiempoMenu; 
		tiempo = (int) (tiempos) / 1000; 
		puntos = 0;  
		mago.dibujar(entorno); 
		String direccion = ""; 
		if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA)){
			direccion = "izq"; 
			mago.moverIzquierda(vigas, entorno);  
		} else {
			direccion = "der"; 
			mago.moverDerecha(vigas, entorno);    
		}
		
		for(int i = 0; i < monstruos.length; i++){
			Monstruo monstruo = monstruos[i]; 
			if(monstruo == null){
				continue; 
			}
			monstruo.dibujar(entorno); 

			if(!monstruo.hayColisionAbajo(vigas)){
				monstruo.caida(vigas);  
			} 

			if(monstruo.hayColisionAbajo(vigas)){
				monstruo.mover(vigas); 
			} 

			if(monstruo.congeladoMonstruo(hechizo)){
				monstruo.empujarMonstruo(mago, vigas, direccion);  
			} 

			if(monstruo.getY() > entorno.alto()){
				puntos += 5; 
				kills++; 
				monstruos[i] = null;  
			}
		} 



		for(Viga viga : vigas){
			if(viga != null){
				viga.dibujar(entorno); 
			}
		} 

		textoEnPantalla(mago); 
		comportamientoMago(); 

		for(Monstruo monstruo : monstruos){
			if(monstruo == null){
				continue; 
			} 
			monstruo.dibujar(entorno); 
			if(!monstruo.hayColisionAbajo(vigas)){
				monstruo.caida(vigas);  
			} 
			if(monstruo.hayColisionAbajo(vigas)){
				monstruo.mover(vigas); 
			} 
			if(monstruo.congeladoMonstruo(hechizo)){
				monstruo.empujarMonstruo(mago, vigas, direccion); 
				kills ++; 
				textoPuntos((int) monstruo.getX(), (int) monstruo.getY(), 2);  
			}
		}

		if(puntos == -20){
			causaGameOver = 1; 
			estadoDelJuego = 2; 
		}
		if(mago.getVidas() == 0){
			causaGameOver = 2; 
			estadoDelJuego = 2; 
		} 
		if(tiempo == 90){
			causaGameOver = 3; 
			estadoDelJuego = 2; 
		} 
		if(puntos == 20){
			estadoDelJuego = 3; 
		} 
		if(kills > 0 && kills % 20 == 0){
			mago.aumentarVida(); 
		}
		crearMonstruos(2, 25, 20, 5, 1); 
	}

	private void iniciarJuego(){
		// Iniciar objetos solo cuando comienza el juego 

		ArrayList<Viga> vigas = escenario.getVigas(); 
		//vigas = crearVigas(entorno); 
		mago = new Mago(entorno.ancho() / 2, entorno.alto() / 2 + 150, 15, 30, 4); 
		spawns = generarSpawns(entorno); 
		monstruos = monstruosIniciales(2, 25, 20, 5, 1);  
	} 

	private void resetearJuego(){
		kills = 0; 
		tiempos = 0; 
		tiempo = 0; 
		mago.reiniciarVidas(); // Reinicia las vidas del personaje 
		causaGameOver = 0; 
		iniciarJuego(); 
	} 

	// Métodos de generación del mapa 

	/*public static Viga[] crearVigas(Entorno e){
		int pisos = 3; 
		Viga[] vigas = new Viga[pisos * (pisos + 1) / 2];  
		int y = 0; 
		int x = 0; 
		int indice = 0; 
		for(int i = 1; i <= pisos; i++){
			y += 100; 
			int expansion = i * (-50); 
			for(int j = 1; j <= i; j++){
				x = (e.ancho() - expansion) / (i + 1) * j + expansion / 2; 
				vigas[indice] = new Viga(x, y, 100, 30); 
				indice ++; 
			}
		}
		return vigas; 
	} */

	private Spawn[] generarSpawns(Entorno entorno){
		int cantSpawns = 10; 
		Spawn[] spawns = new Spawn[cantSpawns]; 
		double aux = 10.8; 

		for(int i = 0; i < cantSpawns / 2; i++){
			double x = entorno.ancho() / aux * (i + 1) - 58; 
			spawns[i] = new Spawn((int) x, 5);  
		} 

		for(int i = cantSpawns / 2; i < cantSpawns; i++){
			double x = entorno.ancho() / aux * (i + 1) + 38; 
			spawns[i] = new Spawn((int) x, 5); 
		}
		return spawns; 
	} 

	// Método de mago 

	public void comportamientoMago(){
		ArrayList<Viga> vigas = escenario.getVigas(); 
		if(!mago.hayColisionAbajo(vigas)){
			mago.caida(vigas);          
		}

		if(mago.hayColisionArriba(vigas)){
			mago.reiniciarCaida();
		} 

		if((entorno.estaPresionada(entorno.TECLA_DERECHA) || entorno.estaPresionada('d')) && !mago.hayColisionDerecha(vigas)){
			mago.moverDerecha(vigas, entorno);   
			mago.setMirando("der"); 
		}

		if((entorno.estaPresionada(entorno.TECLA_IZQUIERDA) || entorno.estaPresionada('a')) && !mago.hayColisionIzquierda(vigas)){
			mago.moverIzquierda(vigas, entorno); 
			mago.setMirando("izq"); 
		} 

		if((entorno.sePresiono(entorno.TECLA_ARRIBA) || entorno.sePresiono('w')) && !mago.hayColisionArriba(vigas) && mago.hayColisionAbajo(vigas) && !mago.estaSaltando){
			mago.salto(vigas);  
		} 

		if(mago.hayColisionAbajo(vigas)){
			this.mago.actualizarCaida();
		} 

		if(mago.murioMago(monstruos, entorno)){
			mago.muerteMago(entorno);
		} 

		if ((entorno.sePresiono(entorno.TECLA_ESPACIO) || entorno.sePresiono('c') || entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) && hechizo == null) {
			if (mago.getMirando().equals("der"))
				hechizo = new Hechizo((int) (mago.getX() + mago.getAncho() / 2 + 2), (int) mago.getY(), 5, 5, 5, mago.getMirando());
			else
				hechizo = new Hechizo((int) (mago.getX() - mago.getAncho() / 2 - 1), (int) mago.getY(), 5, 5, 5, mago.getMirando()); 
		} 

		if(hechizo != null){
			hechizo.dibujar(entorno); 
			hechizo.mover(); 
			if(hechizo.seFue(entorno)){
				hechizo = null; 
			} 
			for(Viga viga : vigas){
				if(viga.tocaHechizo(hechizo)){
					hechizo = null;  
				}
			}
		}	
	} 

	// Métodos de generación de monstruos 

	public Monstruo nuevoMonstruo(int x, int y, int ancho, int alto, int caida, double velocidad, int indice, int spawn){
		return new Monstruo(x, y, ancho, alto, caida, velocidad, indice, spawn); 
	} 

	private Monstruo[] monstruosIniciales(int y, int ancho, int alto, int caida, double velocidad) {
		int cantMonstruos = 6;
		Monstruo[] monstruos = new Monstruo[cantMonstruos]; 

		int monstruosIniciales = 0;
		while (monstruosIniciales < 3)
			monstruosIniciales = (int) (Math.random() * cantMonstruos);
		for (int i = 0; i <= monstruosIniciales; i++) {
			int random = (int) (Math.random() * 10);
			while (spawns[random].getHayMonstruo()) {
				random = (int) (Math.random() * 10);
			}
			monstruos[i] = nuevoMonstruo(spawns[random].getX(), 2, 25, 20, 5, 2, i, random);
			spawns[random].setHayMonstruo(true); 
		}
		return monstruos; 
	} 

	public void crearMonstruos(int y, int ancho, int alto, int caida, double velocidad) {
		int cantidadNulos = 0;
		int i = -1;

		for (Monstruo monstruo : monstruos) {
			i++;
			if (monstruo == null) {
				int random = (int) (Math.random() * 700);
				if (random < 1) {
					int indiceRandom = (int) (Math.random() * 10);
					while (spawns[indiceRandom].getHayMonstruo()) {
						indiceRandom = (int) (Math.random() * 10);
					}
					monstruos[i] = nuevoMonstruo(spawns[indiceRandom].getX(), 2, 25, 20, 5, 2, i, indiceRandom);
					spawns[indiceRandom].setHayMonstruo(true);
				}
				else
					cantidadNulos++;
			}
		}

		if (cantidadNulos > 2) {
			i = -1;
			for (Monstruo monstruo : monstruos) {
				int indiceRandom = (int) (Math.random() * 10);
				while (spawns[indiceRandom].getHayMonstruo()) {
					indiceRandom = (int) (Math.random() * 10);
				}
				i++;
				if (monstruo == null && cantidadNulos > 2) {
					cantidadNulos -= 1;
					monstruos[i] = nuevoMonstruo(spawns[indiceRandom].getX(), 2, 25, 20, 5, 2, i, indiceRandom);
					spawns[indiceRandom].setHayMonstruo(true); 
				}
			}
		}
	} 

	// Método de interfaz 

	private void dibujarMenu(){
		Color colorMarron = new Color(83, 94, 92);
		entorno.colorFondo(Color.cyan);
		entorno.cambiarFont("Curlz MT", 20, colorMarron, entorno.NEGRITA);
		entorno.escribirTexto("Presiona ENTER para instrucciones", entorno.ancho() / 2 - 160, entorno.alto() / 2 + 90);
		entorno.cambiarFont("Curlz MT", 30, Color.BLACK, entorno.NEGRITA);
		entorno.escribirTexto("Presiona ESPACIO para empezar", entorno.ancho() / 2 - 210, entorno.alto() / 2 + 130);
	} 

	private void dibujarGameOver(){
		entorno.cambiarFont("Arial", 30, Color.RED); 
		entorno.escribirTexto("Game Over", entorno.ancho() / 2 - 70, entorno.alto() / 2 - 50);
		if (causaGameOver == 2){
			entorno.escribirTexto("¡Perdiste todas tus vidas!", entorno.ancho() / 2 - 160, entorno.alto() / 2 - 20);
		}
		if (causaGameOver == 3){
			entorno.escribirTexto("¡Se acabó el tiempo!", entorno.ancho() / 2 - 140, entorno.alto() / 2 - 20);
		}
		entorno.cambiarFont("Arial", 20, Color.BLACK);
		entorno.escribirTexto("Presiona ENTER para reiniciar", entorno.ancho() / 2 - 140, entorno.alto() / 2 + 10);
	} 

	private void dibujarGanaste() {
		Color color = new Color(5, 173, 28);
		entorno.cambiarFont("Arial", 30, color);
		entorno.escribirTexto("¡GANASTE!", entorno.ancho() / 2 - 85, entorno.alto() / 2 - 50);
		entorno.cambiarFont("Arial", 20, Color.BLACK);
		entorno.escribirTexto("Presiona ENTER para reiniciar", entorno.ancho() / 2 - 140, entorno.alto() / 2 + 10);
	} 

	public void textoEnPantalla(Mago mago) {
		Color colorSalvados = new Color(5, 173, 28);
		Color colorVidas = new Color(222, 38, 81);
		entorno.cambiarFont("Arial", 20, Color.blue, entorno.NEGRITA);
		entorno.escribirTexto("TIEMPO: " + tiempo, entorno.ancho() / 20, 35);
		entorno.cambiarFont("Arial", 20, Color.MAGENTA, entorno.NEGRITA);
		entorno.escribirTexto("KILLS: " + kills, (entorno.ancho() / 20) * 5, 35);
		entorno.cambiarFont("Arial", 20, colorSalvados, entorno.NEGRITA);
		entorno.cambiarFont("Arial", 20, Color.red, entorno.NEGRITA);
		entorno.cambiarFont("Arial", 20, colorVidas, entorno.NEGRITA);
		entorno.escribirTexto("VIDAS: " + mago.getVidas(), entorno.ancho() / 20, 70);
		entorno.escribirTexto("PUNTOS: " + puntos, (entorno.ancho() / 20) * 16, 70);
		
	} 

	public void textoPuntos(int x, int y, int color){
		if(color == 0){
			entorno.cambiarFont("Arial", 20, Color.green, entorno.ITALICA); 
		} else if(color == 1){
			entorno.cambiarFont("Arial", 20, Color.red, entorno.ITALICA);
		} else {
			entorno.cambiarFont("Arial", 20, Color.cyan, entorno.ITALICA);
		}
		entorno.escribirTexto("+1", x, y); 
	} 

	public Spawn[] getSpawns(){
		return spawns; 
	} 

	public Monstruo[] getMonstruos(){
		return monstruos; 
	} 

	public void sumarKill(){
		kills++; 
	} 

	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
