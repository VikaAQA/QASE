package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCasesResponseDto {

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

        @SerializedName("total")
        @Expose
        private Integer total;

        @SerializedName("filtered")
        @Expose
        private Integer filtered;

        @SerializedName("count")
        @Expose
        private Integer count;

        @SerializedName("entities")
        @Expose
        private List<CaseItem> entities;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CaseItem {

        // добавляй поля по мере необходимости

        @SerializedName("id")
        @Expose
        private Integer id;

        @SerializedName("type")
        @Expose
        private Integer type;

        // при необходимости:
        // private String title;
        // private Integer priority;
    }
}

