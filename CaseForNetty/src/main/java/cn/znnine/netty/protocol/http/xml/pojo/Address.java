package cn.znnine.netty.protocol.http.xml.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 地址信息
 *
 * @author lihongjian
 * @since 2021/11/7
 */

@XStreamAlias("billTo")
public class Address {
    /**
     * 第一街道信息（必填）
     */
    private String street1;

    /**
     * 第二街道信息（可选）
     */
    private String street2;

    /**
     * 城市
     */
    private String city;

    /**
     * State abbreviation(required for the U.S and Canada, optional otherwise)
     */
    private String state;

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street1='" + street1 + '\'' +
                ", street2='" + street2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
