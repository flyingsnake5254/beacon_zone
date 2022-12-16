輔助科技期末報告
---------------------
iBeacon 自動簽到系統
410977007 軟體三 盧姵君
---------------------
--------------------
相關文件與影片：
ｇitgub : https://github.com/flyingsnake5254/beacon_zone
google ppt : https://docs.google.com/presentation/d/1n8ngc5xpTAEHDnT2zMvzEEU6__5NFmsaocKhgo17QyI/edit?usp=sharing
youtube 功能演示：https://youtu.be/N5Pk1W2u90s
apk 位置：beacon_zone/app/build/outputs/apk/debug/app-debug.apk
---------------------
開發設備與套件
    設備：THLight USBeacon B4022T (規格：iBeacon)
    開發環境：Android Studio (JAVA)
    資料庫：Firebase Realtime Database
    其他 library : android beacon library (https://altbeacon.github.io/android-beacon-library/)
--------------------
專案說明
開發一結合 iBeacon 與 app 的自動簽到系統。與其他簽到方式比較，具有以下優勢：
    1. 相較於傳統紙本簽到更方便，且更容易將資料做數據處理。
    2. 相較於 Wifi 簽到，iBeacon 具有低成本與低功耗之優勢。

由於開發時間有限，本專案將著重在如何實現利用 iBeacon 進行簽到，至於有關跨平台、簽到防偽機制、帳號管理等，將不再本專案重點範圍內。
--------------------

程式說明
以下將說明幾個類別：
Welcome :
    用以實現開場動畫

LoginActivity :
    編寫登入程式，其中使用者分為 root, teacher, student，其中 root 目前為協助開發與測試的帳號，其有 reset database 之功能，故並不開放一般使用者使用。

StudentPage, TeacherPage, RootPage :
    三種使用者登入後會到的不同主頁面，其頁面會有該權限可以使用的各種功能，如下：
        root : reset database
        teacher : 查看缺席狀況
        student : 查看出缺情狀況、查看缺席情況
    其中 StudentPage 還同時負責背景偵測與解開 Beacon 封包

StudentShowSign :
    學生查詢出缺勤狀況的頁面
        綠色 => 有出席
        紅色 => 缺席


-----------------------




