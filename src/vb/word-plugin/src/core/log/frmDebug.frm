VERSION 5.00
Begin VB.Form frmDebug 
   BorderStyle     =   5  'Sizable ToolWindow
   ClientHeight    =   1680
   ClientLeft      =   6345
   ClientTop       =   8325
   ClientWidth     =   8550
   LinkTopic       =   "Form1"
   MaxButton       =   0   'False
   MinButton       =   0   'False
   ScaleHeight     =   1680
   ScaleWidth      =   8550
   ShowInTaskbar   =   0   'False
   Begin VB.TextBox txtDebug 
      BeginProperty Font 
         Name            =   "Arial"
         Size            =   8.25
         Charset         =   178
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   1695
      Left            =   0
      Locked          =   -1  'True
      MultiLine       =   -1  'True
      ScrollBars      =   2  'Vertical
      TabIndex        =   0
      Top             =   0
      Width           =   8535
   End
End
Attribute VB_Name = "frmDebug"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
'=========================================================================================
'  Debug Window
'  Shows the messages in a window
'=========================================================================================
'  Created By: Behrooz Sangani <bs20014@yahoo.com>
'  Published Date: 12/06/2002
'  WebSite: http://www.geocities.com/bs20014/
'  Legal Copyright: Behrooz Sangani © 12/06/2002
'=========================================================================================

Private Sub Form_Load()
    Caption = sTitle & " Debug Window"
End Sub

Private Sub Form_QueryUnload(Cancel As Integer, UnloadMode As Integer)
    If UnloadMode = 0 Then
        Cancel = 1
        Me.Hide
    End If
End Sub

Private Sub Form_Resize()
    txtDebug.Move 0, 0, Me.ScaleWidth, Me.ScaleHeight
End Sub
