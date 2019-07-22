package nicolasmederoc.ethcyptomkt.comunicacion_api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InterfaceApi {
    @GET("v1/ticker?market=ETHARS")
    Call<Ethars> getAll();

    @GET("miner/{ETH-Account}/currentStats")
    Call<Pool> getAllPool();
}