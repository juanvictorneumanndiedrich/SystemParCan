package reportes;

import modelo.EtapaModelo;

/**
 * Bean plano para el listado Jasper de Etapas.
 */
public class ReporteEtapaDTO {

	/** Descripcion de la etapa */
	private String descripcion;
	/** Cantidad de grupos que pertenecen a la etapa */
	private Integer cantidadGrupos;
	/** Estado como texto (Activo o Inactivo) */
	private String estado;

	/**
	 * Crea una fila del listado de Etapas.
	 *
	 * @param descripcion descripcion de la etapa
	 * @param cantidadGrupos cantidad de grupos que pertenecen a la etapa
	 * @param estado estado como texto (Activo o Inactivo)
	 */
	public ReporteEtapaDTO(String descripcion, Integer cantidadGrupos, String estado) {
		super();
		this.descripcion = descripcion;
		this.cantidadGrupos = cantidadGrupos;
		this.estado = estado;
	}

	/**
	 * Convierte una EtapaModelo en una fila del listado.
	 *
	 * @param etapa etapa de origen
	 */
	public static ReporteEtapaDTO desde(EtapaModelo etapa) {
		int cantidadGrupos = etapa.getGrupos() != null ? etapa.getGrupos().size() : 0;
		return new ReporteEtapaDTO(
				etapa.getEtap_descripcion(),
				cantidadGrupos,
				etapa.isEtap_estado() ? "Activo" : "Inactivo");
	}

	/** @return descripcion de la etapa */
	public String getDescripcion() {
		return descripcion;
	}

	/** @return cantidad de grupos que pertenecen a la etapa */
	public Integer getCantidadGrupos() {
		return cantidadGrupos;
	}

	/** @return estado como texto (Activo o Inactivo) */
	public String getEstado() {
		return estado;
	}

}
