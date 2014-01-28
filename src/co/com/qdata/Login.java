/**
 * @nameProject Auditoria Presencial Movil
 * @nameClass Login
 * @author Irma Fernanda Alayon 
 * @version 1.0
 * @date 26/03/2012
 */

package co.com.qdata;

import integra.auditoriapre.movil.R;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapPrimitive;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import co.com.qdata.Persistencia.DBManaged;
import co.com.qdata.llamaServicio.LlamaServicio;
import co.com.qdata.usuario.Usuario;

public class Login extends Activity{
	
	private static final String TABLA_1 = "create table if not exists "
									  + "user_file "
								  	  + "("
								  	  + "ID INT PRIMARY KEY , "
								  	  + "USER TEXT "
								  	  + "PASSWORD TEXT"
								  	  + ")";	
	
	private static final String DB_NAME = "QDATA_MOVIL";
	private static final int VERSION = 1;

	private TextView usuario;
	private TextView contrasena;
	private CheckBox almacenarUser;
	private Button btn_consultar;
	
	/**
	 * @author Irma Fernanda Alayon
	 * @date 26/03/2012
	 * Metodo que activa la ventana de la actidad (clase).
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);		
		usuario = (TextView) findViewById(R.id.usuario);
		contrasena = (TextView) findViewById(R.id.contrasena);
		almacenarUser = (CheckBox) findViewById(R.id.RecoardarLogin);		
		btn_consultar = (Button) findViewById(R.id.btconectar);
		btn_consultar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				validar_Login();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.ejemplo_menu, menu);
		return true;
	}

	/**
	 *@author Irma Fernanda Alayon
	 * @date 26/03/2012
	 * Metodo que activa el traslado de la activity Login a Consulta
	 */
	public void aceptar() {
		Intent intent = new Intent(Login.this, Consulta.class);
		startActivity(intent);
	}

	/**
	 * @author Irma Fernanda Alayon
	 * @date 26/03/2012
	 *  Metodo que activa el traslado de la activity Login a ella misma.
	 */
	public void cancelar() {
		Intent intent = new Intent(Login.this, Login.class);
		startActivity(intent);
	}

	public void validar_Login() {
		String url = "";
		
		try{
			SQLiteDatabase db = SQLiteDatabase.openDatabase("data/data/integra.auditoriapre.movil/databases/QDATA_MOVIL", null, SQLiteDatabase.OPEN_READONLY);
			url = DBManaged.recuperarURL(db, "select URL from url_file where ID = 1");
			if(!url.equals("")){
				if(validarUsuario(usuario.getText().toString()) && validarContrasena(contrasena.getText().toString())) {
					try{				
						if (almacenarUser.isChecked()) {
							almacenarDatos();
						}
						llamaServicio(url);
					}catch(SQLiteException ex){
						mostrarMensaje("Ocurrio un error al guardar los datos del usuario");
					}
				}
			}else
				mostrarMensaje("Por favor configure la URL del servico Web");
		}catch(SQLiteException ex)
		{
			mostrarMensaje("A ocurrido un error al recuperar la URL registrada");
		}
	
	}

	private void almacenarDatos() throws SQLiteException {
		Usuario usuario = null;
		DBManaged db = new DBManaged(getApplicationContext(), DB_NAME, VERSION, TABLA_1);
		usuario = db.recuperaruSuarioContrasena("SELECT ID, USER, PASSWORD FROM user_file WHERE ID = 1");
		try
		{
		if (!(usuario == null)){
			db.ejecutarQuery(db.getWritableDatabase(), 
					"insert into user_file(ID, USER, PASSWORD) values('1','" + this.usuario.toString() + "','" + this.contrasena.toString() + "'");
		}else{
			db.ejecutarQuery(db.getWritableDatabase(),
					"update user_file set USER = '" + this.usuario.toString() + "', PASSWORD = '" + this.contrasena.toString() + "'");
		}
		}catch(SQLiteException ex){
			mostrarMensaje("Se presento un error en la aplicación");
		}catch (Exception e) {
			mostrarMensaje("Se presento un error en la aplicación");
		}
	}

	private void mostrarMensaje(String mensaje){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage((mensaje))
				.setCancelable(false)
				.setNegativeButton("Aceptar",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
									dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();			
	}
	
	private boolean validarUsuario(String nom_usuario){
		if(nom_usuario == ""){
			mostrarMensaje("Por favor ingrese un nombre de usuario");
			return false;
		}else if(nom_usuario.length() > 30){
			mostrarMensaje("La longitud del usuario supera los 30 carateres");
			return false;
		}
		return true;
	}
	
	private boolean validarContrasena(String contrasena){
		if(contrasena == ""){
			mostrarMensaje("Por favor ingrese el password del usuario");
			return false;
		}else if(contrasena.length() > 30){
			mostrarMensaje("La longitud del password supera los 30 carateres");
			return false;
		}
		return true;
	}
	
	private void llamaServicio(String url){
							
		String NameSpace = "http://tempuri.org/ConsultaAfiliadosOnLine/Service1";
		String SoapAction = "http://tempuri.org/ConsultaAfiliadosOnLine/Service1/consultarUsuarioMovil";
		String Method = "consultarUsuarioMovil";
		
		try {
			
			PropertyInfo[] propiedades = new PropertyInfo[2];
			PropertyInfo UserName = new PropertyInfo();
			PropertyInfo Password = new PropertyInfo();

			UserName.setName("nombre_usuario");
			UserName.setValue(usuario.getText().toString());
			UserName.setType(String.class);
			propiedades[0] = UserName;
			Password.setName("contrasena");
			Password.setValue(contrasena.getText().toString());
			Password.setType(String.class);
			propiedades[1] = Password;
			LlamaServicio servicio = new LlamaServicio(NameSpace, SoapAction, Method, url);
			SoapPrimitive response = servicio.llamaServicioPrimitive(propiedades);
			
			concederDenegarAcceso(Integer.parseInt(response.toString()));

		}
		catch (XmlPullParserException ex){
			mostrarMensaje("Error de conversión de datos");
		}
		catch (Exception ex) {
			mostrarMensaje("Se preseneto un error al realizar la consulta de afiliado");
		}

	}
	
	private void concederDenegarAcceso(int valor){
		switch(valor){
		case 0:
			mostrarMensaje("Usuario y password incorrectos");
			cancelar();
			break;
		case 1:
			aceptar();
			break;
		case 2:
			mostrarMensaje("Password de usuario incorrecto");
			cancelar();
			break;
		case 3:
			mostrarMensaje("Nombre de usuario incorrecto");
			cancelar();
			break;
		case 7:
			mostrarMensaje("Se produjo un error, Favor intente más tarde");
			cancelar();
			break;
		case 8:
			mostrarMensaje("Se produjo un error, Favor intente más tarde");
			cancelar();
			break;
		case 9: 
			mostrarMensaje("Se produjo un error, Favor intente más tarde");
			cancelar();				
			break;
			default:
				mostrarMensaje("Se produjo un error, Favor intente más tarde");
				cancelar();		
				break;
		}
	}

	/**
	 * @author Irma Fernanda Alayï¿½n Perilla
	 * @date 26/03/2012
	 * @compania Quality Data S.A Metodo que se encarga de mostrar el menu desplegable.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.login:
			Intent intent = new Intent(Login.this, Login.class);
			startActivity(intent);
			return true;

		case R.id.configurar:
			Intent intento = new Intent(Login.this, Configurar.class);
			startActivity(intento);
			return true;

		case R.id.salir:
			moveTaskToBack(true);
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}