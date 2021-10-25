Attribute VB_Name = "Constants"
'Plugin version
Public Const PLUGIN_NAME As String = "Business Manager Plugin"
Public Const PLUGIN_VERSION As String = "3.1.2"

' Global variables
Public ELWIS_CONFIG_PATH As String
Public I18N_MANAGER As I18nManager
Public WORDAPP As Word.Application
Public helpAssistent As Balloon
Public processOnActive As Boolean
Public xmlRecived As String
Public LOG_DEBUG As Boolean
Public log As LogManager
Public documentType As Integer

' XML FILES
Public Const ELWIS_FILE_I18N As String = "XMLResources_"
Public Const ELWIS_FILE_TELECOMS_I18N As String = "_telecoms"

Public Const ELWIS_FILE_TEMPLATE As String = "TemplateFields.xml"
Public Const ELWIS_FILE_TELECOMS_FIELDS As String = "TelecomsFields.xml"
Public Const ELWIS_FILE_TABLE As String = "TableFields.xml"

' Document Types
Public Const ELWIS_TYPE_COMMUNICATION As Integer = 2
Public Const ELWIS_TYPE_TEMPLATE As Integer = 5
Public Const ELWIS_TYPE_CAMPAIGNTEMPLATE As Integer = 4

' Misc Constants
Public Const ELWIS_SENDBTN As String = "ELWIS_SENDBTN"
Public Const ELWIS_CONFIGBTN As String = "ELWIS_CONFIGBTN"
Public Const ELWIS_UPDATEBTN As String = "ELWIS_UPDATEBTN"
Public Const ELWIS_FIELDSBTN As String = "ELWIS_FIELDSBTN"
Public Const ELWIS_COMPANY_TELECOMS_FIELDS As String = "btncompany_telecoms"
Public Const ELWIS_EMPLOYEE_TELECOMS_FIELDS As String = "btnemployee_telecoms"
Public Const ELWIS_ADDRESS_TELECOMS_FIELDS As String = "btnaddress_telecoms"
Public Const ELWIS_COMPANY_BAR_FIELDS As String = "btncompanyfields"
Public Const ELWIS_EMPLOYEE_BAR_FIELDS As String = "btnemployeefields"
Public Const ELWIS_ADDRESS_BAR_FIELDS As String = "btncontactfields"


Public Const ELWIS_CONTINUE As Integer = 0
Public Const ELWIS_REPEAT As Integer = 1
Public Const ELWIS_FAIL As Integer = 2
Public Const ELWIS_CHANGE_TEMPLATEFIELDS As Integer = 3
Public Const ELWIS_FIELDS_ARE_UPDATED As Integer = 3

' Encrypt key
Public Const ELWIS_ENCRYPT_KEY As String = "fm2uf6dm0dy9mq7w"

' Constants Windows Register
Public Const ELWIS_REGISTER_ROOT As String = "Software\Elwis-Client"
Public Const ELWIS_REGISTER_CONFIG As String = ELWIS_REGISTER_ROOT & "\Config"

Public Const ELWIS_REGISTER_ATTRIB_TELECOMS_KEY As String = "telecomsKey"
Public Const ELWIS_REGISTER_ATTRIB_HOST As String = "host"
Public Const ELWIS_REGISTER_ATTRIB_USER As String = "user"
Public Const ELWIS_REGISTER_ATTRIB_PASS As String = "password"
Public Const ELWIS_REGISTER_ATTRIB_SAVEPASS As String = "savepass"
Public Const ELWIS_REGISTER_ATTRIB_COMPANY As String = "company"

Public Const ELWIS_REGISTER_ATTRIB_PROXY As String = "proxy"
Public Const ELWIS_REGISTER_ATTRIB_PROXYPORT As String = "proxyport"
Public Const ELWIS_REGISTER_ATTRIB_PROXYUSER As String = "proxyuser"
Public Const ELWIS_REGISTER_ATTRIB_PROXYPASS As String = "proxypass"
Public Const ELWIS_REGISTER_ATTRIB_LANG As String = "lang"
Public Const ELWIS_REGISTER_ATTRIB_TOOLBARID As String = "barid"
Public Const ELWIS_REGISTER_ATTRIB_FIRSTRUN As String = "firstrun"


' Constants MACROS
Public Const ELWIS_MACROS_ONACTION As String = "doit"

' Constants XML
Public Const ELWIS_ATTRIB_BEGINGROUP As String = "begingroup"
Public Const ELWIS_ATTRIB_KEY As String = "key"
Public Const ELWIS_ATTRIB_NAME As String = "name"
Public Const ELWIS_ATTRIB_VALUE As String = "value"
Public Const ELWIS_ATTRIB_TOOLTIP As String = "tooltip"
Public Const ELWIS_ATTRIB_STYLE As String = "style"
Public Const ELWIS_ATTRIB_TYPE As String = "type"
Public Const ELWIS_ATTRIB_FACEID As String = "faceid"
Public Const ELWIS_ATTRIB_POSITION As String = "position"
Public Const ELWIS_ATTRIB_PROTECTION As String = "protection"
Public Const ELWIS_ATTRIB_VISIBLE As String = "visible"
Public Const ELWIS_ATTRIB_ALIGN As String = "align"



Public Const ELWIS_NODE_GUI As String = "gui"
Public Const ELWIS_NODE_CONTROL As String = "control"
Public Const ELWIS_NODE_SUBCONTROL As String = "subcontrol"
Public Const ELWIS_NODE_OPERATION As String = "operation"
Public Const ELWIS_NODE_PARAMETER As String = "parameter"
Public Const ELWIS_NODE_PARAMETERS As String = "parameters"

Public Type URLPARAMS
uploadId As Integer
fileType As Integer
fileLang As Integer
fileVersion As Integer
End Type

Public Type USER_INFO
user As String
password As String
company As String
End Type
