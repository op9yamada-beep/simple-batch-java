## 1.プログラム説明
現場でバッチを作ってとお願いされた場合の圧縮バージョンです。
お客様の依頼的には：
中継地点でバーコードをバイトの子が読むからそれで商品情報が読めるからそれで
データベースに登録されているので、
その情報に目印の情報をつけて、次の行程でトラックに積み込むから別のデータベースに
サイズの合計とかいれて保存してほしいんだよねぇ。

みたいな感じで依頼が来たと想定しています。

## 2.処理の流れ
処理の流れ的には配送の商品情報DB読み込み→データをロジックで加工し→トラックの積み込み用のDBに登録というシンプルな構造で、バッチはこのような構成を取る事が多いです。
このプログラムに肉付きしてテーブルを増やしたり、加工を増やしたりする感じですね。

## 3.プログラムの読み方について
// ** このコメントは学習用の細く書いたコメントです。

読み方としてはmainのApp.javaから追っていきます。
URL:https://github.com/op9yamada-beep/simple-batch-java/blob/main/demo/src/main/java/com/example/app/App.java
DB接続のDataProcessor.javaでファクトリーで接続を確立します。
その後に
DB読み込み用のShippingReader.java
加工用のDataProcessor.java
DB書き込み用のShippingWriter.java
に遷移し処理を終了します。

# ソースコードが見当たらない場合
src/main/java/com/example/
├── app/App.java                   
├── dto/ShippingDto.java
├── reader/ShippingReader.java
├── processor/DataProcessor.java
├── writer/ShippingWriter.java
└── util/ Logger.java 

マジックリテラルになるSQL部分はpropertiesファイルで指定し読み込みをしています。
(src/main/resources/sql.properties)

sql.propertiesの中身は実際現場で使われるようなJOINやGROUP BYを組み合わせたようなサンプルを使っています。

## 4.現場の品質管理の視点
ネームスペース、命名規則、命名規則のキャメル、スペース・改行、処理が重くならないように考慮
クラス設計、メソッド分割、Javadocの記法、処理の抜け、助長なコード、助長なコメント
もっと賢く書けるコードの残存、
実装方法の選択間違い（Springなどだと実装方法Aと実装方法Bがある場合になぜその実装方法を選んだのかなどと突っ込まれることがある）
などJavaの基礎となる部分は当たり前のように知って実装できる必要がある。

## 5.実際動かす時に
# DataProcessor.javaの下の部分は自分の環境に合わせて変える必要があります。your_** の部分です。URLのtimezoneあたりも合わせないとエラーになるかも。
    private static final String URL = "jdbc:mysql://localhost:3306/your_contener?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Tokyo";
    private static final String USER = "your_user"; // ** 実際の現場では config.properties などから読み込むのが一般的ですが、
    private static final String PASSWORD = "your_password"; // ** サンプルとして分かりやすく定数化または直書きしています。

## 6.動作環境
基本的にjavaやMySQLが入っている環境であればソースをそのまま落として実行すれば動くはずです。

## 7.テスト用のSQL配備（実際に動かしてテストする場合につかってください）
-- 1. 読み込み元テーブル（T_DETAIL_LOG）
CREATE TABLE IF NOT EXISTS T_DETAIL_LOG (
    shipping_id VARCHAR(50) PRIMARY KEY,
    route_id INT,
    status INT,
    col_01 VARCHAR(100), col_02 VARCHAR(100), col_03 VARCHAR(100), col_04 VARCHAR(100), col_05 VARCHAR(100),
    col_06 VARCHAR(100), col_07 VARCHAR(100), col_08 VARCHAR(100), col_09 VARCHAR(100), col_10 VARCHAR(100),
    col_11 VARCHAR(100), col_12 VARCHAR(100), col_13 VARCHAR(100), col_14 VARCHAR(100), col_15 VARCHAR(100),
    col_16 VARCHAR(100), col_17 VARCHAR(100), col_18 VARCHAR(100), col_19 VARCHAR(100), col_20 VARCHAR(100)
);

-- 2. 配送ルートマスタ（INNER JOIN用）
CREATE TABLE IF NOT EXISTS M_TRANSIT_ROUTE (
    route_id INT PRIMARY KEY,
    is_active INT
);

-- 3. 書き込み先テーブル（T_SHIPPING_RESULT）
CREATE TABLE IF NOT EXISTS T_SHIPPING_RESULT (
    shipping_id VARCHAR(50),
    result_col_01 VARCHAR(100),
    updated_at TIMESTAMP
);

-- マスタデータ（有効ルート）
INSERT INTO M_TRANSIT_ROUTE (route_id, is_active) VALUES (1, 1);

-- ログデータ（条件にヒットするデータ例）
INSERT INTO T_DETAIL_LOG (
    shipping_id, route_id, status, 
    col_01, col_02, col_03, col_04, col_05, 
    col_06, col_07, col_08, col_09, col_10, 
    col_11, col_12, col_13, col_14, col_15, 
    col_16, col_17, col_18, col_19, col_20
) VALUES (
    'SHIP-001', 1, 1,
    'data01', 'data02', 'data03', 'data04', 'data05',
    'data06', 'data07', 'data08', 'data09', 'data10',
    'data11', 'data12', 'data13', 'data14', 'data15',
    'data16', 'data17', 'data18', 'data19', 'data20'
);
