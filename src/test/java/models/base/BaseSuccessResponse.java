package models.base;

public interface BaseSuccessResponse<R> {

    Boolean getStatus();
    R getResult();
}
