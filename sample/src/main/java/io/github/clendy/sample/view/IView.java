package io.github.clendy.sample.view;

import java.util.List;

import io.github.clendy.sample.model.Entity;

/**
 * IView
 *
 * @author Clendy
 * @date 2016/11/16 016 13:48
 * @e-mail yc330483161@outlook.com
 */
public interface IView extends BaseView {

    void response(List<Entity> entities);
}
