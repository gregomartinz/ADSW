package es.upm.dit.adsw.rr;

import java.util.ArrayList;

/**
 * 
 * @author Grego
 *
 */
public class ListaRestaurantesSingleton {

	private static ListaRestaurantesSingleton LISTA = new ListaRestaurantesSingleton();
	private ArrayList<Restaurante> lista;
	private boolean cargando = false;
	private boolean listaCargada = false;

	/**
	 * 
	 */
	private ListaRestaurantesSingleton() {

		lista = new ArrayList<Restaurante>();
	}

	/**
	 * M�todo que devuelve la instancia de la clase
	 * @return Instancia compartida por la clase
	 */
	public static ListaRestaurantesSingleton getInstance() {
		return LISTA;
	}

	/**
	 * M�todo que devuelve la lista de restaurantes compartida
	 * @return ArrayList de restaurantes compartida
	 */
	public ArrayList<Restaurante> devuelve() {
		return lista;
	}

	/**
	 * Establece el comienzo de la carga
	 * @param b Parametro para establecer el semaforo
	 */
	public void setComienzaCarga(boolean b) {
		if (b) {		
		cargando = true;
		}else{
		cargando = false;
		}
	}

	/**
	 * M�todo que devuelve si ha comenzado ya la carga
	 * @return true si ha empezado la carga y false si no lo ha hecho
	 */
	public boolean getSiEstaCargando() {
		return cargando;
	}
	public void setListaCargada(){
		listaCargada = true;
	}
	public boolean getSiListaCargada(){
		return listaCargada;
	}
}
