package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class GetCaseResponseDto  {

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

            @SerializedName("title")
            @Expose
            private String title;

            @SerializedName("description")
            @Expose
            private String description;

            @SerializedName("severity")
            @Expose
            private Integer severity;

            @SerializedName("priority")
            @Expose
            private Integer priority;

            @SerializedName("type")
            @Expose
            private Integer type;

            @SerializedName("isManual")
            @Expose
            private Boolean isManual;

            @SerializedName("isToBeAutomated")
            @Expose
            private Boolean isToBeAutomated;

            @SerializedName("status")
            @Expose
            private Integer status;

            @SerializedName("steps")
            @Expose
            private List<Step> steps;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Step {

            @SerializedName("position")
            @Expose
            private Integer position;

            @SerializedName("action")
            @Expose
            private String action;

            @SerializedName("expected_result")
            @Expose
            private String expectedResult;
        }
    }
