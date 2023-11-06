package org.ezone.room.manager;

import org.ezone.room.entity.Member;
import org.springframework.security.core.Authentication;

public interface MemberManager {
    Member get(Authentication authentication);
}
