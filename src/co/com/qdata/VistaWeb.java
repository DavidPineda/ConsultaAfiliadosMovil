package co.com.qdata;

import integra.auditoriapre.movil.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.view.Menu;
import android.view.MenuInflater;


public class VistaWeb extends Activity {
	WebView mWebView;
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.vistaweb);

	    mWebView = (WebView) findViewById(R.id.webview);
	    mWebView.getSettings().setJavaScriptEnabled(true);
	    mWebView.loadUrl("http://192.168.1.108:8888/INTEGRAARS/Prestacion/Auditoria_Presencial/PS_Auditoria_Presencial_movil.aspx");
	    //mWebView.loadUrl("http://201.245.120.62:8081/INTEGRAARS/Prestacion/Auditoria_Presencial/PS_Auditoria_Presencial_Movil.aspx");
	    //mWebView.loadUrl("http://201.245.120.62:8081/Integra2010/PRESTACION/AUDITORIA_PRESENCIAL/PS_AUDITORIA_PRESENCIAL.aspx");
	}
	
	@Override
	   public boolean onCreateOptionsMenu(Menu menu) {
	       MenuInflater inflater = getMenuInflater();
	       inflater.inflate(R.menu.ejemplo_menu, menu);
	       return true;
	   }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.login:
			Intent intent = new Intent(VistaWeb.this,
					Login.class);
			startActivity(intent);
			return true;

		case R.id.configurar:
			Intent intento = new Intent(VistaWeb.this,
					Configurar.class);
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
