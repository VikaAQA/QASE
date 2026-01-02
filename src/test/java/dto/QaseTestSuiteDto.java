package dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QaseTestSuiteDto {
    String suit_name;
    String description;
    String preconditions;
@Builder.Default
    String parent_suite="";
}
