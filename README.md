# TimerApplication

■共通仕様

| 項目 | 設定値 |
| --- | --- | 
| URL | http://[IPアドレス]/v1/ |
| レスポンス形式 | JSON |
| エンコーディング | UTF-8 |

■レスポンス

| レスポンスコード | 説明 |
| --- | --- | 
| 200 | OK。データ取得正常終了 | 
| 201 | Created。データ更新正常終了 | 
| 204 | Deleted。データ削除正常終了 | 
| 400 | Bad Request。パラメータ及びヘッダー不正。パラメータが無いもしくは書式が異なる。 | 
| 404 | Not Found。存在しないAPI呼び出し。 | 
| 409 | Conflict。値の重複による登録失敗。 | 
| 500 | Internal Server Error。その他サーバ起因のAPIエラー。 | 

■API一覧

| API名 | PATH | メソッド | 説明 |　戻り値 |
| --- | --- | --- | --- |　--- |
| バージョン取得 | Version | GET | 起動時にバージョンの取得を行うAPI |　{"status": 200 ,"error":"", "ver":"1.0.0"} |
| マスタデータ取得 | MasterSync | GET | 起動時にサーバから最新のマスタを取得するAPI |　{"status": 200 ,"error":"", "jan_code":"11111111"},"name":"カップヌードル","wait_time":180 |
| 茹で時間登録・照会 | BoilTime | GET | 茹で時間を照会するAPI |　{"status": 200 ,"error":"", "wait_time":120} |
| 茹で時間登録・照会 | BoilTime | POST | 茹で時間を登録するAPI |　{"status": 201 ,"error":""} |
