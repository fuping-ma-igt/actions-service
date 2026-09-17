@echo off
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0run-runtime-demo.ps1"
exit /b %ERRORLEVEL%
