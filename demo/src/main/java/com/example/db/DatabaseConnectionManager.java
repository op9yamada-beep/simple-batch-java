package com.example.db;

import com.example.util.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ファクトリーを形成しデータベースへ接続するクラスです。
 * <p>
 * データベースへ定数にて接続し、バッチ処理におけるトランザクションの初期設定からクローズ処理までを行います。
 * </p>
 * 
 * <p><strong>【設計上の特徴】</strong></p>
 * <ul>
 *   <li>ファクトリーメソッドパターンを採用し、DB接続の生成処理をカプセル化しています。</li>
 *   <li>接続確立時に自動コミットを無効化（setAutoCommit(false)）し、安全なトランザクション制御を担保します。</li>
 *   <li>例外発生時や処理終了時に確実に接続を破棄するクローズ処理を提供します。</li>
 * </ul>
 * 
 * @author yamada.y
 * @version 1.0.0
 */
public class DatabaseConnectionManager {

    // DB接続用ステータス
    private static final String URL = "jdbc:mysql://localhost:3306/your_container?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Tokyo";
    private static final String USER = "root"; // ** 実際の現場では config.properties などから読み込むのが一般的ですが、
    private static final String PASSWORD = ""; // ** サンプルとして分かりやすく定数化または直書きしています。

    /**
     * データベースへの接続インスタンスを生成して返却します。　// **（ファクトリーメソッド）。
     * 
     * @return 確立された Connection インスタンス
     * @throws SQLException 接続に失敗した場合
     */
    public static Connection connect() throws SQLException {
        Logger.log("データベースへの接続を確立しています...");
        try {
            // MySQL用JDBCドライバの明示的なロード　// **（古いバージョンや環境への配慮）
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBCドライバが見つかりません。", e);
        }

        // コネクションを生成して返す
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        // バッチ処理では手動コミット（トランザクション制御）にするのが現場の常道
        conn.setAutoCommit(false); // **　勝手にcommitしないように明示する。
        
        Logger.log("データベース接続に成功しました。");
        return conn;
    }

    /**
     * データベース接続を安全にクローズします。
     * 
     * @param conn クローズ対象のコネクション
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) { // ** DBがクローズされていなければ。
                    conn.close(); // **　DBをクローズする
                }
            } catch (SQLException e) {
                Logger.err("データベース接続のクローズ時にエラーが発生しました: " + e.getMessage());
            }
        }
    }
}