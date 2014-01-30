/**
 * @nameProject Auditpooria Presencial Movil
 * @nameClass Configurar
 * @author Irma Fernanda Alayon 
 * @version 1.0
 * @date 27/03/2012
 */

package co.com.qdata;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import co.com.qdata.Persistencia.DBManaged;

import com.co.qdata.consultaOnline.R;

public class Configurar extends Activity implements OnClickListener{

	private Button btn_guardar;
	private TextView url;
	private static final String TABLA = "create table if not exists "
			 						  + "url_file "
		 						  	  + "("
		 						  	  + "ID INT PRIMARY KEY, "
		 						  	  + "URL TEXT "
		 						  	  + ")";
	private static final String DB_NAME = "QDATA_MOVIL";
	private static final int VERSION = 1;
	
	/**
	 * @author Irma Fernanda Alayon
	 * @date 27/03/2012
	 * Metodo que activa la ventana de la actividad
	 * */
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

	public void AlmacenarURL() {
	
		String url_value = url.getText().toString();
			
		Pattern pat = Pattern.compile("^(http://)([a-zA-Z0-9\\.]+/)+[a-zA-Z-0-9]+(\\.asmx)$");
		Matcher mat = pat.matcher(url_value);

		if (url_value == "") {
			mostrarMensaje("Ingrese la URL");
		}else if(!mat.matches()){
			mostrarMensaje("la url ingresada no es válida");		
		}
		else {
			try {
				DBManaged database = new DBManaged(getApplicationContext(), DB_NAME, VERSION, TABLA);
				String url_guardada = database.recuperarURL("select URL from url_file where ID = 1");
				if(url_guardada == ""){
					database.insertarUrl(1, url_value, "url_file");
				}
				else{
					database.modificarUrl(1, url_value, "url_file");
				}
				mostrarMensaje("URL registrada correctamente");				
			} catch (SQLiteException ex) {
				mostrarMensaje("Se hapresentado un error, Favor intente mas tarde");
			}	
		}
	}

	
	public void leerDatos() {
		
		try{
			SQLiteDatabase db = SQLiteDatabase.openDatabase("data/data/com.co.qdata.consultaOnline/databases/QDATA_MOVIL", null, SQLiteDatabase.OPEN_READONLY);
			if(db != null){
				if(DBManaged.existeTabla(db, "select URL from url_file where ID = 1")){
					String url_guardada = DBManaged.recuperarURL(db, "select URL from url_file where ID = 1");
					if(!(url_guardada == "")){
						url.setText(url_guardada);
					}else{
						mostrarMensaje("No se ha ingresado ninguna URL");
					}
				}else{
					mostrarMensaje("No se ha ingresado ninguna URL");
				}
			}else{
				mostrarMensaje("No se ha ingresado ninguna URL");				
			}
		}catch (SQLiteCantOpenDatabaseException ex) {
				mostrarMensaje("No se ha ingresado ninguna URL");	
		}catch (SQLiteException ex) {
				mostrarMensaje("Se hapresentado un error, Favor intente mas tarde");
		}
		catch (Exception e) {
			mostrarMensaje("Se hapresentado un error, Favor intente mas tarde");
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
}