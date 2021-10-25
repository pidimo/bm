<%@ include file="/Includes.jsp" %>

"accessRight" : {
    "CONTACT" : {
        "VIEW" : "${app2:hasAccessRight(pageContext.request,'CONTACT' ,'VIEW')}",
        "CREATE" : "${app2:hasAccessRight(pageContext.request,'CONTACT' ,'CREATE')}",
        "UPDATE" : "${app2:hasAccessRight(pageContext.request,'CONTACT' ,'UPDATE')}",
        "DELETE" : "${app2:hasAccessRight(pageContext.request,'CONTACT' ,'DELETE')}"
    },
    "CONTACTPERSON" : {
        "VIEW" : "${app2:hasAccessRight(pageContext.request,'CONTACTPERSON' ,'VIEW')}",
        "CREATE" : "${app2:hasAccessRight(pageContext.request,'CONTACTPERSON' ,'CREATE')}",
        "UPDATE" : "${app2:hasAccessRight(pageContext.request,'CONTACTPERSON' ,'UPDATE')}",
        "DELETE" : "${app2:hasAccessRight(pageContext.request,'CONTACTPERSON' ,'DELETE')}"
    },
    "APPOINTMENT" : {
        "VIEW" : "${app2:hasAccessRight(pageContext.request,'APPOINTMENT' ,'VIEW')}",
        "CREATE" : "${app2:hasAccessRight(pageContext.request,'APPOINTMENT' ,'CREATE')}",
        "UPDATE" : "${app2:hasAccessRight(pageContext.request,'APPOINTMENT' ,'UPDATE')}",
        "DELETE" : "${app2:hasAccessRight(pageContext.request,'APPOINTMENT' ,'DELETE')}"
    },
    "APPOINTMENTPARTICIPANT" : {
        "VIEW" : "${app2:hasAccessRight(pageContext.request,'APPOINTMENTPARTICIPANT' ,'VIEW')}",
        "CREATE" : "${app2:hasAccessRight(pageContext.request,'APPOINTMENTPARTICIPANT' ,'CREATE')}",
        "UPDATE" : "${app2:hasAccessRight(pageContext.request,'APPOINTMENTPARTICIPANT' ,'UPDATE')}",
        "DELETE" : "${app2:hasAccessRight(pageContext.request,'APPOINTMENTPARTICIPANT' ,'DELETE')}"
    },
    "PRODUCT" : {
        "VIEW" : "${app2:hasAccessRight(pageContext.request,'PRODUCT' ,'VIEW')}",
        "CREATE" : "${app2:hasAccessRight(pageContext.request,'PRODUCT' ,'CREATE')}",
        "UPDATE" : "${app2:hasAccessRight(pageContext.request,'PRODUCT' ,'UPDATE')}",
        "DELETE" : "${app2:hasAccessRight(pageContext.request,'PRODUCT' ,'DELETE')}"
    },
    "MAIL" : {
        "VIEW" : "${app2:hasAccessRight(pageContext.request,'MAIL' ,'VIEW')}",
        "DELETE" : "${app2:hasAccessRight(pageContext.request,'MAIL' ,'DELETE')}",
        "EXECUTE" : "${app2:hasAccessRight(pageContext.request,'MAIL' ,'EXECUTE')}"
    }
}
