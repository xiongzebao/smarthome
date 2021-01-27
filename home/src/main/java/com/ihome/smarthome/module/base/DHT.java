package com.ihome.smarthome.module.base;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/27 17:49
 */

public class DHT {
   private float temp;
   private int humi;

    public DHT(float temp, int humi) {
        this.temp = temp;
        this.humi = humi;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public int getHumi() {
        return humi;
    }

    public void setHumi(int humi) {
        this.humi = humi;
    }
}
