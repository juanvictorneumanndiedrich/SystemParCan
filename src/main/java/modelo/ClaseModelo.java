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

@Entity(name = "tb_clase")
public class ClaseModelo {

	// La clase representa el dia del evento

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer clase_id;

	@Column(name = "clase_fechaclase", nullable = false)
	private LocalDate clase_fechaClase;

	@Column(nullable = false, length = 100)
	private String clase_descripcion;

	// ======================== MUCHOS A UNO ==================================
	@ManyToOne
	@JoinColumn(name = "grupo_id")
	private GrupoCatequesisModelo grupoCatequesis;

	// ======================= UNO A MUCHOS ===================================

	// LISTA de todas las asistencias marcadas en esta fecha especifica y la asistencia es el registro individual
	@OneToMany(mappedBy = "clase", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<AsistenciaModelo> asistencias;

	public ClaseModelo() {
		super();

	}

	public Integer getClase_id() {
		return clase_id;
	}

	public void setClase_id(Integer clase_id) {
		this.clase_id = clase_id;
	}

	public LocalDate getClase_fechaClase() {
		return clase_fechaClase;
	}

	public void setClase_fechaClase(LocalDate clase_fechaClase) {
		this.clase_fechaClase = clase_fechaClase;
	}

	public String getClase_descripcion() {
		return clase_descripcion;
	}

	public void setClase_descripcion(String clase_descripcion) {
		this.clase_descripcion = clase_descripcion;
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