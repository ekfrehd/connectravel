package com.connectravel.service;


import com.connectravel.domain.dto.MemberDTO;
import com.connectravel.domain.entity.Member;

import java.util.List;

public interface MemberService {

  void createMember(Member member);

  void modifyMember(MemberDTO memberDTO);

  List<Member> getMembers();

  MemberDTO getMember(Long id);

  void deleteMember(Long idx);

  Member changeSellerByEmail(String email);

  MemberDTO entityToDTO(Member member);

  Member dtoToEntity(MemberDTO memberDTO);

}