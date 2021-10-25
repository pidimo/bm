    //<%@ page import="com.jatun.common.web.JavaScriptEncoder"%>
        //<%@ page import="com.piramide.elwis.web.common.util.JSPHelper" %>
        //<%@ page language="java" contentType="text/javascript; charset=UTF-8"%>
        //<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
        // st-xmlhttp v2.0, add support to show tooltip message
        // modified by: miky
        //

            <%
                //constant messages
                request.setAttribute("EXPIRED", JavaScriptEncoder.encode(JSPHelper.getMessage(request, "Common.sessionExpired")));
                request.setAttribute("ERROR", JavaScriptEncoder.encode(JSPHelper.getMessage(request, "error.tooltip.unexpected")));
                request.setAttribute("LOADING", JavaScriptEncoder.encode(JSPHelper.getMessage(request, "Common.message.loading")));
            %>

        // Creates a new Ajax object.
            function Ajax() {
                this.countRequest = 0;
                this.showDivId = null;
            };

        //set id of an HTMLDOM DIV element to show tolltip message
            Ajax.prototype.setShowMessageDivId = function(id) {
                if (id != null && id != undefined && id.length > 0) {
                    this.showDivId = id;
                } else {
                    alert("not valid HTLDOM id");
                }
            };

            Ajax.prototype.makeHttpRequest = function(url, callback_function, return_xml, errorprocess_function) {
                //to show tooltip
                var ajaxObj = this;
                if (ajaxObj.showDivId != null) {
                    ajaxObj.tooltipShow('${LOADING}');
                    ajaxObj.countRequest++;
                }

                var http_request = false;

                if (window.XMLHttpRequest) { // Mozilla, Safari,...
                    http_request = new XMLHttpRequest();
                    if (http_request.overrideMimeType) {
                        http_request.overrideMimeType('text/xml');
                    }
                } else if (window.ActiveXObject) { // IE
                    try {
                        http_request = new ActiveXObject("Msxml2.XMLHTTP");
                    } catch (e) {
                        try {
                            http_request = new ActiveXObject("Microsoft.XMLHTTP");
                        } catch (e) {
                        }
                    }
                }

                if (!http_request) {
                    alert('Unfortunatelly you browser doesn\'t support this feature.');
                    return false;
                }
                http_request.onreadystatechange = function() {
                    if (http_request.readyState == 4) {
                        if (http_request.status == 200) {
                            if (return_xml) {
                                eval(callback_function + '(http_request.responseXML)');
                            } else {
                                eval(callback_function + '(http_request.responseText)');
                            }

                            //hide tooltip
                            if (ajaxObj.showDivId != null) {
                                ajaxObj.tooltipHide();
                            }
                        } else {

                            if (errorprocess_function != null && errorprocess_function != undefined) {
                                eval(errorprocess_function + '(http_request.status)');
                            }

                            //message error process
                            if (ajaxObj.showDivId != null) {
                                ajaxObj.errorProcess(http_request.status);
                            } else {
                                alert('There was a problem with the request.(Code: ' + http_request.status + ')');
                            }
                        }
                    }
                }
                http_request.open('GET', url, true);
                http_request.setRequestHeader("isAjaxRequest", "true");
                http_request.send(null);
            };


        /*server response error process*/
            Ajax.prototype.errorProcess = function(requestStatusCode) {
                this.countRequest--;
                if (requestStatusCode == 404) { //session expired http request status code
                    this.tooltipShow('${EXPIRED}');
                } else {
                    this.tooltipShow('${ERROR}');
                }
            };

            Ajax.prototype.tooltipShow = function(message) {
                var msgDiv = document.getElementById(this.showDivId);
                msgDiv.innerHTML = unescape(message);
                msgDiv.style.visibility = "visible";
            };

            Ajax.prototype.tooltipHide = function() {
                var msgDiv = document.getElementById(this.showDivId);
                this.countRequest--;
                if (this.countRequest <= 0) {
                    msgDiv.style.visibility = "hidden";
                    this.countRequest = 0;
                } else {
                    this.tooltipShow('${LOADING}');
                }
            };
