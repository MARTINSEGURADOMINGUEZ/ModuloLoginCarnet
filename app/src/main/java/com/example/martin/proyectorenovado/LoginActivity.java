package com.example.martin.proyectorenovado;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class LoginActivity extends Activity {

    EditText edt1 , edt2;
    private Button btn1;

    ArrayList apellido = new ArrayList();
    ArrayList nombre = new ArrayList();
    ArrayList codigo = new ArrayList();
    ArrayList passwordJ = new ArrayList();
    ArrayList estado = new ArrayList();

    public static final String ESTADOPASS ="1";
    public static final String ESTADODENIED ="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt1 = (EditText)findViewById(R.id.edt1);
        edt2 = (EditText)findViewById(R.id.edt2);
        btn1 = (Button)findViewById(R.id.btn1);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usuario = String.valueOf(edt1.getText().toString());
                String password = String.valueOf(edt2.getText().toString());

                if(!usuario.equals("")&& !password.equals("")){

                    estado.clear();
                    nombre.clear();
                    apellido.clear();
                    codigo.clear();
                    passwordJ.clear();

                    AsyncHttpClient client = new AsyncHttpClient();
                    String url = "http://examenfinal2016.esy.es/ModuloCafeteriaSimple/ScriptLogin.php";


                    RequestParams requestParams = new RequestParams();
                    requestParams.add("Usuario",usuario.toString());
                    requestParams.add("Password",password.toString());

                    RequestHandle post = client.post(url, requestParams, new AsyncHttpResponseHandler() {


                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                            if (statusCode == 200) {

                                try {

                                    JSONArray jsonArray = new JSONArray(new String(responseBody));
                                    //JSONObject o = new JSONObject(new String(responseBody));

                                    for(int i=0; i<jsonArray.length();i++){

                                        estado.add(jsonArray.getJSONObject(i).get("Estado"));

                                    }

                                    String estadoM = estado.get(0).toString();

                                    //SIEMPRE DEBUGGER PARA VER EN QUE PARTE DEL CODIGO SE CAE EL APLICATIVO
                                    //Toast.makeText(getApplicationContext(),""+estadoM,Toast.LENGTH_LONG).show();

                                    if (String.valueOf(estadoM).equals(ESTADOPASS)){

                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            apellido.add(jsonArray.getJSONObject(i).get("Apellido"));
                                            nombre.add(jsonArray.getJSONObject(i).get("Nombre"));
                                            codigo.add(jsonArray.getJSONObject(i).get("Usuario"));
                                            passwordJ.add(jsonArray.getJSONObject(i).get("Password"));

                                        }

                                        String Apellido = apellido.get(0).toString().toUpperCase();
                                        String Nombre = nombre.get(0).toString();
                                        String CodigoI = codigo.get(0).toString();
                                        String PasswordI = passwordJ.get(0).toString();

                                        Intent intent = new Intent(LoginActivity.this, SuccessActivity.class);
                                        intent.putExtra("Apellido", String.valueOf(Apellido));
                                        intent.putExtra("Nombre", String.valueOf(Nombre));
                                        intent.putExtra("Usuario", String.valueOf(CodigoI));
                                        intent.putExtra("Password", String.valueOf(PasswordI));

                                        startActivity(intent);

                                    }else
                                    if (String.valueOf(estadoM).equals(ESTADODENIED)){

                                        //Log.d("VALOR_estadoM",estadoM.toString());

                                        //Toast.makeText(getApplicationContext(),""+estadoM,Toast.LENGTH_LONG).show();

                                        Alerta1("CREDENCIALES INCORRECTAS, PLEASE AGAIN!!!");

                                    }else{

                                        Alerta1("Algo extraño esta sucediendo chico .... :V");

                                    }

                                } catch (JSONException e) {

                                    e.printStackTrace();

                                }


                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            Alerta2("Error Conexión a red...");

                        }
                    });

                }else {

                    Alerta1("INGRESE CODIGO Y PASSWORD PLEASE!!!");

                }

            }
        });

    }

    public void Alerta1(String Mensaje){

        edt1.setText("");
        edt2.setText("");

        Crouton.makeText(this,""+Mensaje.toString().toUpperCase(),Style.ALERT).show();

    }

    public void Alerta2(String Mensaje2){

        edt1.setText("");
        edt2.setText("");

        Crouton.makeText(this,""+Mensaje2.toString().toUpperCase(),Style.INFO).show();

    }

    public void Alerta3(String Mensaje3){

        edt1.setText("");
        edt2.setText("");

        Crouton.makeText(this,Mensaje3.toString().toUpperCase(),Style.CONFIRM).show();

    }

}
