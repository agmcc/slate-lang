package com.github.agmcc.slate.compiler;

import java.nio.file.Path;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class SlateFile {

  private Path filePath;

  private byte[] data;
}
