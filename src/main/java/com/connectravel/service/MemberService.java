package com.connectravel.service;


import com.connectravel.domain.dto.MemberDTO;
import com.connectravel.domain.entity.Member;

import java.util.List;

public interface MemberService {

  void createMember(Member member);

  void updateMember(MemberDTO memberDTO);

  List<Member> getMembers();

  MemberDTO getMember(Long id);

  MemberDTO getMember(String email);

  void deleteMember(Long idx);

  Member updateSeller(MemberDTO memberDTO);

  MemberDTO entityToDTO(Member member);

  Member dtoToEntity(MemberDTO memberDTO);

}