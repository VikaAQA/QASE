package models.project.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class GetProjectResponseDto {

    @SerializedName("status")
    @Expose
    private Boolean status;

    @SerializedName("result")
    @Expose
    private Result result;

    // ---------- Вложенные классы ----------

    @Data
    public static class Result {
        @SerializedName("title")
        @Expose
        private String title;

        @SerializedName("code")
        @Expose
        private String code;

        @SerializedName("counts")
        @Expose
        private Counts counts;
    }

    @Data
    public static class Counts {
        @SerializedName("cases")
        @Expose
        private Integer cases;

        @SerializedName("suites")
        @Expose
        private Integer suites;

        @SerializedName("milestones")
        @Expose
        private Integer milestones;

        @SerializedName("runs")
        @Expose
        private Runs runs;

        @SerializedName("defects")
        @Expose
        private Defects defects;
    }

    @Data
    public static class Runs {
        @SerializedName("total")
        @Expose
        private Integer total;

        @SerializedName("active")
        @Expose
        private Integer active;
    }

    @Data
    public static class Defects {
        @SerializedName("total")
        @Expose
        private Integer total;

        @SerializedName("open")
        @Expose
        private Integer open;
    }
}

