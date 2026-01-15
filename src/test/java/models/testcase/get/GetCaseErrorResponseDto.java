package models.testcase.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data

public class GetCaseErrorResponseDto {
        @SerializedName("status")
        @Expose
        private Boolean status;

        @SerializedName("errorMessage")
        @Expose
        private String errorMessage;
    }

