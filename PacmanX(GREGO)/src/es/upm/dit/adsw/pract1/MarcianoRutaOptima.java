package es.upm.dit.adsw.pract1;

/**
 * Marcianito que analiza la forma mas corta de llegar al jugador y empieza a recorrerla.
 *
 * @author Jose A. Manas
 * @version 24/3/2012
 */
public class MarcianoRutaOptima
        extends Marciano {

    /**
     * Constructor.
     *
     * @param laberinto el laberinto en el que se mueve.
     * @param celda     posicion inicial.
     * @param dt        delta de tiempo para irse moviendo.
     */
    public MarcianoRutaOptima(Laberinto laberinto, Celda celda, int dt) {
        super(laberinto, celda, dt);
        setImagen("Anibal.png");
    }

    /**
     * Determina en que direccion vamos a movernos.
     * Calcula la ruta optima entre el marciano y la posicion actual del jugador y da un primer paso en esa ruta.
     * Hay que recalcular continuamente porque el jugador puede moverse de sitio.
     *
     * @return direccion del proximo movimiento.
     */
    protected Direccion seleccionDireccion() {
     // extraer la matriz de distancias del laberinto
        int [][] matriz = MapaDistancias.getDistancias(getLaberinto(), getLaberinto().getJugador().getCelda());
     // buscar la dirección con conexión y distancia menor
        Direccion optima = null;
        int minima = Integer.MAX_VALUE;
        for (Direccion dir : Direccion.values()) {
        	Celda ayuda = getLaberinto().getConexion(getCelda(), dir);
        	if (ayuda != null) {
        		PuntoXY pxy = ayuda.getPunto();
            	int x = pxy.getX();
            	int y = pxy.getY();
            	
    			if (matriz[x][y]< minima) {
    				minima = matriz[x][y];
    				optima = dir;
    			}
				
			}
        	
		}
        
        return optima;
    }
}
