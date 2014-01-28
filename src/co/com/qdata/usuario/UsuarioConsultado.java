package co.com.qdata.usuario;

public class UsuarioConsultado {
	
	private String tipo_documento;
	private String numero_documento;
	private String estado;
	private String nombre_afiliado;
	private String ficha;
	private String nivel_sisben;
	private String regimen;
	private String tipo_subsidio;
	private String ciudad;
	private String departamento;
	private String ips_primaria;
	
	public String getTipo_documento() {
		return tipo_documento;
	}
	public void setTipo_documento(String tipo_documento) {
		this.tipo_documento = tipo_documento;
	}
	public String getNumero_documento() {
		return numero_documento;
	}
	public void setNumero_documento(String numero_documento) {
		this.numero_documento = numero_documento;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getNombre_afiliado() {
		return nombre_afiliado;
	}
	public void setNombre_afiliado(String nombre_afiliado) {
		this.nombre_afiliado = nombre_afiliado;
	}
	public String getFicha() {
		return ficha;
	}
	public void setFicha(String ficha) {
		this.ficha = ficha;
	}
	public String getNivel_sisben() {
		return nivel_sisben;
	}
	public void setNivel_sisben(String nivel_sisben) {
		this.nivel_sisben = nivel_sisben;
	}
	public String getRegimen() {
		return regimen;
	}
	public void setRegimen(String regimen) {
		this.regimen = regimen;
	}
	public String getTipo_subsidio() {
		return tipo_subsidio;
	}
	public void setTipo_subsidio(String tipo_subsidio) {
		this.tipo_subsidio = tipo_subsidio;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getIps_primaria() {
		return ips_primaria;
	}
	public void setIps_primaria(String ips_primaria) {
		this.ips_primaria = ips_primaria;
	}
	
	@Override
	public String toString(){
		return "Estado: " + this.estado + "\n" +
			   "Tipo Documento: " + this.tipo_documento + "\n" +
			   "Número Documento: " + this.numero_documento + "\n" +
			   "Nombre: " + this.nombre_afiliado + "\n" +
			   "Ficha: " + this.ficha + "\n" +
			   "Nivel Sisben: " + this.nivel_sisben + "\n" +
			   "Regimen: " + this.regimen + "\n" +
			   "Tipo Subsidio: " + this.tipo_subsidio + "\n" +
			   "Ciudad: " + this.ciudad + "\n" +
			   "Departamento: " + this.departamento + "\n" +
			   "IPS primaria: " + this.ips_primaria;
	}
	
}
