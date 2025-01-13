package com.checkping.service.member.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class MemberDto extends User {

    private String organizationId;
    private String email;
    private String password;
    private String name;
    private String role;
    private String phoneNum;
    private String jobRole;
    private String jobTitle;
    private String introduction;
    private String remark;


    public MemberDto(String username,String password, String role, String name, String phoneNum,
                     String jobRole, String jobTitle, String introduction, String organizationId) {
        super(username,  password == null ? "" : password,
                Collections.singletonList(new SimpleGrantedAuthority(role)));

        this.organizationId = organizationId;
        this.email = username;
        this.password = password;
        this.name = name;
        this.role = role;
        this.phoneNum = phoneNum;
        this.jobRole = jobRole;
        this.jobTitle = jobTitle;
        this.introduction = introduction;
    }

    public Map<String, Object> getClaims() {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("email", email);
        dataMap.put("name", name);
        dataMap.put("role", role);
        dataMap.put("organizationId", organizationId);
        dataMap.put("phoneNum", phoneNum);
        dataMap.put("jobRole", jobRole);
        dataMap.put("jobTitle", jobTitle);
        dataMap.put("introduction", introduction);

        return dataMap;
    }


}
