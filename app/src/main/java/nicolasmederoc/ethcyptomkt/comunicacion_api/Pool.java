package nicolasmederoc.ethcyptomkt.comunicacion_api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import nicolasmederoc.ethcyptomkt.comunicacion_api.PoolData;

public class Pool {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private PoolData data;

    public PoolData getData(){
        return data;
    }
}
