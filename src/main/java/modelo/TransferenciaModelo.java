package modelo;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

// Registra el historial de cambios de grupo de un catequizando (transferencias de fin de anho / de etapa).
// La inscripcion activa del catequizando (InscripcionModelo) siempre apunta al grupo actual;
// cada vez que se transfiere, se actualiza esa inscripcion y se deja constancia aca de
// desde que grupo y hacia que grupo se hizo el cambio, y cuando.
@Entity(name = "tb_transferencia")
public class TransferenciaModelo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer transf_id;

	@Column(name = "transf_fecha", nullable = false)
	private LocalDate transf_fecha;

	@Column(name = "transf_observacion", length = 255, nullable = true)
	private String transf_observacion;

	// ======================== MUCHOS A UNO ==================================
	@ManyToOne(optional = false)
	@JoinColumn(name = "catz_id")
	private CatequizandoModelo catequizando;

	@ManyToOne(optional = false)
	@JoinColumn(name = "grupo_origen_id")
	private GrupoCatequesisModelo grupoOrigen;

	@ManyToOne(optional = false)
	@JoinColumn(name = "grupo_destino_id")
	private GrupoCatequesisModelo grupoDestino;

	public TransferenciaModelo() {
		super();
	}

	public Integer getTransf_id() {
		return transf_id;
	}

	public void setTransf_id(Integer transf_id) {
		this.transf_id = transf_id;
	}

	public LocalDate getTransf_fecha() {
		return transf_fecha;
	}

	public void setTransf_fecha(LocalDate transf_fecha) {
		this.transf_fecha = transf_fecha;
	}

	public String getTransf_observacion() {
		return transf_observacion;
	}

	public void setTransf_observacion(String transf_observacion) {
		this.transf_observacion = transf_observacion;
	}

	public CatequizandoModelo getCatequizando() {
		return catequizando;
	}

	public void setCatequizando(CatequizandoModelo catequizando) {
		this.catequizando = catequizando;
	}

	public GrupoCatequesisModelo getGrupoOrigen() {
		return grupoOrigen;
	}

	public void setGrupoOrigen(GrupoCatequesisModelo grupoOrigen) {
		this.grupoOrigen = grupoOrigen;
	}

	public GrupoCatequesisModelo getGrupoDestino() {
		return grupoDestino;
	}

	public void setGrupoDestino(GrupoCatequesisModelo grupoDestino) {
		this.grupoDestino = grupoDestino;
	}

}