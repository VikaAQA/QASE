package models.testcase.update;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCaseRequestDto {
    private Integer type;
}

