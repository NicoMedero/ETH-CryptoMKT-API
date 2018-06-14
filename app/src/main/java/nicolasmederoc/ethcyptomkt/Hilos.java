package nicolasmederoc.ethcyptomkt;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nicolasmederoc.ethcyptomkt.comunicacion_api.Datum;
import nicolasmederoc.ethcyptomkt.comunicacion_api.Ethars;
import nicolasmederoc.ethcyptomkt.comunicacion_api.InterfaceApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Hilos{
    static Datum datos;

    public void asyncTask(){
        new Cveth().execute();
    }


    public static class Cveth extends AsyncTask<Boolean,Boolean,Void> implements Callback<Ethars> {

        Boolean err = false;

        @Override
        protected Void doInBackground(Boolean... booleans) {
            String baseUrl = "https://api.cryptomkt.com/";
            try{
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
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void onResponse(Call<Ethars> call, Response<Ethars> response) {
            Ethars mEthars;
            if(response.isSuccessful()){
                err = false;
                mEthars = response.body();
                datos = mEthars.getData().get(0);
            } else {
                System.out.println(response.errorBody());
            }
        }

        @Override
        public void onFailure(Call<Ethars> call, Throwable t) {
        }

        protected void onPreExecute(){
        }

        protected void onPostExecute(Void v){
        }

    }
}

