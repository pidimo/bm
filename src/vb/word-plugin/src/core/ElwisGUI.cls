VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
  Persistable = 0  'NotPersistable
  DataBindingBehavior = 0  'vbNone
  DataSourceBehavior  = 0  'vbNone
  MTSTransactionMode  = 0  'NotAnMTSObject
END
Attribute VB_Name = "ElwisGui"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = True
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = True
Option Explicit
Private WithEvents sendBtn As CommandBarButton
Attribute sendBtn.VB_VarHelpID = -1
Private WithEvents fieldBtn As CommandBarButton
Attribute fieldBtn.VB_VarHelpID = -1
Private WithEvents configBtn As CommandBarButton
Attribute configBtn.VB_VarHelpID = -1
Private WithEvents updateBtn As CommandBarButton
Attribute updateBtn.VB_VarHelpID = -1


Private Const FACE_SEND As Integer = 270
Private Const FACE_CONFIG As Integer = 548
Private Const FACE_FIELDS As Integer = 464

Private telecomsId As String

Private showMessage As Boolean

' Strutctures
Public Type control
Name As String
type As String
End Type

Public Type GUI
beginGroup As Boolean
faceid As String
key As String
style As String
toolTip As String
End Type

Public Type operation
Name As String
parameters As String
End Type

Public Type POPUP
pGUI As GUI
End Type

Public Type BUTTON
bGUI As GUI
bOPERATION As operation
End Type

Public Type TOOLBAR
key As String
position As String
protection As Integer
visible As Boolean
End Type

Public Sub initialize(progId As String, ByRef cmdBars As Office.CommandBars, isUpdateTelecoms As Boolean)
Dim elwisBar As Office.CommandBar

showMessage = True

Dim fs As Scripting.FileSystemObject
Set fs = New Scripting.FileSystemObject
'ELWIS_CONFIG_PATH = getRegisterKey("installPath") & "Elwis-Data\"
ELWIS_CONFIG_PATH = getApplicationDataPath() & "\Elwis-Plugin\"

' Initializating I18NManager
If (I18N_MANAGER Is Nothing) Then
    Set I18N_MANAGER = New I18nManager
    I18N_MANAGER.initialize getRegisterKey(ELWIS_REGISTER_ATTRIB_LANG)
End If

'log.SaveToLog "CREATE CONTROLS:" & TypeName(cmdBars)

On Error GoTo createToolBar

Set elwisBar = cmdBars(RESOURCE_MAIN_TOOLBAR_I18N)

If elwisBar Is Nothing Then
createToolBar:
    Set elwisBar = cmdBars.Add(Name:=RESOURCE_MAIN_TOOLBAR_I18N, temporary:=False)
    elwisBar.visible = True
    elwisBar.position = msoBarTop
    elwisBar.NameLocal = RESOURCE_MAIN_TOOLBAR_I18N
End If
Set sendBtn = elwisBar.FindControl(type:=msoControlButton, tag:=ELWIS_SENDBTN)
If sendBtn Is Nothing Then
    Set sendBtn = elwisBar.Controls.Add(type:=msoControlButton)
    With sendBtn
        .Caption = I18N_MANAGER.I18N(RESOURCE_TOOLBAR.SENDDOCUMENT)
        .visible = True
        .tag = ELWIS_SENDBTN
        .style = msoButtonIconAndCaption
        .OnAction = "!<" & progId & ">"
        .faceid = FACE_SEND
        '.beginGroup = True
    End With
End If

Set fieldBtn = elwisBar.FindControl(type:=msoControlButton, tag:=ELWIS_FIELDSBTN)
If fieldBtn Is Nothing Then
    Set fieldBtn = elwisBar.Controls.Add(type:=msoControlButton)
    With fieldBtn
        .Caption = I18N_MANAGER.I18N(RESOURCE_TOOLBAR.TEMPLATEFIELDS)
        .visible = True
        .tag = ELWIS_FIELDSBTN
        .style = msoButtonIconAndCaption
        .OnAction = "!<" & progId & ">"
        .faceid = FACE_FIELDS
    End With
    'log.SaveToLog "Begin create Field Bar"
    createTemplateFields cmdBars
    
End If

Set configBtn = elwisBar.FindControl(type:=msoControlButton, tag:=ELWIS_CONFIGBTN)
If configBtn Is Nothing Then
    Set configBtn = elwisBar.Controls.Add(type:=msoControlButton)
    With configBtn
        .Caption = I18N_MANAGER.I18N(RESOURCE_TOOLBAR.Config)
        .visible = True
        .tag = ELWIS_CONFIGBTN
        .style = msoButtonIconAndCaption
        .OnAction = "!<" & progId & ">"
        .faceid = FACE_CONFIG
    End With
End If

Set updateBtn = elwisBar.FindControl(type:=msoControlButton, tag:=ELWIS_UPDATEBTN)
If updateBtn Is Nothing Then
    Set updateBtn = elwisBar.Controls.Add(type:=msoControlButton)
    With updateBtn
        .Caption = I18N_MANAGER.I18N(RESOURCE_TOOLBAR.BTNUPDATEFIELDS)
        .visible = True
        .tag = ELWIS_UPDATEBTN
        .style = msoButtonIconAndCaption
        .OnAction = "!<" & progId & ">"
        .faceid = 1015
    End With
End If
If isUpdateTelecoms Then
createTemplateFields cmdBars, True
End If
End Sub

Private Sub createTemplateFields(ByRef cmdBars As Office.CommandBars, Optional update As Boolean = False)
'   Dim barTemplate
 '   barTemplate = getRegisterKey(RESOURCE_TEMPLATE_TOOLBAR_I18N)
'    If barTemplate = "" Then
        Dim root As IXMLDOMElement
        Dim file As String
        file = ELWIS_FILE_TEMPLATE
        If update Then file = ELWIS_FILE_TELECOMS_FIELDS
        'On Error GoTo xmlerror:
        Set root = getDOMFromFile(file)
        If Not (root Is Nothing) Then
            telecomsId = ""
            If Not update Then
                createFieldBar root, cmdBars
            Else
                UpdateTelecoms root, cmdBars
            End If
            ' Save telecoms id
            setRegisterKey ELWIS_REGISTER_ATTRIB_TELECOMS_KEY, telecomsId
        End If
  '  End If
xmlerror:
'Debug.Print "XMLERROR:" & Err.Description
'log.SaveToLog "XML ERROR:" & Err.Description
End Sub

Private Function getBookmark(parameters As String)
Dim values() As String
Dim param() As String
Dim params() As String
values = Split(parameters, "-")
params = Split(values(1), ",")
param = Split(params(0), "=")
getBookmark = param(1)
End Function

Private Sub UpdateTelecoms(root As IXMLDOMElement, cmdBars As Office.CommandBars)
Dim popNode As IXMLDOMNode

' -------------- For COMPANY ----------------
Set popNode = root.selectSingleNode("//telecom[@name='company']/subcontrol")
updateTelecomFieldList popNode, ELWIS_COMPANY_TELECOMS_FIELDS

' -------------- For EMPLOYEE ----------------
Set popNode = root.selectSingleNode("//telecom[@name='employee']/subcontrol")
updateTelecomFieldList popNode, ELWIS_EMPLOYEE_TELECOMS_FIELDS

' -------------- For ADDRESS ----------------
Set popNode = root.selectSingleNode("//telecom[@name='address']/subcontrol")
updateTelecomFieldList popNode, ELWIS_ADDRESS_TELECOMS_FIELDS
End Sub


Private Sub updateTelecomFieldList(controlNode As IXMLDOMNode, tagTelecom As String)
Dim cmdbar As CommandBar
Set cmdbar = CommandBars.Item(RESOURCE_TEMPLATE_TOOLBAR_I18N)
Dim popBar As CommandBarPopup

Set popBar = cmdbar.FindControl(type:=msoControlPopup, tag:=tagTelecom, Recursive:=True)
If popBar Is Nothing Then
    Dim pop As POPUP
    pop.pGUI = getGui(controlNode.selectSingleNode(ELWIS_NODE_GUI))
    Set popBar = addPopUp(pop, cmdbar)
End If

    If controlNode.hasChildNodes Then
        Dim subcontrol As IXMLDOMNode
        Dim buttons As Scripting.Dictionary
        Set buttons = New Scripting.Dictionary
        
        Dim nodeList As IXMLDOMNodeList
        Set nodeList = controlNode.selectNodes(ELWIS_NODE_SUBCONTROL)

        Dim btnAux As CommandBarButton
        For Each btnAux In popBar.Controls
            btnAux.Delete
        Next btnAux

        For Each subcontrol In nodeList
            Dim btn As BUTTON
            btn.bGUI = getGui(subcontrol.selectSingleNode(ELWIS_NODE_GUI))
            btn.bOPERATION = getOperation(subcontrol.selectSingleNode(ELWIS_NODE_OPERATION))
            addButton btn, popBar
        Next subcontrol
    End If
End Sub

Function isValidField(fieldName As String, ByRef fields As IXMLDOMNodeList, validateFields As Boolean) As Boolean
Dim node As IXMLDOMNode

If Not validateFields Then
isValidField = True
Exit Function
End If

isValidField = False
For Each node In fields
If getValue("value", node.attributes) = fieldName Then
    isValidField = True
    Exit Function
End If
Next
End Function

Function isValidTableField(fieldName As String, ByRef fields As IXMLDOMNodeList) As Boolean
Dim node As IXMLDOMNode
isValidTableField = False
For Each node In fields
If node.text = fieldName Then
    isValidTableField = True
    Exit Function
End If
Next
End Function

Function ValidateTemplateFields() As Boolean
    ValidateTemplateFields = True
Dim mf As MailMergeField
Dim fieldNodes As IXMLDOMNodeList
Dim tableFieldsNodes As IXMLDOMNodeList
Dim telecomNodes As IXMLDOMNodeList
Dim fields As IXMLDOMElement
Dim tableFields As IXMLDOMElement
Dim telecomFields As IXMLDOMElement

'Xml de los fields
Set fields = getDOMFromFile(ELWIS_FILE_TEMPLATE)
Set fieldNodes = fields.selectNodes("//*/parameter[@name='field']")

'Xml de los fields de la tabla de saleprocess
Set tableFields = getDOMFromFile(ELWIS_FILE_TABLE)
Set tableFieldsNodes = tableFields.selectNodes("//*/field")

''---
' Como los telecoms son fields adicionales, no necesariamente existen
' por lo que no se valida.
Dim validateTelecoms As Boolean
validateTelecoms = False
Dim telecomKey
telecomKey = getRegisterKey(ELWIS_REGISTER_ATTRIB_TELECOMS_KEY)
If telecomKey <> "" Then
    Set telecomFields = getDOMFromFile(ELWIS_FILE_TELECOMS_FIELDS)
    Set telecomNodes = telecomFields.selectNodes("//*/parameter[@name='field']")
    validateTelecoms = True
End If
''---
Dim i
i = 0
Dim oldColor

For i = 1 To WORDAPP.ActiveDocument.MailMerge.fields.Count
    Set mf = WORDAPP.ActiveDocument.MailMerge.fields.Item(i)
    Dim field As String
    field = getMergeFieldName(mf)
    If Not isValidField(field, fieldNodes, True) And _
    Not isValidField(field, telecomNodes, validateTelecoms) And _
    Not isValidTableField(field, tableFieldsNodes) Then
        mf.Select
        oldColor = WORDAPP.Selection.Font.Color
        WORDAPP.Selection.Font.Color = wdColorRed
        WORDAPP.Selection.Font.Strikethrough = True
        ValidateTemplateFields = False
    End If
Next
If Not ValidateTemplateFields Then
    WORDAPP.Selection.MoveRight Unit:=wdCharacter, Count:=1
    WORDAPP.Selection.Font.Strikethrough = False
    WORDAPP.Selection.Font.Color = oldColor
    
End If
End Function

Function getMergeFieldName(mf As MailMergeField) As String
Dim Name As String
getMergeFieldName = Trim(Mid(mf.code, 12, InStr(mf.code, " \*") - 12))
End Function

Public Sub UpdateTelecomsFields()

Debug.Print "UpdateTelecomsFields:" & ELWIS_CONFIG_PATH & ELWIS_FILE_TELECOMS_FIELDS
Dim Msg() As String
Dim XML_HEADER As String
XML_HEADER = "<?xml version=" & Chr$(34) & "1.0" & Chr$(34) & " encoding=" & Chr$(34) & "UTF-8" & Chr$(34) & "?>" & vbCrLf
Msg = Split(xmlRecived, "<===>")
'On Error GoTo FAIL
Open ELWIS_CONFIG_PATH & ELWIS_FILE_TELECOMS_FIELDS For Output As #1    ' Fields del telecom
Print #1, XML_HEADER
Print #1, Msg(0)
Close #1    ' Cierra el archivo.

'Debug.Print "ELWIS_CONFIG_PATH & ELWIS_FILE_I18N en ELWIS_FILE_TELECOMS_I18N & .xml:" & ELWIS_CONFIG_PATH & ELWIS_FILE_I18N & "en" & ELWIS_FILE_TELECOMS_I18N & ".xml"
Open ELWIS_CONFIG_PATH & ELWIS_FILE_I18N & "en" & ELWIS_FILE_TELECOMS_I18N & ".xml" For Output As #1 ' Recursos en Ingles
Print #1, XML_HEADER
Print #1, Msg(1)
Close #1    ' Cierra el archivo.

Open ELWIS_CONFIG_PATH & ELWIS_FILE_I18N & "es" & ELWIS_FILE_TELECOMS_I18N & ".xml" For Output As #1  ' Recursos en espaniol
Print #1, XML_HEADER
Print #1, Msg(2)
Close #1    ' Cierra el archivo.

Open ELWIS_CONFIG_PATH & ELWIS_FILE_I18N & "de" & ELWIS_FILE_TELECOMS_I18N & ".xml" For Output As #1   ' Recursos en aleman
Print #1, XML_HEADER
'Print #1, Mid(Msg(3), 1, Len(Msg(3)) - 9)
Print #1, Msg(3)
Close #1    ' Cierra el archivo.

Open ELWIS_CONFIG_PATH & ELWIS_FILE_I18N & "fr" & ELWIS_FILE_TELECOMS_I18N & ".xml" For Output As #1   ' Recursos en frances
Print #1, XML_HEADER
Print #1, Msg(5) 'we must keep the 4 for telecom key for backward compatibility, so new telecoms responses must be added after it.
Close #1    ' Cierra el archivo.



xmlRecived = ""
I18N_MANAGER.initialize (getRegisterKey(ELWIS_REGISTER_ATTRIB_LANG))
createTemplateFields WORDAPP.CommandBars, True
setRegisterKey ELWIS_REGISTER_ATTRIB_TELECOMS_KEY, Trim(Msg(4))

End Sub

Private Function validateDocument(ids As String) As Boolean
Dim id As String
Dim kind As String

Dim varComment
validateDocument = False
On Error GoTo FAIL
Dim execute As Boolean
varComment = WORDAPP.ActiveDocument.BuiltInDocumentProperties(wdPropertyComments)
'log.SaveToLog " IDS:" & ids & " - " & varComment

If CBool(varComment = "readonly") Then
Exit Function
End If

Dim data
data = Split(ids, ":")
If CStr(data(0)) = "2" Or CStr(data(0)) = "4" Or CStr(data(0)) = "5" Then
Dim params
params = Split(data(1), "-")
If UBound(params) = 1 Then
validateDocument = True
End If
End If
FAIL:
End Function

Private Function getDocumentType(ids As String) As Integer
getDocumentType = -1
Dim data
On Error GoTo chau
data = Split(ids, ":")
getDocumentType = CInt(data(0))
chau:
End Function

Private Sub sendBtn_Click(ByVal Ctrl As Office.CommandBarButton, CancelDefault As Boolean)
    ' Si el documento es invalido no hace NADA!!!!!
    If Not validateDocument(decode(WORDAPP.ActiveDocument.BuiltInDocumentProperties(wdPropertyAuthor))) Then ' Invalid document
        Exit Sub
    End If
    
    frmSend.processActive Me, True, False
    
    Dim sendTheFile As Boolean
    sendTheFile = frmSend.continueUpload
    
    If isTemplate Then
        If Not ValidateTemplateFields() Then
            sendTheFile = False
            sucessMsg RESOURCE_MSG.ERRTEMPLATEFIELDSOLD
        Else
        ' removes and add again the campaign bookmark if it does exist
        ' The only one page bookmark is campaign, this the reason is here.
        ' If in the future there will be more than it, this call must be
        ' updated in order to be generic according the XML configurations
        ' of type page and operation addBookmark
         If isCampaignTemplate Then
            reAddPageBookmark "campaign"
         End If
        End If
    End If
    
    If sendTheFile Then
        frmSend.showAndSendFile Me
    End If
    
End Sub
Private Sub configBtn_Click(ByVal Ctrl As Office.CommandBarButton, CancelDefault As Boolean)
    frmConfig.Show vbModal
End Sub

Private Function isTemplate() As Boolean
Dim docType
    docType = getDocumentType(decode(WORDAPP.ActiveDocument.BuiltInDocumentProperties(wdPropertyAuthor)))
    isTemplate = ELWIS_TYPE_CAMPAIGNTEMPLATE = docType Or ELWIS_TYPE_TEMPLATE = docType
End Function
Private Function isCampaignTemplate() As Boolean
Dim docType
    docType = getDocumentType(decode(WORDAPP.ActiveDocument.BuiltInDocumentProperties(wdPropertyAuthor)))
    isCampaignTemplate = ELWIS_TYPE_CAMPAIGNTEMPLATE = docType
End Function

Private Function notIsElwisFile() As Boolean
Dim docType
    docType = getDocumentType(decode(WORDAPP.ActiveDocument.BuiltInDocumentProperties(wdPropertyAuthor)))
    notIsElwisFile = ELWIS_TYPE_CAMPAIGNTEMPLATE <> docType _
                     And ELWIS_TYPE_TEMPLATE <> docType _
                     And ELWIS_TYPE_COMMUNICATION <> docType
End Function

Private Sub updateBtn_Click(ByVal Ctrl As Office.CommandBarButton, CancelDefault As Boolean)
   frmSend.processActive Me, True
   If frmSend.continueUpload Then
    'If isTemplate Then
        If notIsElwisFile Or isTemplate Then
            If Not ValidateTemplateFields() Then
                sucessMsg RESOURCE_MSG.ERRTEMPLATEFIELDSOLD
            Else
                sucessMsg RESOURCE_MSG.FIELDSUPDATE
            End If
        Else
            sucessMsg RESOURCE_MSG.FIELDSUPDATE
        End If
     'End If
    End If
End Sub

Private Sub fieldBtn_Click(ByVal Ctrl As Office.CommandBarButton, CancelDefault As Boolean)
Dim cmdbar As CommandBar
On Error GoTo createToolTempBar
Set cmdbar = WORDAPP.CommandBars(RESOURCE_TEMPLATE_TOOLBAR_I18N)
Dim helpAssEnable As Boolean
helpAssEnable = (getRegisterKey("disableHelp") = "false")


If Ctrl.State = msoButtonUp Then
    Ctrl.State = msoButtonDown
    cmdbar.visible = True
    If helpAssEnable Then
        Set helpAssistent = WORDAPP.Assistant.NewBalloon
        With helpAssistent
        .Heading = I18N_MANAGER.I18N(RESOURCE_ASSISTENT.WINDOW_TITLE)
        .text = I18N_MANAGER.I18N(RESOURCE_ASSISTENT.text)
        .Icon = msoIconNone
        .Mode = msoModeModeless
        .BUTTON = msoButtonSetNone
        .Show
        End With
    End If
Else
    Ctrl.State = msoButtonUp
    cmdbar.visible = False
    If helpAssEnable And Not (helpAssistent Is Nothing) Then
    helpAssistent.Close
    End If
End If
'WORDAPP.CommandBars.Item(CInt(getRegisterKey(RESOURCE_TEMPLATE_TOOLBAR_I18N))).visible = True

createToolTempBar:
'WORDAPP.CommandBars.Item(CInt(getRegisterKey(RESOURCE_TEMPLATE_TOOLBAR_I18N))).visible = True

End Sub
Private Sub createFieldBar(root As IXMLDOMElement, cmdBars As Office.CommandBars)
Dim NodeValue
Dim attrib As IXMLDOMAttribute
Dim toolbarNode As IXMLDOMNode
Dim control As IXMLDOMNode
For Each toolbarNode In root.childNodes
Dim cbar As CommandBar
Set cbar = addToolBar(getToolBar(toolbarNode.attributes), cmdBars)
    For Each control In toolbarNode.selectNodes(ELWIS_NODE_CONTROL)
    processToolBar control, cbar
    Next
Next
End Sub
'Private Sub processToolBar(controlNode As IXMLDOMNode, ByRef cbar As CommandBar)
Private Sub processToolBar(controlNode As IXMLDOMNode, ByRef cbar As Object)
Dim node As IXMLDOMNode
Dim btn As BUTTON

    
    If controlNode.hasChildNodes Then
    Select Case getTypeControl(controlNode)
    Case "popup"
    Dim pop As POPUP
    Dim popBar As CommandBarPopup
    Dim subcontrol As IXMLDOMNode
    pop.pGUI = getGui(controlNode.selectSingleNode(ELWIS_NODE_GUI))
    Set popBar = addPopUp(pop, cbar)
    'log.SaveToLog "POPUP:" & popBar.Caption & " - TAG:" & popBar.tag
    
    For Each subcontrol In controlNode.selectNodes(ELWIS_NODE_SUBCONTROL)
        If getTypeControl(subcontrol) = "popup" Then
            Call processToolBar(subcontrol, popBar)
        Else
        btn.bGUI = getGui(subcontrol.selectSingleNode(ELWIS_NODE_GUI))
        btn.bOPERATION = getOperation(subcontrol.selectSingleNode(ELWIS_NODE_OPERATION))
        
        Dim pos As Integer
        pos = InStr(1, btn.bOPERATION.parameters, "elwis_tel_")
        If pos > 0 Then
            Dim pos2 As Integer
            Dim telecom As String
            pos2 = InStr(pos + 10, btn.bOPERATION.parameters, ",")
            If pos2 = 0 Then
            telecom = Mid(btn.bOPERATION.parameters, pos + 10)
            Else
            telecom = Mid(btn.bOPERATION.parameters, pos + 10, pos2 - (pos + 10))
            End If
            telecomsId = telecom & ", " & telecomsId
            'log.SaveToLog "TelecomID:" & telecom
        End If
        addButton btn, popBar
        End If
    Next
    Set popBar = Nothing
    Case "button"
    btn.bGUI = getGui(controlNode.selectSingleNode(ELWIS_NODE_GUI))
    btn.bOPERATION = getOperation(controlNode.selectSingleNode(ELWIS_NODE_OPERATION))
    addButton btn, cbar
    End Select
    End If
    'log.SaveToLog "TELECOMSFIELDS>>>>>>:" & telecomsId
End Sub


Private Function getToolBar(attributes As IXMLDOMNamedNodeMap) As TOOLBAR
getToolBar.key = getValue(ELWIS_ATTRIB_KEY, attributes)
getToolBar.visible = Not CBool(getValue(ELWIS_ATTRIB_VISIBLE, attributes) = "false")
getToolBar.protection = Val(getValue(ELWIS_ATTRIB_PROTECTION, attributes))
getToolBar.position = getValue(ELWIS_ATTRIB_POSITION, attributes)
End Function

Private Function getTypeControl(control As IXMLDOMNode) As String
getTypeControl = getValue(ELWIS_ATTRIB_TYPE, control.attributes)
End Function

Private Function getGui(guiNode As IXMLDOMNode) As GUI
getGui.key = getValue(ELWIS_ATTRIB_KEY, guiNode.attributes)
getGui.toolTip = getValue(ELWIS_ATTRIB_TOOLTIP, guiNode.attributes)
If getValue(ELWIS_ATTRIB_BEGINGROUP, guiNode.attributes) = "true" Then getGui.beginGroup = True
getGui.style = getValue(ELWIS_ATTRIB_STYLE, guiNode.attributes)
getGui.faceid = getValue(ELWIS_ATTRIB_FACEID, guiNode.attributes)
End Function

Private Function getOperation(operationNode As IXMLDOMNode) As operation
Dim param As IXMLDOMNode
Dim parameters As String
Dim i As Integer

getOperation.Name = getValue(ELWIS_ATTRIB_NAME, operationNode.attributes)

If operationNode.selectSingleNode(ELWIS_NODE_PARAMETERS).hasChildNodes Then

Dim paramLen As Integer
Dim parameterNodes As IXMLDOMNodeList
Set parameterNodes = operationNode.selectSingleNode(ELWIS_NODE_PARAMETERS).selectNodes(ELWIS_NODE_PARAMETER)

paramLen = parameterNodes.Length

    parameters = ""
    i = 1
    For Each param In parameterNodes
            parameters = parameters & getValue(ELWIS_ATTRIB_NAME, param.attributes) & "=" & getValue(ELWIS_ATTRIB_VALUE, param.attributes)
            If i < paramLen Then parameters = parameters & ","
            i = i + 1
    Next
End If
getOperation.parameters = parameters
''log.SaveToLog parameters
End Function

Private Function getValue(Name As String, attributes As IXMLDOMNamedNodeMap) As String
'On Error GoTo ErrorHandler
Dim obj As Object
Set obj = attributes.getNamedItem(Name)
If obj Is Nothing Then
    getValue = ""
    Exit Function
End If
getValue = obj.NodeValue
End Function

Private Sub addButton(btn As Variant, ByRef cbar As Object)
Dim btnBar As CommandBarButton
Set btnBar = cbar.Controls.Add(type:=msoControlButton, temporary:=False)
With btnBar
    If btn.bGUI.faceid <> -1 Then .faceid = btn.bGUI.faceid
    Dim nameKey As String
    
    nameKey = btn.bGUI.key
    .Caption = I18N_MANAGER.I18NXML(nameKey)
    .Parameter = btn.bOPERATION.Name & "-" & btn.bOPERATION.parameters & ",tooltip=" & btn.bGUI.toolTip
    .tag = nameKey
    .beginGroup = btn.bGUI.beginGroup
    .OnAction = ELWIS_MACROS_ONACTION
    
        
    Select Case btn.bGUI.style
    Case "icon-caption"
    .style = msoButtonIconAndCaption
    Case "icon"
    .style = msoButtonIcon
    Case "caption"
    .style = msoButtonCaption
    End Select
End With
End Sub

Private Function addPopUp(pop As POPUP, ByRef cbar As Object) As CommandBarPopup
Set addPopUp = cbar.Controls.Add(msoControlPopup)
                                 
With addPopUp
    .Caption = I18N_MANAGER.I18NXML(pop.pGUI.key)
    .ToolTipText = I18N_MANAGER.I18NXML(pop.pGUI.toolTip)
    .beginGroup = pop.pGUI.beginGroup
    .tag = pop.pGUI.key
End With
End Function

Function addToolBar(bar As TOOLBAR, cmdBars As Office.CommandBars) As CommandBar
    On Error GoTo tempFail
    Set addToolBar = cmdBars(RESOURCE_TEMPLATE_TOOLBAR_I18N)
    If addToolBar Is Nothing Then
tempFail:
    Set addToolBar = cmdBars.Add(position:=msoBarTop, temporary:=False)
    With addToolBar
    .Name = RESOURCE_TEMPLATE_TOOLBAR_I18N
    .NameLocal = RESOURCE_TEMPLATE_TOOLBAR_I18N
    'I18N_MANAGER.I18NXML(bar.key)
    .visible = False
    .protection = bar.protection
'    setRegisterKey RESOURCE_TEMPLATE_TOOLBAR_I18N, addToolBar.Index
        Select Case bar.position
        Case "top"
        .position = msoBarTop
        Case "botton"
        .position = msoBarBottom
        Case "left"
        .position = msoBarLeft
        Case "right"
        .position = msoBarRight
        Case Else
        .position = msoBarFloating
        End Select
    End With
    End If
End Function

Sub doit()
'log.SaveToLog "Method:" & Application.CommandBars.ActionControl.tag & "(" & Application.CommandBars.ActionControl.Parameter & ")"
End Sub

Sub finish()
sendBtn.Delete
Set sendBtn = Nothing
configBtn.Delete
Set configBtn = Nothing
fieldBtn.Delete
Set fieldBtn = Nothing
End Sub


Public Sub dispose()
Set sendBtn = Nothing
Set fieldBtn = Nothing
Set configBtn = Nothing
End Sub
