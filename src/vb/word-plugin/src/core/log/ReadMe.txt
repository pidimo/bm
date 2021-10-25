=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-==
		LogTool Dll v1.2
=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-==
	Created By: Behrooz Sangani <bs20014@yahoo.com>
	Published Date: 12/06/2002
	WebSite: http://www.geocities.com/bs20014/
	Legal Copyright: Behrooz Sangani © 12/06/2002
=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-==

Methods and Properties
======================

===============
LogMeBaby Class
===============

LogType
    ErrorLog		1	'Logs Error Message
    WarningLog		2	'Logs Warning Message
    InformationLog		3	'Logs Information

SaveType
    AppendToLog		1	'Append to existing log
    OverWriteLog		2	'Start a new log file

AppTitle			string	'Will be shown in log file header

LogFile			string	'Path to the log file

ShowMsgAfterLog		Boolean 'whether to show message box after loggin

SaveToLog		Sub
    Message		Required'Message to be logged
    LogType		Optional'Default value is InformationLog
    SaveType		Optional'Default value is AppendToLog



========================
PredefinedMessages Class
========================

Function					Returns
--------					-------

Win32APIErr 	ErrCode As WIN32API_ERRORS    	String
Winnet32Err     ErrCode As WINNET32_ERRORS      String
SecurityErr     ErrCode As SECURITY_ERRORS      String
WinUserErr	ErrCode As WINUSER_ERRORS	String
EventLogErr	ErrCode As EVENTLOG_ERRORS	String
RpcErr		ErrCode As RPC_ERRORS		String

=====================
Copyright (c) 2002 Behrooz Sangani