package com.bravos.steak.commonutils.shared.type;

import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenClaims {

  @NonNull
  private Long id;

  private Collection<String> authorities;

  @NonNull
  private Long iat;

  @NonNull
  private Long exp;

  @NonNull
  private Long jti;

  private Map<String, Object> metadata;

  /**
   * Convert to map
   *
   * @return map representation of the claims
   */
  public Map<String, Object> toMap() {
    return Map.of(
        "id", id,
        "authorities", authorities == null ? new ArrayList<>() : authorities,
        "iat", iat,
        "exp", exp,
        "jti", jti,
        "metadata", metadata == null ? Map.of() : metadata
    );
  }

}
