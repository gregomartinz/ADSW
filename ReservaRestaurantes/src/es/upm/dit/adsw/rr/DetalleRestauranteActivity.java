package es.upm.dit.adsw.rr;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class DetalleRestauranteActivity extends Activity {

	private static final String TAG = "Restaurantes añadidos";
	private int posicion;
	private boolean seRepite = false;
	private boolean editar = false;
	private ArrayList<Restaurante> lista;	
	private ListaRestaurantesSingleton s;	
	private String nom;
	private String dir;
	private String tel;
	private String tipo;
	private EditText nombre;
	private EditText direccion;
	private EditText telefono;
	private RadioButton tradicional;
	private RadioButton internacional;
	private RadioButton comidaRapida;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.segundo);

		// Actividad por defecto
		this.nombre = (EditText) findViewById(R.id.editText1);
		this.direccion = (EditText) findViewById(R.id.editText2);
		this.telefono = (EditText) findViewById(R.id.editText3);
		this.tradicional = (RadioButton) findViewById(R.id.radio0);
		this.internacional = (RadioButton) findViewById(R.id.radio1);
		this.comidaRapida = (RadioButton) findViewById(R.id.radio2);
		this.s = ListaRestaurantesSingleton.getInstance();
		this.lista = s.devuelve();
		borrarCampos();

		//Actividad llamada desde ListaRestauranteActivity
		Bundle extras = getIntent().getExtras();
		posicion = extras.getInt("posicion");
		if (posicion <= lista.size()) {
			editar();
		}
	}
	
	/**
	 * Método que crea el menú de esta Activity
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
	       MenuInflater inflater = getMenuInflater();
	       inflater.inflate(R.menu.menu2, menu);
	       return true;
	   }
	 /**
	  * Método que establece que hacer en cada botón del menú
	  */
	public boolean onOptionsItemSelected(MenuItem item) {
	      switch (item.getItemId()) {
	      //Botón guardar
	      case R.id.item21:
	    	  guardar(getCurrentFocus());
	         return true;
	      //Botón cancelar
	      case R.id.item22:
	    	  cancelar(getCurrentFocus());
	         return true;
	      case R.id.item23:
	    	  String url = "tel:" + telefono.getText().toString().trim();
	  		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
	  		startActivity(intent);
	  		return true;
	      default:
	         return super.onOptionsItemSelected(item);
	      }
	   }

	/**
	 * Método que guarda un resturante en la Lista
	 * @param view
	 */
	public void guardar(View view) {
		try {
			// Cojo cada valor de la pantalla
			this.nom = nombre.getText().toString();
			this.dir = direccion.getText().toString();
			this.tel = telefono.getText().toString();
			this.tipo = obtenerTipo();

			// Compruebo que ningún campo está vacío
			if (nombre.getText().length() == 0 || direccion.getText().length() == 0 || telefono.getText().length() == 0 || tipo.isEmpty()) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Falta el nombre del restaurante", Toast.LENGTH_SHORT);
				toast.show();
				return;
			}

			// Creo el restaurante
			Restaurante rest = new Restaurante(nom, dir, tel, tipo);
			
			// Compruebo que el restaurante no existe
			for (Restaurante r : lista) {
				if (r.igual(rest)) {
					seRepite = true;
					break;
				}
				if (r.cambia1(rest)) {
					editar = true;
				}
			}
			// Si se repite, no hago nada
			if (seRepite) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"El restaurante ya está en la lista",
						Toast.LENGTH_SHORT);
				toast.show();
				borrarCampos();
				Intent intent = new Intent(getApplicationContext(),
						ListaRestaurantesActivity.class);
				startActivity(intent);
			}
			// Si se quiere editar, cambio sólo los valores
			if (editar) {
				Restaurante rEdit = lista.get(posicion);
				rEdit.setNombre(nombre.getText().toString());
				rEdit.setDireccion(direccion.getText().toString());
				rEdit.setTelefono(telefono.getText().toString());
				rEdit.setTipo(obtenerTipo());
				borrarCampos();
				Intent intent = new Intent(getApplicationContext(),
						ListaRestaurantesActivity.class);
				startActivity(intent);
			}
			// Si no se repite y no lo hay que editarlo, lo crea
			if (!seRepite && !editar) {
				lista.add(rest);
				Log.i(TAG, lista.toString());
				borrarCampos();
				Intent intent = new Intent(getApplicationContext(),
						ListaRestaurantesActivity.class);
				startActivity(intent);
			}
		} catch (Exception e) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Ha habido un error interno",
					Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	/**
	 * Metodo que hace volver a la Activity principal
	 * @param view
	 */
	public void cancelar(View view) {
		borrarCampos();
		//Intent que vuelve a ListaRestauranteActivity
		Intent intent = new Intent(getApplicationContext(),
				ListaRestaurantesActivity.class);
		startActivity(intent);
	}

	public void llamar(View view){
		String url = "tel:" + telefono.getText().toString().trim();
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
		startActivity(intent);
	}
	
	/**
	 * Método que obtiene el tipo del restaurante
	 * @return Un String con el tipo de resturante
	 */
	private String obtenerTipo() {
		if (tradicional.isChecked()) {
			return " tradicional";
		}
		if (internacional.isChecked()) {
			return " internacional";
		}
		if (comidaRapida.isChecked()) {
			return " comida rapida";
		}
		return "";
	}

	/**
	 * Método que establece el tipo del restaurante
	 * @param a Tipo que se quiere establecer
	 */
	private void ponerTipo(String a) {
		if (a.equals(" tradicional")) {
			tradicional.setChecked(true);
		}
		if (a.equals(" internacional")) {
			internacional.setChecked(true);
		}
		if (a.equals(" comida rapida")) {
			comidaRapida.setChecked(true);
		}
	}

	/**
	 * Pone todos los campos limpios
	 */
	public void borrarCampos() {
		nombre.setText("");
		nom = "";
		direccion.setText("");
		dir = "";
		telefono.setText("");
		tel = "";
	}

	/**
	 * Metodo que muestra los datos de un restaurante para ser editado
	 */
	private void editar() {
		Bundle extras = getIntent().getExtras();
		posicion = extras.getInt("posicion");
		nombre.setText(lista.get(posicion).getNombre());
		direccion.setText(lista.get(posicion).getDireccion());
		telefono.setText(lista.get(posicion).getTelefono());
		ponerTipo(lista.get(posicion).getTipo());
	}
}