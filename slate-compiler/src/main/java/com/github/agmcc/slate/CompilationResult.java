package com.github.agmcc.slate;

import java.util.List;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@ToString
public class CompilationResult {

  private List<String> errors;
}
