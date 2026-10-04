package com.example.writer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import com.example.dto.ShippingDto;
import com.example.util.Logger;

/**
 * 配送データの書き込みを担当するデータアクセス（Writer）クラスです。
 * <p>
 * 外部から受け取った加工済みの配送データリストを、
 * プロパティファイルから取得したSQLを用いてデータベースへ一括登録します。
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
public class ShippingWriter {

    private final Connection conn;
    private final Properties sqlProp;

    /**
     * コンストラクタ
     * 
     * @param conn 有効なデータベース接続インスタンス
     * @param sqlProp SQLプロパティ設定インスタンス
     */
    public ShippingWriter(Connection conn, Properties sqlProp) {
        this.conn = conn;
        this.sqlProp = sqlProp;
    }

/**
     * 加工済みの配送データリストをデータベースに書き込みます。
     * <p>
     * プロパティファイルから取得した INSERT 用のSQLを元に {@link PreparedStatement} を生成し、
     * リスト内の各DTOから値を取り出してバッチ処理（一括実行）を行います。
     * </p>
     * 
     * @param processedList 加工済み配送データのリスト（nullまたは空の場合は処理を行いません）
     * @throws SQLException データベースへの書き込みエラー、またはSQL構文エラーが発生した場合
     */
    public void write(List<ShippingDto> processedList) throws SQLException {
        if (processedList == null || processedList.isEmpty()) {
            Logger.log("書き込むべきデータが存在しません。");
            return;
        }

        // 例：INSERTまたはUPDATE用のSQL文（必要に応じてプロパティファイルから取得してもOKです）
        Logger.log("1つ目の配送データ書き込みを開始します...");
        String sqlSelect = sqlProp.getProperty("batch.shipping.insert.result");

        Logger.log("配送データの書き込みを開始します。件数: " + processedList.size() + " 件");

        try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) {

            pstmt.setInt(1, 1);

            int count = 0;

            for (ShippingDto dto : processedList) {
                // DTOから値を取り出してプレースホルダーにバインド
                pstmt.setString(1, dto.getShippingId());
                pstmt.setString(2, dto.getCol01()); // 加工済みの値など

                // バッチに追加　// **（複数のSQLをまとめて効率よく実行するテクニック）
                pstmt.addBatch();
                count++;

            } // ** 一定件数ごとに実行するなどのチューニングも実務では行います

            // まとめて実行
            pstmt.executeBatch(); // ** 一括送信にて通信回数を減らす
            Logger.log("データ書き込みが完了しました。総件数: " + count + " 件");
        }
    }
}
