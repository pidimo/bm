VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
  Persistable = 0  'NotPersistable
  DataBindingBehavior = 0  'vbNone
  DataSourceBehavior  = 0  'vbNone
  MTSTransactionMode  = 0  'NotAnMTSObject
END
Attribute VB_Name = "Core"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = True
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = True
Option Explicit
   Dim guiManager As ElwisGui
   Dim fieldsSalesProcess As Scripting.Dictionary
   Private WithEvents wordEvent As Word.Application
Attribute wordEvent.VB_VarHelpID = -1
   Dim APPLICATION_DATA_PATH As String
   Private Const ELWIS_INDENTIFIER As String = "<*>"
   
 

Public Sub setWordApplication(v As Object, progId As String)
 Set WORDAPP = v
 Set wordEvent = v
 
 
 
 
 Dim isUpdateTelecoms As Boolean
 isUpdateTelecoms = False
 
 If getRegisterKey("changeLang") = "true" Then
 Dim bar As CommandBar
 isUpdateTelecoms = True
   
 Set bar = v.CommandBars(RESOURCE_TEMPLATE_TOOLBAR_I18N)
 bar.Delete
 Set bar = Nothing
 
 Set bar = v.CommandBars(RESOURCE_MAIN_TOOLBAR_I18N)
 bar.Delete
 Set bar = Nothing
 
 setRegisterKey "changeLang", "false"
 End If
 
 If guiManager Is Nothing Then
 Set guiManager = New ElwisGui
 guiManager.initialize progId, WORDAPP.CommandBars, isUpdateTelecoms
 End If
 
 'Borrando document de la carpeta temporal
 Dim fs As Scripting.FileSystemObject
 Set fs = New Scripting.FileSystemObject

 Dim files As String
 On Error GoTo sinborrar
 
 files = fs.GetSpecialFolder(2) & "\BMDoc*.doc"
 fs.DeleteFile files, True
 
 
sinborrar:
Set fs = Nothing
 'Inicializando el ApplicationDataPath para leer los archivos xml

 
 'Set helpTimer = New VB.ti
 ' Cuando hay varios word que se abren, controlar cuando cierra WORD, que solo el ultimo
 'WORD que se este ejecutanto libere los recursos.
End Sub

' Este evente sirve para poder mostrar la ayuda en caso de se realize el evento
' en una variable
Private Sub wordEvent_WindowBeforeDoubleClick(ByVal Sel As Selection, Cancel As Boolean)
    'Debug.Print "Bcount:" & Sel.Bookmarks.Count
    Dim elwis_fields() As Variant
    elwis_fields = Array("address_", "salesprocess_", "person_", "company_", "employee_")
    If (getRegisterKey("disableHelp") = "false") Then
        Dim book As bookmark
        For Each book In Sel.Bookmarks
        Dim i
            For i = 0 To UBound(elwis_fields)
            If InStr(1, book.Name, CStr(elwis_fields(i))) > 0 Then
                showToolTip "field=" & book.Name & ",tooltip=tip" & book.Name
                Exit Sub
            End If
            Next
        Next
    End If
End Sub

'----------------------------
'Antes se usaba para poder actualizar las variables del documento, cuando
'se modificaba algun valor del communication
'Private Sub wordEvent_DocumentOpen(ByVal Doc As Document)
    ' No more update field support
'    If isValidDocument(Doc.BuiltInDocumentProperties(wdPropertyAuthor)) Then
'        Dim com As String
'        updateFieldValues Doc.BuiltInDocumentProperties(wdPropertyComments), Doc.Bookmarks
'    End If
'End Sub
'----------------------------
Private Sub wordEvent_Quit()
    On Error GoTo FAIL
    wordEvent.CommandBars(RESOURCE_MAIN_TOOLBAR_I18N).visible = True
    'wordEvent.CommandBars(RESOURCE_TEMPLATE_TOOLBAR_I18N).visible = False
FAIL:
    dispose
End Sub


Sub updateFieldValues(com As String, ByRef mBookmarks As Word.Bookmarks)
Dim data As String

If InStr(1, com, ELWIS_INDENTIFIER) <> 0 Then
        data = Mid(com, Len(ELWIS_INDENTIFIER) + 1)
        Dim fValue
        Dim i
        fValue = Split(data, "|")
        If UBound(fValue) > 0 Then
        For i = 0 To UBound(fValue)
        Dim field
        field = Split(fValue(i), "=")
        If UBound(field) > 0 Then
        UpdateBookmark CStr(field(0)), CStr(field(1)), mBookmarks
        End If
        Next
        End If
        End If
End Sub

Sub UpdateBookmark(BookmarkToUpdate As String, TextToUse As String, ByRef mBookmarks As Word.Bookmarks)
    Dim BMRange As Range
    On Error GoTo noFound
    Set BMRange = WORDAPP.ActiveDocument.Bookmarks(BookmarkToUpdate).Range
    BMRange.text = TextToUse
    WORDAPP.ActiveDocument.Bookmarks.Add BookmarkToUpdate, BMRange
    GoTo Found
noFound:
'log.SaveToLog "No existe:" & BookmarkToUpdate & " - " & TextToUse
Found:
End Sub

Private Function isValidDocument(value As String) As Boolean
Dim data
data = Split(decode(value), ":")
isValidDocument = False
If UBound(data) > 0 Then
Select Case CStr(data(0))
'Case "temp", "cont", "camp"
Case "2"
isValidDocument = True
End Select
End If
End Function

Public Sub process(tag As String, parameters As String)
'log.SaveToLog "BUTTON:" & tag
'log.SaveToLog "PARAMETERS:" & parameters

Dim values() As String
Dim helpAssEnable As Boolean
values = Split(parameters, "-")

helpAssEnable = (getRegisterKey("disableHelp") = "false")

If helpAssEnable Then showToolTip values(1)

Select Case values(0)
Case "addField"
processAddField values(1)
Case "addBookmark"
processBookMark values(1)
Case "addTable"
processSalesProcess values(1)
End Select
End Sub

Private Sub showToolTip(paramsStr As String)
Dim title As String
Dim tip As String
Dim var As String
Dim i
Dim params() As String
params = Split(paramsStr, ",")
title = I18N_MANAGER.I18N(RESOURCE_ASSISTENT.WINDOW_TITLE)
var = ""
Debug.Print "Params:" & paramsStr
For i = 0 To UBound(params)
    If (InStr(1, params(i), "field") > 0) Then
        var = Mid(params(i), InStr(1, params(i), "=") + 1)
    ElseIf InStr(1, params(i), "tooltip") > 0 Then
        tip = Mid(params(i), InStr(1, params(i), "=") + 1)
    End If
Next
Debug.Print var & " - " & tip
If var <> "" Then title = title & vbCrLf & "<" & I18N_MANAGER.I18NXML(var) & ">"
showAssistent title, tip
ActiveerTimer (10)
End Sub

Private Sub showAssistent(title As String, tip As String)

If Not (WORDAPP.Assistant.NewBalloon Is Nothing) Then

If WORDAPP.Assistant.visible = False Then
WORDAPP.Assistant.visible = True
End If

On Error GoTo helpAssInit
If helpAssistent Is Nothing Then
helpAssInit:
Set helpAssistent = WORDAPP.Assistant.NewBalloon
With helpAssistent
    .Icon = msoIconNone
    .Mode = msoModeModeless
    .BUTTON = msoButtonSetNone
End With
End If

With helpAssistent
    .Heading = title
    .text = I18N_MANAGER.I18NXML(tip, True)
    .Close
    .Show
End With


End If

End Sub

Private Sub processAddField(data As String)
Dim Name As String
Dim toolTip As String
Dim hasEdit As Boolean
Dim param() As String
Dim params() As String
params = Split(data, ",")

'log.SaveToLog "processAddField - Size Parameter:" & UBound(params)
param = Split(params(0), "=")
Name = param(1)
hasEdit = True
If UBound(params) = 1 And InStr(1, params(1), "tooltip") = 0 Then
    param = Split(params(1), "=")
    hasEdit = CBool(param(1))
End If
'addField Name, hasEdit
addField Name, False ' For bookmark problem, now update no more :(
End Sub

Private Sub processBookMark(data As String)
Dim param() As String
Dim params() As String
Dim bookmark As String
Dim bookmarkType As String
params = Split(data, ",")
' Extract bookmarkType parameter
param = Split(params(0), "=")
bookmarkType = param(1)
' Extract bookmark parameter
param = Split(params(1), "=")
bookmark = param(1)


'log.SaveToLog "processBookMark - Size Parameter:" & UBound(params)
Select Case bookmarkType
Case "PAGE"
' Re add the campaign bookmark
reAddPageBookmark bookmark
'log.SaveToLog "Add Page Bookmark:" & bookmark
End Select
End Sub


Private Sub addField(VarName As String, hasEdit As Boolean)
Dim mergefield As String
Dim f As field
    mergefield = "MERGEFIELD  " & I18N_MANAGER.I18NXML(VarName) & " "
    
    'Debug.Print "Merge Field: " & mergefield
    'log.SaveToLog "MERGE FIELD: " & mergefield
    
    Set f = WORDAPP.Selection.fields.Add(Range:=WORDAPP.Selection.Range, type:=wdFieldEmpty, text:=mergefield, PreserveFormatting:=True)
    f.code.text = " MERGEFIELD " & VarName & " \* MERGEFORMAT"
    
    If hasEdit Then
        WORDAPP.Selection.MoveLeft Unit:=wdWord, Count:=1, Extend:=wdExtend
        With WORDAPP.ActiveDocument.Bookmarks
            .Add Range:=WORDAPP.Selection.Range, Name:=VarName
            .DefaultSorting = wdSortByName
            .ShowHidden = True
        End With
        
        WORDAPP.Selection.MoveRight Unit:=wdCharacter, Count:=1
    End If
End Sub

Private Sub addRowBookMark(var As String)
    WORDAPP.Selection.SelectRow
    With WORDAPP.ActiveDocument.Bookmarks
        .Add Range:=WORDAPP.Selection.Range, Name:=var
        .DefaultSorting = wdSortByName
        .ShowHidden = False
    End With
End Sub


Private Sub processSalesProcess(data As String)

Dim root As IXMLDOMElement
Set root = getDOMFromFile(ELWIS_FILE_TABLE)

Dim param() As String
Dim params() As String

Dim fields As New Scripting.Dictionary
Dim fieldAttributes As Scripting.Dictionary

Dim node As IXMLDOMNode
Dim colum As IXMLDOMNode
'log.SaveToLog "--SalesProcess--"
'log.SaveToLog "DATA:" & data
For Each node In root.childNodes
params = Split(data, ",")
param = Split(params(0), "=")
'log.SaveToLog "Attrib Node:" & node.attributes.getNamedItem(ELWIS_ATTRIB_NAME).NodeValue
If node.attributes.getNamedItem(ELWIS_ATTRIB_NAME).NodeValue = param(1) Then
For Each colum In node.childNodes


Set fieldAttributes = New Scripting.Dictionary
'currently key and align attributes are supported
fieldAttributes.Add ELWIS_ATTRIB_KEY, colum.attributes.getNamedItem(ELWIS_ATTRIB_KEY).NodeValue
fieldAttributes.Add ELWIS_ATTRIB_ALIGN, colum.attributes.getNamedItem(ELWIS_ATTRIB_ALIGN).NodeValue


fields.Add colum.firstChild.text, fieldAttributes
Next
End If
Next
addSalesProcessTable param(1), fields

End Sub

Private Sub addSalesProcessTable(Name As String, columns As Scripting.Dictionary)
Dim i
Dim fieldAttributes
Dim field

WORDAPP.ActiveDocument.Tables.Add Range:=WORDAPP.Selection.Range, NumRows:=3, NumColumns:=columns.Count, _
                          DefaultTableBehavior:=wdWord9TableBehavior, _
                          AutoFitBehavior:=wdAutoFitFixed

Dim attributes
fieldAttributes = columns.Items
For i = 0 To columns.Count - 1
    attributes = fieldAttributes(i).Items
    
    CellTitle CStr(attributes(0)), CBool(i = 0)
Next

field = columns.Keys
For i = 0 To columns.Count - 1
    attributes = fieldAttributes(i).Items
    
    CellField CStr(field(i)), CStr(attributes(1))
Next
addRowBookMark Name
End Sub


Private Sub CellTitle(var As String, first As Boolean)
If Not first Then
    WORDAPP.Selection.MoveRight Unit:=wdCell
End If
    WORDAPP.Selection.fields.Add Range:=WORDAPP.Selection.Range, type:=wdFieldEmpty, _
        PreserveFormatting:=True
    
    WORDAPP.Selection.Font.Bold = wdToggle
    WORDAPP.Selection.TypeText text:=I18N_MANAGER.I18NXML(var)
    WORDAPP.Selection.ParagraphFormat.Alignment = wdAlignParagraphCenter
   
    
End Sub

Private Sub CellField(var As String, align As String)
Dim mergefield As String
WORDAPP.Selection.MoveRight Unit:=wdCell

If (align = "right") Then
    WORDAPP.Selection.ParagraphFormat.Alignment = wdAlignParagraphRight
Else
    WORDAPP.Selection.ParagraphFormat.Alignment = wdAlignParagraphLeft
End If




Dim f
mergefield = "MERGEFIELD  " & I18N_MANAGER.I18NXML(var) & " "
Set f = WORDAPP.Selection.fields.Add(Range:=WORDAPP.Selection.Range, type:=wdFieldEmpty, text:=mergefield, PreserveFormatting:=True)
f.code.text = " MERGEFIELD " & var & " \* MERGEFORMAT"

WORDAPP.Selection.MoveLeft Unit:=wdWord, Count:=1, Extend:=wdExtend
With WORDAPP.ActiveDocument.Bookmarks
    .Add Range:=WORDAPP.Selection.Range, Name:=var
    .DefaultSorting = wdSortByName
    .ShowHidden = True
End With

WORDAPP.Selection.MoveRight Unit:=wdCharacter, Count:=1
End Sub

Public Sub dispose()
Set WORDAPP = Nothing
Set wordEvent = Nothing
guiManager.dispose
I18N_MANAGER.dispose
Set guiManager = Nothing
Set I18N_MANAGER = Nothing
If TimerActive Then Call DeactiveerTimer
End Sub
