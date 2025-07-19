package xyz.sadiulhakim.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Route {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String path;

    private Object body;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String method;
}
