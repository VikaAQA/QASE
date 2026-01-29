package models.testcase.update;

import com.google.gson.annotations.Expose;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCaseRequestDto {
        @Expose private String title;
        @Expose private String description;
        @Expose private String preconditions;
        @Expose private String postconditions;
        @Expose private Integer severity;
        @Expose private Integer priority;
        @Expose private Integer behavior;
        @Expose private Integer type;
        @Expose private Integer layer;
}
