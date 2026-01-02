package models.project.create;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data //оптимизирует код без геттеров и сеттеров
@Builder //собирает обьект
public class CreateProjectRequestDto {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("access")
    @Expose
    private String access;
    @SerializedName("group")
    @Expose
    private String group;
}
