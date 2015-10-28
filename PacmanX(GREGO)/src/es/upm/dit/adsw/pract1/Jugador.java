package es.upm.dit.adsw.pract1;

import java.awt.*;

/**
 * La ficha azul: el jugador.
 *
 * @author Jose A. Manas
 * @version 21/3/2012
 */
public class Jugador {
    private final Laberinto laberinto;
    private Celda celda;
    private int[][] distancias;
    private Image imagen;

    /**
     * Constructor.
     *
     * @param laberinto el laberinto en el que se mueve.
     * @param salida    posicion inicial.
     */
    public Jugador(Laberinto laberinto, Celda salida) {
        this.laberinto = laberinto;
        setCelda(salida);
        salida.setEstado(Estado.JUGADOR);
        imagen = GUI.setImagen("Fran1.png");
    }

    /**
     * Getter.
     *
     * @return donde esta ahora.
     */
    public Celda getCelda() {
        return celda;
    }

    /**
     * Se intenta mover en la direccion indicada.
     *
     * @param direccion en la que desea moverse.
     */
    public void intentaMover(Direccion direccion) {
        try {
            // calculamos la celda a la que iriamos.
            Celda celda2 = laberinto.getConexion(celda, direccion);
            if (celda2 == null) {
                // no hay una celda alcanzable en esa dirección
                return;
            }

            // vamos a ver que hay en el tablero:
            // esto debe estar sincronizado
            // para que las fichas se esten quietas mientras miro
            Monitor monitor = laberinto.getMonitor();
            monitor.mueveJugador(this, celda2);
            // Si llegamos a la meta, se acaba el juego.
            if (celda.getTipo() == Tipo.DESTINO)
                laberinto.jugadorGana();
        } catch (JugadorComido jugadorComido) {
            //se acabo, hemos tropezado con un bicho
            laberinto.jugadorPierde();
        }
    }

    /**
     * Setter.
     * Ademas, vacia la celda actual y marca un jugador en la nueva.
     *
     * @param nueva a donde va.
     */
    public void setCelda(Celda nueva) {
        Celda anterior = this.celda;
        if (anterior != null)
            anterior.setEstado(Estado.VACIA);
        nueva.setEstado(Estado.JUGADOR);
        this.celda = nueva;
        laberinto.pinta();
        distancias = MapaDistancias.getDistancias(laberinto, celda);
    }

    /**
     * Devuelve el mapa de distancias: desde cada celda a donde esta el jugador.
     *
     * @return mapa de distancias.
     */
    public int[][] getDistancias() {
        return distancias;
    }

    /**
     * Getter.
     *
     * @return imagen para pintar.
     */
    public Image getImagen() {
        return imagen;
    }
}
