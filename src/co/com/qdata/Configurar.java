/**
 * @nameProject Auditoria Presencial Movil
 * @nameClass Configurar
 * @author Irma Fernanda Alayon 
 * @version 1.0
 * @date 27/03/2012
 */

package co.com.qdata;

import integra.auditoriapre.movil.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import co.com.qdata.Persistencia.DBManaged;

public class Configurar extends Activity implements OnClickListener{

	private Button btn_guardar;
	private TextView url;
	private static final String TABLA = "create table if not exists "
			 						  + "url_file "
		 						  	  + "("
		 						  	  + "ID INT PRIMARY KEY , "
		 						  	  + "URL TEXT "
		 						  	  + ")";
	private static final String DB_NAME = "QDATA_MOVIL";
	private static final int VERSION = 1;
	
	/**
	 * @author Irma Fernanda Alayon
	 * @date 27/03/2012
	 * Metodo que activa la ventana de la actividad
	 * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configurar);
		btn_guardar = (Button) findViewById(R.id.btguardar);
		btn_guardar.setOnClickListener(this);
		url = (TextView) findViewById(R.id.urlusuer);
		leerDatos();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.ejemplo_menu, menu);
		return true;
	}

	/**
	 * @author Irma Fernanda Alayon
	 * @date 27/03/2012
	 * Metodo que identifica la ruta en la cual puede almacenar el archivo que
	 * se va a crear para almacenar la url, que digite el usuario y crea el
	 * archivo.
	 * 
	 * @param v
	 */
	public void AlmacenarURL() {

		
		String url_value = url.getText().toString();
			
		Pattern pat = Pattern.compile("r");
		Matcher mat = pat.matcher(url_value);

		if (url_value == "") {
			Toast.makeText(getBaseContext(),
					"Ingrese la URL",
					Toast.LENGTH_LONG).show();
		}else if(!mat.matches()){
			Toast.makeText(getBaseContext(),
					"La URL contiene caracteres invalidos",
					Toast.LENGTH_LONG).show();			
		}
		else {

				try {
					
					DBManaged database = new DBManaged(getApplicationContext(), DB_NAME, VERSION, TABLA);
					database.insertarUrl(1, url_value);
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage(("URL registrada correctamente"))
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

				} catch (Exception ex) {
					Toast.makeText(getBaseContext(),
							"Se hapresentado un error, Favor intente mas tarde",
							Toast.LENGTH_LONG).show();
				}	
		}
	}

	/**
	 * @author Irma Fernanda Alayon Perilla
	 * @date 27/03/2012
	 * Mmetodo ue lee el archivo en el cual esta la url de caonexion para validar el Loguin de usuario.
	 * @param v
	 */
	public void leerDatos() {
		
		try{
			DBManaged database = new DBManaged(getApplicationContext(), DB_NAME, VERSION, TABLA);
			String url_guardada = database.recuperarURL("select URL from url_file where ID = 1");
			if(!(url_guardada == "")){
				url.setText(url_guardada);
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(("No se ha ingresado ninguna URL"))
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
		} catch (Exception ex) {
			Toast.makeText(getBaseContext(),
					"Se hapresentado un error, Favor intente mas tarde",
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * @author Irma Fernanda Alayon Perilla
	 * @date 27/03/2012
	 * @compania Quality Data S.A Metodo que se encarga de mostrar el menu
	 *           desplegable.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.login:
			Intent intent = new Intent(Configurar.this, Login.class);
			startActivity(intent);
			return true;
		case R.id.configurar:
			Intent intento = new Intent(Configurar.this, Configurar.class);
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

	@Override
	public void onClick(View v) {
		if(v.getId() == btn_guardar.getId()){
			AlmacenarURL();
		}
	}
}