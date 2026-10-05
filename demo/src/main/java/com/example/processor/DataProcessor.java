package com.example.processor;

import java.util.ArrayList;
import java.util.List;

import com.example.util.Logger;
import com.example.dto.ShippingDto;

/**
 * 読み込んだ配送データに対する加工・ビジネスロジックを担当するクラスです。
 * <p>
 * データベースから取得した生の配送データリストを受け取り、
 * バリデーションチェック、値の整形、および荷物のサイズ・容積分析を行います。
 * 処理結果のサマリーをログ出力し、加工済みの新しいリストを返却します。
 * </p>
 * 
 * <p><strong>【設計上の特徴】</strong></p>
 * <ul>
 *   <li>ステートレスなユーティリティ的クラスとして設計しており、メソッドはすべて static です。</li>
 *   <li>不正なデータ（必須項目欠落など）を検知した場合は、処理をスキップしてエラーカウントに計上します。</li>
 * </ul>
 * 
 * @author yamada.y
 * @version 1.0.0
 * @see ShippingDto
 * @see Logger
 */
public class DataProcessor {

    /**
     * 配送データリストに対して、バリデーションチェックや値の整形、サイズ分析を行います。
     * <p>
     * リスト内の各要素を走査し、荷物の種別（col02）に応じた容積計算や
     * データの加工（末尾への識別子付与など）を施した上で、加工済みデータのリストを生成します。
     * </p>
     * 
     * @param inputList 読み込み済みの配送データリスト（nullまたは空の場合は空のリストを返却します）
     * @return 加工済みの配送データリスト（エラーレコードは除外されます）
     */
    public static List<ShippingDto> process(List<ShippingDto> inputList) {
        if (inputList == null || inputList.isEmpty()) {
            Logger.log("加工対象のデータが存在しません。");
            return new ArrayList<>(); // ListやArrayListはJavaの標準ユーティリティーです。<>ダイヤモンド演算子内に指定されたデータを格納します。
        }

        Logger.log("データの加工・サイズ分析処理を開始します。対象件数: " + inputList.size() + " 件");
        List<ShippingDto> processedList = new ArrayList<>(); // List型にArrayListを代入できるのはListがArrayListのインターフェースだからです。オブジェクト指向のポリモーフィズムの考え方です。

        int processedCount = 0;
        int errorCount = 0;
        
        // 分析用の集計変数
        int smallCount = 0;
        int largeCount = 0;
        double totalVolume = 0.0;

        for (ShippingDto dto : inputList) {
            // 1. 簡易バリデーション
            if (dto.getShippingId() == null || dto.getShippingId().isEmpty()) {
                Logger.err("警告: 配送IDが空のレコードを検出しました。スキップします。");
                errorCount++;
                continue;
            }

            // 2. データの加工・整形（例：col_01 の末尾に特定のステータスや目印を付与する）
            String originalCol01 = dto.getCol01();
            if (originalCol01 != null) {
                dto.setCol01(originalCol01 + "_PROCESSED"); // 加工の証
            } else {
                dto.setCol01("DEFAULT_PROCESSED");
            }

            // 3. ユーティリティメソッドを呼び出してサイズ計算を実行
            double itemVolume = calculateVolume(dto);
            
            // 荷物の種別に応じたカウント集計（DTOの col02 の値で判定）
            String cargoType = dto.getCol02();
            if ("A".equals(cargoType)) { // ** .equals()はよく使います。オブジェクトの比較に==などは通常使いません。
                smallCount++;
            } else if ("B".equals(cargoType)) {
                largeCount++;
            }
            
            // 総容積に加算 // ** コードを読むだけでは視覚的に処理がわかりずらい物はこのように内容を記載します。
            totalVolume += itemVolume;

            // 4. 加工済みのリストに追加
            processedList.add(dto);
            processedCount++;
        }

        // 分析結果のサマリーをログ出力
        Logger.log("--- 【配送データ分析サマリー】 ---");
        Logger.log("  - 有効処理件数: " + processedCount + " 件");
        Logger.log("  - 小型荷物(A)件数: " + smallCount + " 件");
        Logger.log("  - 大型荷物(B)件数: " + largeCount + " 件");
        Logger.log("  - 荷物総容積 (Total Volume): " + totalVolume + " m3");
        Logger.log("--------------------------------");

        Logger.log(String.format("データ加工完了。正常処理: %d 件, スキップ(エラー): %d 件", processedCount, errorCount));
        return processedList;
    }

    /**
     * 荷物の種類や特性から容積（Volume）を算出する内部ユーティリティメソッドです。
     * <p>
     * 荷物種別（col02）が "A" の場合は1.5、"B" の場合は5.0、
     * それ以外の場合は標準値として2.5を算出します。
     * また、計算された容積情報をDTOの col01 に追記します。
     * </p>
     * 
     * @param dto 対象の配送データDTO（null不可）
     * @return 算出された容積（double値）
     */
    private static double calculateVolume(ShippingDto dto) {
        String cargoType = dto.getCol02();
        double itemVolume = 0.0;

        if ("A".equals(cargoType)) {
            itemVolume = 1.5; // 小型
        } else if ("B".equals(cargoType)) {
            itemVolume = 5.0; // 大型
        } else {
            itemVolume = 2.5; // 標準
        }

        // 計算結果や加工ステータスをDTOに反映
        String currentCol01 = dto.getCol01() != null ? dto.getCol01() : "";
        dto.setCol01(currentCol01 + "_VOL:" + itemVolume);

        return itemVolume;
    }
}