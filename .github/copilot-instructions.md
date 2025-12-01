# Money Management 向け Copilot ガイド

## プロジェクト概要
- Spring Boot 3.5.8 + Java 21 の単一モジュール Gradle プロジェクトで、エントリポイントは `MoneyManagementApplication`。
- 収入/支出管理を想定しているが、現状は `Demo` エンティティを CRUD する最小構成。
- MVC レイヤは controller → service → repository の 3 層で明確に分離されている。
- テンプレートは Thymeleaf、スタイルは Tailwind CDN + `static/css/common.css` を組み合わせる。

## バックエンドアーキテクチャ
- `controller/Index.java` がルート (`GET/POST /`) を処理し、Model に `demos` を積んで `templates/index.html` を描画する。
- POST `/` は `DemoService` に保存を委譲し、`name` 未入力時だけ `templates/error/400.html` を返すバリデーションがある。
- `DemoService` は `DemoRepository` の薄いラッパーとして `getAllDemos` と `save` を提供し、@RequiredArgsConstructor で依存注入している。
- 例外処理や DTO 変換は未実装のため、新機能でもまず service 層でドメインロジックを完結させる方針に合わせる。

## データ層と設定
- `repository/DemoRepository` は `JpaRepository<Demo, Integer>`。新しい永続化対象も同じパターンで作成し、ID は `GenerationType.IDENTITY` が既定。
- `repository/entity/Demo` は `@Table(name = "demo")` 固定のため、テーブル名・カラムを変える場合はマイグレーションも忘れずに更新。
- `application.properties` はすべての DB 接続設定を `${DB_*}` 環境変数から読み込み、Hibernate SQL/バインドログを DEBUG/TRACE で出す構成。
- `docker-compose.yml` の `mm` サービスは `DB_PORT:-5432` をデフォルトにしているが、MySQL には `.env` の `DB_PORT=3306` を必ず読み込ませて整合性を取る。

## フロントエンド/テンプレート
- `templates/index.html` は `th:each="demoItem : ${demos}"` でリスト表示するため、モデルキー `demos` を欠かさないこと。
- Tailwind は CDN (`@tailwindcss/browser@4`) で読み込み、`static/css/common.css` の `@theme` でカスタムカラーを定義している。
- 入力フォームは `th:action="@{/}"` と `name="name"` を固定で期待するため、サーバ側フォームバインドとも整合させる。
- エラービューは `templates/error/400.html` のみ。追加エラーを増やす際は `ControllerAdvice` ではなくテンプレートを増やすのが既存方針。

## 開発ワークフロー
- 初回は `.env.example` をコピーして DB 資格情報を設定し、`docker compose up -d --wait` で `db` と `mm` を起動する。
- Makefile で `make up/down/logs/db` をラップしており、`make logs` は `mm` サービス、`make db` は `docker compose exec db mysql -u${DB_USER}` を実行する。
- アプリ単体をローカルで動かすときは `./gradlew bootRun`、テストは `./gradlew test`（JUnit Platform）で実行。
- DB には MySQL 8.0.44 を使用し、`spring.jpa.hibernate.ddl-auto=update` が有効なのでテーブル変更はローカルで自動反映されるが、本番相当ではマイグレーション管理が必要。
- SQL/バインドログが INFO/DEBUG/TRACE で大量に出るため、CI や新規テストではログノイズを考慮してアサーションを組む。

## 実装上のヒント
- 新しい機能も controller → service → repository の責務分離を守り、`@RequiredArgsConstructor` で依存を注入するのが既定スタイル。
- Lombok (`@Data`, `@RequiredArgsConstructor`) を多用するため、手動で getter/setter を書かずアノテーションを揃える。
- 環境依存値はコードにベタ書きせず `application.properties` で `${}` プレースホルダを使って注入する。
- フォーム POST 後は `redirect:/...` を返して PRG パターンを維持し、ビューに直接フォワードしないことが現行挙動と整合する。
