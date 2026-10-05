package com.example.reader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.example.dto.ShippingDto;
import com.example.util.Logger;

/**
 * 配送データの読み込みを担当するデータアクセス（Reader）クラスです。
 * <p>
 * 外出しされたプロパティファイルから複数のSQL（通常抽出クエリおよび集計用JOINクエリ）を取得し、
 * データベースへのプレースホルダー付きプリペアードステートメントを実行します。
 * 取得した結果セットは、後続の加工処理で扱いやすいように {@link ShippingDto} のリストへマッピングして返却します。
 * </p>
 * 
 * <p><strong>【設計上の特徴】</strong></p>
 * <ul>
 *   <li>データベースの接続（Connection）は外部からインジェクション（コンストラクター経由で受給）します。</li>
 *   <li>内部での例外（SQLException）はキャッチせず、上位のトランザクション管理機構（Appクラス）へスローします。</li>
 *   <li>リソースのリークを防ぐため、try-with-resources構文により確実なクローズ処理を行っています。</li>
 * </ul>
 *
 * @author yamada.y
 * @version 1.0.0
 * @see ShippingDto
 * @see Logger
 */
public class ShippingReader {

    private Connection conn;
    private Properties sqlProp;

    // コンストラクタでコネクションとSQL用のプロパティを受け取る
    public ShippingReader(Connection conn, Properties sqlProp) {
        this.conn = conn;
        this.sqlProp = sqlProp;
    }

    /**
     * プロパティファイルから複数のSQLを順次実行し、配送データを一括読み込みします。
     * 
     * @return データベースから取得した配送データのリスト（{@link ShippingDto} の集合）
     * @throws SQLException データベースへのアクセスエラー、SQL構文エラー、または接続断が発生した場合
     */
    public List<ShippingDto> read() throws SQLException {
        List<ShippingDto> dataList = new ArrayList<>();

        // =============================================================
        // 1つ目のSQL（batch.shipping.select.simple）の実行
        // =============================================================
        String sqlSelect = sqlProp.getProperty("batch.shipping.select.simple"); // ** sql.propertiesから指定したキーの文字列(このファイルの場合はSQL)を取得します。
        Logger.log("1つ目の配送データ読み込みを開始します...");

        try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) { // ** まずSQLのステートメントを作成します。
            // パラメータのバインド (r.is_active = ?, d.status = ?)
            pstmt.setInt(1, 1); // ** ?部分を置き換えます。1番目の?を1に置き換えるとなります。
            pstmt.setString(2, "READY"); // ** ?部分を置き換えます。2番目の?をREADYに置き換えるとなります。

            try (ResultSet rs = pstmt.executeQuery()) { // ** SQLを実行します。
                int count = 0;
                while (rs.next()) { // ** 先頭の結果から順に処理していきます。
                    ShippingDto dto = new ShippingDto();
                    dto.setShippingId(rs.getString("shipping_id")); // ** getString()で対応する値を取得しDTOオブジェクトにセットしていきます。
                    dto.setCol01(rs.getString("col_01"));
                    dto.setCol02(rs.getString("col_02"));
                    dto.setCol03(rs.getString("col_03"));
                    dto.setCol04(rs.getString("col_04"));
                    dto.setCol05(rs.getString("col_05"));
                    dto.setCol06(rs.getString("col_06"));
                    dto.setCol07(rs.getString("col_07"));
                    dto.setCol08(rs.getString("col_08"));
                    dto.setCol09(rs.getString("col_09"));
                    dto.setCol10(rs.getString("col_10"));
                    dto.setCol11(rs.getString("col_11"));
                    dto.setCol12(rs.getString("col_12"));
                    dto.setCol13(rs.getString("col_13"));
                    dto.setCol14(rs.getString("col_14"));
                    dto.setCol15(rs.getString("col_15"));
                    dto.setCol16(rs.getString("col_16"));
                    dto.setCol17(rs.getString("col_17"));
                    dto.setCol18(rs.getString("col_18"));
                    dto.setCol19(rs.getString("col_19"));
                    dto.setCol20(rs.getString("col_20"));

                    dataList.add(dto);
                    count++;
                }
                Logger.log("1つ目のデータ読み込み完了。件数: " + count + " 件");
            }
        }

        // =============================================================
        // 2つ目のSQL（batch.shipping.select.detail）の実行（大量列・JOIN・GROUP BY）
        // =============================================================
        String sqlQuery = sqlProp.getProperty("batch.shipping.select.detail"); // ** 上のSQLとは別のSQLをプロパティファイルから読み込みます。
        Logger.log("2つ目の配送データ読み込み（集計クエリ）を開始します...");

        try (PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            // パラメータのバインド (r.is_active = ?)
            pstmt.setInt(1, 1);

            try (ResultSet rs = pstmt.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    ShippingDto dto = new ShippingDto();
                    dto.setShippingId(rs.getString("shipping_id"));
                    dto.setCol01(rs.getString("col_01"));
                    dto.setCol02(rs.getString("col_02"));
                    dto.setCol03(rs.getString("col_03"));
                    dto.setCol04(rs.getString("col_04"));
                    dto.setCol05(rs.getString("col_05"));
                    dto.setCol06(rs.getString("col_06"));
                    dto.setCol07(rs.getString("col_07"));
                    dto.setCol08(rs.getString("col_08"));
                    dto.setCol09(rs.getString("col_09"));
                    dto.setCol10(rs.getString("col_10"));
                    dto.setCol11(rs.getString("col_11"));
                    dto.setCol12(rs.getString("col_12"));
                    dto.setCol13(rs.getString("col_13"));
                    dto.setCol14(rs.getString("col_14"));
                    dto.setCol15(rs.getString("col_15"));
                    dto.setCol16(rs.getString("col_16"));
                    dto.setCol17(rs.getString("col_17"));
                    dto.setCol18(rs.getString("col_18"));
                    dto.setCol19(rs.getString("col_19"));
                    dto.setCol20(rs.getString("col_20"));
                    dataList.add(dto);
                    count++;
                }
                Logger.log("2つ目のデータ読み込み完了。件数: " + count + " 件");
            }
        }

        Logger.log("すべてのデータ読み込みが完了しました。合計件数: " + dataList.size() + " 件");
        return dataList;
    }
}