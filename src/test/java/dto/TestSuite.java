package dto;


import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestSuite {
    String title;

    String description;

    String preconditions;

    @SerializedName("parent_id")
    int parentId;
}
