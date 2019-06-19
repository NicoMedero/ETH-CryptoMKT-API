package nicolasmederoc.ethcyptomkt;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nicolasmederoc.ethcyptomkt.comunicacion_api.Datum;
import nicolasmederoc.ethcyptomkt.comunicacion_api.Ethars;
import nicolasmederoc.ethcyptomkt.comunicacion_api.InterfaceApi;
import nicolasmederoc.ethcyptomkt.comunicacion_api.Comunicacion_Pool;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity
        implements Callback<Ethars>{

    public TextView compra;
    public TextView venta,ethMes,pesosMes,reportedH,currentH,averageH,workersN;
    public String a="";
    public Boolean err=false;
    public Datum datos = new Datum();
    public ProgressBar progresoCompra;
    public ProgressBar progresoVenta;
    public Comunicacion_Pool comunicacion_pool = new Comunicacion_Pool();

    private final String baseUrl = "https://api.cryptomkt.com/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compra = findViewById(R.id.compra);
            compra.setText("");
        venta = findViewById(R.id.textView4);
            venta.setText("");
        ethMes = findViewById(R.id.ethmes);
            ethMes.setText("");
        pesosMes = findViewById(R.id.pesosmes);
            pesosMes.setText("");
        reportedH = findViewById(R.id.reported);
            reportedH.setText("");
        currentH = findViewById(R.id.current);
            currentH.setText("");
        averageH = findViewById(R.id.average);
            averageH.setText("");
        workersN = findViewById(R.id.workers);
            workersN.setText("");

        progresoCompra = findViewById(R.id.progresoCompra);
        progresoVenta = findViewById(R.id.progresoVenta);

        progresoVenta.setVisibility(View.GONE);
        progresoCompra.setVisibility(View.GONE);

        err = false;
    }

    public void recargar (View view) {
        new HiloComunicar().execute();
    }

    /** Comunicacion con la API */
    public void comunicar(){
        Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

        Retrofit retrofit = new Retrofit
                                .Builder()
                                .baseUrl(baseUrl)
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build();

        InterfaceApi interfaceApi = retrofit.create(InterfaceApi.class);
        Call<Ethars> call = interfaceApi.getAll();
        call.enqueue(this);
    }

    @Override

    public void onResponse(Call<Ethars> call, Response<Ethars> response) {
        Ethars mEthars;
        if(response.isSuccessful()){
            err = false;
            mEthars = response.body();
            datos = mEthars.getData().get(0);
            Log.d("comunicar"," Se pudo comunicar"+datos.getBid());
        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override  //Acá van las fallas.
    public void onFailure(Call<Ethars> call, Throwable t) {
        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
        Log.d("comunicar"," NO Se pudo comunicar"+datos.getBid());
        err = true;
    }

    public void enviarInfo(){
        comunicacion_pool.obtenerInfo(
                datos.getBid(),
                ethMes,
                pesosMes,
                reportedH,
                currentH,
                averageH,
                workersN);
    }

    /**Hilo para recargar, porque es una tarea Asíncrona.*/
    class HiloComunicar extends AsyncTask<Boolean,Boolean,Void>{

        @Override
        protected Void doInBackground(Boolean... booleans) {
            try{
                comunicar();
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPreExecute(){
            progresoCompra.setVisibility(View.VISIBLE);
            progresoVenta.setVisibility(View.VISIBLE);
            compra.setText("");
            venta.setText("");
        }

        protected void onPostExecute(Void v) {
            if (!err) {
                progresoCompra.setVisibility(View.GONE);
                progresoVenta.setVisibility(View.GONE);
                compra.setText(datos.getAsk());
                venta.setText(datos.getBid());
                enviarInfo();
            } else {
                progresoCompra.setVisibility(View.GONE);
                progresoVenta.setVisibility(View.GONE);
                compra.setText("-");
                venta.setText("-");
                //this.cancel(true);
            }
        }
    }

    /**Acción del botón que te lleva a la página de CryptoMKT*/
    public void cryptoMKT (View view){
        Uri uri = Uri.parse("http://www.cryptomkt.com/es");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}