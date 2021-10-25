package com.piramide.elwis.utils.webmail;

import com.piramide.elwis.utils.WebMailConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Util to manage mail state as bit code
 * @author Miguel A. Rojas Cardenas
 * @version 4.10
 */
public class MailStateUtil {
    private static Log log = LogFactory.getLog(MailStateUtil.class);

    public static final int DEFAULT = 0;

    public static final int READ = 1;
    public static final int ANSWERED = 2;
    public static final int SEND = 4;
    public static final int FORWARD = 8;

    public static Byte getDefault() {
        return (byte) DEFAULT;
    }

    public static boolean hasMailState(Byte mailState, int stateToVerify) {
        return mailState != null && (stateToVerify == (mailState.byteValue() & stateToVerify));
    }

    public static boolean hasReadState(Byte state) {
        return hasMailState(state, READ);
    }

    public static boolean hasAnsweredState(Byte state) {
        return hasMailState(state, ANSWERED);
    }

    public static boolean hasSendState(Byte state) {
        return hasMailState(state, SEND);
    }

    public static boolean hasForwardState(Byte state) {
        return hasMailState(state, FORWARD);
    }

    public static Byte addReadState(Byte state) {
        return addState(state, READ);
    }

    public static Byte addAnsweredState(Byte state) {
        state = removeForwardState(state);
        return addState(state, ANSWERED);
    }

    public static Byte addAnsweredState() {
        Byte state = addReadState(getDefault());
        return addAnsweredState(state);
    }

    public static Byte addSendState(Byte state) {
        return addState(state, SEND);
    }

    public static Byte addSendState() {
        Byte state = addReadState(getDefault());
        return addSendState(state);
    }

    public static Byte addForwardState(Byte state) {
        state = removeAnsweredState(state);
        return addState(state, FORWARD);
    }

    public static Byte addForwardState() {
        Byte state = addReadState(getDefault());
        return addForwardState(state);
    }

    public static Byte removeReadState(Byte state) {
        return removeState(state, READ);
    }

    public static Byte removeAnsweredState(Byte state) {
        return removeState(state, ANSWERED);
    }

    public static Byte removeSendState(Byte state) {
        return removeState(state, SEND);
    }

    public static Byte removeForwardState(Byte state) {
        return removeState(state, FORWARD);
    }

    public static Byte markAs(Byte state, String markAsConstant) {
        if (WebMailConstants.MarkAs.READ.equal(markAsConstant)) {
            state = addReadState(state);
        } else if (WebMailConstants.MarkAs.UNREAD.equal(markAsConstant)) {
            state = removeReadState(state);
        }
        return state;
    }

    private static Byte addState(Byte state, int stateToAdd) {
        state = fixState(state);
        if (!hasMailState(state, stateToAdd)) {
            state =(byte) (state.byteValue() + stateToAdd);
        }
        return state;
    }

    private static Byte removeState(Byte state, int stateToRomove) {
        state = fixState(state);
        if (hasMailState(state, stateToRomove)) {
            state =(byte) (state.byteValue() - stateToRomove);
        }
        return state;
    }

    private static Byte fixState(Byte state) {
        return state != null ? state : getDefault();
    }
}
