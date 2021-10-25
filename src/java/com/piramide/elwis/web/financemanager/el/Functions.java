package com.piramide.elwis.web.financemanager.el;

import com.piramide.elwis.cmd.financemanager.IncomingInvoiceCmd;
import com.piramide.elwis.cmd.financemanager.InvoiceCmd;
import com.piramide.elwis.cmd.financemanager.InvoicePositionCmd;
import com.piramide.elwis.dto.financemanager.InvoiceDTO;
import com.piramide.elwis.dto.financemanager.InvoicePositionDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class Functions {
    private static Log log = LogFactory.getLog(Functions.class);

    public static boolean creditNoteIsRelatedWithInvoice(String invoiceId,
                                                         javax.servlet.ServletRequest servletRequest) {
        InvoiceDTO invoiceDTO = getInvoice(invoiceId, servletRequest);
        return null != invoiceDTO &&
                !FinanceConstants.InvoiceType.Invoice.equal((Integer) invoiceDTO.get("type")) &&
                null != invoiceDTO.get("creditNoteOfId");
    }

    public static boolean isInvoiceRelatedToCreditNote(String invoiceId, javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);

        InvoiceCmd invoiceCmd = new InvoiceCmd();
        invoiceCmd.setOp("isInvoiceRelatedToCreditNote");
        invoiceCmd.putParam("invoiceId", invoiceId);
        invoiceCmd.putParam("companyId", user.getValue(Constants.COMPANYID));
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(invoiceCmd, request);
            return (Boolean) resultDTO.get("isInvoiceRelatedToCreditNote");
        } catch (AppLevelException e) {
            log.error("Unexpexted error was happen when trying execute InvoiceCmd", e);
        }

        return false;
    }

    public static InvoiceDTO getInvoice(String invoiceIdAsString,
                                        javax.servlet.ServletRequest servletRequest) {
        Integer invoiceId = null;
        if (null != invoiceIdAsString &&
                !"".equals(invoiceIdAsString.trim())) {
            try {
                invoiceId = Integer.valueOf(invoiceIdAsString);
            } catch (NumberFormatException e) {
                log.error("-> Parse " + invoiceIdAsString + " to Integer FAIL ", e);
                return null;
            }
        }

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        InvoiceCmd invoiceCmd = new InvoiceCmd();
        invoiceCmd.setOp("getInvoice");
        invoiceCmd.putParam("invoiceId", invoiceId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(invoiceCmd, request);
            InvoiceDTO invoiceDTO = (InvoiceDTO) resultDTO.get("getInvoice");
            if (null != invoiceDTO) {
                return invoiceDTO;
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + InvoiceCmd.class.getName() + " FAIL", e);
        }

        return null;
    }

    public static boolean invoiceIsCreditNote(String invoiceIdAsString,
                                              javax.servlet.ServletRequest servletRequest) {
        Integer invoiceId = null;
        if (null != invoiceIdAsString &&
                !"".equals(invoiceIdAsString.trim())) {
            try {
                invoiceId = Integer.valueOf(invoiceIdAsString);
            } catch (NumberFormatException e) {
                log.error("-> Parse " + invoiceIdAsString + " to Integer FAIL ", e);
                return false;
            }
        }

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        InvoiceCmd invoiceCmd = new InvoiceCmd();
        invoiceCmd.setOp("isCreditNote");
        invoiceCmd.putParam("invoiceId", invoiceId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(invoiceCmd, request);
            if (resultDTO.isFailure()) {
                return false;
            }

            boolean isCreditNote = (Boolean) resultDTO.get("isCreditNote");

            if (null != resultDTO.get("invoiceDTO")) {
                InvoiceDTO invoiceDTO = (InvoiceDTO) resultDTO.get("invoiceDTO");
                request.setAttribute("invoiceDTO", invoiceDTO);
            }

            if (isCreditNote) {
                InvoiceDTO creditNoteDTO = (InvoiceDTO) resultDTO.get("creditNoteDTO");
                request.setAttribute("creditNoteDTO", creditNoteDTO);
            }

            return isCreditNote;
        } catch (AppLevelException e) {
            log.error("-> Execute " + InvoiceCmd.class.getName() + " FAIL", e);
        }

        return false;
    }

    public static List getNetGrossOptions(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        List<LabelValueBean> netGrossOptions = new ArrayList<LabelValueBean>();
        netGrossOptions.add(
                new LabelValueBean(JSPHelper.getMessage(request, "NetGross.option.gross"),
                        FinanceConstants.NetGrossFLag.GROSS.getConstantAsString())
        );
        netGrossOptions.add(
                new LabelValueBean(JSPHelper.getMessage(request, "NetGross.option.net"),
                        FinanceConstants.NetGrossFLag.NET.getConstantAsString())
        );

        return netGrossOptions;
    }

    public static String getInvoiceNetGross(String invoiceId,
                                            javax.servlet.ServletRequest servletRequest) {
        InvoiceDTO invoiceDTO = getInvoice(invoiceId, servletRequest);
        if (null == invoiceDTO) {
            return FinanceConstants.NetGrossFLag.NET.getConstantAsString();
        }

        if (FinanceConstants.NetGrossFLag.NET.equal((Integer) invoiceDTO.get("netGross"))) {
            return FinanceConstants.NetGrossFLag.NET.getConstantAsString();
        }

        return FinanceConstants.NetGrossFLag.GROSS.getConstantAsString();
    }

    public static List<InvoicePositionDTO> getSourceInvoicePositions(Integer invoiceId, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        InvoicePositionCmd invoicePositionCmd = new InvoicePositionCmd();
        invoicePositionCmd.setOp("getInvoicePositions");
        invoicePositionCmd.putParam("invoiceId", invoiceId);
        invoicePositionCmd.putParam("companyId", user.getValue(Constants.COMPANYID));
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(invoicePositionCmd, request);
            return (List<InvoicePositionDTO>) resultDTO.get("getInvoicePositions");
        } catch (AppLevelException e) {
            log.error("Cannot execute InvoicePositionCmd", e);
        }
        return new ArrayList<InvoicePositionDTO>();
    }

    public static List<InvoicePositionDTO> getInvoicePositionsByContract(Integer contractId, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        InvoicePositionCmd invoicePositionCmd = new InvoicePositionCmd();
        invoicePositionCmd.setOp("getInvoicePositionsByContract");
        invoicePositionCmd.putParam("contractId", contractId);
        invoicePositionCmd.putParam("companyId", user.getValue(Constants.COMPANYID));
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(invoicePositionCmd, request);
            return (List<InvoicePositionDTO>) resultDTO.get("getInvoicePositionsByContract");
        } catch (AppLevelException e) {
            log.error("Cannot execute InvoicePositionCmd", e);
        }
        return new ArrayList<InvoicePositionDTO>();
    }

    public static InvoicePositionDTO getInvoicePosition(String positionId, HttpServletRequest request) {
        InvoicePositionCmd invoicePositionCmd = new InvoicePositionCmd();
        invoicePositionCmd.setOp("getInvoicePosition");
        invoicePositionCmd.putParam("positionId", positionId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(invoicePositionCmd, request);
            return (InvoicePositionDTO) resultDTO.get("getInvoicePosition");
        } catch (AppLevelException e) {
            //
        }

        return null;
    }

    public static Map getIncomingInvoiceInfoMap(String incomingInvoiceId, HttpServletRequest request) {
        Map incomingInvoiceMap = new HashMap();

        if (!GenericValidator.isBlankOrNull(incomingInvoiceId)) {
            IncomingInvoiceCmd incomingInvoiceCmd = new IncomingInvoiceCmd();
            incomingInvoiceCmd.putParam("incomingInvoiceId", incomingInvoiceId);
            incomingInvoiceCmd.putParam("op", "lightRead");

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(incomingInvoiceCmd, request);
                if (!resultDTO.isFailure()) {
                    incomingInvoiceMap.putAll(resultDTO);
                }
            } catch (AppLevelException e) {
                log.debug("Incoming invoice was deleted..........");
            }
        }

        return incomingInvoiceMap;
    }

}
