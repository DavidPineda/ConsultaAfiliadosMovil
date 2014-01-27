/**
 * @nameProject Integra ARS Movil
 * @nameClass Consulta
 * @author Irma Fernanda Alayon 
 * @version 1.0
 * @date 26/01/2012
 */

package co.com.qdata;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import integra.auditoriapre.movil.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Consulta extends Activity {

	/**
	 * M�todo que activa la ventana de la actidad (clase).
	 * 
	 * @author Irma Fernanda Alay�n
	 * @date 26/01/2012
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consulta);

		/**
		 * Declaracion de la variable que elmacena el valor de btatras, para que
		 * al darle clic en este boton retorne al usuario a la ventana de Login
		 */
	}
	
	@Override
	   public boolean onCreateOptionsMenu(Menu menu) {
	       MenuInflater inflater = getMenuInflater();
	       inflater.inflate(R.menu.ejemplo_menu, menu);
	       return true;
	   }

	/**
	 * M�todo que envia los parametros al servico web de Consulta Online
	 * Afiliados, para obener los datos de la persona que corresponde al
	 * Documento o Codigo Interno Digitado
	 * 
	 * @author Irma Fernanda Alay�n
	 * @date 26/01/2012
	 * @param v
	 */
	public void ConsultaAfiliado(View v) {
		String NameSpace = "http://tempuri.org/ConsultaAfiliadosOnLine/Service1";
		String SoapAction = "http://tempuri.org/ConsultaAfiliadosOnLine/Service1/Consultar_OnLine_Movil";
		String Method = "Consultar_OnLine_Movil";
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
			builder.setMessage(
					("ERROR: de Lectura" + e.getClass().getName() + ": " + e.getMessage()))

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
		 * ruta : Integra ARS Movil/ res/ layout/ consulta.
		 */
		TextView documento = (TextView) findViewById(R.id.indoc);
		TextView codigo = (TextView) findViewById(R.id.indato);
		String code_value = codigo.getText().toString();
		String document_value = documento.getText().toString();
		barraProgreso();
		/**
		 * Se v�lida que por lo menos uno de los dos campos deben contener
		 * informaci�n al momento de hacer la peticion del servicio web
		 */
		if ((code_value == null || code_value.length() == 0)
				&& (document_value == null || document_value.length() == 0)) {

			// se crea un alertDiallog advirtiendo que no se estan enviado datos
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("El dato no es v�lido")
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

			return;
		} 
	else {
			/**
			 * Se envian los datos que son tomados por el servico web , los
			 * cules se reciben por los parametros del servicio para que este
			 * realice la consulta.
			 * 
			 * Se crean 2 variables, cada una corresponde a uno de los parametrs
			 * que espera el servicion web
			 */
			SoapObject request = new SoapObject(NameSpace, Method);

			PropertyInfo CodigoAfi = new PropertyInfo();
			PropertyInfo DocumentoAfi = new PropertyInfo();

			DocumentoAfi.setName("strDocumento");
			DocumentoAfi.setValue(document_value);
			DocumentoAfi.setType(String.class);

			CodigoAfi.setName("strCodigoInterno");
			CodigoAfi.setValue(code_value);
			CodigoAfi.setType(String.class);

			request.addProperty(DocumentoAfi);
			request.addProperty(CodigoAfi);

			/**
			 * Declaracion de la variable en la cual se van a enviar los datos
			 * al servicio web.
			 */
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			/**
			 * Declaracion de la variable que transporta los datos al servicio
			 * web, y trae la respuesta del mismo
			 */
			HttpTransportSE androidHttpTransport = new HttpTransportSE(Url);
			System.out.print(Url);

			try {
				androidHttpTransport.call(SoapAction, envelope);
				/**
				 * Declaraci�n de la variable que gurada lo informacion que se
				 * obtiene del servicio web.
				 */
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

				/**
				 * AlertDialog que muestra la informacion de la consulta.
				 */

					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage(response.toString())
							.setCancelable(false)
							.setNegativeButton("Aceptar",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					AlertDialog alert = builder.create();
					alert.show();
			}
			/**
			 * Muestra si se presento algun error y sus detalles, en un
			 * alertDialog
			 */
			catch (Exception e) {
				//barraProgreso();
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(
						("ERROR: No existe un usuario con el dato especificado " ))

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
	}
	
	/**
	 * M�todo que crea y activa la barra de progreso
	 * 
	 * @author Irma Fernanda Alayon
	 * @date 27/01/2012
	 */
	public void barraProgreso(){
		/**
		 * se ingresa el mensaje que va aparecer en la barra.
		 */
		final ProgressDialog dialog = ProgressDialog.show(this, "Buscando",
				"Sus Datos", true);
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
		/**
		 * Se activa la barra de progreso
		 */
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
}