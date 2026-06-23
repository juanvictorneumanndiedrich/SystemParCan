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
import jakarta.persistence.OneToMany;

@Entity(name = "tb_catequizando")
public class CatequizandoModelo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer catz_id;

	@Column(length = 100, nullable = false)
	private String catz_nombre;

	@Column(length = 100, nullable = false)
	private String catz_apellido;

	@Column(length = 50, nullable = false, unique = false)
	private String catz_documento;

	@Column(length = 45, nullable = false)
	private String catz_telefono;

	@Column(length = 100, nullable = false)
	private String catz_correo;

	@Column(length = 100, nullable = false)
	private String catz_direccion;

	@Column(name = "catz_fechanacimiento", nullable = false)
	private LocalDate catz_fechaNacimiento;

	@Column(name = "catz_fecharegistro", nullable = false)
	private LocalDate catz_fechaRegistro;

	@Column(nullable = false)
	private Boolean catz_estado;

	//========================= UNO A MUCHOS ==================================
	// LISTA: Un alumno puede tener varias inscripciones
	@OneToMany(mappedBy = "catequizando", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<InscripcionModelo> inscripciones;

	public CatequizandoModelo() {
		super();

	}

	public Integer getCatz_id() {
		return catz_id;
	}

	public void setCatz_id(Integer catz_id) {
		this.catz_id = catz_id;
	}

	public String getCatz_nombre() {
		return catz_nombre;
	}

	public void setCatz_nombre(String catz_nombre) {
		this.catz_nombre = catz_nombre;
	}

	public String getCatz_apellido() {
		return catz_apellido;
	}

	public void setCatz_apellido(String catz_apellido) {
		this.catz_apellido = catz_apellido;
	}

	public String getCatz_documento() {
		return catz_documento;
	}

	public void setCatz_documento(String catz_documento) {
		this.catz_documento = catz_documento;
	}

	public String getCatz_telefono() {
		return catz_telefono;
	}

	public void setCatz_telefono(String catz_telefono) {
		this.catz_telefono = catz_telefono;
	}

	public String getCatz_correo() {
		return catz_correo;
	}

	public void setCatz_correo(String catz_correo) {
		this.catz_correo = catz_correo;
	}

	public String getCatz_direccion() {
		return catz_direccion;
	}

	public void setCatz_direccion(String catz_direccion) {
		this.catz_direccion = catz_direccion;
	}

	public LocalDate getCatz_fechaNacimiento() {
		return catz_fechaNacimiento;
	}

	public void setCatz_fechaNacimiento(LocalDate catz_fechaNacimiento) {
		this.catz_fechaNacimiento = catz_fechaNacimiento;
	}

	public LocalDate getCatz_fechaRegistro() {
		return catz_fechaRegistro;
	}

	public void setCatz_fechaRegistro(LocalDate catz_fechaRegistro) {
		this.catz_fechaRegistro = catz_fechaRegistro;
	}

	public Boolean isCatz_estado() {
		return catz_estado;
	}

	public void setCatz_estado(Boolean catz_estado) {
		this.catz_estado = catz_estado;
	}

	public List<InscripcionModelo> getInscripciones() {
		return inscripciones;
	}

	public void setInscripciones(List<InscripcionModelo> inscripciones) {
		this.inscripciones = inscripciones;
	}

}
