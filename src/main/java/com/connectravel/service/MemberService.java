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

  MemberDTO entityToDTO(Member member);

  Member dtoToEntity(MemberDTO memberDTO);

  public void vaildateDuplicateMember(Member member);

  public Member editMember(MemberDTO memberDTO);

  public Member changePassword(MemberDTO memberDTO);

  public Member changeSeller(MemberDTO memberDTO);

}