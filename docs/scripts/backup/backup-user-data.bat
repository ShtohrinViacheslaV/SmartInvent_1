@echo off
set DATA_DIR=D:\SmartInvent\uploads
set BACKUP_DIR=D:\Backups\UserData
set DATESTAMP=%date:~10,4%-%date:~4,2%-%date:~7,2%

mkdir "%BACKUP_DIR%\%DATESTAMP%"
xcopy /E /I "%DATA_DIR%" "%BACKUP_DIR%\%DATESTAMP%\uploads"
