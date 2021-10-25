package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.*;

/**
 * Used as wrapper to tansport telecoms related to telecomtypes.
 *
 * @author Fernando Montaño
 * @version $Id: TelecomWrapperDTO.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class TelecomWrapperDTO implements Serializable, Comparable {
    private static Log log = LogFactory.getLog(TelecomWrapperDTO.class);
    private String telecomTypeName;
    private String telecomTypeType;
    private String telecomTypeId;
    private String telecomTypePosition;
    private String predeterminedIndex;
    private List telecoms;

    public TelecomWrapperDTO() {
        this.telecoms = new LinkedList();
    }

    public TelecomWrapperDTO(TelecomDTO dto, TelecomTypeDTO telecomTypeDTO) {
        telecomTypeId = telecomTypeDTO.get("telecomTypeId").toString();
        telecomTypeName = (String) telecomTypeDTO.get("telecomTypeName");
        telecomTypeType = (String) telecomTypeDTO.get("telecomTypeType");
        telecomTypePosition = telecomTypeDTO.get("telecomTypePosition").toString();
        this.telecoms = new LinkedList();
        this.telecoms.add(dto);
    }

    public void addTelecomDTO(TelecomDTO telecomDTO) {
        telecoms.add(telecomDTO);
    }

    public TelecomDTO getTelecomDTO(int index) {
        return (TelecomDTO) telecoms.get(index);
    }

    public void removeTelecomDTO(TelecomDTO telecomDTO) {
        telecoms.remove(telecomDTO);
    }

    public String getTelecomTypeName() {
        return telecomTypeName;
    }

    public void setTelecomTypeName(String telecomTypeName) {
        this.telecomTypeName = telecomTypeName;
    }

    public String getTelecomTypeId() {
        return telecomTypeId;
    }

    public void setTelecomTypeId(String telecomTypeId) {
        this.telecomTypeId = telecomTypeId;
    }

    public List getTelecoms() {
        return telecoms;
    }

    public void setTelecoms(List telecoms) {
        this.telecoms = telecoms;
    }

    public int getSize() {
        return telecoms.size();
    }

    private void verifySize(int index, List list) {
        if (index >= list.size()) {
            while (index > list.size() - 1) {
                list.add(new TelecomDTO());
            }
        }
    }

    public TelecomDTO getTelecom(int index) {
        verifySize(index, telecoms);
        return (TelecomDTO) telecoms.get(index);
    }

    public void setTelecom(int index, TelecomDTO data) {
        verifySize(index, telecoms);
        telecoms.set(index, data);
    }

    public String getTelecomTypeType() {
        return telecomTypeType;
    }

    public void setTelecomTypeType(String telecomTypeType) {
        this.telecomTypeType = telecomTypeType;
    }

    public void removeTelecomDTO(int index) {
        telecoms.remove(index);
    }

    public String getTelecomTypePosition() {
        return telecomTypePosition;
    }

    public void setTelecomTypePosition(String telecomTypePosition) {
        this.telecomTypePosition = telecomTypePosition;
    }

    public String getPredeterminedIndex() {
        return predeterminedIndex;
    }

    public void setPredeterminedIndex(String predeterminedIndex) {
        this.predeterminedIndex = predeterminedIndex;
    }

    public static Map addToMapTelecomDTO(Map telecoms, TelecomDTO telecomDTO, TelecomTypeDTO telecomTypeDTO) {
        if (telecoms.containsKey(telecomTypeDTO.get("telecomTypeId").toString())) {
            TelecomWrapperDTO wrapperDTO = (TelecomWrapperDTO) telecoms.get(telecomTypeDTO.get("telecomTypeId").toString());
            wrapperDTO.setTelecomTypeName((String) telecomTypeDTO.get("telecomTypeName"));
            wrapperDTO.setTelecomTypeType((String) telecomTypeDTO.get("telecomTypeType"));
            wrapperDTO.setTelecomTypePosition(telecomTypeDTO.get("telecomTypePosition").toString());
            wrapperDTO.getTelecoms().add(telecomDTO);
        } else {
            TelecomWrapperDTO wrapperDTO = new TelecomWrapperDTO(telecomDTO, telecomTypeDTO);
            wrapperDTO.setPredeterminedIndex("0"); //is the first element so it must be predetermined by default.
            telecoms.put(telecomTypeDTO.get("telecomTypeId").toString(), wrapperDTO);
        }

        return telecoms;
    }

    public static synchronized void sortTelecomMapByPosition(Map telecomMap) {
        List mapKeys = new ArrayList(telecomMap.keySet());
        List mapValues = new ArrayList(telecomMap.values());
        telecomMap.clear();
        TreeSet sortedSet = new TreeSet(mapValues);
        Object[] sortedArray = sortedSet.toArray();
        int size = sortedArray.length;
        for (int i = 0; i < size; i++) {
            telecomMap.put(mapKeys.get(mapValues.indexOf(sortedArray[i])), sortedArray[i]);
        }
    }

    public int compareTo(Object o) {
        TelecomWrapperDTO toCompare = (TelecomWrapperDTO) o;
        int currentPos = Integer.parseInt(telecomTypePosition);
        int toComparePos = Integer.parseInt(toCompare.getTelecomTypePosition());
        if (currentPos > toComparePos) {
            return 1;
        } else if (currentPos < toComparePos) {
            return -1;
        } else { //equal (there is no equal, because it should have another telecomtypeid
            //in this case use te telecomtypeid to sort
            int currentTelecomTypeId = Integer.parseInt(telecomTypeId);
            int toTelecomTypeId = Integer.parseInt(toCompare.getTelecomTypeId());
            if (currentTelecomTypeId > toTelecomTypeId) {
                return 1;
            } else if (currentTelecomTypeId < toTelecomTypeId) {
                return -1;
            }
        }
        return 1; //by default put as greater than the new.
    }

    public String toString() {
        return new String(new StringBuffer()
                .append("[")
                .append("TelecomTypeId=")
                .append(telecomTypeId)
                .append(", ")
                .append("TelecomTypeName=")
                .append(telecomTypeName)
                .append(", ")
                .append("Type=")
                .append(telecomTypeType)
                .append(", ")
                .append("Pos=")
                .append(telecomTypePosition)
                .append(", ")
                .append("telecoms=")
                .append(telecoms)

                .append("]"));
    }

}
