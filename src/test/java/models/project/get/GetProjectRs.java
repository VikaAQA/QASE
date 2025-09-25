package models.project.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class GetProjectRs {

    @SerializedName("status")
    @Expose
    public Boolean status;

    @SerializedName("projects")
    @Expose
    public List<Project> projects;

    @SerializedName("total")
    @Expose
    public Integer total;

    @Data
    public static class Project {
        @SerializedName("code")
        @Expose
        public String code;

        @SerializedName("title")
        @Expose
        public String title;

        @SerializedName("description")
        @Expose
        public String description;

        @SerializedName("is_available")
        @Expose
        public Boolean isAvailable;

        @SerializedName("is_archived")
        @Expose
        public Boolean isArchived;

        @SerializedName("is_private")
        @Expose
        public Boolean isPrivate;

        @SerializedName("avatar_url")
        @Expose
        public String avatarUrl;

        @SerializedName("case_count")
        @Expose
        public Integer caseCount;

        @SerializedName("suite_count")
        @Expose
        public Integer suiteCount;

        @SerializedName("milestone_count")
        @Expose
        public Integer milestoneCount;

        @SerializedName("run_total")
        @Expose
        public Integer runTotal;

        @SerializedName("run_active")
        @Expose
        public Integer runActive;

        @SerializedName("defect_total")
        @Expose
        public Integer defectTotal;

        @SerializedName("defect_unresolved")
        @Expose
        public Integer defectUnresolved;

        @SerializedName("is_creator")
        @Expose
        public Boolean isCreator;

        @SerializedName("members")
        @Expose
        public List<Member> members;

        @SerializedName("favorite")
        @Expose
        public Boolean favorite;
    }

    @Data
    public static class Member {
        @SerializedName("internal_id")
        @Expose
        public Integer internalId;

        @SerializedName("name")
        @Expose
        public Object name;

        @SerializedName("role_title")
        @Expose
        public String roleTitle;

        @SerializedName("user")
        @Expose
        public User user;

        @SerializedName("role")
        @Expose
        public Role role;

        @SerializedName("type")
        @Expose
        public Integer type;

        @SerializedName("status")
        @Expose
        public Integer status;
    }

    @Data
    public static class Role {
        @SerializedName("internal_id")
        @Expose
        public Integer internalId;

        @SerializedName("title")
        @Expose
        public String title;

        @SerializedName("description")
        @Expose
        public String description;

        @SerializedName("is_default")
        @Expose
        public Boolean isDefault;

        @SerializedName("is_owner")
        @Expose
        public Boolean isOwner;
    }

    @Data
    public static class User {
        @SerializedName("name")
        @Expose
        public String name;

        @SerializedName("firstName")
        @Expose
        public String firstName;

        @SerializedName("lastName")
        @Expose
        public String lastName;

        @SerializedName("email")
        @Expose
        public String email;

        @SerializedName("avatar_url")
        @Expose
        public String avatarUrl;

        @SerializedName("is_online")
        @Expose
        public Boolean isOnline;

        @SerializedName("last_action")
        @Expose
        public String lastAction;

        @SerializedName("is_active")
        @Expose
        public Boolean isActive;
    }
}