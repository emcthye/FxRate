package io.github.emcthye.fxrate.util.base;

public interface BaseView<P extends BasePresenter, Object> {

    void render(Object result);

    void renderLoading();

    void renderEmpty();

    void renderError();

}
