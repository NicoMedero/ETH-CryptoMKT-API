package nicolasmederoc.ethcyptomkt.comunicacion_api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PoolData {
    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("lastSeen")
    @Expose
    private String lastSeen;

    @SerializedName("reportedHashrate")
    @Expose
    private float reported;

    @SerializedName("currentHashrate")
    @Expose
    private float currentHashrate;

    @SerializedName("validShares")
    @Expose
    private float validShares;

    @SerializedName("invalidShares")
    @Expose
    private String invalidShares;

    @SerializedName("staleShares")
    @Expose
    private String staleShares;

    @SerializedName("averageHashrate")
    @Expose
    private float average;

    @SerializedName("activeWorkers")
    @Expose
    private String workers;

    @SerializedName("unpaid")
    @Expose
    private String unpaid;

    @SerializedName("unconfirmed")
    @Expose
    private String unconfirmed;

    @SerializedName("coinsPerMin")
    @Expose
    private float coinsPerMin;

    @SerializedName("usdPerMin")
    @Expose
    private float usdPerMin;

    @SerializedName("btcPerMin")
    @Expose
    private String btcPerMin;

    public float getReported(){
        float aux = reported / 1000000;
        return aux;
    }

    public float getCurrentHashrate(){
        float aux = currentHashrate / 1000000;
        return aux;
    }

    public float getAverage(){
        float aux = average / 1000000;
        return aux;
    }


    public float getCoinsPerMonth(){
        float aux = 43200 * coinsPerMin;
        return aux;
    }

    public float getUsd(){
        float aux = 43200 * usdPerMin;
        return aux;
    }

    public String getWorkers(){return workers;}
}
