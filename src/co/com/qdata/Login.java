/**
 * @nameProject Auditoria Presencial Movil
 * @nameClass Login
 * @author Irma Fernanda Alayon 
 * @version 1.0
 * @date 26/03/2012
 */

package co.com.qdata;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import integra.auditoriapre.movil.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {

	/**
	 * @author Irma Fernanda Alayon
	 * @date 26/03/2012
	 * Metodo que activa la ventana de la actidad (clase).
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// Al ejecutarse la ventan de login se va amostar ls informacion
		// obtenida por los
		// metodos para usuario y contrasena.
		LeerArchivoUser(getCurrentFocus());
		LeerArchivoPass(getCurrentFocus());

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
		Intent intent = new Intent(Login.this, VistaWeb.class);
		startActivity(intent);
	}

	/**
	 * @author Irma Fernanda Alay�n
	 * @date 26/03/2012
	 *  Metodo que activa el traslado de la activity Login a ella misma.
	 */
	public void cancelar() {
		Intent intent = new Intent(Login.this, Login.class);
		startActivity(intent);
	}

	/** 
	 * @author Irma Fernanda Alay�n
	 * @date 26/03/2012
	 * Metodo que utiliza el servicio web ubicado en la URL que se define en el
	 * metodo para validar el login del usuario para tener acceso a la consulta
	 * online

	 */
	public void validar_Login(View v) {
		String NameSpace = "http://tempuri.org/ConsultaAfiliadosOnLine/Service1";
		String SoapAction = "http://tempuri.org/ConsultaAfiliadosOnLine/Service1/VerificarLogin";
		// String Url =
		// "http://192.168.1.107/CONSULTAAFILIADOSONLINEMovil/Service1.asmx";
		String Method = "VerificarLogin";
		final int READ_BLOCK_SIZE = 100;
		String Url = "";

		/**
		 * Se lee el archivo en el cual se encuentra almacenado el valor de la
		 * url
		 */
		try {

			// Se lee el archivo de texto indicado
			FileInputStream fin = openFileInput("Url.txt");
			InputStreamReader isr = new InputStreamReader(fin);
			char[] inputBuffer = new char[READ_BLOCK_SIZE];
			Url = "";

			// Se lee el archivo de texto mientras no se llegue al final de �l
			int charRead;
			while ((charRead = isr.read(inputBuffer)) > 0) {
				// Se lee por bloques de 100 caracteres
				// ya que se desconoce el tama�o del texto
				// Y se va copiando a una cadena de texto
				String strRead = String.copyValueOf(inputBuffer, 0, charRead);
				Url += strRead;
				inputBuffer = new char[READ_BLOCK_SIZE];
			}

			// Se muestra el texto leido en la caje de texto
			// url.setText(Url);

			isr.close();

			// Toast.makeText(getBaseContext(),"El archivo ha sido cargado",
			// Toast.LENGTH_SHORT).show();

		} catch (IOException e) {
			// TODO: handle exception
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(("ERROR: Configure la URL "))
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

		/**
		 * Varibles que contien los datos que se van a enviar como parametros al
		 * servicio web, las cuales se reciben por medio de las cajas de texto
		 * descritas identificadas en en layout main, el cual se encuentra en la
		 * ruta : Integra ARS Movil/ res/ layout/ main.
		 */
		TextView usuario = (TextView) findViewById(R.id.usuario);
		TextView contrasena = (TextView) findViewById(R.id.contrasena);
		String usuario_value = usuario.getText().toString();
		String contrasena_value = contrasena.getText().toString();

		/**
		 * Condicion que valida la longitud de nombre de usuario y password para
		 * poder enviar los datos al servicio web, las cajas de texto
		 * correspondientes a usuario y password no pueden ir vacias
		 */
		if ((usuario_value.length() == 0 || usuario_value.length() > 30)
				&& (contrasena_value.length() == 0 || contrasena_value.length() > 30)) {

			/**
			 * se crea el AlertDialog que muestra la respuesta obtenida del
			 * servicio web
			 */
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"La longitud de Usuario o Contrase�a no es v�lida")
					.setCancelable(false)
					.setNegativeButton("Aceptar",
							new DialogInterface.OnClickListener() {

								/**
								 * este metodo activa el boton del AlertDialog y
								 * llama el el metodo cancelar() para que al dar
								 * clic en el boton aparezca la activity que
								 * esta designada en cancelar()
								 */
								public void onClick(DialogInterface dialog,
										int id) {
									cancelar();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();

			return;
		}

		else if (usuario_value.length() == 0 && contrasena_value.length() != 0) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("El campo USUARIO no puede estar vacio")
					.setCancelable(false)
					.setNegativeButton("Aceptar",
							new DialogInterface.OnClickListener() {

								/**
								 * este metodo activa el boton del AlertDialog y
								 * llama el el metodo cancelar() para que al dar
								 * clic en el boton aparezca la activity que
								 * esta designada en cancelar()
								 */
								public void onClick(DialogInterface dialog,
										int id) {
									cancelar();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}

		else if (contrasena_value.length() == 0) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("El campo CONTRASE�A no puede estar vacio")
					.setCancelable(false)
					.setNegativeButton("Aceptar",
							new DialogInterface.OnClickListener() {

								/**
								 * este metodo activa el boton del AlertDialog y
								 * llama el el metodo cancelar() para que al dar
								 * clic en el boton aparezca la activity que
								 * esta designada en cancelar()
								 */
								public void onClick(DialogInterface dialog,
										int id) {
									cancelar();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}

		/**
		 * En esta seccion si los datos que se envian son validos se einvia al
		 * servicio web
		 */
		else {
			/**
			 * CheckBox que admite el almacenamiento en el archivo planos e
			 * Uuser y Pass de usuario y la contrase�a.
			 * 
			 */
			CheckBox almacenarUser = (CheckBox) findViewById(R.id.RecoardarLogin);
			if (almacenarUser.isChecked() == true) {
				almacenarUP();
			}

			SoapObject request = new SoapObject(NameSpace, Method);

			PropertyInfo UserName = new PropertyInfo();
			PropertyInfo Password = new PropertyInfo();

			UserName.setName("strUsuario");
			UserName.setValue(usuario_value);
			UserName.setType(String.class);

			Password.setName("strClave");
			Password.setValue(contrasena_value);
			Password.setType(String.class);

			request.addProperty(UserName);
			request.addProperty(Password);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(Url);

			try {
				/**
				 * Se crea la variable de transporte que se encarga de traer la
				 * respuesta de el servicio web
				 * 
				 */
				androidHttpTransport.call(SoapAction, envelope);
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
				/**
				 * se valida la respuesta obtenida del servicio web para poder
				 * ingresar a la activity de consulta, llamando a los metodos
				 * aceptar(), si los datos del usuario son validos; y cancelar()
				 * si los datos no son validos.
				 */

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				if ((response.toString().equals("0-USUARIO NO EXISTE"))
						|| (response.toString().equals("0-CLAVE INVALIDA"))) {

					if (response.toString().equals("0-USUARIO NO EXISTE")) {
						builder.setMessage("�l Usuario No Existe")
								.setCancelable(false)
								.setNegativeButton("Aceptar",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												cancelar();
											}

										});
					} else if (response.toString().equals("0-CLAVE INVALIDA")) {
						builder.setMessage("La Clave es Inv�lida")
								.setCancelable(false)
								.setNegativeButton("Aceptar",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												cancelar();
											}

										});
					}
				} else if (response.toString().equals("1-OK")) {
					builder.setMessage("Bienvenido")
							.setCancelable(false)
							.setNegativeButton("Aceptar",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											aceptar();
										}
									});
				}
				AlertDialog alert = builder.create();
				alert.show();
				return;

			}
			/**
			 * Si surge algun error el alertDialog muestra un mensaje
			 * describiendo dicho evento
			 */
			catch (Exception e) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("ERROR: Configura la URL de manera manual")
						.setCancelable(false)
						.setNegativeButton("Aceptar",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										cancelar();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
				return;
			}
		}
	}

	/**
	 * @author IRMA FERNANDA ALAYON PERILLA
	 * @date 16/03/2012
	 * @compa�ia Quality Data S.A M�todo que almacena la informaicion ingresada
	 *           en el campo usuario en el archivo User.txt.
	 */
	public void AlmacenarUser() {
		TextView usuario = (TextView) findViewById(R.id.usuario);
		String usuario_value = usuario.getText().toString();
		String user = usuario_value;
		// String pass = contrasena_value;

		// Clase que permite grabar texto en un archivo
		FileOutputStream fout = null;
		try {

			fout =
			// Metodo que escribe y abre un archivo con un
			// nombre
			// especifica
			// La constante MODE_WORLD_READABLE indica que este
			// arvhivo
			// lo
			// puede
			// leer cualquier apllicacion
			openFileOutput("User.txt", MODE_WORLD_READABLE);

			// Convierte un stream de caracteres en un stream de
			// bytes
			OutputStreamWriter ows = new OutputStreamWriter(fout);
			ows.write(user); // Escribe en el buffer la cadena de
								// texto
			ows.flush(); // Volca lo que hay en el buffer al
							// archivo
			ows.close(); // Cierra el archivo de texto

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		Toast.makeText(getBaseContext(), "Se ha guardado el Usuario!!!",
				Toast.LENGTH_SHORT).show();

	}

	/**
	 * @author IRMA FERNANDA ALAYON PERILLA
	 * @date 26/03/2012
	 * @compa�ia Quality Data S.A 
	 * M�todo que almacena la informaicion ingresada
	 * en el campo contrase�a en el archivo Pass.txt.
	 */
	public void AlmacenarPass() {
		TextView contrasena = (TextView) findViewById(R.id.contrasena);
		String contrasena_value = contrasena.getText().toString();
		String user = contrasena_value;
		// String pass = contrasena_value;

		// Clase que permite grabar texto en un archivo
		FileOutputStream fout = null;
		try {

			fout =
			// Metodo que escribe y abre un archivo con un
			// nombre
			// especifica
			// La constante MODE_WORLD_READABLE indica que este
			// arvhivo
			// lo
			// puede
			// leer cualquier apllicacion
			openFileOutput("Pass.txt", MODE_WORLD_READABLE);

			// Convierte un stream de caracteres en un stream de
			// bytes
			OutputStreamWriter ows = new OutputStreamWriter(fout);
			ows.write(user); // Escribe en el buffer la cadena de
								// texto
			ows.flush(); // Volca lo que hay en el buffer al
							// archivo
			ows.close(); // Cierra el archivo de texto

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		Toast.makeText(getBaseContext(), "Se ha guardado el Usuario!!!",
				Toast.LENGTH_SHORT).show();

	}

	/**
	 * @author IRMA FERNANDA ALAYON PERILLA
	 * @date 26/03/2012
	 * @compa�ia Quality Data S.A
	 * M�todo que implementa los dos metodos de AlmacenarUser() y
	 * AlmacenarPass() para ejecutarlos al mismo tiempo suando se seleccione el
	 * CheckBox "Recordar Usuario"
	 */
	public void almacenarUP() {
		AlmacenarUser();
		AlmacenarPass();
	}

	/**
	 * @author IRMA FERNANDA ALAYON PERILLA
	 * @date 26/03/2012
	 * @compa�ia Quality Data S.A M�todo que se encarga de leer el Achivo
	 *           User.txt, para asignarselo a la caja de texto de usurio en el
	 *           login, cuando el usuario halla seleccionado "Recordar Usuario".
	 * @param v
	 */
	public void LeerArchivoUser(View v) {
		TextView user = (TextView) findViewById(R.id.usuario);
		final int READ_BLOCK_SIZE = 100;
		String Usuario = "";
		try {

			// Se lee el archivo de texto indicado
			FileInputStream fin = openFileInput("User.txt");
			InputStreamReader isr = new InputStreamReader(fin);

			char[] inputBuffer = new char[READ_BLOCK_SIZE];
			Usuario = "";

			// Se lee el archivo de texto mientras no se llegue al final de �l
			int charRead;
			while ((charRead = isr.read(inputBuffer)) > 0) {
				// Se lee por bloques de 100 caracteres
				// ya que se desconoce el tama�o del texto
				// Y se va copiando a una cadena de texto
				String strRead = String.copyValueOf(inputBuffer, 0, charRead);
				Usuario += strRead;

				inputBuffer = new char[READ_BLOCK_SIZE];
			}

			// Se muestra el texto leido en la caje de texto

			user.requestFocusFromTouch();
			user.setText(Usuario);
			isr.close();

			// Toast.makeText(getBaseContext(),"El archivo ha sido cargado",
			// Toast.LENGTH_SHORT).show();

		} catch (IOException e) {
			// TODO: handle exception

		}
	}

	/**
	 * @author IRMA FERNANDA ALAYON PERILLA
	 * @date 26/03/2012
	 * @compa�ia Quality Data S.A M�todo que se encarga de leer el Achivo
	 *           Pass.txt, para asignarselo a la caja de texto de contrase�a en
	 *           el login, cuando el usuario halla seleccionado
	 *           "Recordar Usuario".
	 * @param v
	 */
	public void LeerArchivoPass(View v) {
		TextView contrasena = (TextView) findViewById(R.id.contrasena);
		final int READ_BLOCK_SIZE = 100;
		String Pass = "";
		try {

			// Se lee el archivo de texto indicado
			FileInputStream fin = openFileInput("Pass.txt");
			InputStreamReader isr = new InputStreamReader(fin);

			char[] inputBuffer = new char[READ_BLOCK_SIZE];
			Pass = "";

			// Se lee el archivo de texto mientras no se llegue al final de �l
			int charRead;
			while ((charRead = isr.read(inputBuffer)) > 0) {
				// Se lee por bloques de 100 caracteres
				// ya que se desconoce el tama�o del texto
				// Y se va copiando a una cadena de texto
				String strRead = String.copyValueOf(inputBuffer, 0, charRead);
				Pass += strRead;

				inputBuffer = new char[READ_BLOCK_SIZE];
			}

			// Se muestra el texto leido en la caje de texto

			contrasena.requestFocusFromTouch();
			contrasena.setText(Pass);
			isr.close();

			// Toast.makeText(getBaseContext(),"El archivo ha sido cargado",
			// Toast.LENGTH_SHORT).show();

		} catch (IOException e) {
			// TODO: handle exception

		}
	}

	/**
	 * @author Irma Fernanda Alay�n Perilla
	 * @date 26/03/2012
	 * @compa�ia Quality Data S.A Met�do que se encarga de mostrar el men�
	 *           desplegable.
	 * 
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
