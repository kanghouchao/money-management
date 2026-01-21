package com.technos.moneyManagement.config;

import com.technos.moneyManagement.repository.entity.ExpenseCategory;
import com.technos.moneyManagement.repository.entity.IncomeCategory;
import com.technos.moneyManagement.service.FinancialRecordService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** 初期データを自動投入するコンポーネント アプリケーション起動時に1月1日から25日までの模擬データを生成します */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final FinancialRecordService financialRecordService;
  private final Random random = new Random(2026_01_25); // 再現性のため固定シード

  @Override
  public void run(String... args) {
    log.info("=== データ初期化開始 ===");

    // 1月1日から24日までの日常支出を追加
    for (int day = 1; day <= 24; day++) {
      LocalDate date = LocalDate.of(2026, 1, day);
      generateDailyExpenses(date);
    }

    // 25日は給料日（週末のため前倒し）
    LocalDate payDay = LocalDate.of(2026, 1, 25);
    generateSalaryIncome(payDay);
    generateDailyExpenses(payDay);

    log.info("=== データ初期化完了 ===");
  }

  /** 日常の支出を生成 */
  private void generateDailyExpenses(LocalDate date) {
    int dayOfWeek = date.getDayOfWeek().getValue(); // 1=月, 7=日

    // 毎日の食費（朝昼晩）
    generateFoodExpenses(date, dayOfWeek);

    // 交通費（平日のみ）
    if (dayOfWeek <= 5) {
      financialRecordService.registerExpense(
          date, new BigDecimal("800"), "通勤定期券", ExpenseCategory.TRANSPORTATION);
    }

    // その他のランダムな支出
    generateRandomExpenses(date, dayOfWeek);
  }

  /** 食費の生成 */
  private void generateFoodExpenses(LocalDate date, int dayOfWeek) {
    int day = date.getDayOfMonth();

    // 朝食（コンビニやカフェ）
    if (dayOfWeek <= 5) {
      int breakfastCost = 300 + random.nextInt(200); // 300-500円
      financialRecordService.registerExpense(
          date, new BigDecimal(breakfastCost), "朝食（コンビニ）", ExpenseCategory.FOOD);
    } else {
      int breakfastCost = 400 + random.nextInt(300); // 400-700円
      financialRecordService.registerExpense(
          date, new BigDecimal(breakfastCost), "朝食（カフェ）", ExpenseCategory.FOOD);
    }

    // 昼食
    if (dayOfWeek <= 5) {
      int lunchCost = 800 + random.nextInt(400); // 800-1200円
      financialRecordService.registerExpense(
          date, new BigDecimal(lunchCost), "昼食（定食屋）", ExpenseCategory.FOOD);
    } else {
      // 週末は少し豪華に
      int lunchCost = 1200 + random.nextInt(800); // 1200-2000円
      financialRecordService.registerExpense(
          date, new BigDecimal(lunchCost), "昼食（レストラン）", ExpenseCategory.FOOD);
    }

    // 夕食
    if (dayOfWeek <= 5) {
      if (random.nextBoolean()) {
        // 自炊
        int dinnerCost = 400 + random.nextInt(300); // 400-700円
        financialRecordService.registerExpense(
            date, new BigDecimal(dinnerCost), "夕食（自炊用食材）", ExpenseCategory.FOOD);
      } else {
        // 外食
        int dinnerCost = 1000 + random.nextInt(1000); // 1000-2000円
        financialRecordService.registerExpense(
            date, new BigDecimal(dinnerCost), "夕食（外食）", ExpenseCategory.FOOD);
      }
    } else {
      // 週末の外食
      int dinnerCost = 2000 + random.nextInt(2000); // 2000-4000円
      financialRecordService.registerExpense(
          date, new BigDecimal(dinnerCost), "夕食（週末外食）", ExpenseCategory.FOOD);
    }

    // 週に1-2回のスーパー買い出し
    if (dayOfWeek == 3 || dayOfWeek == 6) {
      int groceryCost = 3000 + random.nextInt(2000); // 3000-5000円
      financialRecordService.registerExpense(
          date, new BigDecimal(groceryCost), "スーパーで買い出し", ExpenseCategory.FOOD);
    }
  }

  /** ランダムな支出の生成 */
  private void generateRandomExpenses(LocalDate date, int dayOfWeek) {
    int day = date.getDayOfMonth();

    // 月初に固定費
    if (day == 5) {
      financialRecordService.registerExpense(
          date, new BigDecimal("8500"), "電気代", ExpenseCategory.UTILITIES);
      financialRecordService.registerExpense(
          date, new BigDecimal("4200"), "ガス代", ExpenseCategory.UTILITIES);
      financialRecordService.registerExpense(
          date, new BigDecimal("3500"), "水道代", ExpenseCategory.UTILITIES);
    }

    if (day == 10) {
      financialRecordService.registerExpense(
          date, new BigDecimal("8800"), "スマホ代", ExpenseCategory.COMMUNICATION);
      financialRecordService.registerExpense(
          date, new BigDecimal("5500"), "インターネット代", ExpenseCategory.COMMUNICATION);
    }

    // 週末の娯楽
    if (dayOfWeek == 6 || dayOfWeek == 7) {
      if (random.nextInt(10) < 6) { // 60%の確率
        int entertainmentCost = 2000 + random.nextInt(3000); // 2000-5000円
        String[] activities = {"映画", "カラオケ", "ゲーム購入", "書籍購入", "趣味用品"};
        financialRecordService.registerExpense(
            date,
            new BigDecimal(entertainmentCost),
            activities[random.nextInt(activities.length)],
            ExpenseCategory.ENTERTAINMENT);
      }
    }

    // 日用品（週に1回程度）
    if (day % 7 == 2) {
      int dailyCost = 1500 + random.nextInt(2000); // 1500-3500円
      financialRecordService.registerExpense(
          date, new BigDecimal(dailyCost), "ドラッグストアで買い物", ExpenseCategory.DAILY_NECESSITIES);
    }

    // 衣服（月に1-2回）
    if (day == 8 || day == 22) {
      int clothingCost = 5000 + random.nextInt(10000); // 5000-15000円
      financialRecordService.registerExpense(
          date, new BigDecimal(clothingCost), "衣類購入", ExpenseCategory.CLOTHING);
    }

    // 美容（月に1-2回）
    if (day == 12) {
      financialRecordService.registerExpense(
          date, new BigDecimal("4500"), "ヘアカット", ExpenseCategory.BEAUTY);
    }

    // 医療（たまに）
    if (day == 15) {
      financialRecordService.registerExpense(
          date, new BigDecimal("2300"), "薬局で風邪薬", ExpenseCategory.MEDICAL);
    }

    // 教育・自己投資（たまに）
    if (day == 18) {
      financialRecordService.registerExpense(
          date, new BigDecimal("3800"), "オンライン講座", ExpenseCategory.EDUCATION);
    }
  }

  /** 給与収入の生成（25日） */
  private void generateSalaryIncome(LocalDate date) {
    // 基本給（手取り約42万円）
    financialRecordService.registerIncome(
        date, new BigDecimal("420000"), "1月分給与（週末のため前倒し支給）", IncomeCategory.SALARY);

    log.info("給与データを追加しました: {} - 420,000円", date);
  }
}
