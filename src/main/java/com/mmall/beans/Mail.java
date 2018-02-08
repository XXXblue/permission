package com.mmall.beans;

import lombok.*;

import java.util.Set;

/**
 * @Author: XBlue
 * @Date: Create in 2018/2/122:04
 * @Description:
 * @Modified By:
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mail {
    private String subject;

    private String message;

    private Set<String>receivers;
}
