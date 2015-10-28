package es.upm.dit.adsw.pract1;

import java.awt.*;

/**
 * Un bicho.
 *
 * @author Jose A. Manas
 * @version 21/3/2012
 */
public abstract class Marciano
        extends Thread {
    private final Laberinto laberinto;
    private Celda celda;
    private final int dt;
    private volatile Modo modo = Modo.ACTIVO;
    private Image imagen;

    /**
     * Constructor.
     *
     * @param laberinto el laberinto en el que se mueve.
     * @param celda     posicion inicial.
     * @param dt        delta de tiempo para irse moviendo.
     */
    public Marciano(Laberinto laberinto, Celda celda, int dt) {
        this.laberinto = laberinto;
        setCelda(celda);
        this.dt = dt;
        modo = Modo.ACTIVO;
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
     * Getter.
     *
     * @return intervalo de tiempo para ir moviendo.
     */
    public int getDt() {
        return dt;
    }

    /**
     * Getter.
     *
     * @return laberinto en donde estamos.
     */
    public Laberinto getLaberinto() {
        return laberinto;
    }

    /**
     * Setter.
     *
     * @param modo modo: ACTIVO, PARADO o FIN.
     */
    public void setModo(Modo modo) {
        this.modo = modo;
    }

    /**
     * Getter.
     *
     * @return modo: ACTIVO, PARADO o FIN.
     */
    public Modo getModo() {
        return modo;
    }

    /**
     * Se intenta mover en la direccion indicada.
     *
     * @param direccion en la que desea moverse.
     * @throws InterruptedException si se ve interrumpido.
     * @throws JugadorComido        cuando choca con el jugador.
     */
    public void intentaMover(Direccion direccion)
            throws InterruptedException, JugadorComido {
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
        monitor.mueveMarciano(this, celda2);
    }

    /**
     * Setter.
     * Ademas vacia la celda actual y marca un bicho en la nueva.
     *
     * @param nueva a donde va.
     */
    public void setCelda(Celda nueva) {
        Celda anterior = this.celda;
        if (anterior != null)
            anterior.setEstado(Estado.VACIA);
        nueva.setEstado(Estado.BICHO);
        this.celda = nueva;
        laberinto.pinta();
    }

    /**
     * Getter.
     *
     * @return imagen para pintar.
     */
    public Image getImagen() {
        return imagen;
    }

    /**
     * Carga una imagen de un fichero grafico.
     *
     * @param fichero nombre del fichero.
     */
    public void setImagen(String fichero) {
        imagen = GUI.setImagen(fichero);
    }

    /**
     * Actividad del marciano.
     */
    @Override
    public void run() {
        while (getModo() != Modo.FIN) {
            try {
                if (getModo() == Modo.ACTIVO) {
                    Direccion direccion = seleccionDireccion();
                    intentaMover(direccion);
                }
                Thread.sleep(getDt());
            } catch (JugadorComido jugadorComido) {
                //se acabo, hemos tropezado con el jugador
                getLaberinto().jugadorPierde();
            } catch (InterruptedException e) {
                // ante una interrupcion, acaba el run y el thread
                setModo(Modo.FIN);
            }
        }
    }

    /**
     * Determina en que direccion vamos a movernos.
     * Hay diferentes algoritmos.
     *
     * @return direccion del proximo movimiento.
     */
    protected abstract Direccion seleccionDireccion();

}
