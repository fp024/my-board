package org.fp024.security;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fp024.domain.MemberDTO;
import org.fp024.security.domain.CustomUser;
import org.fp024.service.MemberService;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final MemberService memberService;

  @Override
  public @NonNull UserDetails loadUserByUsername(@NonNull String userName)
      throws UsernameNotFoundException {
    LOGGER.warn("Load User by userName: {}", userName);
    Optional<MemberDTO> optionalMember = memberService.read(userName);
    MemberDTO member =
        optionalMember.orElseThrow(() -> new UsernameNotFoundException("userName: " + userName));
    return new CustomUser(member);
  }
}
