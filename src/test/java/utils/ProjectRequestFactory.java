package utils;

import models.project.create.CreateProjectRq;

public class ProjectRequestFactory {

    public static CreateProjectRq validProject() {
        return CreateProjectRq.builder()
                .title("TMSAPI")
                .code("API")
                .description("test")
                .group("all")
                .access("all")
                .build();
    }

    public static CreateProjectRq projectWithEmptyTitle() {
        return CreateProjectRq.builder()
                .title("")
                .code("API")
                .description("Project created via API with empty title")
                .group("all")
                .access("all")
                .build();
    }

    public static CreateProjectRq projectWithEmptyCode(String title) {
        return CreateProjectRq.builder()
                .title(title)
                .code("") //
                .description("Project created via API with empty code")
                .group("all")
                .access("all")
                .build();
    }
}
