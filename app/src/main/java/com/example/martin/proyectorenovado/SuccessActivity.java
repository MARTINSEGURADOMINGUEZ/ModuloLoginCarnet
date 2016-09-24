package com.example.martin.proyectorenovado;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.snowdream.android.widget.SmartImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class SuccessActivity extends Activity {


    private ListView lvw;

    ArrayList codigo = new ArrayList();
    ArrayList dni = new ArrayList();
    ArrayList apellido = new ArrayList();
    ArrayList nombre = new ArrayList();
    ArrayList facultad = new ArrayList();
    ArrayList carrera = new ArrayList();
    ArrayList foto = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        Intent intentrecibido = getIntent();

        String StrinNombre = intentrecibido.getStringExtra("Nombre");
        String StrinApellido = intentrecibido.getStringExtra("Apellido");
        //revisar que lleguen estos 2 datos
        String StringUsuario = intentrecibido.getStringExtra("Usuario");
        String StringPassword = intentrecibido.getStringExtra("Password");
        //String StringEstado = intentrecibido.getStringExtra("Estado");


       Crouton.makeText(this,"Bienvenido : "+StrinApellido+" ,"+StrinNombre, Style.CONFIRM).show();


            //Este error de NullPointer Exception me tuvo loco por mas de 1 HORA xD
            //Siempre inicializar variables y tambien los escuchadores.
            lvw = (ListView)findViewById(R.id.lvw1);

            CargarCarnet(String.valueOf(StringUsuario),String.valueOf(StringPassword));

        }

    public void CargarCarnet( String usuario , String password){

        codigo.clear();
        dni.clear();
        apellido.clear();
        nombre.clear();
        facultad.clear();
        carrera.clear();
        foto.clear();

        final ProgressDialog progress = new ProgressDialog(SuccessActivity.this);
        progress.setMessage("Cargando Datos...");
        progress.show();


        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://examenfinal2016.esy.es/ModuloCafeteriaSimple/ScriptCarnet.php";

        RequestParams requestParams = new RequestParams();
        requestParams.add("usuario",usuario.toString());
        requestParams.add("password",password.toString());


        RequestHandle post = client.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if(statusCode==200){

                    progress.dismiss();

                    try {

                        JSONArray jsonArray = new JSONArray(new String(responseBody));

                        for(int i=0; i<jsonArray.length();i++){

                            codigo.add(jsonArray.getJSONObject(i).getString("Codigo"));
                            dni.add(jsonArray.getJSONObject(i).getString("Dni"));
                            apellido.add(jsonArray.getJSONObject(i).getString("Apellido"));
                            nombre.add(jsonArray.getJSONObject(i).getString("Nombre"));
                            facultad.add(jsonArray.getJSONObject(i).getString("Facultad"));
                            carrera.add(jsonArray.getJSONObject(i).getString("Carrera"));
                            foto.add(jsonArray.getJSONObject(i).getString("Foto"));

                        }


                        lvw.setAdapter(new ImagenAdapter(getApplicationContext()));

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Toast.makeText(getApplicationContext(),"NO CONEXIÓN DE RED... ",Toast.LENGTH_LONG).show();

            }
        });

    }

    private class ImagenAdapter extends BaseAdapter {

        Context context;
        LayoutInflater layoutInflater;
        SmartImageView smartImageView;
        TextView txtCodigo, txtDni, txtApellido, txtNombre, txtFacultad , txtCarrera;

        public ImagenAdapter(Context applicationContext) {

            this.context = applicationContext;
            layoutInflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return foto.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewGroup viewGroup = (ViewGroup)layoutInflater.inflate(R.layout.activity_main_item2,null);
            smartImageView = (SmartImageView)viewGroup.findViewById(R.id.imagen1);
            txtCodigo = (TextView)viewGroup.findViewById(R.id.txt1);
            txtDni = (TextView)viewGroup.findViewById(R.id.txt2);
            txtApellido = (TextView)viewGroup.findViewById(R.id.txt3);
            txtNombre = (TextView)viewGroup.findViewById(R.id.txt4);
            txtFacultad = (TextView)viewGroup.findViewById(R.id.txt5);
            txtCarrera = (TextView)viewGroup.findViewById(R.id.txt6);

            String urlFinal="http://examenfinal2016.esy.es/ModuloCafeteriaSimple/imagenes/"+foto.get(position).toString();
            Rect rect = new Rect(smartImageView.getLeft(),smartImageView.getTop(),smartImageView.getRight(),smartImageView.getBottom());

            smartImageView.setImageUrl(urlFinal,rect);

            txtCodigo.setText(" \n  CODIGO: 000"+codigo.get(position).toString());
            txtCodigo.setTextColor(getResources().getColor(R.color.negro));
            txtDni.setText(" \n  DNI: "+dni.get(position).toString());
            txtDni.setTextColor(getResources().getColor(R.color.negro));
            txtApellido.setText(" \n  APELLIDOS: "+apellido.get(position).toString());
            txtApellido.setTextColor(getResources().getColor(R.color.negro));
            txtNombre.setText(" \n  NOMBRES: "+nombre.get(position).toString());
            txtNombre.setTextColor(getResources().getColor(R.color.negro));
            txtFacultad.setText(" \n  FACULTAD: "+facultad.get(position).toString());
            txtFacultad.setTextColor(getResources().getColor(R.color.negro));
            txtCarrera.setText(" \n  CARRERA: "+carrera.get(position).toString());
            txtCarrera.setTextColor(getResources().getColor(R.color.negro));

            return viewGroup;
        }
    }
}
