package com.bravos.steak.commonutils.shared.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.PrivateKey;
import java.security.PublicKey;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneralKeyPair {

  private PrivateKey privateKey;

  private PublicKey publicKey;

}
