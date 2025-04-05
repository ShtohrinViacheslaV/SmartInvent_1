@echo off
set CONFIG_DIR=D:\SmartInvent\backend\src\main\java\com\smartinvent\config
set BACKUP_DIR=D:\Backups\Config
set DATESTAMP=%date:~10,4%-%date:~4,2%-%date:~7,2%

mkdir "%BACKUP_DIR%\%DATESTAMP%"
xcopy /E /I "%CONFIG_DIR%" "%BACKUP_DIR%\%DATESTAMP%\config"
