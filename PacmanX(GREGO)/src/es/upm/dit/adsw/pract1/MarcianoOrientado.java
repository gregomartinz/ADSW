package es.upm.dit.adsw.pract1;

/**
 * Marcianito que mira hacia donde esta el jugador e intenta ir hacia alli.
 *
 * @author Jose A. Manas
 * @version 24/3/2012
 */
public class MarcianoOrientado
        extends Marciano {
    /**
     * Constructor.
     *
     * @param laberinto el laberinto en el que se mueve.
     * @param celda     posicion inicial.
     * @param dt        delta de tiempo para irse moviendo.
     */
    public MarcianoOrientado(Laberinto laberinto, Celda celda, int dt) {
        super(laberinto, celda, dt);
        setImagen("FantasmaRojo.png");
    }

    /**
     * Determina en que direccion vamos a movernos.
     * Mira en que direccion esta el jugador e intenta moverse hacia el.
     * Puede que el marciano se quede encajonado en una esquina del laberinto.
     *
     * @return direccion del proximo movimiento.
     */
    protected Direccion seleccionDireccion() {
        Jugador jugador = getLaberinto().getJugador();
        int dx = jugador.getCelda().getPunto().getX() - getCelda().getPunto().getX();
        int dy = jugador.getCelda().getPunto().getY() - getCelda().getPunto().getY();

        double angulo = Math.atan2(dy, dx);

        String direcciones = getDirecciones(angulo);
        for (char c : direcciones.toCharArray()) {
            Direccion direccion;
            if (c == 'N')
                direccion = Direccion.NORTE;
            else if (c == 'S')
                direccion = Direccion.SUR;
            else if (c == 'E')
                direccion = Direccion.ESTE;
            else
                direccion = Direccion.OESTE;
            Celda celda2 = getLaberinto().getConexion(getCelda(), direccion);
            if (celda2 != null)
                return direccion;
        }
        return null;
    }

    /**
     * Genera una string con una serie de direcciones para ir probando en orden.
     * Por ejemplo, "ENSW" para ir probando este, si no norte, si no sur, si no oeste.
     *
     * @param angulo angulo del vector desde donde estoy al jugador.
     * @return direcciones.
     */
    private String getDirecciones(double angulo) {
        // pasamos de (-pi, +pi) a (0, 2pi)
        if (angulo < 0)
            angulo += 2 * Math.PI;
        if (angulo < Math.PI / 4)
            return "ENSW";
        if (angulo < Math.PI / 2)
            return "NEWS";
        if (angulo < 3 * Math.PI / 4)
            return "NWES";
        if (angulo < Math.PI)
            return "WNSE";
        if (angulo < 5 * Math.PI / 4)
            return "WSNE";
        if (angulo < 3 * Math.PI / 2)
            return "SWEN";
        if (angulo < 7 * Math.PI / 4)
            return "SEWN";
        return "ESNW";
    }
}
