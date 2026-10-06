package reportes;

import modelo.SacramentoModelo;

/**
 * Bean plano para el listado Jasper de Sacramentos. Solo lleva el nombre:
 * el listado enumera los sacramentos, sin contar catequizandos ni catequistas.
 */
public class ReporteSacramentoDTO {

	/** Nombre del sacramento */
	private String nombre;

	/**
	 * Crea una fila del listado de Sacramentos.
	 *
	 * @param nombre nombre del sacramento
	 */
	public ReporteSacramentoDTO(String nombre) {
		super();
		this.nombre = nombre;
	}

	/**
	 * Convierte un SacramentoModelo en una fila del listado.
	 *
	 * @param sacramento sacramento de origen
	 */
	public static ReporteSacramentoDTO desde(SacramentoModelo sacramento) {
		return new ReporteSacramentoDTO(sacramento.getSacr_nombre());
	}

	/** @return nombre del sacramento */
	public String getNombre() {
		return nombre;
	}

}
