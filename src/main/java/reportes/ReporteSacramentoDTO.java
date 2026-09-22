package reportes;

import modelo.SacramentoModelo;

/**
 * Bean plano para el listado Jasper de Sacramentos.
 */
public class ReporteSacramentoDTO {

	private String nombre;
	private Integer catequizandosConEsteSacramento;
	private Integer catequistasConEsteSacramento;

	public ReporteSacramentoDTO(String nombre, Integer catequizandosConEsteSacramento,
			Integer catequistasConEsteSacramento) {
		super();
		this.nombre = nombre;
		this.catequizandosConEsteSacramento = catequizandosConEsteSacramento;
		this.catequistasConEsteSacramento = catequistasConEsteSacramento;
	}

	public static ReporteSacramentoDTO desde(SacramentoModelo sacramento) {
		int catequizandos = sacramento.getCatequizandos() != null ? sacramento.getCatequizandos().size() : 0;
		int catequistas = sacramento.getCatequistas() != null ? sacramento.getCatequistas().size() : 0;
		return new ReporteSacramentoDTO(sacramento.getSacr_nombre(), catequizandos, catequistas);
	}

	public String getNombre() {
		return nombre;
	}

	public Integer getCatequizandosConEsteSacramento() {
		return catequizandosConEsteSacramento;
	}

	public Integer getCatequistasConEsteSacramento() {
		return catequistasConEsteSacramento;
	}

}
