package com.proximitychat.api.datatransfer;

import lombok.Data;

@Data
public class RegisterResponse {
  private boolean successful;
  private String error;
}
