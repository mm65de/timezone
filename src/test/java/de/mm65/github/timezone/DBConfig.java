package de.mm65.github.timezone;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
class DBConfig {
    String url;
    String usr;
    String pwd;
}
