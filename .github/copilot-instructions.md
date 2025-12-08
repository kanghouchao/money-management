# Money Management 向け Copilot ガイド

## アーキテクチャ
- Spring Boot 3.5.8 + Java 21 の単一モジュール Gradle（`build.gradle`）で、`MoneyManagementApplication` → controller → service → repository の 3 層のみを許容。
- 画面はすべて `FinancialController` がルート `/` から出し分け、一覧 (`financial/records`)、フォーム (`financial/income-form` / `expense-form`)、分析 (`financial/overview` / `calendar`) の 3 画面へ遷移する。

## サービス / リポジトリ
- `FinancialRecordService` は `FinancialRecordRepository` だけを呼び出す。取得は `findAllByOrderByRecordedAtDesc` か `findAllByRecordedAtBetweenOrderByRecordedAtAsc` 経由で順序を保証する。
- `registerIncome/Expense` は `recordedAt` 未入力を `LocalDate.now()` で補正し、`Income` / `Expense` ビルダーを使って永続化するのが唯一の登録経路。
- `getCurrentMonthCategoryBreakdown` は `MonthlyCategoryBreakdown` に今月のカテゴリ別合計を詰めてチャート用モデル (`incomeChartLabels` 等) を整形する。
- `getRecordsForMonth` は `YearMonth` から 1 ヶ月分のレンジを作り、カレンダー画面向けに昇順で返す。

## データモデル
- `FinancialRecord` は `@Inheritance(SINGLE_TABLE)` + `record_discriminator` で `Income`/`Expense` を同居させ、`@PrePersist` で `recordType` を自動設定するため手動代入禁止。
- 金額カラムは `precision=15, scale=2`、フォーム側も `@DecimalMin("0.01")` / HTML `min="1" step="1"` を維持すること。
- `IncomeCategory` / `ExpenseCategory` は絵文字アイコン + 日本語ラベルを持ち、DB には列挙文字列 (VARCHAR 64) を保存する。

## フォーム & ビュー契約
- `IncomeForm` / `ExpenseForm` (`controller/request`) は `@DateTimeFormat` 済み `recordedAt` とカテゴリ必須。GET では `ensureIncomeForm/ensureExpenseForm` を呼んで `Model` に追加する。
- POST 時は `BindingResult` エラーで同じテンプレートを再描画し、必ず `incomeCategories` / `expenseCategories` を再投入。成功時のみ `RedirectAttributes#addFlashAttribute("successMessage", …)` で `/` へ PRG。
- `financial/records.html` は `records` (List<`FinancialRecordView`>) が前提で、`record.recordType.label` や `#temporals.format` を使う。空リスト時はプレースホルダ表示。
- `financial/overview.html` は Chart.js 4 を CDN で読み込み、`incomeChartLabels/Values` と `expenseChartLabels/Values` が空ならグラフを描画しない。新しい分析画面もこの配列形式に合わせる。
- `financial/calendar.html` は `weeks` (List<List<`CalendarDayView`>>) と `weekdays`, `today`, `monthIncomeTotal` などが揃っていることが条件。

## フロントエンド指針
- すべてのテンプレートで Tailwind Play CDN + `static/css/common.css` のカスタムテーマ (`@theme`) を共有。追加スタイルはページ内 `<style>` で閉じ、既存カラーパレットを破壊しない。
- UI ナビはルート (`/`, `/income/new`, `/expense/new`, `/overview`, `/calendar`) を直リンクする構成。新規画面も既存ボタン配置・ラベルを踏襲する。

## 開発ワークフロー
- `.env.example` を `.env` にコピーし、`DB_HOST=db`・`DB_PORT=3306` 等を指定。`make up` (=`docker compose up -d`) で `mm` と `db` を起動、`make down` でボリュームごと停止。
- サービス単体起動は `./gradlew bootRun`、テストは `./gradlew test`。Docker 内 DB に入る場合は `make db` が `mysql -u${DB_USER}` をラップする。
- `spring.jpa.hibernate.ddl-auto=create-drop` + `logging.level.org.hibernate.SQL=DEBUG` / `BasicBinder=TRACE` がデフォルトなので、大量 INSERT 時のログノイズを想定して実装する。

## よくある落とし穴
- `Model` に `incomeForm` / `expenseForm` が無い状態でテンプレートを開くとバインディング例外になる。GET で new する既存ヘルパーを必ず利用。
- `FinancialRecordType` は派生エンティティが自動設定するため、DTO から値を渡さない。重複して設定すると `recordType` が null のまま保存される恐れがある。
- `FinancialRecordService#getCurrentMonthCategoryBreakdown` は全件ロード後に今月分だけフィルタするため、件数が増えると遅くなる。月次処理を追加する際は同じ前提を共有し、テスト用データ件数を抑える。
- フラッシュメッセージキーは `successMessage` 固定。別のキーを使うと `records.html` で表示されない。
