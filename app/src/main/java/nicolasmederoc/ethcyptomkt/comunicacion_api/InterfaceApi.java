package nicolasmederoc.ethcyptomkt.comunicacion_api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InterfaceApi {
    @GET("v1/ticker?market=ETHARS")
    Call<Ethars> getAll();

    @GET("miner/0x19ccacdaf3b0856eb44f3a5dc0f4c3029df12981/currentStats")
    Call<Pool> getAllPool();
}