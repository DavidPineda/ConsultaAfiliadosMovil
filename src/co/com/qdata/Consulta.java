/**
 * @nameProject Integra ARS Movil
 * @nameClass Consulta
 * @author Irma Fernanda Alayon 
 * @version 1.0
 * @date 26/01/2012
 */

package co.com.qdata;

import integra.auditoriapre.movil.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapPrimitive;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import co.com.qdata.Persistencia.DBManaged;
import co.com.qdata.llamaServicio.LlamaServicio;
import co.com.qdata.usuario.UsuarioConsultado;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

public class Consulta extends Activity implements OnClickListener{

	private TextView codInterno, docAfiliado;
	private Button btnCodInterno, btnNumDocumento; 
	/**
	 * Metodo que activa la ventana de la actidad (clase).
	 * 
	 * @author Irma Fernanda Alayon
	 * @date 26/01/2012
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consulta);
		
		codInterno = (TextView) findViewById(R.id.contrato_afil);
		docAfiliado = (TextView) findViewById(R.id.numero_doc);
		btnCodInterno = (Button) findViewById(R.id.btcodigo);
		btnNumDocumento = (Button) findViewById(R.id.btdocumento);
		btnCodInterno.setOnClickListener(this);
		btnNumDocumento.setOnClickListener(this);
		
	}
	
	@Override
	   public boolean onCreateOptionsMenu(Menu menu) {
	       MenuInflater inflater = getMenuInflater();
	       inflater.inflate(R.menu.ejemplo_menu, menu);
	       return true;
	   }

	/**
	 * Metodo que envia los parametros al servico web de Consulta Online
	 * Afiliados, para obener los datos de la persona que corresponde al
	 * Documento o Codigo Interno Digitado
	 * 
	 * @author Irma Fernanda Alayï¿½n
	 * @date 26/01/2012
	 * @param v
	 */
	public void ConsultaAfiliado(View v) {
		
		if(v.getId() == btnCodInterno.getId()){
			if(!validarCodinterno())
				mostrarMensaje("Código Interno incorrecto");
			else
				llamarServicio(this.codInterno.getText().toString(), "");
		}else if(v.getId() == btnNumDocumento.getId()){
			if(!validarNumeroDoc())
				mostrarMensaje("Número de documento incorrecto");
			else 
				llamarServicio("", this.docAfiliado.getText().toString());
		}
	}
		
	private void llamarServicio(String codInterno, String numAfiliado){
		
		String NameSpace = "http://tempuri.org/ConsultaAfiliadosOnLine/Service1";
		String SoapAction = "http://tempuri.org/ConsultaAfiliadosOnLine/Service1/consultaAfiliadoMovil";
		String Method = "consultaAfiliadoMovil";
		String url = "";
		PropertyInfo[] propiedades = new PropertyInfo[1];
		PropertyInfo propiedad = new PropertyInfo();
		//Se inicia la barra de progreso
		barraProgreso();
		
		try{

			SQLiteDatabase db = SQLiteDatabase.openDatabase("data/data/integra.auditoriapre.movil/databases/QDATA_MOVIL", null, SQLiteDatabase.OPEN_READONLY);
			url = DBManaged.recuperarURL(db, "select URL from url_file where ID = 1");
			if(!url.equals("")){
				if(!codInterno.equals("")){
					propiedad.setName("codigo_interno");
					propiedad.setValue(codInterno);
					propiedad.setType(String.class);
				}else{
					propiedad.setName("numero_documento");
					propiedad.setValue(numAfiliado);
					propiedad.setType(String.class);					
				}
				propiedades[0] = propiedad;
				LlamaServicio servicio = new LlamaServicio(NameSpace, SoapAction, Method, url);
				SoapPrimitive response = servicio.llamaServicioPrimitive(propiedades);
				if(!(response == null) && !response.toString().equals("")){
					if(Integer.parseInt(response.toString()) == 1 || Integer.parseInt(response.toString()) == 0){
						mostrarMensaje("Ocurrio un error en el Web Service, Favor intentar mas tarde");
					}else{
						mostrarDatos(response);
					}
				}
			}
			else
				mostrarMensaje("Por favor configure la URL del servico Web");
		}catch(JSONException ex){
			mostrarMensaje("Se preseneto un error al realizar la consulta de afiliado");
		}
		catch (Exception ex) {
			mostrarMensaje("Se preseneto un error al realizar la consulta de afiliado");
		}
		
	}
	
	private void mostrarDatos(SoapPrimitive response) throws JSONException{
		UsuarioConsultado user = crearLista(response.toString());
		mostrarMensaje(user.toString());
	}
	
	private UsuarioConsultado crearLista(String strJSON) throws JsonIOException{
		Gson gson = new Gson();
		UsuarioConsultado user = new UsuarioConsultado();
		user = gson.fromJson(strJSON, UsuarioConsultado.class);
		return user;
	}
				
	private boolean validarCodinterno(){
		if(this.codInterno.getText().toString().equals("")){
			return false;
		}
		return validarNumero(this.codInterno.getText().toString());
	}
	
	private boolean validarNumeroDoc(){
		if(this.docAfiliado.getText().toString().equals("")){
			return false;
		}
		return validarNumero(this.docAfiliado.getText().toString());
	}
	
	private boolean validarNumero(String numero){
		Pattern pat = Pattern.compile("[0-9]+");
		Matcher mat = pat.matcher(numero);
		return mat.matches();		
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
	
	/**
	 * Metodo que crea y activa la barra de progreso
	 * @author Irma Fernanda Alayon
	 * @date 27/01/2012
	 */
	public void barraProgreso(){
		/**
		 * se ingresa el mensaje que va aparecer en la barra.
		 */
		final ProgressDialog dialog = ProgressDialog.show(this, "Buscando", "Sus Datos", true);
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				dialog.dismiss();
			}
		};
		
		Thread checkUpdate = new Thread() {
			public void run() {
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0);
			}
		};
		checkUpdate.start();
	}
	
	 @Override
	   public boolean onOptionsItemSelected(MenuItem item) {
	      switch (item.getItemId()) {
	      case R.id.login:
	    	  Intent intent = new Intent(Consulta.this,
						Login.class);
				startActivity(intent);
	         return true;
	         
	      case R.id.configurar:
	    	  Intent intento = new Intent (Consulta.this, Configurar.class);
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
		ConsultaAfiliado(v);
	}
}