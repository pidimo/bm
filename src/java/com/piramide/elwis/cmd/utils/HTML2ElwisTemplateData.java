package com.piramide.elwis.cmd.utils;

import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oro.text.perl.Perl5Util;

import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Feb 10, 2005
 * Time: 3:24:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class HTML2ElwisTemplateData {

    private final Log log = LogFactory.getLog(this.getClass());

    public static boolean BreakOperation = false;
    private static final String TAG_OPEN = "<label";
    private static final String ATTR_ID_OPEN = "id=\"";
    private static final String ATTR_ID_CLOSE = "\"";
    //    private static final String RIGHT_LIMIT = "";
    //    private static final String LEFT_LIMIT = "";
    private static final String RIGHT_LIMIT = "[||";
    private static final String LEFT_LIMIT = "||]";

    private static final int MERGEFIELD_SIZE = RIGHT_LIMIT.length();
    private List fields;
    private Map images;
    private String[] fieldNames;

    public HTML2ElwisTemplateData() {
        fields = new ArrayList();
        images = new HashMap();
    }

    /**
     * Read the HTML file, result of convertion with OpenOffice.<br/>
     * In the html tag &lt;IMG&gt, replace the value of attribute SRC that contain name of image file with: "cid:" and unique identifier of image file,
     * becouse for send email html with image is necessary.<br/>
     * i.e:<br/>
     * <code>&lt;IMG SRC="template-1234_me45tc34ad.png"/&gt;</code><br/>   will replace with <br/>
     * <code>&lt;IMG SRC="cid:me45tc34ad"/&gt;</code><br/>
     * Write the template cache file that contain structure data and fill the Structure field <i>fields</i>
     *
     * @param result
     */

    private int[] GetRealStartPosition(int position, StringBuffer result, int lastFieldPos) {
        int a;
        int b;

        int tagPos = result.lastIndexOf(TAG_OPEN, position);
        if (tagPos > lastFieldPos) {
            int idPos = result.indexOf(ATTR_ID_OPEN, tagPos);
            if (idPos != -1 && idPos < position) {
                a = idPos + ATTR_ID_OPEN.length();
                b = result.indexOf(ATTR_ID_CLOSE, a);
            } else {
                return null;
            }
        } else {
            return null;
        }

        //log.debug("INDEXXX:" + a + "-" + b);
        return new int[]{a, b};
    }

    public void initializeHtmlCache(Object templateId, Object campaignId, Object languageId, String[] nameFields) throws IOException {


        FileInputStream fis = new FileInputStream(CampaignResultPageList.pathCampaignTemplateHtml(templateId, campaignId, languageId));
        BufferedReader stream = new BufferedReader(new InputStreamReader(fis));

        String line;
        Perl5Util perlUtil = new Perl5Util();
        StringBuffer result = new StringBuffer();
        while ((line = stream.readLine()) != null) { // Replace line by line with "cid", maybe fail if <img not stay in one line
            while (perlUtil.match("/<IMG SRC=\"([\\w-]+_\\w+_(\\w+)\\.\\w\\w\\w)\"/", line)) {
                String cid = perlUtil.group(2);
                result.append(perlUtil.preMatch())
                        .append("<IMG SRC=\"cid:")
                        .append(cid).append("\"");
                line = perlUtil.postMatch();
                if (!images.containsKey(cid)) {
                    images.put(cid, perlUtil.group(1));
                }
            }
            result.append(line).append("\n");
        }
        stream.close();
        fis.close();
        //result is the final content of file
        //byte[] b = new byte[400];
        //result.toString().getBytes();
        //bcount = b.length;

        int lastPos = 0;
        int foundPos = 0;
        int leftLimit = 0;
        int beforeLimitPos = 0;
        int ini = 0;

        while ((foundPos = result.indexOf(RIGHT_LIMIT, lastPos)) >= 0) {
            if ((leftLimit = result.indexOf(LEFT_LIMIT, foundPos)) > 0) {
                //System.out.println("Found in:" + foundPos + " - LeftLimit:" + leftLimit);

                if (!isValidVariable(foundPos, leftLimit, result)) {
                    lastPos = foundPos + RIGHT_LIMIT.length();
                    continue;
                }

                int[] pos = GetRealStartPosition(foundPos + RIGHT_LIMIT.length(), result, beforeLimitPos);
                if (pos == null) {
                    lastPos = leftLimit;
                    log.debug("DELIMITER NOT VALID:::::" + result.substring(foundPos, leftLimit + LEFT_LIMIT.length()));
                    continue;
                }

                int start = pos[0];
                int end = pos[1];
                String field = fieldType(result.substring(start, end), nameFields);

                if (field == null) {
                    lastPos = leftLimit;
                    log.debug("FIELD NOT FOUND:::::" + result.substring(start, end));
                    continue;
                }

                String value = result.substring(ini, foundPos);
                //System.out.println("Field:" + field + " - Value:" + value);
                fields.add(new DefaultKeyValue(field, value));
                lastPos = leftLimit + LEFT_LIMIT.length();
                ini = leftLimit + LEFT_LIMIT.length();
                beforeLimitPos = leftLimit + LEFT_LIMIT.length();

                //System.out.println("LastPosition:" + lastPos);
            }
        }

        String tail = null;
        // Maybe exist data to end of file that not contain a variable field, then save in variable tail
        if (lastPos < result.length()) {
            tail = result.substring(lastPos);
        }
        //System.out.println("Field:"+fields);
        // Write the HtmlCachefile in campagin folder
        //FileOutputStream writeHtml = new FileOutputStream("./template-170.cache");
        FileOutputStream writeHtml = new FileOutputStream(CampaignResultPageList.pathCampaignTemplateCacheFile(templateId, campaignId, languageId));
        for (Iterator iterator = fields.iterator(); iterator.hasNext();) {
            KeyValue entry = (KeyValue) iterator.next();
            //System.out.println("--------------------FIELD: " + entry.getKey() + "---------------------\n" + entry.getValue());
            writeHtml.write((CampaignTemplateManager.FIELD_BLOCK + "\n").getBytes());
            writeHtml.write((entry.getKey() + "\n").getBytes());
            writeHtml.write((entry.getValue() + "\n").getBytes());

        }
        if (tail != null) {
            //System.out.println("--------------------TAIL---------------------\n" + tail);
            fields.add(tail);
            writeHtml.write((CampaignTemplateManager.TAIL_BLOCK + "\n").getBytes());
            writeHtml.write((tail + "\n").getBytes());
        }
        writeHtml.close();
        initializeImagesCache(templateId, campaignId);
    }

    public void initializeImagesCache(Object templateId, Object campaignId) throws IOException {
        if (images.size() > 0) {
            FileOutputStream writeImages = new FileOutputStream(CampaignResultPageList.pathCampaignImagesCacheFile(templateId, campaignId));

            for (Iterator iterator = images.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry entry = (Map.Entry) iterator.next();
                writeImages.write((new StringBuffer().append(entry.getKey() + "=")
                        .append(CampaignResultPageList.pathCampaignTemplateFolder(templateId, campaignId)).append(entry.getValue()).append("\n").toString()).getBytes());
            }
            writeImages.close();
        }
    }


    /**
     * Calculate the Field name in buffer
     *
     * @return Return name of variable FIELD
     */

    private String fieldType(String field, String[] nameFields) {
        //System.out.println("FieldType:"+field);
        for (int j = 0; j < nameFields.length; j++) {
            //String fieldsNoaction = VariableConstants.FIELDS_NOACTIONS[j];
            //String fieldsNoaction = Integer.toString(j);
            // If found valid name field, is returned

            if (nameFields[j].toLowerCase().equals(field.trim().toLowerCase())) {
                return Integer.toString(j);
            }
        }
        return null;
    }

    /**
     * verif if is an valid variable i.e. [||address_name||]
     *
     * @param begin
     * @param end
     * @param result
     * @return true or false
     */
    private boolean isValidVariable(int begin, int end, StringBuffer result) {
        //position of any start tag
        int startTagPos = result.indexOf("<", begin);
        return (startTagPos == -1 || startTagPos > end);
    }


    public List getFields() {
        return fields;
    }

    public Map getImages() {
        return images;
    }

    private void showFieldNames(String[] nameFields) {
        log.debug("fields::::" + nameFields.toString());
        for (int j = 0; j < nameFields.length; j++) {
            log.debug("field::::" + j + "-" + nameFields[j] + "\n");
        }
    }

}