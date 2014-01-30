package co.com.qdata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.co.qdata.consultaOnline.R;

public class Main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.menu);
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.ejemplo_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.login:
			Intent intent = new Intent(Main.this,
					Login.class);
			startActivity(intent);
			return true;

		case R.id.configurar:
			Intent intento = new Intent(Main.this,
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