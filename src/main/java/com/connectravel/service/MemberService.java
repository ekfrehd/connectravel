package com.connectravel.service;

import com.connectravel.dto.MemberDTO;
import com.connectravel.entity.Member;

public interface MemberService {
    MemberDTO entityToDTO(Member member);
    Member dtoToEntity(MemberDTO memberDTO);
}
