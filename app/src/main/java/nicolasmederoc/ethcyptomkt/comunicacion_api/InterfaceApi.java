package nicolasmederoc.ethcyptomkt.comunicacion_api;

import nicolasmederoc.ethcyptomkt.R;
import retrofit2.Call;
import retrofit2.http.GET;

public interface InterfaceApi {
    @GET("v1/ticker?market=ETHARS")
    Call<Ethars> getAll();

    @GET("miner/"+ R.string.my_eth_direction)
    Call<Pool> getAllPool();
}