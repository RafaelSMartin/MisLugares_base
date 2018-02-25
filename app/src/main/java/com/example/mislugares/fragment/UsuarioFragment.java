package com.example.mislugares.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.mislugares.R;
import com.example.mislugares.firebase.LoginActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Rafael S Martin on 25/02/2018.
 */

public class UsuarioFragment extends Fragment {

    private ImageLoader lectorImagenes;

    @Override
    public View onCreateView(LayoutInflater inflador, ViewGroup contenedor, Bundle savedInstanceState) {
        View vista = inflador.inflate(R.layout.fragment_usuario, contenedor, false);

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        TextView nombre = (TextView) vista.findViewById(R.id.nombre);
        TextView correo = (TextView) vista.findViewById(R.id.correo);
        TextView proveedor = (TextView) vista.findViewById(R.id.proveedor);
        TextView telefono = (TextView) vista.findViewById(R.id.telefono);
        TextView uid = (TextView) vista.findViewById(R.id.uid);

        if (usuario != null) {
            nombre.setText("Nombre de Usuario: " + usuario.getDisplayName());
            correo.setText("Correo: " + usuario.getEmail());
            proveedor.setText("Proveedor: " + usuario.getProviders().toString());
            telefono.setText("Telefono: " + usuario.getPhoneNumber());
            uid.setText("uid: " + usuario.getUid());
        }

        //Deslogueo
        Button cerrarSesion = (Button) vista.findViewById(R.id.btn_cerrar_sesion);
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(getActivity())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                goToLoginActivity();
                            }
                        });
            }
        });

        //Carga de imagen con Volley
        loadVolley();

        Uri urlImagen = usuario.getPhotoUrl();
        if(urlImagen != null){
            NetworkImageView fotoUsuario = (NetworkImageView) vista.findViewById(R.id.imagen);
            fotoUsuario.setImageUrl(urlImagen.toString(), lectorImagenes);
        }


        return vista;
    }

    private void goToLoginActivity(){
        Intent i = new Intent(getActivity(), LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        getActivity().finish();
    }

    private void loadVolley(){
        RequestQueue colaPeticiones = Volley.newRequestQueue(getActivity().getApplicationContext());
        lectorImagenes = new ImageLoader(colaPeticiones, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(10);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

}
