package com.connectravel.security.service;

import com.connectravel.domain.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class MemberContext extends User {

  private final Member member;

  public MemberContext(Member member, Collection<? extends GrantedAuthority> authorities) {
    super(member.getUsername(), member.getPassword(), authorities);
    this.member = member;
  }

  public Member getMember() {
    return member;
  }

  public String getNickName() {
    return member.getNickName();
  }

  public String getTel() {
    return member.getTel();
  }

  public String getEmail() {
    return member.getEmail();
  }

}