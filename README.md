# TeamSpilitAutoInput

TeamSplitの勤怠を自動入力するやつ。

## 実行条件

- Java8がインストールされていること
- Chromeがインストールされていること

## 使い方

- [zip](https://github.com/ms-ogawara/TeamSpilitAutoInput/releases/download/v1.0.0/teamsplit-auto-input_1.0.0.zip)をダウンロードして、任意の場所に解凍。

- コマンドプロンプトより、以下のコマンド実行
  ```
  java -Dwebdriver.chrome.driver="chromedriver.exeの絶対パス" -jar teamsplit-auto-input.jar
  ```
  ※zipに同梱しているchromedriver.exeはChromeのバージョンが80のものです。<br/>
  ※お使いのChromeのバージョンが異なる場合は、[こちら](https://chromedriver.chromium.org/downloads)からダウンロードしてください。 
  
- あとは、コンソールに従って入力
