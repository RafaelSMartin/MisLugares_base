package com.example.mislugares.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mislugares.model.Lugar;
import com.example.mislugares.R;
import com.example.mislugares.fragment.SelectorFragment;
import com.example.mislugares.model.TipoLugar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EdicionLugarActivity extends AppCompatActivity {
    private long id;
    //private long _id;
    private String _id;
    private Lugar lugar;
    private EditText nombre;
    private Spinner tipo;
    private EditText direccion;
    private EditText telefono;
    private EditText url;
    private EditText comentario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edicion_lugar);
        Bundle extras = getIntent().getExtras();
        id = extras.getLong("id", -1);
        _id = extras.getString("_id", null);
        if (_id!=null) {
            lugar = new Lugar();
            FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
            lugar.setCreador(usuario.getUid());
        } else {
            lugar = SelectorFragment.getAdaptador().getItem((int) id);
            _id= SelectorFragment.getAdaptador().getKey((int) id);
        }
        nombre = (EditText) findViewById(R.id.nombre);
        nombre.setText(lugar.getNombre());
        direccion = (EditText) findViewById(R.id.direccion);
        direccion.setText(lugar.getDireccion());
        telefono = (EditText) findViewById(R.id.telefono);
        telefono.setText(Integer.toString(lugar.getTelefono()));
        url = (EditText) findViewById(R.id.url);
        url.setText(lugar.getUrl());
        comentario = (EditText) findViewById(R.id.comentario);
        comentario.setText(lugar.getComentario());
        tipo = (Spinner) findViewById(R.id.tipo);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, TipoLugar.getNombres());
        adaptador.setDropDownViewResource(android.R.layout.
                simple_spinner_dropdown_item);
        tipo.setAdapter(adaptador);
        tipo.setSelection(lugar.getTipoEnum().ordinal());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edicion_lugar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_cancelar:
                if(getIntent().getExtras().getBoolean("nuevo", false)) {
                    MainActivity.lugares.borrar(_id);
                }
                finish();
                return true;
            case R.id.accion_guardar:
                lugar.setNombre(nombre.getText().toString());
                lugar.setTipoEnum(TipoLugar.values()[tipo.getSelectedItemPosition()]);
                lugar.setDireccion(direccion.getText().toString());
                lugar.setTelefono(Integer.parseInt(telefono.getText().toString()));
                lugar.setUrl(url.getText().toString());
                lugar.setComentario(comentario.getText().toString());
                MainActivity.lugares.actualiza(_id,lugar);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
