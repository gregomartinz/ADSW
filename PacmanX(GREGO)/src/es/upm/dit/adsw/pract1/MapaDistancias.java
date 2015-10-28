package es.upm.dit.adsw.pract1;

/**
 * Calcula el mapa de distancias. Para cada celda del laberinto, calcula a
 * cuantos saltos esta del jugador.
 * 
 * @author Jose A. Manas
 * @version 24/3/2012
 */
public class MapaDistancias {

	/**
	 * Calcula el mapa de distancias.
	 * 
	 * @param laberinto
	 *            el laberinto de trabajo.
	 * @param celda
	 *            donde esta el jugador.
	 * @return mapa de distancias.
	 */
	public static int[][] getDistancias(Laberinto laberinto, Celda celda) {
		int lado = laberinto.getLado();
		int[][] distancias = new int[lado][lado];

		for (int i = 0; i < distancias.length; i++) {
			for (int j = 0; j < distancias.length; j++) {
				distancias[i][j] = Integer.MAX_VALUE;
			}
		}
		set(laberinto, distancias, celda, 0);
		return distancias;
	}

	/**
	 * Propaga la informacion de distancia a una nueva celda.
	 * 
	 * @param laberinto
	 *            el laberinto de trabajo.
	 * @param distancias
	 *            mapa de distancias que estamos rellenando.
	 * @param celda
	 *            celda cuya distancia acabamos de conocer y vamos a propagar.
	 * @param d
	 *            distancia de la celda d al jugador.
	 */
	private static void set(Laberinto laberinto, int[][] distancias,
			Celda celda, int d) {

		PuntoXY ayuda = celda.getPunto();
		int x = ayuda.getX();
		int y = ayuda.getY();
		if (d < distancias[x][y]) {
			distancias[x][y] = d;
			for (Direccion dir : Direccion.values()) {
				if (laberinto.getConexion(celda, dir) != null) {
					set(laberinto, distancias,
							laberinto.getConexion(celda, dir), d + 1);
				}
			}

		}
	}

	private static void print(int lado, int[][] distancias) {
		for (int y = lado - 1; y >= 0; y--) {
			for (int x = 0; x < lado; x++) {
				System.out.printf("%3d", distancias[x][y]);
			}
			System.out.println();
		}
		System.out.println();
	}
}
