package nicolasmederoc.ethcyptomkt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    private TextView compra;
    private TextView venta,ethMes,pesosMes,reportedH,currentH,averageH,workersN;
    private Button recargar;
    private Datum datos = new Datum();
    private ProgressBar progresoCompra;
    private Comunicacion_Pool comunicacion_pool = new Comunicacion_Pool();

    private static final String baseUrl = "https://api.cryptomkt.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        compra = findViewById(R.id.compra);
            compra.setText("");
        venta = findViewById(R.id.venta);
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

        recargar = findViewById(R.id.recargar);
        recargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comunicar();
            }
        });

        comunicar();
    }

    /** Comunicacion con la API */
    private void comunicar(){
        progresoCompra.setVisibility(View.VISIBLE);
        compra.setText("");
        venta.setText("");

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
            mEthars = response.body();
            datos = mEthars.getData().get(0);
            Log.d("comunicar"," Se pudo comunicar"+datos.getBid());

            progresoCompra.setVisibility(View.GONE);
            compra.setText(datos.getAsk());
            venta.setText(datos.getBid());
            enviarInfo();

        } else {
            progresoCompra.setVisibility(View.GONE);
            compra.setText("-");
            venta.setText("-");
            System.out.println(response.errorBody());
        }
    }

    @Override  //Acá van las fallas.
    public void onFailure(Call<Ethars> call, Throwable t) {
        progresoCompra.setVisibility(View.GONE);
        compra.setText("-");
        venta.setText("-");

        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
        Log.d("comunicar"," NO Se pudo comunicar"+datos.getBid());
    }

    public void enviarInfo(){
        comunicacion_pool.obtenerInfo(
                datos.getBid(),
                ethMes,
                pesosMes,
                reportedH,
                currentH,
                averageH,
                workersN,
                getApplicationContext());
    }

    /**Acción del botón que te lleva a la página de CryptoMKT*/
    public void cryptoMKT (View view){
        Uri uri = Uri.parse("http://www.cryptomkt.com/es");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}