package models.project.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data

public class GetProjectErrorResponseDto {
    @SerializedName("status")
    @Expose
    private Boolean status;

    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;
}
