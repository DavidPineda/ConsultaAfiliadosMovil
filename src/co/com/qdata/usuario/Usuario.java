package co.com.qdata.usuario;

public class Usuario {

	private String nombre_usuario;
	private String contrasena;
	private int id;
	
	public String getNombre_usuario() {
		return nombre_usuario;
	}
	public void setNombre_usuario(String nombre_usuario) {
		this.nombre_usuario = nombre_usuario;
	}
	public String getContrasena() {
		return contrasena;
	}
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Usuario(){
		
	}
	
	public Usuario(int id, String nombre_usuario, String contrasena){
		this.id = id;
		this.nombre_usuario = nombre_usuario;
		this.contrasena = contrasena;
	}
	
}
