package com.piramide.elwis.web.common.taglib;

import org.apache.struts.action.ActionMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1.5
 */
public class MessagesMobilTag extends MessagesTag {

    private final Set<String> concurrenceResourceKeySet = new HashSet<String>() {{
        add("Common.error.concurrency");
    }};

    /**
     * Resource messages that should be personalized
     */
    private static final Map<String, String> personalizedResourceMAP = new HashMap<String, String>() {{
        put("Address.duplicatedWarning", "Address.duplicatedWarning.mobile");
    }};


    @Override
    protected ActionMessage preProcessActionMessage(ActionMessage actionMessage) {

        if (actionMessage != null) {
            if (isConcurrenceError(actionMessage.getKey())) {
                pageContext.getRequest().setAttribute("forwardJson", "ConcurrencyFail");
            }

            actionMessage = personalizeActionMessage(actionMessage);
        }

        return actionMessage;
    }

    private boolean isConcurrenceError(String key) {
        return key != null && concurrenceResourceKeySet.contains(key);
    }

    private ActionMessage personalizeActionMessage(ActionMessage actionMessage) {
        ActionMessage personalizedMessage = actionMessage;

        if (personalizedResourceMAP.containsKey(actionMessage.getKey())) {
            String newKey = personalizedResourceMAP.get(actionMessage.getKey());
            personalizedMessage = new ActionMessage(newKey, actionMessage.getValues());
        }

        return personalizedMessage;
    }
}
