package com.piramide.elwis.cmd.productmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.productmanager.*;
import com.piramide.elwis.dto.productmanager.ProductPictureDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.ProductConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This Class suppervice the operations that arrives to web-browser,
 * such operations be (read, create, update, delete, control concurrency,
 * referencial integriry and entry duplicated); all relatinated with the catalog
 *
 * @author Ivan
 * @version id: ProductPicture.java,v 1.24 2004/08/16 21:53:53 ivan Exp ProductPicture.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public class ProductPictureCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ProductPictureCmd...");
        boolean isRead = true;
        ProductPictureHome home = (ProductPictureHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCTPICTURE);

        if ("create".equals(getOp())) {
            isRead = false;
            try {
                create();
            } catch (CreateException e) {
                log.error("Cannot create the entity on create action " + e);
            }
        }
        if ("update".equals(getOp())) {
            Integer productId = new Integer(paramDTO.get("productId").toString());
            Integer freeTextId = new Integer(paramDTO.get("freeTextId").toString());
            ProductPicturePK pk = new ProductPicturePK(freeTextId, productId);
            isRead = false;
            try {
                update(home, pk);
            } catch (FinderException e) {
                log.error("Cannot find the entity on update" + e);
                resultDTO.setResultAsFailure();
                resultDTO.setForward("Cancel");
                ProductPictureDTO dto = new ProductPictureDTO(paramDTO);
                dto.addNotFoundMsgTo(resultDTO);
                return;
            }
        }
        if ("delete".equals(getOp())) {
            Integer productId = new Integer(paramDTO.get("productId").toString());
            Integer freeTextId = new Integer(paramDTO.get("freeTextId").toString());
            ProductPicturePK pk = new ProductPicturePK(freeTextId, productId);
            isRead = false;
            try {
                delete(home, pk);
            } catch (FinderException e) {
                log.error("Cannot find the entity on delete" + e);
                resultDTO.setResultAsFailure();
                resultDTO.setForward("Cancel");
                ProductPictureDTO dto = new ProductPictureDTO(paramDTO);
                dto.addNotFoundMsgTo(resultDTO);
                return;
            } catch (RemoveException e) {
                log.error("Cannot remove the entity on delete" + e);
                resultDTO.setResultAsFailure();
                resultDTO.setForward("Cancel");
                ProductPictureDTO dto = new ProductPictureDTO(paramDTO);
                dto.addNotFoundMsgTo(resultDTO);
                return;
            }
        }

        if ("readByProductId".equals(getOp())) {
            isRead = false;
            getProductPictureByProductId();
        }

        if (isRead) {
            Integer productId = new Integer(paramDTO.get("productId").toString());
            Integer freeTextId = new Integer(paramDTO.get("freeTextId").toString());
            ProductPicturePK pk = new ProductPicturePK(freeTextId, productId);
            try {
                read(home, pk);
            } catch (FinderException e) {
                log.error("Cannot find the entity on read" + e);
                resultDTO.setResultAsFailure();
                ProductPictureDTO dto = new ProductPictureDTO(paramDTO);
                dto.addNotFoundMsgTo(resultDTO);
                return;
            }
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void create() throws CreateException {
        ProductFreeTextHome freeTextHome = (ProductFreeTextHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT_FREETEXT);
        ArrayByteWrapper file = (ArrayByteWrapper) paramDTO.get("wrappedPicture");
        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        ProductFreeText image = freeTextHome.create(file.getFileData(), companyId, new Integer(FreeTextTypes.FREETEXT_PRODUCT));
        ProductPictureDTO productPictureDTO = new ProductPictureDTO(paramDTO);
        productPictureDTO.put("companyId", paramDTO.get("companyId"));
        productPictureDTO.put("productPictureName", paramDTO.get("productPictureName"));
        productPictureDTO.put("size", paramDTO.get("fileSize"));
        /*productPictureDTO.put("size", new Integer(file.getFileSize() / 1024));*/
        productPictureDTO.put("uploadDate", paramDTO.get("uploadDate"));
        productPictureDTO.put("freeTextId", image.getFreeTextId().toString());
        productPictureDTO.put("productId", new Integer(paramDTO.get("productId").toString()));
        ExtendedCRUDDirector.i.create(productPictureDTO, resultDTO, false);
    }

    private void read(ProductPictureHome home, ProductPicturePK pk) throws FinderException {
        ProductPicture productPicture = home.findByPrimaryKey(pk);
        resultDTO.put("companyId", productPicture.getCompanyId());
        resultDTO.put("freeTextId", productPicture.getFreeTextId());
        resultDTO.put("productId", productPicture.getProductId());
        resultDTO.put("productPictureName", productPicture.getProductPictureName());
        resultDTO.put("size", productPicture.getSize());
        resultDTO.put("uploadDate", productPicture.getUploadDate());
        resultDTO.put("version", productPicture.getVersion());
    }

    private void update(ProductPictureHome home, ProductPicturePK pk) throws FinderException {
        ProductPicture productPicture = home.findByPrimaryKey(pk);
        int version = (new Integer((String) paramDTO.get("version"))).intValue();
        if (productPicture.getVersion().toString().equals(new Integer(version).toString())) {
            productPicture.setVersion(new Integer(version + 1));
        } else {
            resultDTO.isClearingForm = true;
            resultDTO.addResultMessage("Common.error.concurrency");
            resultDTO.setResultAsFailure();
        }
        if (resultDTO.isFailure()) {
            read(home, pk);
            return;
        } else {
            if (resultDTO.isFailure()) {
                return;
            }
            if ("true".equals(paramDTO.get("change").toString())) {
                ProductFreeTextHome freeTextHome = (ProductFreeTextHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT_FREETEXT);
                ArrayByteWrapper file = (ArrayByteWrapper) paramDTO.get("wrappedPicture");
                ProductFreeText image = freeTextHome.findByPrimaryKey(productPicture.getFreeTextId());
                image.setValue(file.getFileData());
                productPicture.setSize((Integer) paramDTO.get("fileSize"));
            }
            productPicture.setCompanyId(new Integer((String) paramDTO.get("companyId")));
            productPicture.setProductPictureName(paramDTO.get("productPictureName").toString());
            productPicture.setUploadDate(new Integer(paramDTO.get("uploadDate").toString()));
        }
    }

    private void delete(ProductPictureHome home, ProductPicturePK pk) throws FinderException, RemoveException {
        ProductPicture productPicture = home.findByPrimaryKey(pk);
        productPicture.remove();
        ProductFreeTextHome frHome = (ProductFreeTextHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT_FREETEXT);
        ProductFreeText freeText = frHome.findByPrimaryKey(pk.freeTextId);
        freeText.remove();
    }

    private void getProductPictureByProductId() {
        List result = new ArrayList();
        Integer productId = new Integer(paramDTO.get("productId").toString());

        ProductPictureHome productPictureHome = (ProductPictureHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCTPICTURE);

        Collection collection = null;
        try {
            collection = productPictureHome.findByProductId(productId);
        } catch (FinderException e) {
            collection = new ArrayList();
        }

        for (Iterator iterator = collection.iterator(); iterator.hasNext(); ) {
            ProductPicture productPicture = (ProductPicture) iterator.next();

            ProductPictureDTO dto = new ProductPictureDTO();
            DTOFactory.i.copyToDTO(productPicture, dto);

            result.add(dto);
        }

        resultDTO.put("productPictureDTOList", result);
    }
}
