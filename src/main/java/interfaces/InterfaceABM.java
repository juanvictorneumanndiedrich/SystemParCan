package interfaces;

/**
 * Contrato que deben cumplir los controladores de las pantallas ABM
 * (Alta, Baja y Modificacion) del sistema.
 *
 * Cada metodo corresponde a un boton o accion de la pantalla. Los botones de la vista
 * quedan conectados a estos metodos mediante {@code vista.setInterfaceABM(this)}.
 */
public interface InterfaceABM {

	/** Prepara la pantalla para cargar un registro nuevo (limpia y habilita los campos). */
	public void nuevo();

	/** Habilita la edicion del registro seleccionado en la tabla. */
	public void editar();

	/** Elimina el registro seleccionado, normalmente previa confirmacion del usuario. */
	public void eliminar();

	/** Cancela la operacion en curso y vuelve la pantalla a su estado inicial. */
	public void cancelar();

	/** Valida los datos ingresados y guarda el registro (alta o modificacion). */
	public void guardar();

	/** Filtra la tabla segun el texto escrito en el campo de busqueda. */
	public void buscar();

}