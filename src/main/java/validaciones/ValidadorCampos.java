package validaciones;

import java.util.regex.Pattern;

/**
 * Clase utilitaria con las reglas generales de validacion de campos,
 * usadas por los controladores antes de guardar cualquier entidad.
 *
 * Reglas disponibles:
 * - Campo obligatorio: no puede estar vacio ni ser null.
 * - Solo texto: solo letras (con acentos y enye) y espacios, sin numeros ni simbolos.
 * - Solo numeros: solo digitos, sin letras ni simbolos.
 *
 * No valida formato (email, fecha, etc.), solo estas tres reglas generales.
 */
public class ValidadorCampos {

	private static final Pattern PATRON_SOLO_TEXTO = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñÜü ]+$");
	private static final Pattern PATRON_SOLO_NUMEROS = Pattern.compile("^[0-9]+$");

	private ValidadorCampos() {
		// clase utilitaria, no se instancia
	}

	/** @return true si el valor no es null y no esta vacio (sin contar espacios). */
	public static boolean esObligatorio(String valor) {
		return valor != null && !valor.trim().isEmpty();
	}

	/** @return true si el valor contiene solo letras y espacios (sin numeros ni simbolos). */
	public static boolean esSoloTexto(String valor) {
		return valor != null && PATRON_SOLO_TEXTO.matcher(valor.trim()).matches();
	}

	/** @return true si el valor contiene solo numeros (sin letras ni simbolos). */
	public static boolean esSoloNumeros(String valor) {
		return valor != null && PATRON_SOLO_NUMEROS.matcher(valor.trim()).matches();
	}
}
