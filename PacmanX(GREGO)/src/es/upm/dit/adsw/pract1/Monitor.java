package es.upm.dit.adsw.pract1;

/**
 * Monitoriza quien hay en cada celda del laberinto. Se envuelve en un
 * synchronized
 * 
 * @author Jose A. Manas
 * @version 19/3/2012
 */
public class Monitor {
	private final Laberinto laberinto;

	public Monitor(Laberinto laberinto) {
		this.laberinto = laberinto;
	}

	/**
	 * Intenta mover el jugador a la celda2. Si no puede moverse, no pasa nada.
	 * Si puede moverse, pone la celda con setCelda(). Si pisa la llave del
	 * laberinto, libera todos los cepos y a los marcianos que puedieran estar
	 * pillador en ellos.
	 * 
	 * @param jugador
	 *            referencia al jugador.
	 * @param celda2
	 *            celda a la que quiere moverse el jugador.
	 * @throws JugadorComido
	 *             si tropieza con un marciano.
	 */
	public synchronized void mueveJugador(Jugador jugador, Celda celda2)
			throws JugadorComido {

		if (celda2.getEstado() == Estado.BICHO) {
			throw new JugadorComido();
		}
		jugador.setCelda(celda2);
		if (celda2.getTipo() == Tipo.LLAVE) {
			laberinto.limpiaCepos();
			notifyAll();
		}

	}

	/**
	 * Intenta mover al marciano a la celda2. Si no puede moverse, no pasa nada.
	 * Si puede moverse, pone la celda con setCelda(). Si pisa un cepo, se queda
	 * bloqueado hasta que el jugador pide la llave.
	 * 
	 * @param marciano
	 *            referencia al marciano.
	 * @param celda2
	 *            celda a la que quiere moverse el marciano.
	 * @throws JugadorComido
	 *             si tropieza con el jugador.
	 * @throws InterruptedException
	 *             si se ve interrumpido.
	 */
	public synchronized void mueveMarciano(Marciano marciano, Celda celda2)
			throws JugadorComido, InterruptedException {

		if (celda2.getEstado() == Estado.BICHO) {
			return;
		}
		if (celda2.getEstado() == Estado.JUGADOR) {
			throw new JugadorComido();
		}
		marciano.setCelda(celda2);
		if (celda2.getTipo() == Tipo.CEPO) {
			wait();
		}
	}
}
