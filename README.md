# java-B11207030-Eason-B11207042-Tony

v1.1: 新增面板10×20網格線、消行機制

v1.2: 調整畫面大小、新增計分功能和隨分數提高下落速度

v1.2.1: 加入End game功能、方塊顏色、修正一些寫法

v1.3: 預覽下個方塊(測試出End game bug)

v1.3.1: 新增反轉功能和快速下落

v1.3.2: 新增背景音樂、消除音效、結束音效

v1.3.3: 新增消除動畫、遊玩區域下移(生成在區域上的功能暫有問題)

v1.3.4: 更改按鍵(wasd)，方塊生成在區域上方(判斷結束部份還有問題)

v1.4.0: 加入Hold功能

v1.4.1: 更改Hold顯示問題

v1.4.2: 修改Hold功能、加入首尾頁功能

v1.4.3: 新增menu功能的按鍵、結束時能回到主選單

v1.4.4: 主頁面調整功能實裝、實裝調整音量(70以下聲音太小)、新增挑戰模式每十秒上升一階障礙

v1.4.5: 修改調整速度功能，新增暫停功能

v1.4.6: 修改輸入鍵位、加速標準、更改輸入程式碼、更改gameover判斷、新增下落預覽功能

v1.4.7: 顯示調整、暫停時可以重來或回主畫面、新增排行榜功能

---
## 組員資訊

| 學號      | 姓名   |
| ------- | ------- |
| B11207030 | 高翊峰 |
| B11207140 | 蘇峰玄 |

---

## 分工說明

| 負責人      | 項目類別 |
| ------- | --------------- |
| 高翊峰    | 調整畫面、消行機制、End game功能、方塊顏色、Hold功能、首尾頁功能、menu頁面按鍵、新增挑戰模式 |
| 蘇峰玄    | 調整畫面、計分功能、隨分數提高下落速度、預覽方塊、反轉功能、快速下落、新增音效、消除動畫、暫停功能、下落預覽功能、排行榜功能|

---

## 報告影片連結

* [點我看遊戲演示影片](https://youtu.be/Wx4hFKNItk8)

---

## 遊戲說明
### 一、 主頁面(上下左右鍵選擇)
•排行榜

•調整音量

•調整速度

•選擇模式

•開始遊戲
### 二、 遊戲頁面
•A、D鍵調整左右

•Q、W鍵旋轉方塊

•E鍵保留方塊

•S鍵加速下落

•Space鍵確定下落

•P鍵暫停
### 三、 暫停頁面
•P鍵繼續遊戲

•R鍵重新開始遊戲

•ESC鍵回主選單
### 四、 結束頁面
•輸入名字登入排行榜

•R鍵重新開始遊戲

•ESC鍵回主選單
### 五、 挑戰模式
•每十秒上升一格障礙

## Tetris 遊戲執行方式

### 系統需求

- 作業系統：
  - Windows 10 或更新版本
  - macOS 10.14 或更新版本
  - Linux 任意主流發行版

- Java 環境：
  - Java 8 ~ Java 21 均可（建議 JDK 17 或 21）
  - 必須已安裝 Java 並設定好環境變數（java 指令可執行）

### 執行方式

1. 下載 `TetrisGame.jar`
2. 開啟終端機（或命令提示字元）
3. 移動到 jar 所在目錄
4. 執行以下指令：

```bash
java -jar TetrisGame.jar
```
---
## AI對話摘要

| 序號 | 主題／功能需求                        | 需求簡述                                  | 回覆摘要                                                                    |
| -- | ------------------------------ | ------------------------------------------ | -------------------------------------------------------------------------------- |
| 1  | 基礎 Tetris 程式碼                  | 請生成一個 Java Swing 版的 Tetris 遊戲程式碼           | 提供了完整 Java Tetris 程式，包括基本 UI、方塊邏輯、下落與旋轉。                                         |
| 2  | 模式選擇功能                         | 想加入「普通模式」與「挑戰模式」選擇功能                       | 提供模式選擇選單功能與開啟對應模式的架構設計。                                                          |
| 3  | 音效功能                           | 想加上方塊消除與遊戲結束的音效                            | 建議使用 `javax.sound.sampled` 播放 `.wav` 檔案，並提供播放音效範例程式。                             |
| 4  | 調整音量功能                         | 想調整音效音量                                    | 建議透過 `FloatControl` 控制音量並提供範例，亦可加入滑桿至選單 UI 以調整音量。                                |
| 5  | Hold 機制                        | 想加入按住並切換目前方塊的機制                            | 解釋 Hold 機制邏輯與如何實作，提供對應代碼（包含交換邏輯與 UI 更新）。                                         |
| 6  | 遊戲速度變化                         | 想根據分數讓遊戲速度逐漸變快                             | 解釋如何用分數調整 `Timer` delay，並提供實作方式。                                                 |
| 7  | 清除動畫效果                         | 想為方塊消除加入動畫                                 | 說明可使用動畫延遲顯示清除過程，建議使用透明度閃爍或顏色閃爍方式實作。                                              |
| 8  | 暫停與繼續功能                        | 想加上按鈕暫停或繼續遊戲                               | 提供完整暫停機制實作方法（暫停 `Timer`、鎖定按鍵等）。                                                  |
| 9  | 記錄分數                           | 想將每場遊戲分數寫入文字檔                              | 提供使用 `BufferedWriter` 寫入 `score.txt` 的簡單儲存程式碼。                                   |
| 10 | 顯示排行榜                          | 想加上排行榜功能                                   | 提供載入分數、排序並限制前 10 名的程式，並建議用自定 class 儲存名稱＋分數。                                      |
| 11 | 自定 HighScoreManager 檔案處理程式碼    | 貼出 `HighScoreManager` 程式並詢問問題              | 檢視與優化 `HighScoreManager` 程式（修正最大筆數、排序、例外處理等）。                                    |
| 12 | 檔案路徑問題：為何 scores.txt 不在預期資料夾中？ | 問為什麼 `scores.txt` 存在外層資料夾而非同一層；放進同一資料夾會讀不到 | 解釋工作目錄與 classpath 的差異，並提供三種解法：印出 `user.dir`、使用相對路徑或動態取得 class 位置，修改 `FILE_NAME`。 |
| 13 | 總結回顧                           | 要求將整段對話內容以表格彙整                             | 提供本表格整理，彙整所有問題與解決方案，協助回顧開發過程。                                                    |

---
## Class
```mermaid
classDiagram
    class Tetris {
        -GamePanel panel
        +main(String[] args)
    }

    class GamePanel {
        -GameBoard board
        -InputHandler inputHandler
        -SoundPlayer backgroundMusic
        -GameState gameState
        +paintComponent(Graphics g)
        +startGame()
    }

    class GameBoard {
        -Block currentBlock
        -Block nextBlock
        -Block holdBlock
        +spawnNewBlock()
        +moveBlockDown()
        +rotateBlock()
        +clearFullLines()
    }

    class SoundPlayer {
        +playSoundOnce()
    }

    class InputHandler {
        +keyPressed(KeyEvent e)
    }

    class HighScoreManager {
        +addScore(String name, int score)
    }

    class ScoreEntry {
        +compareTo(ScoreEntry other)
    }

    class Block {
        <<abstract>>
        +rotate()
        +moveLeft()
        +moveRight()
        +moveDown()
    }

    class IBlock
    class JBlock
    class LBlock
    class OBlock
    class SBlock
    class TBlock
    class ZBlock

    class GameState {
        <<enumeration>>
        MENU
        PLAYING
        PAUSED
        GAME_OVER
        HIGHSCORES
    }

    Tetris --> GamePanel
    GamePanel --> GameBoard
    GamePanel --> InputHandler
    GamePanel --> SoundPlayer
    GamePanel --> HighScoreManager
    GamePanel --> GameState
    GameBoard --> Block
    GameBoard --> SoundPlayer
    InputHandler --> GameBoard
    HighScoreManager --> ScoreEntry
    Block <|-- IBlock
    Block <|-- JBlock
    Block <|-- LBlock
    Block <|-- OBlock
    Block <|-- SBlock
    Block <|-- TBlock
    Block <|-- ZBlock
```









## 時序圖
```mermaid
sequenceDiagram
    participant T as Tetris
    participant GP as GamePanel
    participant IH as InputHandler
    participant GB as GameBoard
    participant SP as SoundPlayer
    participant HSM as HighScoreManager

    T->>GP: new GamePanel()
    T->>GP: startGame()
    GP->>GB: new GameBoard(mode)
    GP->>SP: new SoundPlayer("tetris_theme.wav")
    SP->>SP: loop(Clip.LOOP_CONTINUOUSLY)
    GP->>IH: new InputHandler(this)
    GP->>GP: start gravityTimer

    IH->>GP: keyPressed(KeyEvent) [Start Game]
    GP->>GP: setGameState(PLAYING)
    GP->>GB: resetGameBoard()

    loop Every gravityTimer tick
        GP->>GB: update()
        GB->>GB: moveBlock(0, 1)
        alt Collision detected
            GB->>GB: placeBlock(currentBlock)
            GB->>SP: playSoundOnce("clear_line.wav")
            GB->>GB: clearFullLines()
            GB->>GB: update score, clearline
            alt Challenge mode
                GB->>GB: insertBottomRow()
            end
            GB->>GB: spawnNewBlock()
            alt Game over
                GB->>GP: isGameOver() returns true
                GP->>GP: promptForName()
                GP->>HSM: addScore(name, score)
                GP->>SP: playSoundOnce("game-over.wav")
                GP->>SP: stop()
                GP->>GP: setGameState(GAME_OVER)
            end
        end
    end

    IH->>GB: moveBlock(-1, 0) [Key A: Left]
    IH->>GB: rotateBlock() [Key W: Rotate]
    IH->>GB: dropBlock() [Key SPACE: Drop]

    GP->>GP: display game over screen
    IH->>GP: setGameState(MENU) [Key ESC]
```


## 流程圖

```mermaid
flowchart TD
    A[開始遊戲] --> B[初始化 JFrame 和 GamePanel]
    B --> C[設置 GameBoard 和計時器]
    C --> D[設置 gameState 為 MENU]
    D --> E[顯示主選單]
    E --> F{處理選單輸入}
    F -->|選擇開始遊戲| G[播放背景音樂]
    F -->|選擇高分榜| H[顯示高分榜]
    H -->|按 ESC| E
    G --> I[生成新方塊]
    I --> J[設置 gameState 為 PLAYING]
    J --> K[方塊自動下落]
    K --> L{是否碰到底部或其他方塊?}
    L -->|是| M[固定方塊]
    M --> N{是否有可消除的行?}
    N -->|是| O[消除行並播放消除音效]
    O --> P[更新分數和消除行數]
    N -->|否| P
    P --> Q{是否挑戰模式?}
    Q -->|是| R{10秒計時到?}
    R -->|是| S[插入底部一行]
    S --> T[檢查遊戲結束]
    R -->|否| T
    Q -->|否| T
    T -->|是| U[提示輸入名稱]
    U --> V[儲存高分]
    V --> W[播放遊戲結束音效]
    W --> X[設置 gameState 為 GAME_OVER]
    X --> Y[顯示遊戲結束畫面]
    Y --> Z{處理輸入}
    Z -->|按 R: 重啟| G
    Z -->|按 ESC/ENTER: 返回選單| E
    T -->|否| AA{是否有鍵盤輸入?}
    AA -->|是| AB{輸入類型?}
    AB -->|A: 左移| AC[移動方塊左移]
    AB -->|D: 右移| AD[移動方塊右移]
    AB -->|S: 下移| AE[移動方塊下移]
    AB -->|W: 旋轉| AF[旋轉方塊]
    AB -->|Q: 逆旋轉| AG[逆向旋轉方塊]
    AB -->|SPACE: 快速下落| AH[快速下落並固定]
    AB -->|E: 保留| AI[保留當前方塊]
    AB -->|P: 暫停| AJ[設置 gameState 為 PAUSED]
    AJ --> AK[顯示暫停畫面]
    AK --> AL{處理暫停輸入}
    AL -->|P: 恢復| J
    AL -->|R: 重啟| G
    AL -->|ESC: 返回選單| E
    AC --> K
    AD --> K
    AE --> K
    AF --> K
    AG --> K
    AH --> M
    AI --> I
    AA -->|否| K
    L -->|否| K

```
          
