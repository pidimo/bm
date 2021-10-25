VERSION 5.00
Object = "{831FDD16-0C5C-11D2-A9FC-0000F8754DA1}#2.0#0"; "MSCOMCTL.OCX"
Object = "{63338513-F789-11CE-86F8-0020AFD8C6DB}#1.0#0"; "webupls62.ocx"
Begin VB.Form frmSend 
   AutoRedraw      =   -1  'True
   BorderStyle     =   3  'Fixed Dialog
   Caption         =   "BM Word Plugin"
   ClientHeight    =   1530
   ClientLeft      =   45
   ClientTop       =   330
   ClientWidth     =   4755
   LinkTopic       =   "Form1"
   MaxButton       =   0   'False
   MinButton       =   0   'False
   ScaleHeight     =   1530
   ScaleWidth      =   4755
   ShowInTaskbar   =   0   'False
   StartUpPosition =   2  'CenterScreen
   Begin MSComctlLib.StatusBar statusBar 
      Align           =   2  'Align Bottom
      Height          =   255
      Left            =   0
      TabIndex        =   4
      Top             =   1275
      Width           =   4755
      _ExtentX        =   8387
      _ExtentY        =   450
      Style           =   1
      SimpleText      =   "statusBar"
      ShowTips        =   0   'False
      _Version        =   393216
      BeginProperty Panels {8E3867A5-8586-11D1-B16A-00C0F0283628} 
      EndProperty
      BeginProperty Font {0BE35203-8F91-11CE-9DE3-00AA004BB851} 
         Name            =   "MS Sans Serif"
         Size            =   8.25
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
   End
   Begin VB.Frame frameCaption 
      Caption         =   "frameCaption"
      BeginProperty Font 
         Name            =   "MS Sans Serif"
         Size            =   8.25
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   1215
      Left            =   120
      TabIndex        =   0
      Top             =   0
      Width           =   4455
      Begin WEBUPLOADSLibCtl.WebUploadS uploadFileS 
         Left            =   3960
         Top             =   960
         Accept          =   ""
         AuthScheme      =   0
         FileCount       =   0
         FirewallHost    =   ""
         FirewallPassword=   ""
         FirewallPort    =   80
         FirewallType    =   0
         FirewallUser    =   ""
         FollowRedirects =   0
         Password        =   ""
         ProxyPassword   =   ""
         ProxyPort       =   80
         ProxyServer     =   ""
         ProxySSL        =   0
         ProxyUser       =   ""
         SSLCertStore    =   "MY"
         SSLCertStorePassword=   ""
         SSLCertStoreType=   0
         SSLCertSubject  =   ""
         Timeout         =   60
         URL             =   ""
         User            =   ""
      End
      Begin VB.Label lblPercent 
         BackStyle       =   0  'Transparent
         Caption         =   "0 %"
         BeginProperty Font 
            Name            =   "MS Sans Serif"
            Size            =   8.25
            Charset         =   0
            Weight          =   700
            Underline       =   0   'False
            Italic          =   0   'False
            Strikethrough   =   0   'False
         EndProperty
         ForeColor       =   &H00FFFFFF&
         Height          =   255
         Left            =   2160
         TabIndex        =   3
         Top             =   735
         Width           =   615
      End
      Begin VB.Shape progressShape 
         BackColor       =   &H00C00000&
         BackStyle       =   1  'Opaque
         Height          =   195
         Left            =   165
         Top             =   750
         Width           =   15
      End
      Begin VB.Shape Shape1 
         Height          =   255
         Left            =   120
         Top             =   720
         Width           =   4215
      End
      Begin VB.Label lblSendFile 
         Caption         =   "lblSendFile"
         Height          =   255
         Left            =   240
         TabIndex        =   2
         Top             =   360
         Width           =   4095
      End
      Begin VB.Label sendTo 
         BeginProperty Font 
            Name            =   "MS Sans Serif"
            Size            =   8.25
            Charset         =   0
            Weight          =   700
            Underline       =   0   'False
            Italic          =   0   'False
            Strikethrough   =   0   'False
         EndProperty
         Height          =   255
         Left            =   1200
         TabIndex        =   1
         Top             =   240
         Width           =   3135
      End
   End
End
Attribute VB_Name = "frmSend"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False






Private progress As Integer

Private progressWidth As Integer

Private parameters As URLPARAMS

Private sendFileNow As Boolean

Public successRequest As Integer

Public continueUpload As Boolean

Private Declare Sub Sleep Lib "kernel32" (ByVal dwMilliseconds As Long)



Private showUpdateFieldMessage As Boolean



Public user As String

Public password As String

Public company As String



Private waitForResponse As Boolean







Private Sub sendFile()

Dim urlHost As String

Dim host As String



uploadFileS.Reset

setupProxy

host = getRegisterKey(ELWIS_REGISTER_ATTRIB_HOST)

If Not Right$(host, 1) = "/" Then
    host = host & "/"
End If



urlHost = host & "Upload.do" & "?dto(code)=" & WORDAPP.ActiveDocument.BuiltInDocumentProperties(wdPropertyAuthor)

statusBar.SimpleText = I18N_MANAGER.I18N(RESOURCE_MSG.STATUSUPLOAD)

progressWidth = 4215

progressShape.visible = True

progressShape.Width = 15

progress = progressWidth / 100

progressShape.visible = True

lblPercent.visible = True


Dim t, d

d = Date

t = Time

Dim tname

tname = Day(d) + Month(d) + Year(d) + Hour(t) + Minute(t) + Second(t)



Dim fileName As String

Dim fs As Scripting.FileSystemObject

Set fs = New Scripting.FileSystemObject



fileName = fs.GetSpecialFolder(2) & "\BMDoc" & tname & ".doc"



WORDAPP.ActiveDocument.SaveAs fileName:=fileName, addtorecentfiles:=False



fs.CopyFile WORDAPP.ActiveDocument.FullName, fs.GetSpecialFolder(2) & "\BMDocTemp" & tname & ".doc", True ' Save to temporal windows folder

    uploadFileS.FileCount = 1

    uploadFileS.FileVarNames(1) = "file"

    uploadFileS.FileNames(1) = fs.GetSpecialFolder(2) & "\BMDocTemp" & tname & ".doc"


Set fs = Nothing

On Error GoTo myfail

successRequest = False

uploadFileS.UploadTo urlHost

Unload Me

'Below condition manages the possibility to close the document when it's uploaded.
'The problem if we leave opened word is that the version is not updated after an
'upload, which means the second time the user tries to upload the document
'he will get an concurrency error. This must be solved in the server side
'looking for a way to update the version in the document once it's updloaded
'maybe we can put the version in the request header response.

If successRequest = ELWIS_CONTINUE Then

   If WORDAPP.Documents.Count = 1 Then
   
    WORDAPP.Quit wdSaveChanges

   Else
    
    WORDAPP.ActiveDocument.Close False

   End If

End If



myfail:

''log.SaveToLog "ERROR:" & Err.Description

End Sub



Private Sub setupProxy()

Dim proxy As String

proxy = getRegisterKey(ELWIS_REGISTER_ATTRIB_PROXY)

If proxy <> "" Then

uploadFileS.ProxyServer = proxy

uploadFileS.ProxyPort = getRegisterKey(ELWIS_REGISTER_ATTRIB_PROXYPORT)

uploadFileS.ProxySSL = psTunnel

Else

uploadFileS.ProxyServer = ""

uploadFileS.ProxyPort = "80"

uploadFileS.ProxySSL = psAutomatic

End If






End Sub





Private Sub Form_Activate()

If sendFileNow Then sendFile

End Sub



Public Sub showAndSendFile(ByRef ElwisGUIInstance As ElwisGui)

sendFileNow = True

Me.Show vbModal

End Sub



Private Function getUserInformation() As String

    user = getRegisterKey(ELWIS_REGISTER_ATTRIB_USER)

    company = getRegisterKey(ELWIS_REGISTER_ATTRIB_COMPANY)

    password = getRegisterKey(ELWIS_REGISTER_ATTRIB_PASS)

    getUserInformation = "ok"

    If user = "" Or company = "" Or password = "" Then

       statusBar.SimpleText = I18N_MANAGER.I18N(RESOURCE_MSG.STATUSREQUESTUSERINFO)

       frmLogin.Show vbModal

       getUserInformation = frmLogin.operation

    End If

    

    password = Decrypt(password, ELWIS_ENCRYPT_KEY)

End Function



Public Function processActive(ByRef ElwisGUIInstance As ElwisGui, Optional isUpdate As Boolean = False, Optional showTheUpdateFieldMessage As Boolean = True) As Boolean

progressShape.visible = False

progressShape.Width = 15

lblPercent.visible = False

lblPercent.Caption = "0 %"

Dim code As String

processActive = False

code = WORDAPP.ActiveDocument.BuiltInDocumentProperties(wdPropertyAuthor)



    Dim urlHost As String

    Dim host As String

    host = getRegisterKey(ELWIS_REGISTER_ATTRIB_HOST)

    If host = "" Then

    errorMsg RESOURCE_MSG.MISSINGHOST

    

    Exit Function

    End If

    If Not Right$(host, 1) = "/" Then
        host = host & "/"
    End If
    
    urlHost = host & "Upload.do"

    user = getRegisterKey(ELWIS_REGISTER_ATTRIB_USER)

    company = getRegisterKey(ELWIS_REGISTER_ATTRIB_COMPANY)

    continueUpload = True

    If user = "" Or company = "" Or getRegisterKey(ELWIS_REGISTER_ATTRIB_SAVEPASS) = "false" Then

       statusBar.SimpleText = I18N_MANAGER.I18N(RESOURCE_MSG.STATUSREQUESTUSERINFO)

repeatLogin:

       frmLogin.Show vbModal

       If frmLogin.operation = "cancel" Then

        continueUpload = False

        Me.Hide

        Exit Function

       End If

       password = Encrypt(password, ELWIS_ENCRYPT_KEY)

    Else
        

        password = getRegisterKey(ELWIS_REGISTER_ATTRIB_PASS)

    End If

    On Error GoTo HostNotFound

uploadFileS.Reset

setupProxy

uploadFileS.AddFormVar "dto(code)", code

uploadFileS.AddFormVar "dto(login)", user

uploadFileS.AddFormVar "dto(password)", password

uploadFileS.AddFormVar "dto(company)", company

uploadFileS.AddFormVar "dto(op)", "login"

uploadFileS.AddFormVar "dto(telecomsKey)", getRegisterKey(ELWIS_REGISTER_ATTRIB_TELECOMS_KEY)

uploadFileS.url = urlHost



If isUpdate Then uploadFileS.AddFormVar "dto(uploadOperation)", "operationUpdate"

  

  


  showUpdateFieldMessage = showTheUpdateFieldMessage



  uploadFileS.Upload

  

  

    If successRequest = ELWIS_FAIL Then

HostNotFound:

'log.SaveToLog "Save error if exist:" & Err.Description & "Error code:" & Err.Number & "Error host:" & Err.Source

        If Err.Number = 26002 Then

        'log.SaveToLog Err.Description

            errorMsg RESOURCE_MSG.MISSINGHOST

        End If

        If Not isUpdate Then Unload Me

    

    ElseIf successRequest = ELWIS_REPEAT Then

        GoTo repeatLogin

    ElseIf successRequest = ELWIS_CHANGE_TEMPLATEFIELDS Then

        ElwisGUIInstance.UpdateTelecomsFields

               
        If Not isUpdate Then Unload Me

    ElseIf successRequest = ELWIS_CONTINUE Then

        processActive = True

    End If

End Function







Private Sub uploadFileS_Connected(StatusCode As Integer, Description As String)

If StatusCode <> 0 Then

errorMsg RESOURCE_MSG.ERRORHOSTNOTFOUND

continueUpload = False

End If

End Sub

Private Sub WebUploadS1_SSLStatus(Message As String)
    'Debug.Print Message
End Sub

Private Sub uploadFileS_Error(ErrorCode As Integer, Description As String)

'Debug.Print "Error Code Upload Error: " & ErrorCode & " - Desc: " & Description

End Sub





Private Sub uploadFileS_Transfer(BytesTransferred As Long, text As String)

xmlRecived = xmlRecived & text

If InStr(1, text, "<*END*>") > 0 Then

  successRequest = processResponse(xmlRecived, showUpdateFieldMessage)

  'showUpdateFieldMessage = true

End If



End Sub

            

Private Sub uploadFileS_UploadProgress(PercentDone As Integer)

Dim step

step = progress * PercentDone

If step < progressWidth Then

progressShape.Width = progress * PercentDone

End If

lblPercent.Caption = PercentDone & " %"

End Sub





Private Sub uploadFileS_EndTransfer()

progressShape.Width = progressWidth

lblPercent.Caption = "100 %"

End Sub



Private Function validateDocument(ids As String) As Boolean

Dim id As String

Dim kind As String



Dim varComment

validateDocument = False

On Error GoTo FAIL

Dim execute As Boolean

varComment = WORDAPP.ActiveDocument.BuiltInDocumentProperties(wdPropertyComments)





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





Private Sub Form_Load()

processOnActive = True

setCaption

End Sub



Private Sub setCaption()

Dim host As String
host = getRegisterKey(ELWIS_REGISTER_ATTRIB_HOST)

statusBar.SimpleText = I18N_MANAGER.I18N(RESOURCE_MSG.STATUSAUTHENTICATION)

frameCaption.Caption = I18N_MANAGER.I18N(RESOURCE_FORMSEND.frameCaption)


lblSendFile.Caption = I18N_MANAGER.I18N(RESOURCE_FORMSEND.lblSendFile) & " " & host

End Sub


Private Sub uploadFileS_SSLServerAuthentication(CertEncoded As String, CertSubject As String, CertIssuer As String, Status As String, Accept As Boolean)
 Debug.Print "Server provided the following certificate:" & vbCrLf & vbCrLf & _
             "    Issuer: " & CertIssuer & vbCrLf & _
            "    Subject: " & CertSubject & vbCrLf & vbCrLf & _
            "The following problems have been determined for this certificate: " & Status & vbCrLf & vbCrLf; ""
 Accept = True
 
End Sub



