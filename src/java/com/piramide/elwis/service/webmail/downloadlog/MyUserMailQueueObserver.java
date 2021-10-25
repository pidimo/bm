package com.piramide.elwis.service.webmail.downloadlog;

import java.util.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class MyUserMailQueueObserver implements MyCustomListener {
    private Map<Long, List<Integer>> inQueueMap;
    private Map<Long, List<Integer>> outQueueMap;

    public MyUserMailQueueObserver() {
        inQueueMap = Collections.synchronizedMap(new LinkedHashMap<Long, List<Integer>>());
        outQueueMap = Collections.synchronizedMap(new LinkedHashMap<Long, List<Integer>>());
    }

    public void fire(MyCustomEvent event) {
        if ("QUEUE_USERMAIL".equals(event.getSourceId())) {
            addInQueueMapItem(event.getMillisKey(), event.getDataId());
            //System.out.println("ENTRADA USER MAIL:" + event.getDataId());
        } else if("UNQUEUE_USERMAIL".equals(event.getSourceId())) {
            addOutQueueMapItem(event.getMillisKey(), event.getDataId());
            //System.out.println("SALIDA USER MAIL:" + event.getDataId());
        }
    }

    private void addInQueueMapItem(Long millisKey, Integer userMailId) {
        synchronized (inQueueMap) {
            if (inQueueMap.containsKey(millisKey)) {
                inQueueMap.get(millisKey).add(userMailId);
            } else {
                List<Integer> idList = new ArrayList<Integer>();
                idList.add(userMailId);
                inQueueMap.put(millisKey, idList);
            }
        }
    }

    private void addOutQueueMapItem(Long millisKey, Integer userMailId) {
        synchronized (outQueueMap) {
            if (outQueueMap.containsKey(millisKey)) {
                outQueueMap.get(millisKey).add(userMailId);
            } else {
                List<Integer> idList = new ArrayList<Integer>();
                idList.add(userMailId);
                outQueueMap.put(millisKey, idList);
            }
        }
    }

    public Map<Long, List<Integer>> getInQueueMap() {
        return inQueueMap;
    }

    public Map<Long, List<Integer>> getOutQueueMap() {
        return outQueueMap;
    }

    public Map<Long, List<Integer>> getPreviousUnconsumedQueue() {

        List<Long> inKeyList = new ArrayList<Long>(inQueueMap.keySet());

        for (int i = 0; i < inKeyList.size(); i++) {
            Long key = inKeyList.get(i);
            List<Integer> inList = inQueueMap.get(key);
            List<Integer> outList = outQueueMap.get(key);

            if (inList != null && outList != null) {
                List<Integer> outTemp = new ArrayList<Integer>(outList);

                inList.removeAll(outList);

                if (inList.isEmpty()) {
                    inQueueMap.remove(key);
                    outQueueMap.remove(key);
                } else {
                    outList.removeAll(outTemp);
                }
            }
        }

        return inQueueMap;
    }

}
