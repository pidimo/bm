Attribute VB_Name = "Utils"
Private Const ERR_ID_NULL As Integer = 0
Private Const ERR_ID_FIND As Integer = 1
Private Const ERR_CLOSE As Integer = 2
Private Const SUCCEED As Integer = 3
Private Const ERR_CONCURRENCY As Integer = 4
Private Const ERR_CANTUPLOAD As Integer = 5
Private Const ERR_INVALIDLOGIN As Integer = 6
Private Const ERR_INVALIDCOMPANY As Integer = 7
Private Const ERR_INACTIVECOMPANY As Integer = 8
Private Const ERR_INVALIDDATA As Integer = 9
Public Const ERR_TEMPLATE_FIELDS_EXPIRE As Integer = 10

Private Declare Function SHGetSpecialFolderPath Lib "shell32" Alias _
"SHGetSpecialFolderPathA" (ByVal hwndOwner As Long, ByVal lpszPath As _
String, ByVal nFolder As Long, ByVal fCreate As Long) As Long

Private Const CSIDL_APPDATA As Long = &H1A
Private Const MAX_PATH As Long = 260


Public Function getRegisterKey(Name As String) As String
getRegisterKey = regQuery_A_Key(HKEY_CURRENT_USER, _
                                ELWIS_REGISTER_CONFIG, _
                                Name)
End Function

Public Sub setRegisterKey(Name As String, value As String)
regCreate_Key_Value HKEY_CURRENT_USER, _
                          ELWIS_REGISTER_CONFIG, _
                          Name, value, False
End Sub

Public Function getApplicationDataPath() As String
Dim sBuffer As String
sBuffer = String$(MAX_PATH, vbNullChar)
Call SHGetSpecialFolderPath(1, sBuffer, CSIDL_APPDATA, 0&)
getApplicationDataPath = Left$(sBuffer, InStr(1, sBuffer, vbNullChar) - 1)
End Function



Public Function getDOMFromFile(file As String, Optional validate As Boolean = False) As IXMLDOMElement
Dim xmlDoc As New MSXML2.DOMDocument30
xmlDoc.async = False
'xmlDoc.validate = validate
'log.SaveToLog "Load from :" & ELWIS_CONFIG_PATH & file

Debug.Print "Load from :" & ELWIS_CONFIG_PATH & file
'App.LogEvent "Load from :" & ELWIS_CONFIG_PATH & file, vbLogEventTypeInformation
xmlDoc.Load (ELWIS_CONFIG_PATH & file)

If (xmlDoc.parseError.ErrorCode <> 0) Then
   Dim myErr
   Set myErr = xmlDoc.parseError
   Set getDOMFromFile = Nothing
   Debug.Print "ERROR:" & myErr.reason
Else
   Set getDOMFromFile = xmlDoc.documentElement
End If

End Function

Public Function getXMLResourceFromFile(file As String, Optional ext = ".xml") As Scripting.Dictionary
'Dim obj As Object
Dim obj As Scripting.Dictionary
Dim nodes As IXMLDOMNodeList
'Set obj = CreateObject(Scripting.Dictionary)
Dim dom As IXMLDOMElement
Set dom = getDOMFromFile(file & ext)
If dom Is Nothing Then
Exit Function
'log.SaveToLog dom.xml
End If
Set nodes = dom.childNodes

Set getXMLResourceFromFile = New Scripting.Dictionary
If Not (nodes Is Nothing) Then
    Dim node As IXMLDOMNode
    For Each node In nodes
    On Error GoTo failXML
    getXMLResourceFromFile.Add node.attributes.getNamedItem(ELWIS_ATTRIB_KEY).NodeValue, node.text
    GoTo i_doit
failXML:
    Debug.Print "Error load node resource:" & Err.Description
i_doit:
    Next
End If

On Error GoTo FAIL
Set dom = getDOMFromFile(file & ELWIS_FILE_TELECOMS_I18N & ext)
If dom Is Nothing Then
    Exit Function
End If

Set nodes = dom.childNodes
If Not (nodes Is Nothing) Then
    'Dim node As IXMLDOMNode
    For Each node In nodes
    On Error GoTo failXMLTelecom
    getXMLResourceFromFile.Add node.attributes.getNamedItem(ELWIS_ATTRIB_KEY).NodeValue, node.text
failXMLTelecom:
    Next
End If
FAIL:
End Function

Public Function getResponseCode(response As String) As Integer
Dim aux As String
aux = Mid(result, InStr(1, result, "[") + 1, InStr(1, result, "]") - InStr(1, result, "[") - 1)
getResponseCode = CInt(aux)
End Function
Public Function processResponse(result As String, showTheUpdateFieldMessage As Boolean) As Integer
processResponse = ELWIS_FAIL
Dim value As Integer

Dim loginAgain As Boolean
loginAgain = False

Dim aux As String
aux = Mid(result, InStr(1, result, "[") + 1, InStr(1, result, "]") - InStr(1, result, "[") - 1)
'log.SaveToLog "AUX:" & aux
Select Case CInt(aux)
Case ERR_ID_FIND
errorMsg RESOURCE_MSG.UPLOAD_FAILID, RESOURCE_MSG.UPLOAD_FAIL
Case ERR_CLOSE
errorMsg RESOURCE_MSG.UPLOAD_FAILSTATUS, RESOURCE_MSG.UPLOAD_FAIL
Case SUCCEED
processResponse = ELWIS_CONTINUE
Case ERR_ID_NULL
errorMsg RESOURCE_MSG.UPLOAD_FAILID
Case ERR_CONCURRENCY
errorMsg RESOURCE_MSG.UPLOAD_FAILCONCURRENCY
Case ERR_CANTUPLOAD
processResponse = ELWIS_REPEAT
errorMsg RESOURCE_MSG.UPLOAD_FAILCANTUPLOAD
Case ERR_INVALIDLOGIN
processResponse = ELWIS_REPEAT
errorMsg RESOURCE_MSG.UPLOAD_FAILINVALIDLOGIN
Case ERR_INACTIVECOMPANY
errorMsg RESOURCE_MSG.UPLOAD_FAILINACTIVECOMPANY
Case ERR_INVALIDCOMPANY
processResponse = ELWIS_REPEAT
errorMsg RESOURCE_MSG.UPLOAD_FAILINVALIDCOMPANY
Case ERR_INVALIDDATA
errorMsg RESOURCE_MSG.UPLOAD_FAILINVALIDDATA
Case ERR_TEMPLATE_FIELDS_EXPIRE
processResponse = ELWIS_CHANGE_TEMPLATEFIELDS
'errorMsg RESOURCE_MSG.UPLOAD_FAILINVALIDDATA
xmlRecived = Mid(result, InStr(1, result, "<*>") + 3)
'Case 11
'processResponse = ELWIS_FIELDS_ARE_UPDATED
'If showTheUpdateFieldMessage Then
'sucessMsg RESOURCE_MSG.FIELDSUPDATE
'End If
End Select
If processResponse <> ELWIS_CHANGE_TEMPLATEFIELDS Then
xmlRecived = ""
End If
End Function

Public Sub errorMsg(error As Integer, Optional msgTitle As Integer = -1, Optional title As Integer = RESOURCE_MSG.TITLE_FAIL)
Dim Msg As String
Msg = I18N_MANAGER.I18N(error)
If msgTitle > -1 Then
Msg = Msg & vbCrLf & I18N_MANAGER.I18N(msgTitle)
End If
MsgBox Msg, vbCritical, I18N_MANAGER.I18N(title)
End Sub

Public Sub sucessMsg(Msg As Integer, Optional title As Integer = RESOURCE_MSG.TITLE_SUCCED)
MsgBox I18N_MANAGER.I18N(Msg), vbInformation, I18N_MANAGER.I18N(title)
End Sub

Private Function getDecode(code As String) As String
Dim CHARACTER() As Variant
Dim TABLE_CONVERTION() As Variant
Dim i As Integer
CHARACTER = Array("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", ":")
TABLE_CONVERTION = Array("X", "4", "A", "Y", "C", "0", "B", "J", "F", "O", "R", "W")
getDecode = ""
For i = 0 To UBound(TABLE_CONVERTION)
If TABLE_CONVERTION(i) = code Then
getDecode = CHARACTER(i)
Exit Function
End If
Next
End Function

Public Function decode(code As String) As String
'log.SaveToLog "CODE:" & code
decode = ""
On Error GoTo FAIL
Dim i As Integer
For i = 1 To Len(code)
decode = decode & getDecode(Mid(code, i, 1))
Next
'log.SaveToLog "decode :" & decode
FAIL:
End Function

Public Function Decrypt(EncText As String, WatchWord As String) As String

'All the Dim things Follows
Dim intKey As Integer
Dim intX As Integer
Dim strChar As String
Dim DecText As String

'The Few Lines that work for us.The Following three
'Lines Create the key to Decrypt the Data.
For intX = 1 To Len(WatchWord)
    intKey = intKey + Asc(Mid(WatchWord, intX)) / 20
Next

'Here is our Engine Room that Decrypts the Data by using the key.
For intX = 1 To Len(EncText)
    strChar = Mid(EncText, intX, 1)
    DecText = DecText & Chr(Asc(strChar) Xor intKey)
Next

'Return the Data.
Decrypt = DecText
End Function
Private Function createKey() As String
'ELWIS_ENCRYPT_KEY
Dim i
Dim result As String
'Dim i
Dim num()
num = Array("2", "6", "0", "9", "7")
Dim j
j = 0

For i = 1 To Len(ELWIS_ENCRYPT_KEY)
    result = result & Mid(ELWIS_ENCRYPT_KEY, i, 2) & num(j)
    i = i + 1
    j = j + 1
Next
Debug.Print result
createKey = result
MsgBox createKey
End Function
Public Function Encrypt(PlainText As String, WatchWord As String) As String
Dim key() As Byte
Dim abCipher() As Byte
key() = StrConv(WatchWord, vbFromUnicode)
Call blf_KeyInit(key)

Dim textByte() As Byte
    textByte = StrConv(PlainText, vbFromUnicode)
    abCipher = blf_BytesEnc(textByte)
    Encrypt = EncodeBytes64(abCipher)
End Function

'Function to Encrypt the Plain Text.
Public Function OldEncrypt(PlainText As String, WatchWord As String) As String

'Other Dim Things
Dim intKey As Integer
Dim intX As Integer
Dim strChar As String
Dim EncText As String

'all Mighty visual Basic lines give us the Key
For intX = 1 To Len(WatchWord)
    intKey = intKey + Asc(Mid(WatchWord, intX)) / 20
Next

'the brother of decrypt function.but it earns its
'living by encrypting the data
For intX = 1 To Len(PlainText)
    strChar = Mid(PlainText, intX, 1)
    EncText = EncText & Chr(Asc(strChar) Xor intKey)
Next

'the ever so generous functios should return something.
'Encrypt = EncText
End Function
' Drops a page bookmark and add it again if already exists in the other hand it adds it.
Public Sub reAddPageBookmark(bookmark As String)
If WORDAPP.ActiveDocument.Bookmarks.Exists(bookmark) = False Then
    addPageBookMark bookmark
Else
    WORDAPP.ActiveDocument.Bookmarks(bookmark).Delete
    addPageBookMark bookmark
End If
End Sub

Private Sub addPageBookMark(var As String)
WORDAPP.Selection.EndKey Unit:=wdStory
DeletePageBreaks
WORDAPP.Selection.InsertBreak type:=wdPageBreak
WORDAPP.Selection.WholeStory
    With WORDAPP.ActiveDocument.Bookmarks
        .Add Range:=WORDAPP.Selection.Range, Name:=var
        .DefaultSorting = wdSortByName
        .ShowHidden = False
    End With
    WORDAPP.Selection.MoveLeft Unit:=wdCharacter, Count:=1
End Sub

Private Sub DeletePageBreaks()
   'Replace page breaks
    With WORDAPP.ActiveDocument.Range.Find
        .text = "^m"
        .Replacement.text = ""
        .execute Replace:=wdReplaceAll
    End With
   
End Sub


