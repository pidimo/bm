VERSION 5.00

Begin VB.Form frmConfig 

   BorderStyle     =   3  'Fixed Dialog

   Caption         =   "frmConfig"

   ClientHeight    =   5580

   ClientLeft      =   45

   ClientTop       =   330

   ClientWidth     =   4275

   LinkTopic       =   "Form1"

   MaxButton       =   0   'False

   MinButton       =   0   'False

   ScaleHeight     =   5580

   ScaleWidth      =   4275

   ShowInTaskbar   =   0   'False

   StartUpPosition =   3  'Windows Default

   Begin VB.Frame frameProxy 

      Caption         =   "frameProxy"

      Height          =   2535

      Left            =   0

      TabIndex        =   12

      Top             =   1440

      Width           =   4215

      Begin VB.CheckBox chkSaveUserInfo 

         Caption         =   "Check1"

         Height          =   195

         Left            =   120

         TabIndex        =   21

         Top             =   1800

         Width           =   3135

      End

      Begin VB.TextBox txtProxyPass 

         Height          =   285

         IMEMode         =   3  'DISABLE

         Left            =   1320

         PasswordChar    =   "*"

         TabIndex        =   16

         Top             =   1440

         Width           =   1695

      End

      Begin VB.TextBox txtProxyUser 

         Height          =   285

         Left            =   1320

         TabIndex        =   15

         Top             =   1080

         Width           =   1695

      End

      Begin VB.TextBox btnProxyPort 

         Height          =   285

         Left            =   1320

         TabIndex        =   14

         Top             =   720

         Width           =   975

      End

      Begin VB.TextBox txtProxyServer 

         Height          =   285

         Left            =   1320

         TabIndex        =   13

         Top             =   360

         Width           =   2775

      End

      Begin VB.Label lblProxyNote 

         Caption         =   "Label2"

         Height          =   375

         Left            =   120

         TabIndex        =   22

         Top             =   2040

         Width           =   3975

      End

      Begin VB.Label lblPassword 

         Caption         =   "lblPassword"

         Height          =   255

         Left            =   120

         TabIndex        =   20

         Top             =   1440

         Width           =   855

      End

      Begin VB.Label lblUser 

         Caption         =   "lblUser"

         Height          =   255

         Left            =   120

         TabIndex        =   19

         Top             =   1080

         Width           =   975

      End

      Begin VB.Label lblPort 

         Caption         =   "lblPort"

         Height          =   255

         Left            =   120

         TabIndex        =   18

         Top             =   720

         Width           =   975

      End

      Begin VB.Label lblServer 

         Caption         =   "lblServer"

         Height          =   255

         Left            =   120

         TabIndex        =   17

         Top             =   360

         Width           =   855

      End

   End

   Begin VB.Frame frameConfig 

      Caption         =   "frameConfig"

      Height          =   1455

      Left            =   0

      TabIndex        =   7

      Top             =   0

      Width           =   4215

      Begin VB.TextBox txtIp 

         Height          =   285

         Left            =   720

         TabIndex        =   8

         Top             =   480

         Width           =   3375

      End

      Begin VB.Label Label1 

         Caption         =   "http://"

         Height          =   255

         Left            =   120

         TabIndex        =   11

         Top             =   510

         Width           =   495

      End

      Begin VB.Label lblHost 

         Caption         =   "lblHost"

         Height          =   255

         Left            =   120

         TabIndex        =   10

         Top             =   240

         Width           =   495

      End

      Begin VB.Label lblSample 

         Caption         =   "lblSample"

         Height          =   255

         Left            =   360

         TabIndex        =   9

         Top             =   1080

         Width           =   3135

      End

   End

   Begin VB.Frame frameLanguage 

      Caption         =   "frameLanguage"

      Height          =   1095

      Left            =   0

      TabIndex        =   2

      Top             =   3960

      Width           =   4215

      Begin VB.OptionButton optLang 

         Caption         =   "optLangEs"

         Height          =   255

         Index           =   2

         Left            =   1800

         TabIndex        =   5

         Top             =   480

         Width           =   1935

      End

      Begin VB.OptionButton optLang 

         Caption         =   "optLangDe"

         Height          =   255

         Index           =   3

         Left            =   1800

         TabIndex        =   4

         Top             =   720

         Width           =   1935

      End

      Begin VB.OptionButton optLang 

         Caption         =   "optLangEn"

         Height          =   255

         Index           =   1

         Left            =   1800

         TabIndex        =   3

         Top             =   240

         Width           =   1935

      End

      Begin VB.Label lblLanguage 

         Caption         =   "lblLanguage"

         Height          =   255

         Left            =   120

         TabIndex        =   6

         Top             =   480

         Width           =   1215

      End

   End

   Begin VB.CommandButton btnCancel 

      Caption         =   "btnCancel"

      Height          =   375

      Left            =   2520

      TabIndex        =   1

      Top             =   5160

      Width           =   1215

   End

   Begin VB.CommandButton btnOk 

      Caption         =   "btnOk"

      Height          =   375

      Left            =   720

      TabIndex        =   0

      Top             =   5160

      Width           =   1215

   End

End

Attribute VB_Name = "frmConfig"

Attribute VB_GlobalNameSpace = False

Attribute VB_Creatable = False

Attribute VB_PredeclaredId = True

Attribute VB_Exposed = False

