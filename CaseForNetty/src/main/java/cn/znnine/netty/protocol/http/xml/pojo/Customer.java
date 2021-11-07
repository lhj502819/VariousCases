package cn.znnine.netty.protocol.http.xml.pojo;

import javax.xml.bind.annotation.XmlAnyAttribute;
import java.util.List;

/**
 * 客户信息
 *
 * @author lihongjian
 * @since 2021/11/7
 */
public class Customer {

    @XmlAnyAttribute
    private long customerNumber;

    /**
     * 个人名字
     */
    private String firstName;

    /**
     * 姓
     */
    private String lastName;

    /**
     * 中间名
     */
    private List<String> middleNames;
}
