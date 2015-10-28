package es.upm.dit.adsw.rr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

/**
 * 
 * @author Grego
 * @version 25.05.2012
 */
public class ListaRestaurantesActivity extends ListActivity {

	private ArrayList<Restaurante> lista;
	private ListaRestaurantesSingleton s;
	// Utilizo una barra de progreso dado que me ha resultado más sencillo la
	// implementación de la misma en el código
	private ProgressBar barraDeProgreso;
	private ListView listview;
	private ArrayAdapter<Restaurante> adapt;
	private boolean listaOrdenandose = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.primero);

		this.listview = (ListView) findViewById(android.R.id.list);
		this.s = ListaRestaurantesSingleton.getInstance();
		this.lista = s.devuelve();
		this.barraDeProgreso = (ProgressBar) findViewById(R.id.progressBar1);
		this.adapt = new ArrayAdapter<Restaurante>(getApplicationContext(),
				android.R.layout.simple_list_item_1, android.R.id.text1, lista);
		listview.setAdapter(adapt);
		// Método que indica que hacer en caso de pulsar sobre un restaurante en
		// la lista
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				// Semaforo que impide que se interrumpa la carga o la
				// ordenación
				if (!s.getSiEstaCargando() && !listaOrdenandose) {
					Intent intent = new Intent(getApplicationContext(),
							DetalleRestauranteActivity.class);
					intent.putExtra("posicion", position);
					startActivity(intent);
				}
			}
		});
	}

	/**
	 * Método que crea el menú
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	/**
	 * Método que establece la función de cada botón del menú
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		//Añadir restaurante
		case R.id.item1:
			if (!s.getSiEstaCargando()) {
				lanzar(getCurrentFocus());
			}
			return true;
		//Cargar los restaurantes de txt
		case R.id.item2:
			if (!s.getSiEstaCargando()) {
				cargar(getCurrentFocus());
			}			
			return true;
		//Ordenar la lista
		case R.id.item3:
			if (!s.getSiEstaCargando()) {
				listaOrdenandose = true;
				ordenar(lista);
				listaOrdenandose = false;
				adapt.notifyDataSetChanged();
			}
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Método que abre DetalleRestauranteActivity
	 * 
	 * @param view
	 */
	public void lanzar(View view) {
		Intent i = new Intent(getApplicationContext(),
				DetalleRestauranteActivity.class);
		// Información extra que pasamos por si se quiere editar
		i.putExtra("posicion", lista.size() + 1);
		startActivity(i);
	}

	/**
	 * Método que inicia la carga de restaurantes desde un fichero .txt
	 * 
	 * @param view
	 */
	public void cargar(View view) {

		// Comprueba que no se haya cargado ya
		if (!s.getSiListaCargada()) {
			// Pone el semaforo a rojo para que no se pueda editar la lista
			s.setComienzaCarga(true);
			new LeeFichero().execute();
		}
	}

	/**
	 * Método que ordena la lista de restaurantes
	 * 
	 * @param r
	 *            Lista a ordenar
	 */
	public static void ordenar(ArrayList<Restaurante> r) {
		quickSort(r, 0, r.size() - 1);

	}

	private static int quickSortSecundario(ArrayList<Restaurante> r, int izq,
			int der) {
		int posPivote = izq + (der - izq) / 2;
		String pivote = r.get(posPivote).getNombre();
		while (izq <= der) {
			while (r.get(izq).getNombre().compareTo(pivote) < 0) {
				izq++;

			}
			while (r.get(der).getNombre().compareTo(pivote) > 0) {
				der--;
			}
			if (izq <= der) {
				cambiarPos(r, izq, der);
				izq++;
				der--;
			}
		}
		return izq;
	}

	private static void cambiarPos(ArrayList<Restaurante> r, int i, int j) {
		Restaurante re = r.get(i);
		r.set(i, r.get(j));
		r.set(j, re);
	}

	private static void quickSort(ArrayList<Restaurante> r, int izq, int der) {
		int limite;

		if (izq < der) {
			limite = quickSortSecundario(r, izq, der);
			quickSort(r, izq, limite - 1);
			quickSort(r, limite, der);
		}
	}

	/**
	 * Clase que lee el fichero y los guarda en la lista de restaurantes
	 * 
	 * @author Grego
	 * 
	 */
	private class LeeFichero extends AsyncTask<Void, Integer, Void> {

		int progreso;

		/**
		 * Método que se ejecuta al inicializar la clase Establece que el
		 * progreso es 0;
		 */
		protected void onPreExecute() {
			progreso = 0;
		}

		/**
		 * Método que lee el fichero y crea cada restaurante a partir de los
		 * datos recogidos del mismo
		 */
		protected Void doInBackground(Void... v) {

			AssetManager am = getResources().getAssets();
			InputStream entrada = null;
			try {
				// Saca el texto del fichero y lo divide en lineas
				entrada = am.open("restaurantes.txt");
				InputStreamReader ir = new InputStreamReader(entrada);
				BufferedReader bf = new BufferedReader(ir);
				// Lee la primera línea y establece el número de resturantes que
				// hay en total en el fichero
				String linea = bf.readLine();
				int nRest = Integer.parseInt(linea);
				int sumaTotal = 100 / nRest;
				// Comprueba que la línea no sea nula
				while (linea != null) {					
					linea = bf.readLine();
					if (linea == null) {
						// Termina de actualizar la barra de progreso
						progreso = 100;
						publishProgress(progreso);
						break;
					}					
					Thread.sleep(2000);					
					// Guarda todos los datos del restaurante en un array y
					// llama a un método auxiliar
					String[] array = new String[4];
					array = linea.split(";");
					guardar(array);
					// Va actualizando la barra de progreso
					progreso += sumaTotal;
					publishProgress(progreso);
				}
				
				// Establece que termina la carga
				s.setComienzaCarga(false);
				// Establece que no volverá a cargar
				s.setListaCargada();
				entrada.close();
			} catch (IOException e) {
				Log.i("ListaRestaurantes", "Imposible abrir el fichero");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * Método que se encarga de guardar el restaurante en la lista
		 * 
		 * @param s
		 *            Array pasado con todos los datos para crear el nuevo
		 *            resturante
		 */
		private void guardar(String[] s) {

			boolean seRepite = false;
			// Comprobamos que nincún parametro sea null
			for (int i = 0; i < s.length; i++) {
				if (s[i] == null) {
					return;
				}
			}
			// Asignamos cada parametro a su correspondiente campo
			String nom = s[0];
			String dir = s[1];
			String tel = s[2];
			String tipo = s[3];
			// Creamos el restaurante
			Restaurante rest = new Restaurante(nom, dir, tel, tipo);
			// Comprobamos que no esté repetido en la lista, si lo está no se
			// añade
			for (Restaurante r : lista) {
				if (r.igual(rest)) {
					seRepite = true;
					break;
				}
			}// Si no se repite se añade a la lista
			if (!seRepite) {
				lista.add(rest);
			}
		}

		/**
		 * Método que va actualizando la barra de progreso
		 */
		protected void onProgressUpdate(Integer... values) {
			barraDeProgreso.setProgress(values[0]);
			adapt.notifyDataSetChanged();
		}
	}
}
