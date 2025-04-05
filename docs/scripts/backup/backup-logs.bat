@echo off
set LOG_DIR=D:\SmartInvent\logs
set BACKUP_DIR=D:\Backups\Logs
set DATESTAMP=%date:~10,4%-%date:~4,2%-%date:~7,2%

mkdir "%BACKUP_DIR%\%DATESTAMP%"
xcopy /E /I "%LOG_DIR%" "%BACKUP_DIR%\%DATESTAMP%\logs"
