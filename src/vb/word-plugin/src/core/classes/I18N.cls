VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
  Persistable = 0  'NotPersistable
  DataBindingBehavior = 0  'vbNone
  DataSourceBehavior  = 0  'vbNone
  MTSTransactionMode  = 0  'NotAnMTSObject
END
Attribute VB_Name = "I18nManager"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = True
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = True
Private i18nPosition As Integer
Private currentLang As String
Private XMLResource As Object

Private Function defaultI18N(id As Integer) As String
defaultI18N = LoadResString(id + LANGUAGE.ENGLISH)
End Function

Public Function I18N(id As Integer) As String
On Error GoTo FAIL
I18N = LoadResString(id + i18nPosition)
Exit Function
FAIL:
I18N = defaultI18N(id)
End Function

Public Function I18NXML(key As String, Optional endLine As Boolean = False) As String
I18NXML = XMLResource.Item(key)
If endLine Then
I18NXML = Replace(I18NXML, "|", vbCrLf)
End If
If I18NXML = "" Then
'Debug.Print "--> No Found:" & key
End If
End Function
Public Function getLang() As String
getLang = currentLang
End Function

Public Sub initialize(lang As String)
i18nPosition = LANGUAGE.ENGLISH
currentLang = "en"

If lang = "es" Then
i18nPosition = LANGUAGE.SPANISH
currentLang = "es"
ElseIf lang = "de" Then
i18nPosition = LANGUAGE.GERMAN
currentLang = "de"
ElseIf lang = "fr" Then
i18nPosition = LANGUAGE.FRENCH
currentLang = "fr"
End If

loadXMLResource
End Sub

Private Sub loadXMLResource()
Set XMLResource = getXMLResourceFromFile(ELWIS_FILE_I18N & currentLang)
End Sub

Public Sub dispose()
Set XMLResource = Nothing
End Sub
