package dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestSuite {
    String title;

    String description;

    String preconditions;

    @JsonProperty("parent_id")
    int parentId;
}
