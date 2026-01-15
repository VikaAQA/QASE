package models.testcase.update;

import com.google.gson.annotations.Expose;
import lombok.Builder;
import lombok.Data;
import models.BaseSuccessResponse;

@Data
@Builder
    public class UpdateCaseResponseDto implements BaseSuccessResponse<UpdateCaseResponseDto.Result> {
    @Expose
        private Boolean status;
    @Expose
        private Result result;

        @Data
        @Builder

        public static class Result {
            @Expose
            private Integer id;
        }
}
