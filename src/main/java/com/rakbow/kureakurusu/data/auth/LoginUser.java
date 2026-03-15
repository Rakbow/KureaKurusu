package com.rakbow.kureakurusu.data.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Rakbow
 * @since 2026/3/14 13:06
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {

    private Long id;
    private String name;
    private String avatar;

    private String ipAddress;
    private String agent;

    private Set<String> roles = new HashSet<>();;
    private Set<String> permissions = new HashSet<>();;

    private String ticket;
    private int expires;

}
