package modelo;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity(name = "tb_inscripcion")
public class InscripcionModelo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer inscrip_id;

	@Column(nullable = false)
	private LocalDate inscrip_fecha;

	@Column(nullable = false)
	private Boolean inscrip_estado;

	// ======================== MUCHOS A UNO ==================================
	@ManyToOne
	@JoinColumn(name = "catz_id")
	private CatequizandoModelo catequizando;

	@ManyToOne
	@JoinColumn(name = "grupo_id")
	private GrupoCatequesisModelo grupoCatequesis;

	// ======================= UNO A MUCHOS ===================================
	
	// LISTA: la historia de asistencia de este alumno en este grupo
	
	@OneToMany(mappedBy = "inscripcion", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<AsistenciaModelo> asistencias;
	

	public InscripcionModelo() {
		super();

	}

	public Integer getInscrip_id() {
		return inscrip_id;
	}

	public void setInscrip_id(Integer inscrip_id) {
		this.inscrip_id = inscrip_id;
	}

	public LocalDate getInscrip_fecha() {
		return inscrip_fecha;
	}

	public void setInscrip_fecha(LocalDate inscrip_fecha) {
		this.inscrip_fecha = inscrip_fecha;
	}

	public Boolean getInscrip_estado() {
		return inscrip_estado;
	}

	public void setInscrip_estado(Boolean inscrip_estado) {
		this.inscrip_estado = inscrip_estado;
	}

	public CatequizandoModelo getCatequizando() {
		return catequizando;
	}

	public void setCatequizando(CatequizandoModelo catequizando) {
		this.catequizando = catequizando;
	}

	public GrupoCatequesisModelo getGrupoCatequesis() {
		return grupoCatequesis;
	}

	public void setGrupoCatequesis(GrupoCatequesisModelo grupoCatequesis) {
		this.grupoCatequesis = grupoCatequesis;
	}

	public List<AsistenciaModelo> getAsistencias() {
		return asistencias;
	}

	public void setAsistencias(List<AsistenciaModelo> asistencias) {
		this.asistencias = asistencias;
	}

}