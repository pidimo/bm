package com.piramide.elwis.web.dashboard.component.ui.velocity;

import java.util.List;
import java.util.Map;

/**
 * @author : ivan
 */
public abstract class CustomTemplate implements Template {
    public abstract StringBuilder merge(List<Map> elements, Map data);
}
