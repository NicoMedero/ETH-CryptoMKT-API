package nicolasmederoc.ethcyptomkt.comunicacion_api;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Comunicacion_Pool implements Callback<Pool> {

    private TextView ethMes,pesosMes,reportedH,currentH,averageH,workersN;

    private Context context;
    private float valorVenta = 0;

    private static final String baseUrlPool = "https://api.ethermine.org/";

    private void valorVenta(String a){
        if (a==null) {
            valorVenta = 0;
        } else {
            valorVenta = Float.valueOf(a);
        }
    }

    public void obtenerInfo(String venta, TextView a, TextView b, TextView c, TextView d, TextView e,
                            TextView f, Context appContext){
        valorVenta(venta);
        ethMes = a;
        pesosMes = b;
        reportedH = c;
        currentH = d;
        averageH = e;
        workersN = f;
        context = appContext;

        comunicarPool();
    }

    private void comunicarPool(){
        Gson gson_pool = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit_pool = new Retrofit
                .Builder()
                .baseUrl(baseUrlPool)
                .addConverterFactory(GsonConverterFactory.create(gson_pool))
                .build();

        InterfaceApi interfaceApi = retrofit_pool.create(InterfaceApi.class);
        Call<Pool> call = interfaceApi.getAllPool();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Pool> call, Response<Pool> response) {
        Pool pool;
        if(response.isSuccessful()){
            pool = response.body();
            PoolData poolData = pool.getData();

                ethMes.setText(String.valueOf(poolData.getCoinsPerMonth()));
                pesosMes.setText(
                        String.valueOf(
                                poolData.getCoinsPerMonth() * valorVenta
                        )
                );
                reportedH.setText(String.valueOf(poolData.getReported()));
                currentH.setText(String.valueOf(poolData.getCurrentHashrate()));
                averageH.setText(String.valueOf(poolData.getAverage()));
                workersN.setText(poolData.getWorkers());

        } else {
            Log.d("Comunicacion_Pool", " Unsuccessful");
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<Pool> call, Throwable t) {
        Toast.makeText(context, "Pool Error", Toast.LENGTH_SHORT).show();
    }
}
