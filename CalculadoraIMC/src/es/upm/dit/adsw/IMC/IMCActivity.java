package es.upm.dit.adsw.IMC;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author Grego
 *@version 1.09876238952
 */
public class IMCActivity extends Activity {

	private int alt;
	private double res;
	private double pes;
	private String clasific;
	private EditText altura;
	private EditText peso;
	private TextView clasificacion;
	private TextView resultado;
	private TextView resultado2;
	private DatePicker fechaNacimiento;
	private RadioButton hombre;
	private RadioButton mujer;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.altura = (EditText) findViewById(R.id.altura);
		this.peso = (EditText) findViewById(R.id.peso);
		this.clasificacion = (TextView) findViewById(R.id.clasificacion);
		this.resultado = (TextView) findViewById(R.id.resultado);
		this.resultado2 = (TextView) findViewById(R.id.resultado2);
		this.fechaNacimiento = (DatePicker) findViewById(R.id.fecha);
		this.hombre = (RadioButton) findViewById(R.id.hombre);
		this.mujer = (RadioButton) findViewById(R.id.mujer);
	}

	public void calculoDeIMC(View view) {

		try {
			this.alt = Integer.parseInt(this.altura.getText().toString());

		} catch (Exception e) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Introduzca una altura", Toast.LENGTH_SHORT);
			toast.show();
			this.resultado.setText("");
		}
		try {
			this.pes = Double.parseDouble(this.peso.getText().toString());
		} catch (Exception e) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Introduzca un peso", Toast.LENGTH_SHORT);
			toast.show();
			this.resultado.setText("");
		}

		double altu;
		altu = (double) alt;
		altu = altu / 100;
		double result = pes / (altu * altu);
		result = getDecimal(2, result);
		res = result;
		this.clasific = clasifica(result);
		this.clasificacion.setText(clasific);
		this.resultado.setText(Double.toString((result)));
	}

	public void calculoIGCE(View view) {
		double result = 0;
		int s = 0;
		int edad = calculaEdad();
		calculoDeIMC(view);
		if (edad <= 0) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Edad no válida", Toast.LENGTH_SHORT);
			toast.show();
			this.resultado2.setText("");
			return;
		}
		if (hombre.isChecked()) {
			s = 1;
			if (edad < 16) {
				result = (1.51 * res) - (0.7 * edad) - (3.6 * s) + 3.4;
				result = getDecimal(2, result);
				this.resultado2.setText(Double.toString(result) + "%");
			}
			if (edad >= 16) {
				result = (1.2 * res) + (0.23 * edad) - (10.8 * s) - 5.4;
				result = getDecimal(2, result);
				this.resultado2.setText(Double.toString(result) + "%");
			}
		}
		if (mujer.isChecked()) {
			s = 0;
			if (edad < 16) {
				result = (1.51 * res) - (0.7 * edad) - (3.6 * s) + 3.4;
				result = getDecimal(2, result);
				this.resultado2.setText(Double.toString(result) + "%");
			}
			if (edad >= 16) {
				result = (1.2 * res) + (0.23 * edad) - (10.8 * s) - 5.4;
				result = getDecimal(2, result);
				this.resultado2.setText(Double.toString(result) + "%");
			}
		}
	}

	private double getDecimal(int numeroDecimales, double decimal) {
		decimal = decimal * (java.lang.Math.pow(10, numeroDecimales));
		decimal = java.lang.Math.round(decimal);
		decimal = decimal / java.lang.Math.pow(10, numeroDecimales);

		return decimal;
	}

	private String clasifica(double resultado) {
		String clasif = "";

		if (resultado <= 0) {
			clasif = "";
			return clasif;
		}
		if (resultado <= 16) {
			clasif = "Delgadez severa";
		}
		if (resultado > 16 && resultado <= 16.99) {
			clasif = "Delgadez moderada";
		}
		if (resultado > 17 && resultado <= 18.49) {
			clasif = "Delgadez no muy pronunciada";
		}
		if (resultado > 18.5 && resultado <= 24.99) {
			clasif = "Normal";
		}
		if (resultado > 25 && resultado <= 29.99) {
			clasif = "Sobrepeso";
		}
		if (resultado > 30) {
			clasif = "Obesidad";
		}
		return clasif;
	}

	private int calculaEdad() {
		Calendar nacimiento = Calendar.getInstance();
		nacimiento.set(fechaNacimiento.getYear(),fechaNacimiento.getMonth(), fechaNacimiento.getDayOfMonth());
		int resultado = Calendar.getInstance().get(Calendar.YEAR)
				- nacimiento.get(Calendar.YEAR);
		return resultado;
	}
}