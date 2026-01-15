package models.create;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder

public class CreateCaseRequestDto {

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("preconditions")
    @Expose
    private String preconditions;

    @SerializedName("postconditions")
    @Expose
    private String postconditions;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("severity")
    @Expose
    private Integer severity;

    @SerializedName("priority")
    @Expose
    private Integer priority;

    @SerializedName("type")
    @Expose
    private Integer type;

    @SerializedName("steps")
    @Expose
    private List<Step> steps;

    @Data
    @Builder
    public static class Step {

        @SerializedName("action")
        @Expose
        private String action;

        @SerializedName("expected_result")
        @Expose
        private String expectedResult;

        @SerializedName("data")
        @Expose
        private String data;
    }
}




