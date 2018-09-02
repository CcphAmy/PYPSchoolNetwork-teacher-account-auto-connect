@echo off 
:begin
echo 心跳包跳动中!!!
java PYPSchoolNetwork
echo 下一次心跳包于 5 分钟后进行...
choice /t 150 /d y /n >nul
echo 下一次心跳包于 2.5 分钟后进行...
choice /t 100 /d y /n >nul
echo 下一次心跳包于 50 秒后进行...
choice /t 50 /d y /n >nul
goto begin