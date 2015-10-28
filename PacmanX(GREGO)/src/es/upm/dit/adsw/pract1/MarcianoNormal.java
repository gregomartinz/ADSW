package es.upm.dit.adsw.pract1;

/**
 * Marcianito que elige una direccion al azar en cada movimiento.
 *
 * @author Jose A. Manas
 * @version 24/3/2012
 */
public class MarcianoNormal
        extends Marciano {
    /**
     * Constructor.
     *
     * @param laberinto el laberinto en el que se mueve.
     * @param celda     posicion inicial.
     * @param dt        delta de tiempo para irse moviendo.
     */
    public MarcianoNormal(Laberinto laberinto, Celda celda, int dt) {
        super(laberinto, celda, dt);
        setImagen("FantasmaRosa.png");
    }

    /**
     * Determina en que direccion vamos a movernos.
     * Elige una direccion al azar.
     *
     * @return direccion del proximo movimiento.
     */
    protected Direccion seleccionDireccion() {
        return Direccion.random();
    }
}
