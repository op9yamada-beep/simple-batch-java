package com.example.util;

/**
 * アプリケーション全体の標準出力およびエラー出力を管理するロガーユーティリティクラスです。
 * <p>
 * コンソールに対して、フォーマット化された情報ログ（[INFO]）や
 * エラーログ（[ERROR]）を出力するための静的メソッドを提供します。
 * </p>
 * 
 * <p><strong>【設計上の特徴】</strong></p>
 * <ul>
 *   <li>インスタンス化を不要とするため、すべてのメソッドは static で定義されています。</li>
 *   <li>出力されるログには、重要度に応じたプレフィックスが付与されます。</li>
 * </ul>
 *
 * @author yamada.y
 * @version 1.0.0
 */
public class Logger {
    
    /**
     * 通常の処理経過や完了メッセージを情報ログとして標準出力に出力します。
     * 
     * @param message 出力するメッセージ文字列
     */
    public static void log(String message) { // ** JSの console.log() っぽい名前や、現場っぽいプライベートメソッドにする
        System.out.println("[INFO] " + message); // ** 標準出力でコンソールに表示します。
    }
    
    /**
     * 例外発生時や異常検知時のメッセージをエラーログとして標準エラー出力に出力します。
     * 
     * @param message 出力するエラーメッセージ文字列
     */
    public static void err(String message) {
        System.err.println("[ERROR] " + message);
    }
}