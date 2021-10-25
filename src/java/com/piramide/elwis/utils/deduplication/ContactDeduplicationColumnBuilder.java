package com.piramide.elwis.utils.deduplication;

import com.piramide.elwis.cmd.contactmanager.dataimport.build.BuildStructure;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class ContactDeduplicationColumnBuilder {

    private final static LinkedHashMap<String, Integer> staticColumnMapping;
    static {
        staticColumnMapping = new LinkedHashMap<String, Integer>();
        //address
        staticColumnMapping.put("name1", 40);
        staticColumnMapping.put("name2", 41);
        staticColumnMapping.put("name3", 42);
        staticColumnMapping.put("countryId",43 );
        staticColumnMapping.put("cityId", 44);
        //staticColumnMapping.put("cityZip", 45);
        staticColumnMapping.put("street", 46);
        staticColumnMapping.put("houseNumber", 47);
        staticColumnMapping.put("keywords", 48);
        staticColumnMapping.put("searchName", 49);
        staticColumnMapping.put("languageId", 62);

        staticColumnMapping.put("titleId", 28);
        staticColumnMapping.put("salutationId", 29);
        staticColumnMapping.put("education", 30);
        //customer
        staticColumnMapping.put("number", 51);
        staticColumnMapping.put("sourceId", 52);
        staticColumnMapping.put("branchId", 53);
        staticColumnMapping.put("customerTypeId", 58);
    }

    private Integer companyId;

    public ContactDeduplicationColumnBuilder(Integer companyId) {
        this.companyId = companyId;
    }

    public List<Column> buildColumnsToDeduplicate() {
        List<Column> result = new ArrayList<Column>();

        CompoundGroup organizationCompoundGroup = getOrganizationGroup();
        CompoundGroup contactCompoundGroup = getContactGroup();

        List<Integer> columnIdList = new ArrayList<Integer>(staticColumnMapping.values());
        for (Integer columnId : columnIdList) {
            Column column = findStaticColumnById(organizationCompoundGroup, columnId);
            if (column == null) {
                column = findStaticColumnById(contactCompoundGroup, columnId);
            }

            if (column != null) {
                Column newColumn = column.getCopy();
                result.add(newColumn);
            }
        }

        result.addAll(findCategoryColumns(organizationCompoundGroup));
        //result.addAll(findTelecomColumns(organizationCompoundGroup));

        //define column positions
        for (int i = 0; i < result.size(); i++) {
            Column column = result.get(i);
            column.setPosition(i + 1);
        }

        return result;
    }

    private Column findStaticColumnById(CompoundGroup compoundGroup, Integer columnId) {
        List<Column> allColumns = compoundGroup.getAllColumns();
        for (Column column : allColumns) {
            if (column instanceof StaticColumn && column.getColumnId().equals(columnId)) {
                return column;
            }
        }
        return null;
    }

    private List<Column> findCategoryColumns(CompoundGroup compoundGroup) {
        List<Column> categoryColumns = new ArrayList<Column>();
        List<Column> allColumns = compoundGroup.getAllColumns();
        for (Column column : allColumns) {
            if (column instanceof CategoryDinamicColumn) {
                Column newColumn = column.getCopy();
                categoryColumns.add(newColumn);
            }
        }
        return categoryColumns;
    }

    private List<Column> findTelecomColumns(CompoundGroup compoundGroup) {
        List<Column> telecomColumns = new ArrayList<Column>();
        List<Column> allColumns = compoundGroup.getAllColumns();
        for (Column column : allColumns) {
            if (column instanceof TelecomDinamicColumn) {
                Column newColumn = column.getCopy();
                telecomColumns.add(newColumn);
            }
        }
        return telecomColumns;
    }

    private CompoundGroup getOrganizationGroup() {
        BuildStructure buildBuildStructure = new BuildStructure();
        return buildBuildStructure.buildOrganizationStructure(companyId);
    }

    private CompoundGroup getContactGroup() {
        BuildStructure buildStructure = new BuildStructure();
        return buildStructure.buildContactStructure(companyId);
    }
}
