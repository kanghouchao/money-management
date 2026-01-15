# マネーマネジメントアプリケーション

これは個人の家計を管理するための Spring Boot アプリケーションです。収入・支出の記録や財務レポートの生成などの機能を提供します。

## 必要な環境

1. Docker と Docker Compose
    - お使いの OS に合わせて公式インストール手順に従ってください。
      - [Docker インストールガイド](https://docs.docker.com/get-docker/)
      - [Docker Compose インストールガイド](https://docs.docker.com/compose/install/)
2. Task (go-task)
    - シンプルなタスクランナーです。クロスプラットフォームで動作し、Windows 環境でも推奨されます。
      - [Task インストールガイド](https://taskfile.dev/installation/)

3. Git
    - お使いの OS に合わせて公式インストール手順に従ってください。
      - [Git インストールガイド](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)

4. GitHub アカウント
    - まだお持ちでない場合は GitHub アカウントを作成してください。
      - [GitHub サインアップ](https://github.com/join)

5. Visual Studio Code（推奨）
    - コード編集用に Visual Studio Code をインストールしてください。
      - [VSCode ダウンロード](https://code.visualstudio.com/Download)

## 開発セットアップ

1. リポジトリをクローン
   ```bash
   git clone https://github.com/kanghouchao/money-management.git
   cd money-management
   ```

2. 環境変数の設定
   `.env.example` を `.env` としてコピーし、必要に応じて値を編集してください。
   ```bash
   cp .env.example .env
   ```

3. 開発環境を起動
    ```bash
    task build up
    ```

4. アプリケーションにアクセス
    - ブラウザで `http://localhost:8080` を開いてマネーマネジメントアプリにアクセスします。

5. 開発環境の停止
    ```bash
    task down
    ```

## その他のコマンド
- データベースコンテナに入る (MySQL クライアント)
    ```bash
    task db
    ```

- ログを表示
    ```bash
    task logs
    ```
- ソースのアプデート
    ```bash
    git pull --rebase
    ```

