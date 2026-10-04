package com.example.dto;

import lombok.Data;

/**
 * 配送データおよび集計結果を保持するデータ転送オブジェクト（DTO）クラスです。
 * <p>
 * データベース（T_DETAIL_LOGなど）から読み込まれたレコード情報や、
 * 後続の加工処理（DataProcessor）で受け渡しを行うためのデータ構造をカプセル化します。
 * </p>
 * 
 * <p><strong>【設計上の特徴】</strong></p>
 * <ul>
 *   <li>Lombokの {@code @Data} アノテーションを利用し、定型コード（Getter/Setter/toString等）を自動生成しています。</li>
 *   <li>ビジネスロジックは持たず、純粋なデータ保持（構造体）としての役割に特化しています。</li>
 * </ul>
 * 
 * @author yamada.y
 * @version 1.0.0
 */
@Data // ** Lombokにより、これ1つで Getter, Setter, toString, equals, hashCode がすべて自動生成される！
public class ShippingDto {
    /** 配送管理ID（主キー） */
    private String shippingId;

    /** 配送データ項目 01 */
    private String col01;

    /** 配送データ項目 02 */
    private String col02;

    /** 配送データ項目 03 */
    private String col03;

    /** 配送データ項目 04 */
    private String col04;

    /** 配送データ項目 05 */
    private String col05;

    /** 配送データ項目 06 */
    private String col06;

    /** 配送データ項目 07 */
    private String col07;

    /** 配送データ項目 08 */
    private String col08;

    /** 配送データ項目 09 */
    private String col09;

    /** 配送データ項目 10 */
    private String col10;

    /** 配送データ項目 11 */
    private String col11;

    /** 配送データ項目 12 */
    private String col12;

    /** 配送データ項目 13 */
    private String col13;

    /** 配送データ項目 14 */
    private String col14;

    /** 配送データ項目 15 */
    private String col15;

    /** 配送データ項目 16 */
    private String col16;

    /** 配送データ項目 17 */
    private String col17;

    /** 配送データ項目 18 */
    private String col18;

    /** 配送データ項目 19 */
    private String col19;

    /** 配送データ項目 20 */
    private String col20;
}
