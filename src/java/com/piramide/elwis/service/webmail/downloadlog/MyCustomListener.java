package com.piramide.elwis.service.webmail.downloadlog;

import java.util.EventListener;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public interface MyCustomListener extends EventListener {

public void fire(MyCustomEvent event);

}