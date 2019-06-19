package nicolasmederoc.ethcyptomkt.comunicacion_api;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class Comunicacion_Pool extends AppCompatActivity implements Callback<Pool> {

    public TextView ethMes,pesosMes,reportedH,currentH,averageH,workersN;

    private final String baseUrlPool = "https://api.ethermine.org/";
    public PoolData poolData = new PoolData();
    private Boolean errPool=true;
    public float valorVenta = 0;



    public void valorVenta(String a){
        if (a==null) {
            valorVenta = 0;
        } else {
            valorVenta = Float.valueOf(a);
        }
    }

    public void comunicarPool(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(baseUrlPool)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        InterfaceApi interfaceApi = retrofit.create(InterfaceApi.class);
        Call<Pool> call = interfaceApi.getAllPool();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Pool> call, Response<Pool> response) {
        Pool pool;
        if(response.isSuccessful()){
            errPool = false;
            pool = response.body();
            poolData = pool.getData();
        } else {
            Log.d("Comunicacion_Pool", " Unsuccessful");
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<Pool> call, Throwable t) {
        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        errPool = true;
    }

    public void obtenerInfo(String venta,TextView a, TextView b, TextView c, TextView d, TextView e,
                            TextView f){
        valorVenta(venta);
        ethMes = a;
        pesosMes = b;
        reportedH = c;
        currentH = d;
        averageH = e;
        workersN = f;

        new HiloComunicar().execute();
    }

    class HiloComunicar extends AsyncTask<Boolean,Boolean,Void>{

        @Override
        protected Void doInBackground(Boolean... booleans) {
            try{
                comunicarPool();
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        protected void onPreExecute(){}

        @Override
        protected void onPostExecute(Void aVoid) {
            if (!errPool){
                ethMes.setText(String.valueOf(poolData.getCoinsPerMonth()));
                pesosMes.setText(
                        String.valueOf(
                                poolData.getCoinsPerMonth()*valorVenta
                        )
                );
                reportedH.setText(String.valueOf(poolData.getReported()));
                currentH.setText(String.valueOf(poolData.getCurrentHashrate()));
                averageH.setText(String.valueOf(poolData.getAverage()));
                workersN.setText(poolData.getWorkers());
            } else {
                //this.cancel(true);
            }
        }
    }
}
