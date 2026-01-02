package utils.factories.api;

import models.project.create.CreateProjectRequestDto;

public final class ProjectRequestFactory {

    public static CreateProjectRequestDto valid() {
        return CreateProjectRequestDto.builder()
                .title("TMSAPI")
                .code("API")
                .description("test")
                .group("all")
                .access("all")
                .build();
    }

    public static CreateProjectRequestDto projectWithEmptyTitle() {
        return CreateProjectRequestDto.builder()
                .title("")
                .code("API")
                .description("Project created via API with empty title")
                .group("all")
                .access("all")
                .build();
    }

    public static CreateProjectRequestDto validWithCode(String code) {
        return CreateProjectRequestDto.builder()
                .title("TMSAPI")
                .code(code)
                .description("test")
                .group("all")
                .access("all")
                .build();
    }
}
