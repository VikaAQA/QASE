package models.testcase.create;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.base.BaseSuccessResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCaseResponseDto implements BaseSuccessResponse<CreateCaseResponseDto.Result> {

    @SerializedName("status")
    @Expose
    private Boolean status;

    @SerializedName("result")
    @Expose
    private Result result;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        @SerializedName("id")
        @Expose
        private Integer id;
    }
}



