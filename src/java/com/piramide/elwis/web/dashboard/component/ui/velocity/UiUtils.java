package com.piramide.elwis.web.dashboard.component.ui.velocity;

import com.piramide.elwis.web.dashboard.component.configuration.structure.Column;
import com.piramide.elwis.web.dashboard.component.util.Constant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @author : ivan
 */
public class UiUtils {
    private List<Column> columns;
    private String url = "";

    public UiUtils(List<Column> columns, String url) {
        this.columns = columns;
        this.url = url;
    }

    public String buildURL(Map map) {
        String urlTesl = new String(url);
        for (Column c : columns) {
            String key = c.getName();
            String value = (String) map.get(key);

            int id = c.getId();

            String regex = "(#" + id + ")";

            String nValue = value;
            try {
                nValue = URLEncoder.encode(value, Constant.CHARSET_ENCODING);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            urlTesl = urlTesl.replaceAll(regex, nValue);
        }
        return urlTesl;
    }
}
