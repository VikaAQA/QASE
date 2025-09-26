package utils;

import models.project.create.CreateProjectRq;

public class ProjectRequestFactory {

    public static CreateProjectRq validProject(String code) {
        return CreateProjectRq.builder()
                .title("Valid Project")
                .code(code)
                .description("Project created via API")
                .group("all")
                .access("all")
                .build();
    }

    public static CreateProjectRq projectWithEmptyTitle(String code) {
        return CreateProjectRq.builder()
                .title("") //
                .code(code)
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
