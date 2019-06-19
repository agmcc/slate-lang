package com.github.agmcc.slate.compiler;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class SlateFile {

  private String fileName;

  private byte[] data;
}
