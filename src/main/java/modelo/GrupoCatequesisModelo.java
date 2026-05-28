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

@Entity(name = "tb_grupo_catequesis")
public class GrupoCatequesisModelo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer grup_id;

	@Column(nullable = false)
	private LocalDate grup_anho;

	// ===================== MUCHOS A UNO ===================

	@ManyToOne(optional = false)
	@JoinColumn(name = "etap_id")
	private EtapaModelo etapa;

	@ManyToOne(optional = false)
	@JoinColumn(name = "cat_id")
	private CatequistaModelo catequista;

	// ====================== UNO A MUCHOS ====================

	// LISTA para obtener todos los alumnos inscritos en este grupo
	@OneToMany(mappedBy = "grupoCatequesis", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<InscripcionModelo> inscripciones;

	// LISTA para obtener todas las sesiones o clases de este grupo
	@OneToMany(mappedBy = "grupoCatequesis", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ClaseModelo> clases;

	public GrupoCatequesisModelo() {
		super();

	}

	public Integer getGrup_id() {
		return grup_id;
	}

	public void setGrup_id(Integer grup_id) {
		this.grup_id = grup_id;
	}

	public LocalDate getGrup_anho() {
		return grup_anho;
	}

	public void setGrup_anho(LocalDate grup_anho) {
		this.grup_anho = grup_anho;
	}

	public EtapaModelo getEtapa() {
		return etapa;
	}

	public void setEtapa(EtapaModelo etapa) {
		this.etapa = etapa;
	}

	public CatequistaModelo getCatequista() {
		return catequista;
	}

	public void setCatequista(CatequistaModelo catequista) {
		this.catequista = catequista;
	}

	public List<InscripcionModelo> getInscripciones() {
		return inscripciones;
	}

	public void setInscripciones(List<InscripcionModelo> inscripciones) {
		this.inscripciones = inscripciones;
	}

	public List<ClaseModelo> getClases() {
		return clases;
	}

	public void setClases(List<ClaseModelo> clases) {
		this.clases = clases;
	}
//probar si guarda 
}