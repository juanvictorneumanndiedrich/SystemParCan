package reportes;

import modelo.EtapaModelo;

/**
 * Bean plano para el listado Jasper de Etapas.
 */
public class ReporteEtapaDTO {

	private String descripcion;
	private Integer cantidadGrupos;
	private String estado;

	public ReporteEtapaDTO(String descripcion, Integer cantidadGrupos, String estado) {
		super();
		this.descripcion = descripcion;
		this.cantidadGrupos = cantidadGrupos;
		this.estado = estado;
	}

	public static ReporteEtapaDTO desde(EtapaModelo etapa) {
		int cantidadGrupos = etapa.getGrupos() != null ? etapa.getGrupos().size() : 0;
		return new ReporteEtapaDTO(
				etapa.getEtap_descripcion(),
				cantidadGrupos,
				etapa.isEtap_estado() ? "Activo" : "Inactivo");
	}

	public String getDescripcion() {
		return descripcion;
	}

	public Integer getCantidadGrupos() {
		return cantidadGrupos;
	}

	public String getEstado() {
		return estado;
	}

}
