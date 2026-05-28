package modelo;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity(name = "tb_asistencia")
public class AsistenciaModelo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer asist_id;

	@Column
	private Boolean estado;

	@Lob
	@Column(columnDefinition = "TEXT")
	private String observaciones;

	// ==================== MUCHOS A UNO ===========================
	@ManyToOne
	@JoinColumn(name = "inscrip_id")
	private InscripcionModelo inscripcion;

	@ManyToOne
	@JoinColumn(name = "clase_id")
	private ClaseModelo clase;

	public AsistenciaModelo() {
		super();
	}

	public Integer getAsist_id() {
		return asist_id;
	}

	public void setAsist_id(Integer asist_id) {
		this.asist_id = asist_id;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public InscripcionModelo getInscripcion() {
		return inscripcion;
	}

	public void setInscripcion(InscripcionModelo inscripcion) {
		this.inscripcion = inscripcion;
	}

	public ClaseModelo getClase() {
		return clase;
	}

	public void setClase(ClaseModelo clase) {
		this.clase = clase;
	}

}