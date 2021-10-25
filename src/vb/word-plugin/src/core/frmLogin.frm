VERSION 5.00
Begin VB.Form frmLogin 
   BorderStyle     =   3  'Fixed Dialog
   Caption         =   "Login"
   ClientHeight    =   1635
   ClientLeft      =   2835
   ClientTop       =   3480
   ClientWidth     =   4305
   LinkTopic       =   "Form1"
   MaxButton       =   0   'False
   MinButton       =   0   'False
   ScaleHeight     =   966.012
   ScaleMode       =   0  'User
   ScaleWidth      =   4042.162
   ShowInTaskbar   =   0   'False
   StartUpPosition =   1  'CenterOwner
   Begin VB.CommandButton btnOk 
      Caption         =   "btnOk"
      Default         =   -1  'True
      Height          =   375
      Left            =   600
      TabIndex        =   4
      Top             =   1200
      Width           =   1215
   End
   Begin VB.CommandButton btnCancel 
      Cancel          =   -1  'True
      Caption         =   "btnCancel"
      Height          =   375
      Left            =   2400
      TabIndex        =   5
      Top             =   1200
      Width           =   1215
   End
   Begin VB.TextBox txtUser 
      Height          =   285
      Left            =   2280
      TabIndex        =   0
      Top             =   120
      Width           =   1935
   End
   Begin VB.TextBox txtPassword 
      Height          =   285
      IMEMode         =   3  'DISABLE
      Left            =   2280
      PasswordChar    =   "*"
      TabIndex        =   1
      Top             =   480
      Width           =   1935
   End
   Begin VB.TextBox txtCompany 
      Height          =   285
      Left            =   2280
      TabIndex        =   3
      Top             =   840
      Width           =   1935
   End
   Begin VB.Label lblUser 
      Caption         =   "lblUser"
      Height          =   255
      Left            =   120
      TabIndex        =   7
      Top             =   120
      Width           =   1335
   End
   Begin VB.Label lblPassword 
      Caption         =   "lblPassword"
      Height          =   255
      Left            =   120
      TabIndex        =   6
      Top             =   480
      Width           =   1335
   End
   Begin VB.Label lblCompany 
      Caption         =   "lblCompany"
      Height          =   255
      Left            =   120
      TabIndex        =   2
      Top             =   840
      Width           =   1935
   End
End
Attribute VB_Name = "frmLogin"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Option Explicit
Public operation As String

Private Sub btnCancel_Click()
operation = "cancel"
Me.Hide
End Sub

Private Sub btnOk_Click()
If txtUser.text = "" Or txtPassword.text = "" Or txtCompany.text = "" Then
errorMsg RESOURCE_MSG.ERRORFILLFIELDS
Exit Sub
End If
frmSend.user = txtUser.text
frmSend.password = txtPassword.text
frmSend.company = txtCompany.text
operation = "ok"
Me.Hide
End Sub

Private Sub Form_Activate()
txtUser.text = frmSend.user
txtCompany.text = frmSend.company

If Not txtUser.text = "" Then
    txtPassword.SetFocus
Else
    txtUser.SetFocus
End If

End Sub

Private Sub Form_Load()
lblUser.Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.lblUser)
lblPassword.Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.lblPassword)
lblCompany.Caption = I18N_MANAGER.I18N(RESOURCE_FORMCONFIG.lblCompany)
btnCancel.Caption = I18N_MANAGER.I18N(RESOURCE_APPLICATION.btnCancel)
btnOk.Caption = I18N_MANAGER.I18N(RESOURCE_APPLICATION.btnOk)
End Sub
