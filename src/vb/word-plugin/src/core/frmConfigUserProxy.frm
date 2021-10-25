VERSION 5.00
Object = "{BDC217C8-ED16-11CD-956C-0000C04E4C0A}#1.1#0"; "TABCTL32.OCX"
Begin VB.Form frmConfig 
   BorderStyle     =   3  'Fixed Dialog
   Caption         =   "Configuration"
   ClientHeight    =   3600
   ClientLeft      =   45
   ClientTop       =   330
   ClientWidth     =   4470
   Icon            =   "frmConfigUserProxy.frx":0000
   LinkTopic       =   "Form1"
   MaxButton       =   0   'False
   MinButton       =   0   'False
   ScaleHeight     =   3600
   ScaleWidth      =   4470
   ShowInTaskbar   =   0   'False
   StartUpPosition =   3  'Windows Default
   Begin VB.CommandButton Command1 
      Caption         =   "?"
      Height          =   375
      Left            =   3840
      TabIndex        =   25
      Top             =   3120
      Visible         =   0   'False
      Width           =   495
   End
   Begin TabDlg.SSTab configTab 
      Height          =   3015
      Left            =   0
      TabIndex        =   8
      Top             =   0
      Width           =   4395
      _ExtentX        =   7752
      _ExtentY        =   5318
      _Version        =   393216
      Style           =   1
      MousePointer    =   99
      Tab             =   2
      TabHeight       =   520
      MouseIcon       =   "frmConfigUserProxy.frx":000C
      TabCaption(0)   =   "tabInternet"
      TabPicture(0)   =   "frmConfigUserProxy.frx":0028
      Tab(0).ControlEnabled=   0   'False
      Tab(0).Control(0)=   "frameHost"
      Tab(0).Control(1)=   "frameProxy"
      Tab(0).ControlCount=   2
      TabCaption(1)   =   "tabSecurity"
      TabPicture(1)   =   "frmConfigUserProxy.frx":0044
      Tab(1).ControlEnabled=   0   'False
      Tab(1).Control(0)=   "frameSecurity"
      Tab(1).ControlCount=   1
      TabCaption(2)   =   "tabSetting"
      TabPicture(2)   =   "frmConfigUserProxy.frx":0060
      Tab(2).ControlEnabled=   -1  'True
      Tab(2).Control(0)=   "frameLanguage"
      Tab(2).Control(0).Enabled=   0   'False
      Tab(2).Control(1)=   "frameGeneral"
      Tab(2).Control(1).Enabled=   0   'False
      Tab(2).ControlCount=   2
      Begin VB.Frame frameGeneral 
         Caption         =   "frameGeneral"
         Height          =   855
         Left            =   120
         TabIndex        =   23
         Top             =   2040
         Width           =   4215
         Begin VB.CheckBox chkDisableHelp 
            Caption         =   "chkDisableHelp"
            Height          =   495
            Left            =   240
            TabIndex        =   24
            Top             =   240
            Width           =   3735
         End
      End
      Begin VB.Frame frameLanguage 
         Caption         =   "frameLanguage"
         Height          =   1575
         Left            =   120
         TabIndex        =   21
         Top             =   360
         Width           =   4215
         Begin VB.OptionButton optLang 
            Caption         =   "optLangEn"
            Height          =   255
            Index           =   1
            Left            =   1800
            TabIndex        =   29
            Top             =   240
            Width           =   1935
         End
         Begin VB.OptionButton optLang 
            Caption         =   "optLangDe"
            Height          =   255
            Index           =   3
            Left            =   1800
            TabIndex        =   28
            Top             =   920
            Width           =   1935
         End
         Begin VB.OptionButton optLang 
            Caption         =   "optLangEs"
            Height          =   375
            Index           =   2
            Left            =   1800
            TabIndex        =   27
            Top             =   520
            Width           =   1935
         End
         Begin VB.OptionButton optLang 
            Caption         =   "optLangFr"
            Height          =   255
            Index           =   4
            Left            =   1800
            TabIndex        =   26
            Top             =   1200
            Width           =   1935
         End
         Begin VB.Label lblLanguage 
            Caption         =   "lblLanguage"
            Height          =   255
            Left            =   120
            TabIndex        =   22
            Top             =   720
            Width           =   1215
         End
      End
      Begin VB.Frame frameSecurity 
         Caption         =   "frameSecurity"
         Height          =   2055
         Left            =   -74880
         TabIndex        =   17
         Top             =   360
         Width           =   4215
         Begin VB.CheckBox chkSave 
            Caption         =   "chkSave"
            Height          =   495
            Left            =   360
            TabIndex        =   12
            Top             =   1440
            Width           =   3495
         End
         Begin VB.TextBox txtCompany 
            Height          =   285
            Left            =   2160
            TabIndex        =   11
            Top             =   1080
            Width           =   1815
         End
         Begin VB.TextBox txtPassword 
            Height          =   285
            IMEMode         =   3  'DISABLE
            Left            =   2160
            PasswordChar    =   "*"
            TabIndex        =   10
            Top             =   720
            Width           =   1815
         End
         Begin VB.TextBox txtUser 
            Height          =   285
            Left            =   2160
            TabIndex        =   9
            Top             =   360
            Width           =   1815
         End
         Begin VB.Label lblCompany 
            Caption         =   "lblCompany"
            Height          =   255
            Left            =   120
            TabIndex        =   20
            Top             =   1080
            Width           =   2055
         End
         Begin VB.Label lblPassword 
            Caption         =   "lblPassword"
            Height          =   255
            Left            =   120
            TabIndex        =   19
            Top             =   720
            Width           =   1335
         End
         Begin VB.Label lblUser 
            Caption         =   "lblUser"
            Height          =   255
            Left            =   120
            TabIndex        =   18
            Top             =   360
            Width           =   1335
         End
      End
      Begin VB.Frame frameHost 
         Caption         =   "frameConfig"
         Height          =   1095
         Left            =   -74880
         TabIndex        =   15
         Top             =   360
         Width           =   4215
         Begin VB.TextBox txtIp 
            Height          =   285
            Left            =   720
            TabIndex        =   1
            Top             =   240
            Width           =   3375
         End
         Begin VB.Label lblSample 
            Caption         =   "lblSample"
            Height          =   375
            Left            =   360
            TabIndex        =   16
            Top             =   600
            Width           =   3135
         End
         Begin VB.Label urlLabel 
            Caption         =   "URL"
            Height          =   255
            Left            =   240
            TabIndex        =   0
            Top             =   270
            Width           =   495
         End
      End
      Begin VB.Frame frameProxy 
         Caption         =   "frameProxy"
         Height          =   1455
         Left            =   -74880
         TabIndex        =   6
         Top             =   1440
         Width           =   4215
         Begin VB.TextBox txtProxyServer 
            Height          =   285
            Left            =   1440
            TabIndex        =   2
            Top             =   240
            Width           =   2655
         End
         Begin VB.TextBox txtProxyPort 
            Height          =   285
            Left            =   1440
            TabIndex        =   3
            Top             =   600
            Width           =   855
         End
         Begin VB.Label lblProxyServer 
            Caption         =   "lblProxyServer"
            Height          =   255
            Left            =   120
            TabIndex        =   14
            Top             =   240
            Width           =   1215
         End
         Begin VB.Label lblProxyPort 
            Caption         =   "lblProxyPort"
            Height          =   255
            Left            =   120
            TabIndex        =   13
            Top             =   600
            Width           =   1095
         End
         Begin VB.Label lblProxyNote 
            Caption         =   "lblProxyNote"
            BeginProperty Font 
               Name            =   "MS Sans Serif"
               Size            =   8.25
               Charset         =   0
               Weight          =   400
               Underline       =   0   'False
               Italic          =   -1  'True
               Strikethrough   =   0   'False
            EndProperty
            Height          =   375
            Left            =   120
            TabIndex        =   7
            Top             =   960
            Width           =   3975
         End
      End
   End
   Begin VB.CommandButton btnCancel 
      Caption         =   "btnCancel"
      Height          =   375
      Left            =   2520
      TabIndex        =   5
      Top             =   3120
      Width           =   1215
   End
   Begin VB.CommandButton btnOk 
      Caption         =   "btnOk"
      Default         =   -1  'True
      Height          =   375
      Left            =   720
      TabIndex        =   4
      Top             =   3120
      Width           =   1215
   End
End
Attribute VB_Name = "frmConfig"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False






Private localLang As String



Private Sub btnCancel_Click()

Unload Me

End Sub



Private Sub btnOk_Click()

setRegisterKey ELWIS_REGISTER_ATTRIB_HOST, txtIp.text



If localLang <> I18N_MANAGER.getLang Then

setRegisterKey ELWIS_REGISTER_ATTRIB_LANG, localLang

setRegisterKey "changeLang", "true"

MsgBox I18N_MANAGER.I18N(RESOURCE_MSG.LANGUAGE_CHANGE), vbInformation, "business manager"

Else

setRegisterKey "changeLang", "false"

End If



If chkDisableHelp.value Then

setRegisterKey "disableHelp", "true"

On Error Resume Next

    If Not helpAssistent Is Nothing Then helpAssistent.Close

Else

    setRegisterKey "disableHelp", "false"

End If




setRegisterKey ELWIS_REGISTER_ATTRIB_PROXY, Trim(txtProxyServer.text)

setRegisterKey ELWIS_REGISTER_ATTRIB_PROXYPORT, Trim(txtProxyPort.text)




setRegisterKey ELWIS_REGISTER_ATTRIB_USER, txtUser.text

setRegisterKey ELWIS_REGISTER_ATTRIB_COMPANY, txtCompany.text



If chkSave.value Then

setRegisterKey ELWIS_REGISTER_ATTRIB_PASS, Encrypt(txtPassword.text, ELWIS_ENCRYPT_KEY)

setRegisterKey ELWIS_REGISTER_ATTRIB_SAVEPASS, "true"

Else

setRegisterKey ELWIS_REGISTER_ATTRIB_PASS, ""

setRegisterKey ELWIS_REGISTER_ATTRIB_SAVEPASS, "false"

End If



Unload Me

End Sub



Private Sub changeLanguage()

Dim cmdbar As CommandBar

Set cmdbar = WORDAPP.CommandBars(RESOURCE_TEMPLATE_TOOLBAR_I18N)

End Sub



Private Sub setCaption()

optLang(1).Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.OPTLANGEN)

optLang(2).Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.OPTLANGES)

optLang(3).Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.OPTLANGDE)

optLang(4).Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.OPTLANGFR)

lblProxyNote.Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.lblProxyNote)

lblProxyServer.Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.lblProxyServer)

lblProxyPort.Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.lblProxyPort)

lblSample.Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.lblSample)

lblLanguage.Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.lblLanguage)

frameHost.Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.LBLHOST)

lblUser.Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.lblUser)

lblPassword.Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.lblPassword)

lblCompany.Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.lblCompany)

chkSave.Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.chkSave)

chkDisableHelp.Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.chkDisableHelp)

Me.Caption = PLUGIN_NAME & " " & PLUGIN_VERSION & " - " & I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.Caption)

frameLanguage.Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.frameLanguage)

frameProxy.Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.frameProxy)

frameSecurity.Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.frameSecurity)

frameGeneral.Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.frameGeneral)



configTab.visible = False

configTab.Tab = 2

configTab.Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.TABSETTING)

configTab.Tab = 1

configTab.Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.TABSECURITY)

configTab.Tab = 0

configTab.Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.TABINTERNET)

configTab.visible = True

btnCancel.Caption = I18N_MANAGER.I18N(RESOURCE_APPLICATION.btnCancel)

btnOk.Caption = I18N_MANAGER.I18N(RESOURCE_APPLICATION.btnOk)

End Sub





Private Sub Command1_Click()

frmAbout.Show vbModal

End Sub



Private Sub Form_Load()

setCaption



txtIp.text = getRegisterKey(ELWIS_REGISTER_ATTRIB_HOST)

txtProxyServer.text = getRegisterKey(ELWIS_REGISTER_ATTRIB_PROXY)

txtProxyPort.text = getRegisterKey(ELWIS_REGISTER_ATTRIB_PROXYPORT)



txtUser.text = getRegisterKey(ELWIS_REGISTER_ATTRIB_USER)

txtCompany.text = getRegisterKey(ELWIS_REGISTER_ATTRIB_COMPANY)



If getRegisterKey(ELWIS_REGISTER_ATTRIB_SAVEPASS) = "true" Then

chkSave.value = Checked

txtPassword.text = Decrypt(getRegisterKey(ELWIS_REGISTER_ATTRIB_PASS), ELWIS_ENCRYPT_KEY)

End If



If getRegisterKey("disableHelp") = "true" Then

chkDisableHelp.value = Checked

End If



localLang = I18N_MANAGER.getLang

Select Case localLang

Case "en"

optLang(1).value = True

Case "es"

optLang(2).value = True

Case "de"

optLang(3).value = True

Case "fr"

optLang(4).value = True

End Select

End Sub



Private Sub optLang_Click(Index As Integer)

Select Case Index

Case 1

localLang = "en"

Case 2

localLang = "es"

Case 3

localLang = "de"

Case 4

localLang = "fr"

End Select



End Sub

