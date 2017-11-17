package com.example.computer.ljp;

import cn.bmob.v3.BmobObject;

/**
 * Created by 鹏COMPUTER on 2017/11/17.
 */

public class Data extends BmobObject{
    private boolean Light1;
    private boolean Light2;
    private boolean Light3;
    private boolean Light4;
    private boolean Light5;
    private boolean Light6;
    private boolean motal1;
    private boolean motal2;
    private boolean motal3;
    private String temperature;//温度
    private String humidity;//湿度

    public void setLight1(boolean light1) {
        Light1 = light1;
    }

    public void setLight2(boolean light2) {
        Light2 = light2;
    }

    public void setLight3(boolean light3) {
        Light3 = light3;
    }

    public void setLight4(boolean light4) {
        Light4 = light4;
    }

    public void setLight5(boolean light5) {
        Light5 = light5;
    }

    public void setLight6(boolean light6) {
        Light6 = light6;
    }

    public void setMotal1(boolean motal1) {
        this.motal1 = motal1;
    }

    public void setMotal2(boolean motal2) {
        this.motal2 = motal2;
    }

    public void setMotal3(boolean motal3) {
        this.motal3 = motal3;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public boolean isLight1() {

        return Light1;
    }

    public boolean isLight2() {
        return Light2;
    }

    public boolean isLight3() {
        return Light3;
    }

    public boolean isLight4() {
        return Light4;
    }

    public boolean isLight5() {
        return Light5;
    }

    public boolean isLight6() {
        return Light6;
    }

    public boolean isMotal1() {
        return motal1;
    }

    public boolean isMotal2() {
        return motal2;
    }

    public boolean isMotal3() {
        return motal3;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }
}
