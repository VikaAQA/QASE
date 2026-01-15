package models;

public interface BaseSuccessResponse<R> {

    Boolean getStatus();
    R getResult();
}
