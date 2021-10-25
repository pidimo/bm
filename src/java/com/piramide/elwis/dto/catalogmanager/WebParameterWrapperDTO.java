package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.cmd.utils.VariableConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Web document DTO wrapper
 *
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
public class WebParameterWrapperDTO {
    private Map<String, List> webParameterMap;
    private Map<String, List> newsWebParameterMap;

    public WebParameterWrapperDTO() {
        this.webParameterMap = new HashMap<String, List>();
        this.newsWebParameterMap = new HashMap<String, List>();
    }

    public Map<String, List> getWebParameterMap() {
        return webParameterMap;
    }

    public void setWebParameterMap(Map<String, List> webParameterMap) {
        this.webParameterMap = webParameterMap;
    }

    public Map<String, List> getNewsWebParameterMap() {
        return newsWebParameterMap;
    }

    public void setNewsWebParameterMap(Map<String, List> newsWebParameterMap) {
        this.newsWebParameterMap = newsWebParameterMap;
    }

    public List<WebParameterDTO> getAllWebParameters() {
        List<WebParameterDTO> result = new ArrayList<WebParameterDTO>();

        for (int i = 0; i < VariableConstants.VariableType.values().length; i++) {
            VariableConstants.VariableType variableType = VariableConstants.VariableType.values()[i];
            result.addAll(getWebParameterListByType(variableType));
            result.addAll(getNewsWebParameterListByType(variableType));
        }
        return result;
    }

    public List<WebParameterDTO> getWebParameterListByType(VariableConstants.VariableType variableType) {
        List<WebParameterDTO> result = new ArrayList<WebParameterDTO>();

        if (variableType != null && webParameterMap.containsKey(variableType.getLiteral())) {
            result = webParameterMap.get(variableType.getLiteral());
        }
        return result;
    }

    public List<WebParameterDTO> getNewsWebParameterListByType(VariableConstants.VariableType variableType) {
        List<WebParameterDTO> result = new ArrayList<WebParameterDTO>();

        if (variableType != null && newsWebParameterMap.containsKey(variableType.getLiteral())) {
            result = newsWebParameterMap.get(variableType.getLiteral());
        }
        return result;
    }

    public void addWebParameterDTO(WebParameterDTO webParameterDTO) {
        if (webParameterDTO.get("variableType") != null) {
            VariableConstants.VariableType variableType = VariableConstants.VariableType.findVariableType(webParameterDTO.get("variableType").toString());

            if (variableType != null) {

                String key = variableType.getLiteral();
                if (webParameterMap.containsKey(key)) {
                    webParameterMap.get(key).add(webParameterDTO);
                } else {
                    List dtoList = new ArrayList();
                    dtoList.add(webParameterDTO);
                    webParameterMap.put(key, dtoList);
                }
            }
        }
    }

    private void addNewsWebParameterDTO(WebParameterDTO webParameterDTO) {
        if (webParameterDTO.get("variableType") != null) {
            VariableConstants.VariableType variableType = VariableConstants.VariableType.findVariableType(webParameterDTO.get("variableType").toString());

            if (variableType != null) {

                String key = variableType.getLiteral();
                if (newsWebParameterMap.containsKey(key)) {
                    newsWebParameterMap.get(key).add(webParameterDTO);
                } else {
                    List dtoList = new ArrayList();
                    dtoList.add(webParameterDTO);
                    newsWebParameterMap.put(key, dtoList);
                }
            }
        }
    }

    public void processParamDtoValues(Map paramDto, List webParameterIdList, List newsKeyList) {

        if (webParameterIdList != null) {
            for (int i = 0; i < webParameterIdList.size(); i++) {
                Integer webParameterId = new Integer(webParameterIdList.get(i).toString());
                String key = webParameterId.toString();

                Object variableType = paramDto.get("variableType_" + key);
                Object parameterName = paramDto.get("parameterName_" + key);
                Object variableName = paramDto.get("variableName_" + key);

                if (variableType != null && parameterName != null && variableName != null) {
                    WebParameterDTO webParameterDTO = new WebParameterDTO();
                    webParameterDTO.put("webParameterId", webParameterId);
                    webParameterDTO.put("variableType", variableType);
                    webParameterDTO.put("parameterName", parameterName);
                    webParameterDTO.put("variableName", variableName);

                    addWebParameterDTO(webParameterDTO);
                }
            }
        }

        //process new web parameters
        if (newsKeyList != null) {
            for (int i = 0; i < newsKeyList.size(); i++) {
                String key = newsKeyList.get(i).toString();

                Object variableType = paramDto.get("newVariableType_" + key);
                Object parameterName = paramDto.get("newParameterName_" + key);
                Object variableName = paramDto.get("newVariableName_" + key);

                if (variableType != null && parameterName != null && variableName != null) {
                    WebParameterDTO webParameterDTO = new WebParameterDTO();
                    webParameterDTO.put("newWebParameterKey", key);
                    webParameterDTO.put("variableType", variableType);
                    webParameterDTO.put("parameterName", parameterName);
                    webParameterDTO.put("variableName", variableName);

                    addNewsWebParameterDTO(webParameterDTO);
                }
            }
        }
    }
}
