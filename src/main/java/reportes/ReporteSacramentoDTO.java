package reportes;

import modelo.SacramentoModelo;

/**
 * Bean plano para el listado Jasper de Sacramentos.
 */
public class ReporteSacramentoDTO {

	/** Nombre del sacramento */
	private String nombre;
	/** Cantidad de catequizandos que recibieron el sacramento */
	private Integer catequizandosConEsteSacramento;
	/** Cantidad de catequistas que recibieron el sacramento */
	private Integer catequistasConEsteSacramento;

	/**
	 * Crea una fila del listado de Sacramentos.
	 *
	 * @param nombre nombre del sacramento
	 * @param catequizandosConEsteSacramento cantidad de catequizandos que recibieron el sacramento
	 * @param catequistasConEsteSacramento cantidad de catequistas que recibieron el sacramento
	 */
	public ReporteSacramentoDTO(String nombre, Integer catequizandosConEsteSacramento,
			Integer catequistasConEsteSacramento) {
		super();
		this.nombre = nombre;
		this.catequizandosConEsteSacramento = catequizandosConEsteSacramento;
		this.catequistasConEsteSacramento = catequistasConEsteSacramento;
	}

	/**
	 * Convierte un SacramentoModelo en una fila del listado.
	 *
	 * @param sacramento sacramento de origen
	 */
	public static ReporteSacramentoDTO desde(SacramentoModelo sacramento) {
		int catequizandos = sacramento.getCatequizandos() != null ? sacramento.getCatequizandos().size() : 0;
		int catequistas = sacramento.getCatequistas() != null ? sacramento.getCatequistas().size() : 0;
		return new ReporteSacramentoDTO(sacramento.getSacr_nombre(), catequizandos, catequistas);
	}

	/** @return nombre del sacramento */
	public String getNombre() {
		return nombre;
	}

	/** @return cantidad de catequizandos que recibieron el sacramento */
	public Integer getCatequizandosConEsteSacramento() {
		return catequizandosConEsteSacramento;
	}

	/** @return cantidad de catequistas que recibieron el sacramento */
	public Integer getCatequistasConEsteSacramento() {
		return catequistasConEsteSacramento;
	}

}
