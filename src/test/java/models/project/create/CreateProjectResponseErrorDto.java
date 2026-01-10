package models.project.create;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class CreateProjectResponseErrorDto {
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("errorMessage")
    @Expose
    public String errorMessage;
    @SerializedName("errorFields")
    @Expose
    public List<ErrorField> errorFields;
    @Data
    @Builder
    public static class ErrorField {
    @SerializedName("field")
    @Expose
    public String field;
    @SerializedName("error")
    @Expose
    public String error;
}
}

