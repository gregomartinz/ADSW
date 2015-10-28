package es.upm.dit.adsw.pract1;

/**
 * Tipo de celda en el laberinto.<br>
 * DESTINO - la meta del jugador. <br>
 * CEPO - donde si cae un marciano queda atrapado. <br>
 * LLAVE - donde si pilla el jugador, libera los cepos. <br>
 * DESTINO - las celdas que no tienen nada especial. <br>
 *
 * @author Jose A. Manas
 * @version 24/3/2012
 */
public enum Tipo {
    NORMAL, DESTINO, CEPO, LLAVE
}
