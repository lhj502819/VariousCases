package cn.znnine.kafka.customize.serializer;

import lombok.*;

/**
 * Description：
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/4/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Company {
    private String name;
    private String address;
}
