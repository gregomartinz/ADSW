package es.upm.dit.adsw.rr;

public class Restaurante {

	private String nombre;
	private String direccion;
	private String telefono;
	private String tipo;

	public Restaurante(String nombre, String direccion, String telefono,
			String tipo) {

		this.setNombre(nombre);
		this.setDireccion(direccion);
		this.setTelefono(telefono);
		this.setTipo(tipo);
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean igual(Restaurante r) {
		boolean ayuda = false;
		if (getNombre().toString().equals(r.getNombre().toString())
				&& getDireccion().toString()
						.equals(r.getDireccion().toString())
				&& getTelefono().toString().equals(r.getTelefono().toString())
				&& getTipo().toString().equals(r.getTipo().toString())) {
			ayuda = true;
		}
		return ayuda;
	}

	public boolean cambia1(Restaurante r) {
		boolean ayuda = false;
		if (getNombre().toString().equals(r.getNombre().toString())
				|| getDireccion().toString()
						.equals(r.getDireccion().toString())) {
			ayuda = true;
		}
		return ayuda;
	}

	public String toString() {
		return nombre + " - " + direccion;

	}
}
