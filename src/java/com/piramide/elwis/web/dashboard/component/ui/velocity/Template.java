package com.piramide.elwis.web.dashboard.component.ui.velocity;

import java.util.List;
import java.util.Map;

/**
 * @author : ivan
 */
public interface Template {
    StringBuilder merge(List<Map> elements, Map data);
}
